package com.teammetallurgy.atum.api.recipe.recipes;

import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.api.recipe.RotationRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nonnull;

public class QuernRecipe extends RotationRecipe<QuernTileEntity> {

    public QuernRecipe(Ingredient input, @Nonnull ItemStack output, int rotations) {
        super(AtumRecipeTypes.QUERN.get(), input, output, rotations);
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return AtumRecipeSerializers.QUERN.get();
    }
}