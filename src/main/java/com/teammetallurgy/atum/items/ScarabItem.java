package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ScarabItem extends Item {

    public ScarabItem() {
        super(new Item.Properties().maxStackSize(1).group(Atum.GROUP));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }
}