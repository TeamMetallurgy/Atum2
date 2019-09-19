package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ScarabItem extends Item {

    public ScarabItem() {
        super(new Item.Properties().maxStackSize(1).group(Atum.GROUP));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}