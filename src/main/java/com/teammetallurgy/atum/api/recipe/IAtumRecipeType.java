package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import com.teammetallurgy.atum.integration.crafttweaker.CTKiln;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public interface IAtumRecipeType<T extends IRecipe<?>> extends IRecipeType<T> {
    IRecipeType<KilnRecipe> KILN = register("kiln");
    IRecipeType<QuernRecipe> QUERN = register("quern");
    IRecipeType<SpinningWheelRecipe> SPINNING_WHEEL = register("spinning_wheel");
    /**
     * Instance for the Kiln recipe blacklist.
     * Used to blacklist what Furnace recipes the Kiln grabs
     * Can be used by other mods or in a CraftTweaker script, see {@link CTKiln}
     */
    List<ResourceLocation> kilnBlacklist = new ArrayList<>();

    static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(Atum.MOD_ID, key), new IRecipeType<T>() {
            @Override
            public String toString() {
                return key;
            }
        });
    }
}