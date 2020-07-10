package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class BattleAxeItem extends AxeItem {

    public BattleAxeItem(IItemTier tier, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(tier, attackDamage, attackSpeed, properties.group(Atum.GROUP));
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment == Enchantments.LOOTING || enchantment == Enchantments.SWEEPING;
    }
}