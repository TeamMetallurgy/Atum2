package com.teammetallurgy.atum.utils.recipe;

import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipe;

import javax.annotation.Nonnull;

public class BrewingRecipeNBT extends BrewingRecipe {

    public BrewingRecipeNBT(@Nonnull ItemStack input, @Nonnull ItemStack ingredient, @Nonnull ItemStack output) {
        super(input, ingredient, output);
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack) {
        return StackHelper.areStacksEqualIgnoreSize(this.getInput(), stack);
    }

    @Override
    public boolean isIngredient(@Nonnull ItemStack stack) {
        return StackHelper.areStacksEqualIgnoreSize(this.getIngredient(), stack);
    }
}