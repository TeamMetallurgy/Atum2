package com.teammetallurgy.atum.utils;

import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.api.recipe.quern.QuernRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import org.apache.commons.lang3.StringUtils;

import static net.minecraft.potion.PotionUtils.addPotionToItemStack;
import static net.minecraftforge.common.brewing.BrewingRecipeRegistry.addRecipe;

public class RecipeHelper {

    /* Potion Helpers*/
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

    public static void addFlowerRecipeOre(Block flowerBlock, BlockFlower.EnumFlowerType flowerType, EnumDyeColor color, RegistryEvent.Register<IQuernRecipe> event) {
        ItemStack flower = new ItemStack(flowerBlock, 1, flowerType.getMeta());
        String oreDict = "flower" + StringUtils.capitalize(color.getTranslationKey());
        OreDictHelper.add(flower, oreDict);
        if (!event.getRegistry().containsKey(new ResourceLocation(Constants.MOD_ID, oreDict))) {
            addQuernRecipe(oreDict, new QuernRecipe(oreDict, new ItemStack(Items.DYE, 2, color.getDyeDamage()), 2), event);
        }
    }

    public static void addQuernRecipe(String registryName, IQuernRecipe quernRecipe, RegistryEvent.Register<IQuernRecipe> event) {
        if (!quernRecipe.getInput().isEmpty()) {
            AtumRegistry.registerRecipe(registryName, quernRecipe, event);
        }
    }
}