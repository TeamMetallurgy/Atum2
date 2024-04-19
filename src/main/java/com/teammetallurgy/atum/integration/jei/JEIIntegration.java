package com.teammetallurgy.atum.integration.jei;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.integration.jei.categories.KilnRecipeCategory;
import com.teammetallurgy.atum.integration.jei.categories.QuernRecipeCategory;
import com.teammetallurgy.atum.integration.jei.categories.SpinningWheelRecipeCategory;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
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
    public static final mezz.jei.api.recipe.RecipeType<QuernRecipe> QUERN = mezz.jei.api.recipe.RecipeType.create(Atum.MOD_ID, "quern", QuernRecipe.class);
    public static final mezz.jei.api.recipe.RecipeType<KilnRecipe> KILN = mezz.jei.api.recipe.RecipeType.create(Atum.MOD_ID, "kiln", KilnRecipe.class);
    public static final mezz.jei.api.recipe.RecipeType<SpinningWheelRecipe> SPINNING_WHEEL = mezz.jei.api.recipe.RecipeType.create(Atum.MOD_ID, "spinning_wheel", SpinningWheelRecipe.class);

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Atum.MOD_ID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.QUERN.get()), QUERN);
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.SPINNING_WHEEL.get()), SPINNING_WHEEL);
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.KILN.get()), KILN, RecipeTypes.FUELING);
        registry.addRecipeCatalyst(new ItemStack(AtumBlocks.LIMESTONE_FURNACE.get()), RecipeTypes.SMELTING, RecipeTypes.FUELING);
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            addRecipes(registry, level, AtumRecipeTypes.KILN.get(), KILN);
            registry.addRecipes(KILN, RecipeHelper.getKilnRecipesFromFurnace(level.getRecipeManager(), level));
            addRecipes(registry, level, AtumRecipeTypes.QUERN.get(), QUERN);
            addRecipes(registry, level, AtumRecipeTypes.SPINNING_WHEEL.get(), SPINNING_WHEEL);
        }
        addInfo(new ItemStack(AtumItems.EMMER_DOUGH.get()), registry);
    }

    private <C extends Container, T extends Recipe<C>> void addRecipes(@Nonnull IRecipeRegistration registry, Level level, RecipeType<T> recipeType, mezz.jei.api.recipe.RecipeType jeiRecipeType) {
        registry.addRecipes(jeiRecipeType, RecipeHelper.getRecipes(level.getRecipeManager(), recipeType).stream().filter(r -> r.value().getIngredients().stream().noneMatch(Ingredient::isEmpty)).collect(Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new QuernRecipeCategory(guiHelper));
        registry.addRecipeCategories(new SpinningWheelRecipeCategory(guiHelper));
        registry.addRecipeCategories(new KilnRecipeCategory(guiHelper));
    }

    private void addInfo(ItemStack stack, IRecipeRegistration registry) {
        registry.addIngredientInfo(stack, VanillaTypes.ITEM_STACK, Component.translatable("jei." + stack.getItem().getDescriptionId()));
    }
}