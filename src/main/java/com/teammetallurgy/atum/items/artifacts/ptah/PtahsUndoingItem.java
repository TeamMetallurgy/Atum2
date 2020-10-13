package com.teammetallurgy.atum.items.artifacts.ptah;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class PtahsUndoingItem extends PickaxeItem {

    public PtahsUndoingItem() {
        super(ItemTier.DIAMOND, 2, -2.8F, new Item.Properties().rarity(Rarity.RARE).addToolType(ToolType.AXE, 0).group(Atum.GROUP));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
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
                if (target instanceof StoneBaseEntity) {
                    if (!player.getCooldownTracker().hasCooldown(held.getItem())) {
                        event.setAmount(event.getAmount() * 2);
                    }
                }
            }
        }
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
        if (state.getMaterial() == Material.WOOD) {
            return 11.0F;
        }
        return super.getDestroySpeed(stack, state);
    }
}