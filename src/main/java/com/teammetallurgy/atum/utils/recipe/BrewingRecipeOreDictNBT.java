package com.teammetallurgy.atum.utils.recipe;

import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingOreRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public class BrewingRecipeOreDictNBT extends BrewingOreRecipe {

    public BrewingRecipeOreDictNBT(@Nonnull ItemStack input, @Nonnull String ingredient, @Nonnull ItemStack output) {
        super(input, ingredient, output);
    }

    public BrewingRecipeOreDictNBT(@Nonnull ItemStack input, @Nonnull List<ItemStack> ingredient, @Nonnull ItemStack output) {
        super(input, ingredient, output);
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack) {
        return StackHelper.areStacksEqualIgnoreSize(this.getInput(), stack);
    }

    @Override
    public boolean isIngredient(@Nonnull ItemStack stack) {
        for (ItemStack target : this.getIngredient()) {
            if (StackHelper.areStacksEqualIgnoreSize(target, stack)) {
                return true;
            }

        }
        return false;
    }
}