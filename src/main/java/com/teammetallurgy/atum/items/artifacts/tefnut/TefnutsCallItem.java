package com.teammetallurgy.atum.items.artifacts.tefnut;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityTefnutsCall;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class TefnutsCallItem extends Item {

    public TefnutsCallItem() {
        super();
        this.setMaxDamage(650);
        this.setMaxStackSize(1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return 7200;
    }

    @Override
    @Nonnull
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRotateAroundWhenRendering() {
        return false;
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType slot, @Nonnull ItemStack stack) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        if (slot == EquipmentSlotType.MAINHAND) {
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 3.0D, AttributeModifier.Operation.ADDITION));
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.6D, AttributeModifier.Operation.ADDITION));
        }
        return map;
    }

    @Override
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, @Nonnull World world, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            int j = this.getMaxItemUseDuration(stack) - timeLeft;
            if (j > 21) {
                j = 21;
            }

            if (!world.isRemote) {
                EntityTefnutsCall spear = new EntityTefnutsCall(world, player);
                spear.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, (float) j / 25.0F + 0.25F, 1.0F);
                spear.setDamage(spear.getDamage() * 2.0D);
                spear.setStack(stack);

                world.addEntity(spear);
                world.updateEntity(spear);

                stack.damageItem(4, player);
            }
            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }
}