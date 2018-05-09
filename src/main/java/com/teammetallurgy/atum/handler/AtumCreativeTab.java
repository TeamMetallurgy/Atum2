package com.teammetallurgy.atum.handler;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class AtumCreativeTab extends CreativeTabs {

    public AtumCreativeTab() {
        super(Constants.MOD_ID);
    }

    @Override
    @Nonnull
    public ItemStack getTabIconItem() {
        return new ItemStack(AtumItems.SCARAB);
    }
}