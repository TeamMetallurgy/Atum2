/*package com.teammetallurgy.atum.integration.jei;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.QUERN.get()), QUERN);
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.SPINNING_WHEEL.get()), SPINNING_WHEEL);
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.KILN.get()), KILN, VanillaRecipeCategoryUid.FUEL);
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.LIMESTONE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE, VanillaRecipeCategoryUid.FUEL);
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            addRecipes(registry, level, AtumRecipeTypes.KILN.get(), KILN);
            registry.addRecipes(RecipeHelper.getKilnRecipesFromFurnace(level.getRecipeManager()), KILN);
            addRecipes(registry, level, AtumRecipeTypes.QUERN.get(), QUERN);
            addRecipes(registry, level, AtumRecipeTypes.SPINNING_WHEEL.get(), SPINNING_WHEEL);
        }
        addInfo(new ItemStack(AtumItems.EMMER_DOUGH.get()), registry);
    }

    private <C extends Container, T extends Recipe<C>> void addRecipes(@Nonnull IRecipeRegistration registry, Level level, RecipeType<T> recipeType, ResourceLocation name) {
        registry.addRecipes(RecipeHelper.getRecipes(level.getRecipeManager(), recipeType).stream().filter(r -> r.getIngredients().stream().noneMatch(Ingredient::isEmpty)).collect(Collectors.toCollection(ArrayList::new)), name);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new QuernRecipeCategory(guiHelper));
        registry.addRecipeCategories(new SpinningWheelRecipeCategory(guiHelper));
        registry.addRecipeCategories(new KilnRecipeCategory(guiHelper));
    }

    private void addInfo(ItemStack stack, IRecipeRegistration registry) {
        registry.addIngredientInfo(stack, VanillaTypes.ITEM, Component.translatable("jei." + stack.getItem().getDescriptionId()));
    }
}*/