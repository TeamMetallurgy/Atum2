package com.teammetallurgy.atum.items.artifacts.tefnut;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.EntityItemTefnutsCall;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class TefnutsCallItem extends Item {

    public TefnutsCallItem() {
        super(new Item.Properties().maxDamage(650).rarity(Rarity.RARE).group(Atum.GROUP));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 7200;
    }

    @Override
    @Nonnull
    public UseAction getUseAction(@Nonnull ItemStack stack) {
        return UseAction.BOW;
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

    /*@Override
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, @Nonnull World world, LivingEntity entityLiving, int timeLeft) { //TODO
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            int j = this.getUseDuration(stack) - timeLeft;
            if (j > 21) {
                j = 21;
            }

            if (!world.isRemote) {
                TefnutsCallEntity spear = new TefnutsCallEntity(world, player);
                spear.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, (float) j / 25.0F + 0.25F, 1.0F);
                spear.setDamage(spear.getDamage() * 2.0D);

                world.addEntity(spear);
                if (world instanceof ServerWorld) {
                    ((ServerWorld) world).updateEntity(spear);
                }

                stack.damageItem(4, player, (e) -> {
                    e.sendBreakAnimation(entityLiving.getActiveHand());
                });
            }
            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
        }
    }*/

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity.getClass().equals(ItemEntity.class)) {
            ItemEntity original = (ItemEntity) entity;
            if (original.getItem().getItem() instanceof TefnutsCallItem) {
                EntityItemTefnutsCall tefnutsCall = new EntityItemTefnutsCall(original.world, original.getPosX(), original.getPosY(), original.getPosZ(), original.getItem());
                entity.remove();
                event.setCanceled(true);
                tefnutsCall.setMotion(original.getMotion());
                event.getWorld().addEntity(tefnutsCall);
            }
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Items.DIAMOND;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
    }
}