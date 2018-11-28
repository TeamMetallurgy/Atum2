package com.teammetallurgy.atum.items.food;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemCrunchyScarab extends ItemFood {

    public ItemCrunchyScarab(int amount, float saturation) {
        super(amount, saturation, true);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        if (stack.getItem() == AtumItems.CRUNCHY_GOLD_SCARAB) {
            return true;
        }
        return super.hasEffect(stack);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 10;
    }
}