package com.teammetallurgy.atum.misc.recipe;

import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static net.minecraft.potion.PotionUtils.addPotionToItemStack;

public class RecipeHelper {

    /* Brewing Helpers*/
    public static void addBrewingRecipeWithSubPotions(ItemStack stack, Potion potionType) {
        addBrewingRecipeWithSubPotions(Ingredient.fromStacks(stack), potionType);
    }

    public static void addBrewingRecipeWithSubPotions(Tags.IOptionalNamedTag<Item> tag, Potion potionType) {
        addBrewingRecipeWithSubPotions(Ingredient.fromTag(tag), potionType);
    }

    public static void addBrewingRecipeWithSubPotions(Ingredient ingredient, Potion potionType) {
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER), ingredient, addPotionToItemStack(new ItemStack(Items.POTION), Potions.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD), ingredient, addPotionToItemStack(new ItemStack(Items.POTION), potionType));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.AWKWARD), ingredient, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.WATER), ingredient, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), Potions.WATER), ingredient, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), Potions.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), Potions.AWKWARD), ingredient, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), potionType));
    }

    public static boolean addRecipe(@Nonnull ItemStack input, @Nonnull ItemStack ingredient, @Nonnull ItemStack output) {
        return addRecipe(input, Ingredient.fromStacks(ingredient), output);
    }

    public static boolean addRecipe(@Nonnull ItemStack input, @Nonnull Ingredient ingredient, @Nonnull ItemStack output) {
        return BrewingRecipeRegistry.addRecipe(new BrewingNBT(Ingredient.fromStacks(input), ingredient, output));
    }

    public static <C extends IInventory, T extends IRecipe<C>> Collection<T> getRecipes(RecipeManager recipeManager, IRecipeType<T> recipeType) {
        Map<ResourceLocation, IRecipe<C>> recipesMap = recipeManager.getRecipes(recipeType);
        return (Collection<T>) recipesMap.values();
    }

    public static <C extends IInventory, T extends IRecipe<C>> boolean isItemValidForSlot(World world, @Nonnull ItemStack stack, IRecipeType<T> recipeType) {
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            Collection<T> recipes = RecipeHelper.getRecipes(serverWorld.getRecipeManager(), recipeType);
            for (IRecipe<C> recipe : recipes) {
                for (Ingredient ingredient : recipe.getIngredients()) {
                    if (StackHelper.areIngredientsEqualIgnoreSize(ingredient, stack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static  <C extends IInventory, T extends IRecipe<C>> Boolean isValidRecipeInput(Collection<T> recipes, @Nonnull ItemStack input) {
        for (IRecipe<C> recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (StackHelper.areIngredientsEqualIgnoreSize(ingredient, input)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Collection<KilnRecipe> getKilnRecipesFromFurnace(RecipeManager recipeManager) {
        Collection<KilnRecipe> kilnRecipes = new ArrayList<>();
        for (FurnaceRecipe furnaceRecipe : RecipeHelper.getRecipes(recipeManager, IRecipeType.SMELTING)) {
            for (Ingredient input : furnaceRecipe.getIngredients()) {
                ItemStack output = furnaceRecipe.getRecipeOutput();
                if (input != null && !output.isEmpty()) {
                    if (!KilnTileEntity.canKilnNotSmelt(input) && !KilnTileEntity.canKilnNotSmelt(output)) {
                        kilnRecipes.add(new KilnRecipe(input, output, furnaceRecipe.getExperience(), furnaceRecipe.getCookTime()));
                    }
                }
            }
        }
        return kilnRecipes;
    }
}