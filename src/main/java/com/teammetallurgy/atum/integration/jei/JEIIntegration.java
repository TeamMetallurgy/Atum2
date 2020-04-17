package com.teammetallurgy.atum.integration.jei;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.integration.jei.categories.KilnRecipeCategory;
import com.teammetallurgy.atum.integration.jei.categories.QuernRecipeCategory;
import com.teammetallurgy.atum.integration.jei.categories.SpinningWheelRecipeCategory;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@JeiPlugin
public class JEIIntegration implements IModPlugin {
    public static final ResourceLocation QUERN = new ResourceLocation(Atum.MOD_ID, "quern");
    public static final ResourceLocation SPINNING_WHEEL = new ResourceLocation(Atum.MOD_ID, "spinning_wheel");
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
    public void registerRecipes(@Nonnull IRecipeRegistration registry) {
        ClientWorld world = Minecraft.getInstance().world;
        if (world != null) {
            RecipeManager recipeManager = world.getRecipeManager();
            registry.addRecipes(RecipeHandlers.kilnRecipes.getValues(), KILN);
            registry.addRecipes(RecipeHandlers.quernRecipes.getValues(), QUERN);
            registry.addRecipes(RecipeHelper.getRecipes(recipeManager, IAtumRecipeType.SPINNING_WHEEL), SPINNING_WHEEL);
        }
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