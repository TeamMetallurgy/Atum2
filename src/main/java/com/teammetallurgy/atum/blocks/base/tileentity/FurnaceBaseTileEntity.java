package com.teammetallurgy.atum.blocks.base.tileentity;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nonnull;

public abstract class FurnaceBaseTileEntity extends AbstractFurnaceBlockEntity {

    protected FurnaceBaseTileEntity(BlockEntityType<?> tileEntityType, RecipeType<? extends AbstractCookingRecipe> recipeType, int size) {
        super(tileEntityType, recipeType);
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    @Nonnull
    protected Component getDefaultName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }
}