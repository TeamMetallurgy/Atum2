package com.teammetallurgy.atum.utils;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class AtumCreativeTab extends CreativeTabs {

    public AtumCreativeTab() {
        super(Constants.MOD_ID);
        this.setBackgroundImageName("item_search.png");
    }

    @Override
    @Nonnull
    public ItemStack createIcon() {
        return new ItemStack(AtumItems.SCARAB);
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}