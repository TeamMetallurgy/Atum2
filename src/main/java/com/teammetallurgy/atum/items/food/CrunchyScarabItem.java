package com.teammetallurgy.atum.items.food;

import com.teammetallurgy.atum.Atum;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class CrunchyScarabItem extends Item {

    public CrunchyScarabItem(Item.Properties properties) {
        super(properties.tab(Atum.GROUP));
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 10;
    }
}