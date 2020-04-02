package com.teammetallurgy.atum.integration.jei;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.integration.jei.categories.KilnRecipeCategory;
import com.teammetallurgy.atum.integration.jei.categories.QuernRecipeCategory;
import com.teammetallurgy.atum.integration.jei.categories.SpinningWheelRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@JeiPlugin
public class JEIIntegration implements IModPlugin {
    public static final ResourceLocation QUERN = new ResourceLocation(Atum.MOD_ID, "quern");
    public static final ResourceLocation SPINNING_WHEEL = new ResourceLocation(Atum.MOD_ID, "spinningwheel");
    public static final ResourceLocation KILN = new ResourceLocation(Atum.MOD_ID, "kiln");

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Atum.MOD_ID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.QUERN), QUERN);
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.SPINNING_WHEEL), SPINNING_WHEEL);
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.KILN), KILN, VanillaRecipeCategoryUid.FUEL);
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.LIMESTONE_FURNACE), VanillaRecipeCategoryUid.FURNACE, VanillaRecipeCategoryUid.FUEL);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        registry.addRecipes(RecipeHandlers.quernRecipes.getValues(), QUERN);
        registry.addRecipes(RecipeHandlers.spinningWheelRecipes.getValues(), SPINNING_WHEEL);
        registry.addRecipes(RecipeHandlers.kilnRecipes.getValues(), KILN);
        addInfo(new ItemStack(AtumItems.EMMER_DOUGH), registry);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new QuernRecipeCategory(guiHelper));
        registry.addRecipeCategories(new SpinningWheelRecipeCategory(guiHelper));
        registry.addRecipeCategories(new KilnRecipeCategory(guiHelper));
    }

    private void addInfo(ItemStack stack, IRecipeRegistration registry) {
        registry.addIngredientInfo(stack, VanillaTypes.ITEM, "jei." + stack.getItem().getTranslationKey());
    }
}