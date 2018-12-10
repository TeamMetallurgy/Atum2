package com.teammetallurgy.atum.integration.jei;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.integration.jei.quern.QuernRecipeCategory;
import com.teammetallurgy.atum.integration.jei.quern.QuernRecipeWrapper;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIIntegration implements IModPlugin {
    public static final String QUERN = Constants.MOD_ID + "." + "quern";

    @Override
    public void register(IModRegistry registry) {
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        for (ItemStack stack : AtumRegistry.HIDE_LIST) {
            blacklist.addIngredientToBlacklist(stack);
        }
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.LIMESTONE_FURNACE), VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.FUEL);
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.QUERN), QUERN);
        registry.handleRecipes(IQuernRecipe.class, recipe -> new QuernRecipeWrapper(registry.getJeiHelpers().getGuiHelper(), recipe.getRegistryName(), recipe.getInput(), recipe.getOutput(), recipe.getRotations()), QUERN);
        registry.addRecipes(RecipeHandlers.quernRecipes.getValuesCollection(), QUERN);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new QuernRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }
}