package com.teammetallurgy.atum.init;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.teammetallurgy.atum.misc.recipe.RecipeHelper.addBrewingRecipeWithSubPotions;
import static com.teammetallurgy.atum.misc.recipe.RecipeHelper.addRecipe;
import static net.minecraft.world.item.alchemy.PotionUtils.setPotion;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<RecipeSerializer<?>> event) {
        AtumRecipes.addBrewingRecipes();
    }

    private static void addBrewingRecipes() {
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.DUSTY_BONE.get()), Potions.FIRE_RESISTANCE);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.ECTOPLASM.get()), Potions.INVISIBILITY);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.GLISTERING_DATE.get()), Potions.REGENERATION);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.KHNUMITE.get()), Potions.SLOWNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.MANDIBLES.get()), Potions.WEAKNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.SKELETAL_FISH.get()), Potions.WATER_BREATHING);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.WOLF_PELT.get()), Potions.SWIFTNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.OPHIDIAN_TONGUE_FLOWER.get()), Potions.POISON);

        //Anput's Fingers
        Ingredient anputsFingers = Ingredient.of(AtumItems.ANPUTS_FINGERS_SPORES.get());
        addRecipe(setPotion(new ItemStack(Items.POTION), Potions.WATER), anputsFingers, setPotion(new ItemStack(Items.POTION), Potions.AWKWARD));
        addRecipe(setPotion(new ItemStack(Items.SPLASH_POTION), Potions.WATER), anputsFingers, setPotion(new ItemStack(Items.SPLASH_POTION), Potions.AWKWARD));
        addRecipe(setPotion(new ItemStack(Items.LINGERING_POTION), Potions.WATER), anputsFingers, setPotion(new ItemStack(Items.LINGERING_POTION), Potions.AWKWARD));

        //Fertile Soil modifier (Glowstone)
        addRecipe(setPotion(new ItemStack(Items.POTION), Potions.LEAPING), new ItemStack(AtumItems.FERTILE_SOIL_PILE.get()), setPotion(new ItemStack(Items.POTION), Potions.STRONG_LEAPING));
        addRecipe(setPotion(new ItemStack(Items.POTION), Potions.SWIFTNESS), new ItemStack(AtumItems.FERTILE_SOIL_PILE.get()), setPotion(new ItemStack(Items.POTION), Potions.STRONG_SWIFTNESS));
        addRecipe(setPotion(new ItemStack(Items.POTION), Potions.HEALING), new ItemStack(AtumItems.FERTILE_SOIL_PILE.get()), setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING));
        addRecipe(setPotion(new ItemStack(Items.POTION), Potions.HARMING), new ItemStack(AtumItems.FERTILE_SOIL_PILE.get()), setPotion(new ItemStack(Items.POTION), Potions.STRONG_HARMING));
        addRecipe(setPotion(new ItemStack(Items.POTION), Potions.POISON), new ItemStack(AtumItems.FERTILE_SOIL_PILE.get()), setPotion(new ItemStack(Items.POTION), Potions.STRONG_POISON));
        addRecipe(setPotion(new ItemStack(Items.POTION), Potions.REGENERATION), new ItemStack(AtumItems.FERTILE_SOIL_PILE.get()), setPotion(new ItemStack(Items.POTION), Potions.STRONG_REGENERATION));
        addRecipe(setPotion(new ItemStack(Items.POTION), Potions.STRENGTH), new ItemStack(AtumItems.FERTILE_SOIL_PILE.get()), setPotion(new ItemStack(Items.POTION), Potions.STRONG_STRENGTH));
    }
}