package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import com.teammetallurgy.atum.integration.crafttweaker.CTKiln;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public class AtumRecipeTypes {
    public static final RecipeType<KilnRecipe> KILN = register("kiln");
    public static final RecipeType<QuernRecipe> QUERN = register("quern");
    public static final RecipeType<SpinningWheelRecipe> SPINNING_WHEEL = register("spinning_wheel");
    /**
     * Instance for the Kiln recipe blacklist.
     * Used to blacklist what Furnace recipes the Kiln grabs
     * Can be used by other mods or in a CraftTweaker script, see {@link CTKiln}
     */
    public static final List<ResourceLocation> kilnBlacklist = new ArrayList<>();

    static <T extends Recipe<?>> RecipeType<T> register(final String key) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, key);
        return Registry.register(Registry.RECIPE_TYPE, id, new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });
    }
}