package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.teammetallurgy.atum.misc.recipe.RecipeHelper.addBrewingRecipeWithSubPotions;
import static com.teammetallurgy.atum.misc.recipe.RecipeHelper.addRecipe;
import static net.minecraft.potion.PotionUtils.addPotionToItemStack;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        AtumRecipes.addBrewingRecipes();
    }

    private static void addBrewingRecipes() {
        //addBrewingRecipeWithSubPotions(AtumAPI.Tags.DUSTS_BLAZE, Potions.STRENGTH); //TODO
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.DUSTY_BONE), Potions.FIRE_RESISTANCE);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.ECTOPLASM), Potions.INVISIBILITY);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.GLISTERING_DATE), Potions.REGENERATION);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.KHNUMITE), Potions.SLOWNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.MANDIBLES), Potions.WEAKNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.SKELETAL_FISH), Potions.WATER_BREATHING);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.WOLF_PELT), Potions.SWIFTNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.OPHIDIAN_TONGUE_FLOWER), Potions.POISON);

        //Anput's Fingers
        /*Ingredient cropNetherWart = Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART); //TODO
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER), cropNetherWart, addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.WATER), cropNetherWart, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.AWKWARD));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), Potions.WATER), cropNetherWart, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), Potions.AWKWARD));*/

        //Fertile Soil modifier (Glowstone)
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.LEAPING), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_LEAPING));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.SWIFTNESS), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_SWIFTNESS));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.HEALING), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_HEALING));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.HARMING), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_HARMING));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.POISON), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_POISON));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.REGENERATION), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_REGENERATION));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRENGTH), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_STRENGTH));
    }
}