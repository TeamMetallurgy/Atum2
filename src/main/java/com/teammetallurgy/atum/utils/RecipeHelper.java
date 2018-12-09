package com.teammetallurgy.atum.utils;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;

import static net.minecraft.potion.PotionUtils.addPotionToItemStack;
import static net.minecraftforge.common.brewing.BrewingRecipeRegistry.addRecipe;

public class RecipeHelper {

    public static void addBrewingRecipeWithSubPotions(ItemStack ingredient, PotionType potionType) {
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), ingredient, addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), ingredient, addPotionToItemStack(new ItemStack(Items.POTIONITEM), potionType));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.AWKWARD), ingredient, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.WATER), ingredient, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.WATER), ingredient, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.AWKWARD), ingredient, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), potionType));
    }

    public static void addBrewingRecipeWithSubPotions(String oreDict, PotionType potionType) {
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), oreDict, addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), oreDict, addPotionToItemStack(new ItemStack(Items.POTIONITEM), potionType));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.AWKWARD), oreDict, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.WATER), oreDict, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.WATER), oreDict, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.AWKWARD), oreDict, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), potionType));
    }
}