package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.api.recipe.spinningwheel.ISpinningWheelRecipe;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class RecipeHandlers {
    /**
     * Instance for the Quern recipes
     */
    public static IForgeRegistryModifiable<IQuernRecipe> quernRecipes;
    /**
     * Instance for the Spinning Wheel recipes
     */
    public static IForgeRegistryModifiable<ISpinningWheelRecipe> spinningWheelRecipes;
}