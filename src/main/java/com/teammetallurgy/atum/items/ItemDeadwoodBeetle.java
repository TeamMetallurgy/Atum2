package com.teammetallurgy.atum.items;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemDeadwoodBeetle extends ItemFood {

    public ItemDeadwoodBeetle() {
        super(1, 0.1F, false);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 10;
    }
}