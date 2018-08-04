package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.blocks.glass.BlockAtumStainedGlass;
import com.teammetallurgy.atum.blocks.glass.BlockAtumStainedGlassPane;
import com.teammetallurgy.atum.handler.BlacklistOreIngredient;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.apache.commons.lang3.StringUtils;

import static net.minecraft.potion.PotionUtils.addPotionToItemStack;
import static net.minecraftforge.common.brewing.BrewingRecipeRegistry.addRecipe;

@Mod.EventBusSubscriber
public class AtumRecipes {
    private static void register() {
        addSmeltingRecipes();
        addBrewingRecipes();
    }

    private static void addSmeltingRecipes() {
        GameRegistry.addSmelting(AtumBlocks.IRON_ORE, new ItemStack(Items.IRON_INGOT), 0.7F);
        GameRegistry.addSmelting(AtumBlocks.COAL_ORE, new ItemStack(Items.COAL), 0.1F);
        GameRegistry.addSmelting(AtumBlocks.REDSTONE_ORE, new ItemStack(Items.REDSTONE), 0.7F);
        GameRegistry.addSmelting(AtumBlocks.LAPIS_ORE, new ItemStack(Items.DYE, 1, 4), 0.2F);
        GameRegistry.addSmelting(AtumBlocks.GOLD_ORE, new ItemStack(Items.GOLD_INGOT), 1.0F);
        GameRegistry.addSmelting(AtumBlocks.DIAMOND_ORE, new ItemStack(Items.DIAMOND), 1.0F);
        GameRegistry.addSmelting(AtumBlocks.PALM_LOG, new ItemStack(Items.COAL, 1, 1), 0.15F);
        GameRegistry.addSmelting(AtumBlocks.DEADWOOD_LOG, new ItemStack(Items.COAL, 1, 1), 0.15F);
        GameRegistry.addSmelting(AtumBlocks.LIMESTONE_CRACKED, new ItemStack(AtumBlocks.LIMESTONE), 0.1F);
        GameRegistry.addSmelting(AtumBlocks.SAND, new ItemStack(AtumBlocks.CRYSTAL_GLASS), 0.1F);
    }

    private static void addBrewingRecipes() {
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.MANDIBLES), PotionTypes.WEAKNESS);
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), new ItemStack(AtumItems.ECTOPLASM), new ItemStack(Items.EXPERIENCE_BOTTLE));
    }

    private static void addBrewingRecipeWithSubPotions(ItemStack ingredient, PotionType potionType) {
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), ingredient, addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), ingredient, addPotionToItemStack(new ItemStack(Items.POTIONITEM), potionType));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.AWKWARD), ingredient, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.WATER), ingredient, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.WATER), ingredient, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.AWKWARD), ingredient, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), potionType));
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistry<IRecipe> registry = event.getRegistry();
        final ResourceLocation crystal = new ResourceLocation(Constants.MOD_ID, "crystal_glass");
        final ResourceLocation framed = new ResourceLocation(Constants.MOD_ID, "framed_glass");

        for (EnumDyeColor color : EnumDyeColor.values()) {
            String colorName = StringUtils.capitalize(color.getUnlocalizedName().replace("silver", "lightGray"));
            registry.register(new ShapedOreRecipe(crystal, new ItemStack(BlockAtumStainedGlass.getGlass(AtumBlocks.CRYSTAL_GLASS, color), 8), "GGG", "GDG", "GGG", 'G', AtumBlocks.CRYSTAL_GLASS, 'D', "dye" + colorName).setRegistryName(new ResourceLocation(Constants.MOD_ID, "crystal_" + colorName)));
            registry.register(new ShapedOreRecipe(framed, new ItemStack(BlockAtumStainedGlass.getGlass(AtumBlocks.FRAMED_GLASS, color), 8), "GGG", "GDG", "GGG", 'G', AtumBlocks.FRAMED_GLASS, 'D', "dye" + colorName).setRegistryName(new ResourceLocation(Constants.MOD_ID, "framed_" + colorName)));
            registry.register(new ShapedOreRecipe(framed, BlockAtumStainedGlass.getGlass(AtumBlocks.FRAMED_GLASS, color), " S ", "SGS", " S ", 'S', Items.STICK, 'G', BlockAtumStainedGlass.getGlass(AtumBlocks.CRYSTAL_GLASS, color)).setRegistryName(new ResourceLocation(Constants.MOD_ID, "crystal_to_framed_" + colorName)));
            registry.register(new ShapedOreRecipe(crystal, new ItemStack(BlockAtumStainedGlassPane.getGlass(AtumBlocks.CRYSTAL_GLASS, color), 16), "GGG", "GGG", 'G', BlockAtumStainedGlass.getGlass(AtumBlocks.CRYSTAL_GLASS, color)).setRegistryName(new ResourceLocation(Constants.MOD_ID, "thin_crystal_" + colorName)));
            registry.register(new ShapedOreRecipe(framed, new ItemStack(BlockAtumStainedGlassPane.getGlass(AtumBlocks.FRAMED_GLASS, color), 16), "GGG", "GGG", 'G', BlockAtumStainedGlass.getGlass(AtumBlocks.FRAMED_GLASS, color)).setRegistryName(new ResourceLocation(Constants.MOD_ID, "thin_framed_" + colorName)));
        }
        AtumRecipes.register();
        fixOreDictEntries(registry);
    }

    private static void fixOreDictEntries(IForgeRegistry<IRecipe> registry) {
        IForgeRegistryModifiable<IRecipe> recipes = (IForgeRegistryModifiable<IRecipe>) registry;
        final ResourceLocation stick = new ResourceLocation("stick");
        final ResourceLocation torch = new ResourceLocation("torch");
        final ResourceLocation ladder = new ResourceLocation("ladder");
        final ResourceLocation chest = new ResourceLocation("chest");
        final ResourceLocation trapdoor = new ResourceLocation("trapdoor");

        Ingredient plankWood = new BlacklistOreIngredient("plankWood", stack -> stack.getItem() == Item.getItemFromBlock(Blocks.PLANKS));

        //Stick
        recipes.remove(stick);
        registry.register(new ShapedOreRecipe(stick, new ItemStack(Items.STICK, 4), "P", "P", 'P', plankWood).setRegistryName(new ResourceLocation(Constants.MOD_ID, "stick"))); //Modded planks
        registry.register(new ShapedOreRecipe(stick, new ItemStack(Items.STICK, 4), "P", "P", 'P', new ItemStack(Blocks.PLANKS, 1, OreDictionary.WILDCARD_VALUE)).setRegistryName(stick));

        //Torch
        recipes.remove(torch);
        registry.register(new ShapedOreRecipe(torch, new ItemStack(Blocks.TORCH, 4), "C", "S", 'C', new ItemStack(Items.COAL, 1, OreDictionary.WILDCARD_VALUE), 'S', Items.STICK).setRegistryName(torch));

        //Ladder
        recipes.remove(ladder);
        registry.register(new ShapedOreRecipe(ladder, new ItemStack(Blocks.LADDER, 3), "S S", "SSS", "S S", 'S', "stickWood").setRegistryName(ladder));

        //Chest
        recipes.remove(chest);
        registry.register(new ShapedOreRecipe(chest, new ItemStack(Blocks.CHEST), "PPP", "P P", "PPP", 'P', plankWood).setRegistryName(new ResourceLocation(Constants.MOD_ID, "chest"))); //Modded chests
        registry.register(new ShapedOreRecipe(chest, new ItemStack(Blocks.CHEST), "PPP", "P P", "PPP", 'P', new ItemStack(Blocks.PLANKS, 1, OreDictionary.WILDCARD_VALUE)).setRegistryName(chest));

        //Trapdoor
        recipes.remove(trapdoor);
        registry.register(new ShapedOreRecipe(trapdoor, new ItemStack(Blocks.TRAPDOOR, 2), "PPP", "PPP", 'P', "plankWood").setRegistryName(trapdoor));
    }
}