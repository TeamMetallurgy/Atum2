package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class RecipeHandlers { //TODO Move to json
    /**
     * Instance for the Quern recipes
     */
    public static IForgeRegistryModifiable<IQuernRecipe> quernRecipes;
    /**
     * Instance for the Kiln recipes
     */
    public static IForgeRegistryModifiable<IKilnRecipe> kilnRecipes;
}