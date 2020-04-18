package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class RecipeHandlers { //TODO Move to json?
    /**
     * Instance for the Kiln recipes
     */
    public static IForgeRegistryModifiable<IKilnRecipe> kilnRecipes;
}