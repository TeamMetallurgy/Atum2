package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

import javax.annotation.Nonnull;

public class DaggerItem extends SwordItem {

    public DaggerItem(IItemTier itemTier) {
        this(itemTier, new Item.Properties().group(Atum.GROUP));
    }

    public DaggerItem(IItemTier itemTier, Item.Properties properties) {
        super(itemTier, 2, -2.0F, properties.group(Atum.GROUP));
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) && enchantment != Enchantments.SWEEPING && enchantment != Enchantments.KNOCKBACK;
    }
}