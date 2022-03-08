package com.teammetallurgy.atum.misc;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class AtumItemGroup extends CreativeModeTab {

    public AtumItemGroup() {
        super(Atum.MOD_ID);
        this.setBackgroundImage(new ResourceLocation("item_search.png"));
    }

    @Override
    @Nonnull
    public ItemStack makeIcon() {
        return new ItemStack(AtumItems.SCARAB.get());
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}