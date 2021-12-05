package com.teammetallurgy.atum.misc;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class AtumItemGroup extends ItemGroup {

    public AtumItemGroup() {
        super(Atum.MOD_ID);
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