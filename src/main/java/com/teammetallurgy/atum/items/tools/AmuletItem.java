package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.integration.curios.ISimpleCurioItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;

public class AmuletItem extends Item implements ISimpleCurioItem {

    public AmuletItem(Item.Properties properties) {
        super(properties.rarity(Rarity.RARE).group(Atum.GROUP));
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
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == AtumItems.NEBU_INGOT;
    }
}