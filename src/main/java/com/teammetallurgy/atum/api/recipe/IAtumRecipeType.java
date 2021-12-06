package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public interface IAtumRecipeType<T extends Recipe<?>> extends RecipeType<T> {
    RecipeType<KilnRecipe> KILN = register("kiln");
    RecipeType<QuernRecipe> QUERN = register("quern");
    RecipeType<SpinningWheelRecipe> SPINNING_WHEEL = register("spinning_wheel");
    /**
     * Instance for the Kiln recipe blacklist.
     * Used to blacklist what Furnace recipes the Kiln grabs
     * Can be used by other mods or in a CraftTweaker script, see {@link CTKiln}
     */
    List<ResourceLocation> kilnBlacklist = new ArrayList<>();

    static <T extends Recipe<?>> RecipeType<T> register(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(Atum.MOD_ID, key), new RecipeType<T>() {
            @Override
            public String toString() {
                return key;
            }
        });
    }
}