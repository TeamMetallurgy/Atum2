package com.teammetallurgy.atum.items.artifacts;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.integration.curios.ISimpleCurioItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nonnull;

import net.minecraft.world.item.Item.Properties;

public class RingItem extends Item implements ISimpleCurioItem {

    public RingItem(Properties properties) {
        super(properties.rarity(Rarity.RARE).tab(Atum.GROUP));
    }

    public RingItem() {
        this(new Item.Properties());
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public boolean canRightClickEquip(@Nonnull ItemStack stack) {
        return true;
    }
}