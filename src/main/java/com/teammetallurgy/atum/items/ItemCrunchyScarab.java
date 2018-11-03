package com.teammetallurgy.atum.items;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemCrunchyScarab extends ItemFood {

    public ItemCrunchyScarab(int amount, float saturation) {
        super(amount, saturation, true);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 10;
    }
}