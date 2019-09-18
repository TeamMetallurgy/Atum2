package com.teammetallurgy.atum.items.food;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CrunchyScarabItem extends Item {

    public CrunchyScarabItem(Item.Properties properties) {
        super(properties.group(Atum.GROUP));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        if (stack.getItem() == AtumItems.CRUNCHY_GOLD_SCARAB) {
            return true;
        }
        return super.hasEffect(stack);
    }
Z
    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 10;
    }
}