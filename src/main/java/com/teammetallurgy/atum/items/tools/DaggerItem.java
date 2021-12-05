package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

import javax.annotation.Nonnull;

public class DaggerItem extends SwordItem {

    public DaggerItem(Tier itemTier) {
        this(itemTier, new Item.Properties().tab(Atum.GROUP));
    }

    public DaggerItem(Tier itemTier, Item.Properties properties) {
        super(itemTier, 2, -2.0F, properties.tab(Atum.GROUP));
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) && enchantment != Enchantments.SWEEPING_EDGE && enchantment != Enchantments.KNOCKBACK;
    }
}