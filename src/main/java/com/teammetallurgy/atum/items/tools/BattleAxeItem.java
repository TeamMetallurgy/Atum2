package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class BattleAxeItem extends AxeItem {

    public BattleAxeItem(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(tier, attackDamage, attackSpeed, properties.tab(Atum.GROUP));
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment == Enchantments.MOB_LOOTING || enchantment == Enchantments.SWEEPING_EDGE;
    }
}