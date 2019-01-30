package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.api.recipe.spinningwheel.ISpinningWheelRecipe;
import com.teammetallurgy.atum.integration.crafttweaker.CTKiln;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.ArrayList;
import java.util.List;

public class RecipeHandlers {
    /**
     * Instance for the Quern recipes
     */
    public static IForgeRegistryModifiable<IQuernRecipe> quernRecipes;
    /**
     * Instance for the Spinning Wheel recipes
     */
    public static IForgeRegistryModifiable<ISpinningWheelRecipe> spinningWheelRecipes;
    /**
     * Instance for the Kiln recipes
     */
    public static IForgeRegistryModifiable<IKilnRecipe> kilnRecipes;
    /**
     * Instance for the Kiln recipe blacklist.
     * Used to blacklist what Furnace recipes the Kiln grabs
     * Can be used by other mods or in a CraftTweaker script, see {@link CTKiln}
     */
    public static List<ResourceLocation> kilnBlacklist = new ArrayList<>();
}