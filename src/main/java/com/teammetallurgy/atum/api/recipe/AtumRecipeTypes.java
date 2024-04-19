package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class AtumRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_DEFERRED = DeferredRegister.create(Registries.RECIPE_TYPE, Atum.MOD_ID);
    public static DeferredHolder<RecipeType<?>, RecipeType<KilnRecipe>> KILN = register("kiln");
    public static DeferredHolder<RecipeType<?>, RecipeType<QuernRecipe>> QUERN = register("quern");
    public static DeferredHolder<RecipeType<?>, RecipeType<SpinningWheelRecipe>> SPINNING_WHEEL = register("spinning_wheel");
    /**
     * Instance for the Kiln recipe blacklist.
     * Used to blacklist what Furnace recipes the Kiln grabs
     * Can be used by other mods or in a CraftTweaker script, see {@link com.teammetallurgy.atum.integration.crafttweaker.CTKiln}
     */
    public static final List<ResourceLocation> kilnBlacklist = new ArrayList<>();

    static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(final String key) {
        return RECIPE_TYPE_DEFERRED.register(key, () -> new RecipeType<T>() {
            @Override
            public String toString() {
                return new ResourceLocation(Atum.MOD_ID, key).toString();
            }
        });
    }
}