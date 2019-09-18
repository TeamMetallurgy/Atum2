package com.teammetallurgy.atum.items.artifacts.ptah;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.teammetallurgy.atum.entity.stone.EntityStoneBase;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class PtahsUndoingItem extends PickaxeItem {

    public PtahsUndoingItem() {
        super(ToolMaterial.DIAMOND);
        this.efficiency = 11.0F;
        this.setHarvestLevel("axe", 0);
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment == Enchantments.KNOCKBACK || enchantment == Enchantments.LOOTING || enchantment == Enchantments.SHARPNESS;
    }

    @SubscribeEvent
    public static void onAttack(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) trueSource;
            ItemStack held = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
            if (held.getItem() == AtumItems.PTAHS_UNDOING) {
                LivingEntity target = event.getEntityLiving();
                if (target instanceof EntityStoneBase) {
                    if (!player.getCooldownTracker().hasCooldown(held.getItem())) {
                        event.setAmount(event.getAmount() * 2);
                    }
                }
            }
        }
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, IBlockState state) {
        if (state.getMaterial() == Material.WOOD) {
            return efficiency;
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EquipmentSlotType slot) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        if (slot == EquipmentSlotType.MAINHAND) {
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double) this.attackDamage + 2.0D, 0));
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double) this.attackSpeed, 0));
        }
        return map;
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
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == Items.DIAMOND;
    }
}