package com.teammetallurgy.atum.items.artifacts;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.integration.curios.ISimpleCurioItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nonnull;

public class AmuletItem extends Item implements ISimpleCurioItem {

    public AmuletItem(Item.Properties properties) {
        super(properties.rarity(Rarity.RARE).tab(Atum.GROUP));
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.VANISHING_CURSE || enchantment == Enchantments.MENDING || enchantment == Enchantments.UNBREAKING;
    }

    @Override
    public int getItemEnchantability(@Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    public boolean canRightClickEquip(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == AtumItems.NEBU_INGOT;
    }
}