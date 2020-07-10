package com.teammetallurgy.atum.api.recipe.recipes;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.api.recipe.RotationRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.SpinningWheelTileEntity;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class SpinningWheelRecipe extends RotationRecipe<SpinningWheelTileEntity> {

    public SpinningWheelRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output, int rotations) {
        this(Ingredient.fromStacks(input), output, rotations);
    }

    public SpinningWheelRecipe(Ingredient input, @Nonnull ItemStack output, int rotations) {
        this(new ResourceLocation(Atum.MOD_ID, "spinning_wheel"), input, output, rotations);
    }

    public SpinningWheelRecipe(ResourceLocation id, Ingredient input, @Nonnull ItemStack output, int rotations) {
        super(IAtumRecipeType.SPINNING_WHEEL, id, input, output, rotations);
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return AtumRecipeSerializers.SPINNING_WHEEL;
    }
}