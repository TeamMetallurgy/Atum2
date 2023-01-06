package com.teammetallurgy.atum.items.food;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class CrunchyScarabItem extends Item {

    public CrunchyScarabItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 10;
    }
}