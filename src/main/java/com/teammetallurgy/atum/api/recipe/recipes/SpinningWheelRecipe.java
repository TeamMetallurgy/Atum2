package com.teammetallurgy.atum.api.recipe.recipes;

import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.api.recipe.RotationRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.SpinningWheelTileEntity;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nonnull;

public class SpinningWheelRecipe extends RotationRecipe<SpinningWheelTileEntity> {

    public SpinningWheelRecipe(ItemStack input, @Nonnull ItemStack output, int rotations) {
        super(AtumRecipeTypes.SPINNING_WHEEL.get(), input, output, rotations);
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return AtumRecipeSerializers.SPINNING_WHEEL.get();
    }
}