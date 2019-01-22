package com.teammetallurgy.atum.integration.jei;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.api.recipe.spinningwheel.ISpinningWheelRecipe;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.integration.jei.kiln.KilnRecipeCategory;
import com.teammetallurgy.atum.integration.jei.kiln.KilnRecipeWrapper;
import com.teammetallurgy.atum.integration.jei.quern.QuernRecipeCategory;
import com.teammetallurgy.atum.integration.jei.quern.QuernRecipeWrapper;
import com.teammetallurgy.atum.integration.jei.spinningwheel.SpinningWheelRecipeCategory;
import com.teammetallurgy.atum.integration.jei.spinningwheel.SpinningWheelRecipeWrapper;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIIntegration implements IModPlugin {
    public static final String QUERN = Constants.MOD_ID + "." + "quern";
    public static final String SPINNING_WHEEL = Constants.MOD_ID + "." + "spinningwheel";
    public static final String KILN = Constants.MOD_ID + "." + "kiln";

    @Override
    public void register(IModRegistry registry) {
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        for (ItemStack stack : AtumRegistry.HIDE_LIST) {
            blacklist.addIngredientToBlacklist(stack);
        }
        //Quern
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.QUERN), QUERN);
        registry.handleRecipes(IQuernRecipe.class, recipe -> new QuernRecipeWrapper(registry.getJeiHelpers().getGuiHelper(), recipe.getRegistryName(), recipe.getInput(), recipe.getOutput(), recipe.getRotations()), QUERN);
        registry.addRecipes(RecipeHandlers.quernRecipes.getValuesCollection(), QUERN);
        //Spinning Wheel
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.SPINNING_WHEEL), SPINNING_WHEEL);
        registry.handleRecipes(ISpinningWheelRecipe.class, recipe -> new SpinningWheelRecipeWrapper(recipe.getRegistryName(), recipe.getInput(), recipe.getOutput(), recipe.getRotations()), SPINNING_WHEEL);
        registry.addRecipes(RecipeHandlers.spinningWheelRecipes.getValuesCollection(), SPINNING_WHEEL);
        //Kiln
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.KILN), KILN, VanillaRecipeCategoryUid.FUEL);
        registry.handleRecipes(IKilnRecipe.class, recipe -> new KilnRecipeWrapper(recipe.getRegistryName(), recipe.getInput(), recipe.getOutput(), recipe.getExperience()), KILN);
        registry.addRecipes(RecipeHandlers.kilnRecipes.getValuesCollection(), KILN);

        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.LIMESTONE_FURNACE), VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.FUEL);
        addInfo(new ItemStack(AtumItems.EMMER_DOUGH), registry);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new QuernRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SpinningWheelRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new KilnRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    private void addInfo(ItemStack stack, IModRegistry registry) {
        registry.addIngredientInfo(stack, VanillaTypes.ITEM, "jei." + stack.getItem().getTranslationKey());
    }
}