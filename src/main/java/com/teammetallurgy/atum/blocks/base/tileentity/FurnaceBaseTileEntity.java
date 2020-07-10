package com.teammetallurgy.atum.blocks.base.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public abstract class FurnaceBaseTileEntity extends AbstractFurnaceTileEntity {

    protected FurnaceBaseTileEntity(TileEntityType<?> tileEntityType, IRecipeType<? extends AbstractCookingRecipe> recipeType, int size) {
        super(tileEntityType, recipeType);
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    @Nonnull
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
    }
}