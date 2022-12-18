package com.teammetallurgy.atum.api.recipe.recipes;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.api.recipe.RotationRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.SpinningWheelTileEntity;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SpinningWheelRecipe extends RotationRecipe<SpinningWheelTileEntity> {

    public SpinningWheelRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output, int rotations) {
        this(Ingredient.of(input), output, rotations);
    }

    public SpinningWheelRecipe(Ingredient input, @Nonnull ItemStack output, int rotations) {
        this(new ResourceLocation(Atum.MOD_ID, "spinning_wheel_" + Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(input.getItems()[0].getItem())).getPath() + "_to_" + Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output.getItem())).getPath() + (output.getCount() > 1 ? "_" + output.getCount() : "")), input, output, rotations);
    }

    public SpinningWheelRecipe(ResourceLocation id, Ingredient input, @Nonnull ItemStack output, int rotations) {
        super(AtumRecipeTypes.SPINNING_WHEEL.get(), id, input, output, rotations);
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return AtumRecipeSerializers.SPINNING_WHEEL.get();
    }
}