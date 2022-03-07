package com.teammetallurgy.atum.api.recipe.recipes;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.api.recipe.RotationRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nonnull;
import java.util.Objects;

public class QuernRecipe extends RotationRecipe<QuernTileEntity> {

    public QuernRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output, int rotations) {
        this(Ingredient.of(input), output, rotations);
    }

    public QuernRecipe(Ingredient input, @Nonnull ItemStack output, int rotations) {
        this(new ResourceLocation(Atum.MOD_ID, "quern_" + Objects.requireNonNull(input.getItems()[0].getItem().getRegistryName()).getPath() + "_to_" + Objects.requireNonNull(output.getItem().getRegistryName()).getPath() + (output.getCount() > 1 ? "_" + output.getCount() : "")), input, output, rotations);
    }

    public QuernRecipe(ResourceLocation id, Ingredient input, @Nonnull ItemStack output, int rotations) {
        super(AtumRecipeTypes.QUERN, id, input, output, rotations);
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return AtumRecipeSerializers.QUERN.get();
    }
}