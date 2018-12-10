package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class RecipeHandlers {
    /**
     * Instance for the Quern recipes
     */
    public static IForgeRegistryModifiable<IQuernRecipe> quernRecipes;
}
