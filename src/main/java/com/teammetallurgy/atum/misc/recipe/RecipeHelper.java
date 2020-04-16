package com.teammetallurgy.atum.misc.recipe;

import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.misc.AtumRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

import static net.minecraft.potion.PotionUtils.addPotionToItemStack;

public class RecipeHelper {

    /* Brewing Helpers*/
    public static void addBrewingRecipeWithSubPotions(ItemStack stack, Potion potionType) {
        addBrewingRecipeWithSubPotions(Ingredient.fromStacks(stack), potionType);
    }

    public static void addBrewingRecipeWithSubPotions(Tag<Item> tag, Potion potionType) {
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

    public static void addQuernRecipe(String registryName, IQuernRecipe quernRecipe, RegistryEvent.Register<IQuernRecipe> event) {
        if (!quernRecipe.getInput().isEmpty()) {
            AtumRegistry.registerRecipe(registryName, quernRecipe, event);
        }
    }

    public static <C extends IInventory, T extends IRecipe<C>> Collection<T> getRecipes(RecipeManager recipeManager, IRecipeType<T> recipeType) {
        Map<ResourceLocation, IRecipe<C>> recipesMap = recipeManager.getRecipes(recipeType);
        return (Collection<T>) recipesMap.values();
    }
}