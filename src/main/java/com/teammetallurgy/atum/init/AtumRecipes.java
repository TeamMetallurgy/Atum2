package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import com.teammetallurgy.atum.api.recipe.kiln.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.api.recipe.quern.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.spinningwheel.ISpinningWheelRecipe;
import com.teammetallurgy.atum.api.recipe.spinningwheel.SpinningWheelRecipe;
import com.teammetallurgy.atum.blocks.base.BlockAtumStairs;
import com.teammetallurgy.atum.blocks.glass.BlockAtumStainedGlass;
import com.teammetallurgy.atum.blocks.glass.BlockAtumStainedGlassPane;
import com.teammetallurgy.atum.blocks.linen.BlockLinen;
import com.teammetallurgy.atum.blocks.linen.BlockLinenCarpet;
import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityKiln;
import com.teammetallurgy.atum.blocks.stone.ceramic.BlockCeramic;
import com.teammetallurgy.atum.blocks.stone.ceramic.BlockCeramicSlab;
import com.teammetallurgy.atum.blocks.stone.ceramic.BlockCeramicTile;
import com.teammetallurgy.atum.blocks.stone.ceramic.BlockCeramicWall;
import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestoneBricks;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.BlacklistOreIngredient;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static com.teammetallurgy.atum.utils.recipe.RecipeHelper.*;
import static net.minecraft.block.BlockFlower.EnumFlowerType;
import static net.minecraft.potion.PotionUtils.addPotionToItemStack;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class AtumRecipes {

    @SubscribeEvent
    public static void registerRegistries(RegistryEvent.NewRegistry event) {
        RecipeHandlers.quernRecipes = (IForgeRegistryModifiable<IQuernRecipe>) AtumRegistry.makeRegistry("quern_recipes", IQuernRecipe.class);
        RecipeHandlers.spinningWheelRecipes = (IForgeRegistryModifiable<ISpinningWheelRecipe>) AtumRegistry.makeRegistry("spinning_wheel_recipes", ISpinningWheelRecipe.class);
        RecipeHandlers.kilnRecipes = (IForgeRegistryModifiable<IKilnRecipe>) AtumRegistry.makeRegistry("kiln_recipes", IKilnRecipe.class);
    }

    private static void register() {
        addSmeltingRecipes();
        addBrewingRecipes();
    }

    private static void addSmeltingRecipes() {
        GameRegistry.addSmelting(AtumBlocks.IRON_ORE, new ItemStack(Items.IRON_INGOT), 0.7F);
        GameRegistry.addSmelting(AtumBlocks.COAL_ORE, new ItemStack(Items.COAL), 0.1F);
        GameRegistry.addSmelting(AtumBlocks.REDSTONE_ORE, new ItemStack(Items.REDSTONE), 0.7F);
        GameRegistry.addSmelting(AtumBlocks.LAPIS_ORE, new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), 0.2F);
        GameRegistry.addSmelting(AtumBlocks.GOLD_ORE, new ItemStack(Items.GOLD_INGOT), 1.0F);
        GameRegistry.addSmelting(AtumBlocks.EMERALD_ORE, new ItemStack(Items.EMERALD), 1.0F);
        GameRegistry.addSmelting(AtumBlocks.DIAMOND_ORE, new ItemStack(Items.DIAMOND), 1.0F);
        GameRegistry.addSmelting(AtumBlocks.PALM_LOG, new ItemStack(Items.COAL, 1, 1), 0.15F);
        GameRegistry.addSmelting(AtumBlocks.DEADWOOD_LOG, new ItemStack(Items.COAL, 1, 1), 0.15F);
        GameRegistry.addSmelting(AtumBlocks.LIMESTONE_CRACKED, new ItemStack(AtumBlocks.LIMESTONE), 0.1F);
        GameRegistry.addSmelting(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE), new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CRACKED)), 0.1F);
        GameRegistry.addSmelting(AtumBlocks.LIMESTONE, new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CARVED)), 0.1F);
        GameRegistry.addSmelting(AtumBlocks.SAND, new ItemStack(AtumBlocks.CRYSTAL_GLASS), 0.1F);
        GameRegistry.addSmelting(AtumItems.JEWELED_FISH, new ItemStack(Items.GOLD_NUGGET, 3), 0.3F);
        GameRegistry.addSmelting(AtumItems.GOLD_COIN, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        GameRegistry.addSmelting(AtumItems.EMMER_DOUGH, new ItemStack(AtumItems.EMMER_BREAD), 0.1F);
        GameRegistry.addSmelting(AtumItems.CAMEL_RAW, new ItemStack(AtumItems.CAMEL_COOKED), 0.35F);
        GameRegistry.addSmelting(AtumItems.ECTOPLASM, new ItemStack(Items.SLIME_BALL), 0.1F);
    }

    private static void addBrewingRecipes() {
        addBrewingRecipeWithSubPotions("dustBlaze", PotionTypes.STRENGTH);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.DUSTY_BONE), PotionTypes.FIRE_RESISTANCE);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.ECTOPLASM), PotionTypes.INVISIBILITY);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.GLISTERING_DATE), PotionTypes.REGENERATION);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.KHNUMITE), PotionTypes.SLOWNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.MANDIBLES), PotionTypes.WEAKNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.SKELETAL_FISH), PotionTypes.WATER_BREATHING);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.WOLF_PELT), PotionTypes.SWIFTNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.OPHIDIAN_TONGUE_FLOWER), PotionTypes.POISON);

        //Anput's Fingers
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), "cropNetherWart", addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.WATER), "cropNetherWart", addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.AWKWARD));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.WATER), "cropNetherWart", addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.AWKWARD));

        //Fertile Soil modifier (Glowstone)
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LEAPING), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_LEAPING));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.SWIFTNESS), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_SWIFTNESS));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.HEALING), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_HEALING));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.HARMING), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_HARMING));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.POISON), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_POISON));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.REGENERATION), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_REGENERATION));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRENGTH), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_STRENGTH));
    }

    @SubscribeEvent
    public static void registerQuernRecipes(RegistryEvent.Register<IQuernRecipe> event) {
        addQuernRecipe("emmer_wheat", new QuernRecipe("cropEmmer", new ItemStack(AtumItems.EMMER_FLOUR), 2), event);
        addQuernRecipe("rod_blaze", new QuernRecipe("rodBlaze", new ItemStack(Items.BLAZE_POWDER, 3), 4), event);
        addQuernRecipe("marl", new QuernRecipe(AtumBlocks.MARL, new ItemStack(Items.CLAY_BALL, 3), 2), event);
        addQuernRecipe("gravel", new QuernRecipe("gravel", new ItemStack(Items.FLINT), 3), event);
        addQuernRecipe("sugarcane", new QuernRecipe("sugarcane", new ItemStack(Items.SUGAR, 2), 2), event);

        ////Dyes
        addQuernRecipe("black", new QuernRecipe(AtumItems.ANPUTS_FINGERS_SPORES, new ItemStack(AtumItems.DYE_BLACK, 2), 1), event);
        addQuernRecipe("brown_shrub", new QuernRecipe(AtumBlocks.SHRUB, new ItemStack(AtumItems.DYE_BROWN, 2), 1), event);
        addQuernRecipe("brown_weed", new QuernRecipe(AtumBlocks.WEED, new ItemStack(AtumItems.DYE_BROWN, 2), 1), event);
        addQuernRecipe("green", new QuernRecipe(AtumBlocks.OPHIDIAN_TONGUE, new ItemStack(Items.DYE, 2, EnumDyeColor.GREEN.getDyeDamage()), 1), event);
        addQuernRecipe("red", new QuernRecipe(AtumItems.DATE, new ItemStack(Items.DYE, 2, EnumDyeColor.RED.getDyeDamage()), 1), event);
        addFlowerRecipeOre(Blocks.YELLOW_FLOWER, EnumFlowerType.DANDELION, EnumDyeColor.YELLOW, event);
        addFlowerRecipeOre(Blocks.RED_FLOWER, EnumFlowerType.POPPY, EnumDyeColor.RED, event);
        addFlowerRecipeOre(Blocks.RED_FLOWER, EnumFlowerType.BLUE_ORCHID, EnumDyeColor.LIGHT_BLUE, event);
        addFlowerRecipeOre(Blocks.RED_FLOWER, EnumFlowerType.ALLIUM, EnumDyeColor.MAGENTA, event);
        addFlowerRecipeOre(Blocks.RED_FLOWER, EnumFlowerType.HOUSTONIA, EnumDyeColor.SILVER, event);
        addFlowerRecipeOre(Blocks.RED_FLOWER, EnumFlowerType.RED_TULIP, EnumDyeColor.RED, event);
        addFlowerRecipeOre(Blocks.RED_FLOWER, EnumFlowerType.ORANGE_TULIP, EnumDyeColor.ORANGE, event);
        addFlowerRecipeOre(Blocks.RED_FLOWER, EnumFlowerType.WHITE_TULIP, EnumDyeColor.SILVER, event);
        addFlowerRecipeOre(Blocks.RED_FLOWER, EnumFlowerType.PINK_TULIP, EnumDyeColor.PINK, event);
        addFlowerRecipeOre(Blocks.RED_FLOWER, EnumFlowerType.OXEYE_DAISY, EnumDyeColor.SILVER, event);
        addQuernRecipe("beetroot", new QuernRecipe(Items.BEETROOT, new ItemStack(Items.DYE, 2, EnumDyeColor.RED.getDyeDamage()), 2), event);
        addQuernRecipe("sunflower", new QuernRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta()), new ItemStack(Items.DYE, 4, EnumDyeColor.YELLOW.getDyeDamage()), 2), event);
        addQuernRecipe("lilac", new QuernRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta()), new ItemStack(Items.DYE, 4, EnumDyeColor.MAGENTA.getDyeDamage()), 2), event);
        addQuernRecipe("rose_bush", new QuernRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.ROSE.getMeta()), new ItemStack(Items.DYE, 4, EnumDyeColor.RED.getDyeDamage()), 2), event);
        addQuernRecipe("peony", new QuernRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta()), new ItemStack(Items.DYE, 4, EnumDyeColor.PINK.getDyeDamage()), 2), event);

        //BoP
        addQuernRecipe("rafflesia", new QuernRecipe("plantRafflesia", new ItemStack(Items.DYE, 3, EnumDyeColor.RED.getDyeDamage()), 2), event);
        addQuernRecipe("plant_flax", new QuernRecipe("plantFlax", new ItemStack(Items.DYE, 3, EnumDyeColor.LIGHT_BLUE.getDyeDamage()), 2), event);
        addQuernRecipe("clover", new QuernRecipe("flowerClover", new ItemStack(Items.DYE, 3, EnumDyeColor.SILVER.getDyeDamage()), 2), event);
        addQuernRecipe("swampflower", new QuernRecipe("flowerSwampflower", new ItemStack(Items.DYE, 3, EnumDyeColor.CYAN.getDyeDamage()), 2), event);
        addQuernRecipe("glowflower", new QuernRecipe("flowerGlowflower", new ItemStack(Items.DYE, 3, EnumDyeColor.CYAN.getDyeDamage()), 2), event);
        addQuernRecipe("blue_hydrangea", new QuernRecipe("flowerBlueHydrangea", new ItemStack(Items.DYE, 3, EnumDyeColor.LIGHT_BLUE.getDyeDamage()), 2), event);
        addQuernRecipe("orange_cosmos", new QuernRecipe("flowerOrangeCosmos", new ItemStack(Items.DYE, 3, EnumDyeColor.ORANGE.getDyeDamage()), 2), event);
        addQuernRecipe("pink_daffodil", new QuernRecipe("flowerPinkDaffodil", new ItemStack(Items.DYE, 3, EnumDyeColor.PINK.getDyeDamage()), 2), event);
        addQuernRecipe("wildflower", new QuernRecipe("flowerWildflower", new ItemStack(Items.DYE, 3, EnumDyeColor.MAGENTA.getDyeDamage()), 2), event);
        addQuernRecipe("violet_flower", new QuernRecipe("flowerViolet", new ItemStack(Items.DYE, 3, EnumDyeColor.PURPLE.getDyeDamage()), 2), event);
        addQuernRecipe("bromeliad", new QuernRecipe("flowerBromeliad", new ItemStack(Items.DYE, 3, EnumDyeColor.RED.getDyeDamage()), 2), event);
        addQuernRecipe("wilted_lily", new QuernRecipe("flowerWiltedLily", new ItemStack(Items.DYE, 3, EnumDyeColor.GRAY.getDyeDamage()), 2), event);
        addQuernRecipe("pink_hibiscus", new QuernRecipe("flowerPinkHibiscus", new ItemStack(Items.DYE, 3, EnumDyeColor.PINK.getDyeDamage()), 2), event);
        addQuernRecipe("burning_blossom", new QuernRecipe("flowerBurningBlossom", new ItemStack(Items.DYE, 3, EnumDyeColor.ORANGE.getDyeDamage()), 2), event);
        addQuernRecipe("miners_delight", new QuernRecipe("flowerMinersDelight", new ItemStack(Items.DYE, 3, EnumDyeColor.PINK.getDyeDamage()), 2), event);
        addQuernRecipe("icy_iris", new QuernRecipe("flowerIcyIris", new ItemStack(Items.DYE, 3, EnumDyeColor.LIGHT_BLUE.getDyeDamage()), 2), event);
        addQuernRecipe("rose", new QuernRecipe("flowerRose", new ItemStack(Items.DYE, 3, EnumDyeColor.RED.getDyeDamage()), 2), event);
    }

    @SubscribeEvent
    public static void registerKilnRecipes(RegistryEvent.Register<IKilnRecipe> event) {
        AtumRegistry.registerRecipe("marl", new KilnRecipe(AtumBlocks.MARL, new ItemStack(BlockCeramic.getCeramicBlocks(EnumDyeColor.WHITE)), 0.1F), event);
    }

    public static void addKilnRecipes() {
        //Add valid vanilla & modded recipes
        for (ItemStack input : FurnaceRecipes.instance().getSmeltingList().keySet()) {
            ItemStack output = FurnaceRecipes.instance().getSmeltingList().get(input);
            String inputId = Objects.requireNonNull(input.getItem().getRegistryName()).getPath();
            String outputID = Objects.requireNonNull(output.getItem().getRegistryName()).getPath();
            if (!input.isEmpty() && !TileEntityKiln.canKilnNotSmelt(input) && !TileEntityKiln.canKilnNotSmelt(output)) {
                int inMeta = input.getMetadata();
                int outMeta = output.getMetadata();
                ResourceLocation id = new ResourceLocation(Constants.MOD_ID, inputId + "_" + (inMeta > 0 && inMeta != OreDictionary.WILDCARD_VALUE ? inMeta + "_" : "") +
                        outputID + (outMeta > 0 && outMeta != OreDictionary.WILDCARD_VALUE ? "_" + outMeta : ""));
                if (!RecipeHandlers.kilnRecipes.containsKey(id)) {
                    RecipeHandlers.kilnRecipes.register(new KilnRecipe(input, output, FurnaceRecipes.instance().getSmeltingExperience(output)).setRegistryName(id));
                } else {
                    Atum.LOG.error("Kiln Recipe with ID: " + id + " already exists");
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerSpinningwheelRecipes(RegistryEvent.Register<ISpinningWheelRecipe> event) {
        AtumRegistry.registerRecipe("flax", new SpinningWheelRecipe("cropFlax", new ItemStack(AtumItems.LINEN_THREAD, 3), 4), event);
        AtumRegistry.registerRecipe("wolf_pelt", new SpinningWheelRecipe(AtumItems.WOLF_PELT, new ItemStack(Items.STRING, 2), 5), event);
        AtumRegistry.registerRecipe("cloth_scrap", new SpinningWheelRecipe(AtumItems.SCRAP, new ItemStack(AtumItems.LINEN_THREAD), 2), event);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistry<IRecipe> registry = event.getRegistry();
        final ResourceLocation crystal = new ResourceLocation(Constants.MOD_ID, "crystal_glass");
        final ResourceLocation framed = new ResourceLocation(Constants.MOD_ID, "framed_glass");
        final ResourceLocation linen = new ResourceLocation(Constants.MOD_ID, "linen");
        final ResourceLocation ceramic = new ResourceLocation(Constants.MOD_ID, "ceramic");

        for (EnumDyeColor color : EnumDyeColor.values()) {
            String colorName = StringUtils.capitalize(color.getTranslationKey().replace("silver", "lightGray"));
            AtumRegistry.registerRecipe("crystal_" + colorName, new ShapedOreRecipe(crystal, new ItemStack(BlockAtumStainedGlass.getGlass(AtumBlocks.CRYSTAL_GLASS, color), 8), "GGG", "GDG", "GGG", 'G', AtumBlocks.CRYSTAL_GLASS, 'D', "dye" + colorName), event);
            AtumRegistry.registerRecipe("framed_" + colorName, new ShapedOreRecipe(framed, new ItemStack(BlockAtumStainedGlass.getGlass(AtumBlocks.FRAMED_GLASS, color), 8), "GGG", "GDG", "GGG", 'G', AtumBlocks.FRAMED_GLASS, 'D', "dye" + colorName), event);
            AtumRegistry.registerRecipe("crystal_to_framed_" + colorName, new ShapedOreRecipe(framed, BlockAtumStainedGlass.getGlass(AtumBlocks.FRAMED_GLASS, color), " S ", "SGS", " S ", 'S', "stickWood", 'G', BlockAtumStainedGlass.getGlass(AtumBlocks.CRYSTAL_GLASS, color)), event);
            AtumRegistry.registerRecipe("thin_crystal_" + colorName, new ShapedOreRecipe(crystal, new ItemStack(BlockAtumStainedGlassPane.getGlass(AtumBlocks.CRYSTAL_GLASS, color), 16), "GGG", "GGG", 'G', BlockAtumStainedGlass.getGlass(AtumBlocks.CRYSTAL_GLASS, color)), event);
            AtumRegistry.registerRecipe("thin_framed_" + colorName, new ShapedOreRecipe(framed, new ItemStack(BlockAtumStainedGlassPane.getGlass(AtumBlocks.FRAMED_GLASS, color), 16), "GGG", "GGG", 'G', BlockAtumStainedGlass.getGlass(AtumBlocks.FRAMED_GLASS, color)), event);
            if (color != EnumDyeColor.WHITE) {
                AtumRegistry.registerRecipe("linen_" + colorName, new ShapelessOreRecipe(linen, new ItemStack(BlockLinen.getLinen(color)), "dye" + colorName, BlockLinen.getLinen(EnumDyeColor.WHITE)), event);
                AtumRegistry.registerRecipe("ceramic_" + colorName, new ShapelessOreRecipe(ceramic, new ItemStack(BlockCeramic.getCeramicBlocks(color)), "dye" + colorName, BlockCeramic.getCeramicBlocks(EnumDyeColor.WHITE)), event);
            }
            AtumRegistry.registerRecipe("ceramic_tile_" + colorName, new ShapedOreRecipe(ceramic, new ItemStack(BlockCeramicTile.getTile(color), 3), "CC", 'C', BlockCeramic.getCeramicBlocks(color)), event);
            AtumRegistry.registerRecipe("ceramic_slab_" + colorName, new ShapedOreRecipe(ceramic, new ItemStack(BlockCeramicSlab.getSlab(color), 6), "CCC", 'C', BlockCeramic.getCeramicBlocks(color)), event);
            AtumRegistry.registerRecipe("ceramic_stairs_" + colorName, new ShapedOreRecipe(ceramic, new ItemStack(BlockAtumStairs.getCeramicStairs(color), 4), "C  ", "CC ", "CCC", 'C', BlockCeramic.getCeramicBlocks(color)).setMirrored(true), event);
            AtumRegistry.registerRecipe("ceramic_wall_" + colorName, new ShapedOreRecipe(ceramic, new ItemStack(BlockCeramicWall.getWall(color), 6), "CCC", "CCC", 'C', BlockCeramic.getCeramicBlocks(color)), event);
            AtumRegistry.registerRecipe("linen_carpet_" + colorName, new ShapedOreRecipe(linen, new ItemStack(BlockLinenCarpet.getLinenBlock(color), 3), "LL", 'L', BlockLinen.getLinen(color)), event);
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

        //Stick
        recipes.remove(stick);
        registry.register(new ShapedOreRecipe(stick, new ItemStack(Items.STICK, 4), "P", "P", 'P', "plankWood").setRegistryName(new ResourceLocation(Constants.MOD_ID, "stick"))); //Modded planks
        registry.register(new ShapedOreRecipe(stick, new ItemStack(Items.STICK, 4), "P", "P", 'P', new ItemStack(Blocks.PLANKS, 1, OreDictionary.WILDCARD_VALUE)).setRegistryName(stick));

        //Torch
        recipes.remove(torch);
        registry.register(new ShapedOreRecipe(torch, new ItemStack(Blocks.TORCH, 4), "C", "S", 'C', new ItemStack(Items.COAL, 1, OreDictionary.WILDCARD_VALUE), 'S', Items.STICK).setRegistryName(torch));

        //Ladder
        recipes.remove(ladder);
        registry.register(new ShapedOreRecipe(ladder, new ItemStack(Blocks.LADDER, 3), "S S", "SSS", "S S", 'S', "stickWood").setRegistryName(ladder));

        //Chest
        recipes.remove(chest);
        registry.register(new ShapedOreRecipe(chest, new ItemStack(Blocks.CHEST), "PPP", "P P", "PPP", 'P', "plankWood").setRegistryName(new ResourceLocation(Constants.MOD_ID, "chest"))); //Modded chests
        registry.register(new ShapedOreRecipe(chest, new ItemStack(Blocks.CHEST), "PPP", "P P", "PPP", 'P', new ItemStack(Blocks.PLANKS, 1, OreDictionary.WILDCARD_VALUE)).setRegistryName(chest));

        //Trapdoor
        recipes.remove(trapdoor);
        registry.register(new ShapedOreRecipe(trapdoor, new ItemStack(Blocks.TRAPDOOR, 2), "PPP", "PPP", 'P', "plankWood").setRegistryName(trapdoor));

        ////Cracked Limestone
        final ResourceLocation sword = new ResourceLocation("stone_sword");
        final ResourceLocation shovel = new ResourceLocation("stone_shovel");
        final ResourceLocation pickaxe = new ResourceLocation("stone_pickaxe");
        final ResourceLocation hoe = new ResourceLocation("stone_hoe");
        final ResourceLocation axe = new ResourceLocation("stone_axe");
        final ResourceLocation furnace = new ResourceLocation("furnace");

        Ingredient cobblestone = new BlacklistOreIngredient("cobblestone", stack -> stack.getItem() == Item.getItemFromBlock(AtumBlocks.LIMESTONE_CRACKED));

        //Sword
        recipes.remove(sword);
        registry.register(new ShapedOreRecipe(sword, Items.STONE_SWORD, "C", "C", "S", 'C', cobblestone, 'S', "stickWood").setRegistryName(sword));

        //Shovel
        recipes.remove(shovel);
        registry.register(new ShapedOreRecipe(shovel, Items.STONE_SHOVEL, "C", "S", "S", 'C', cobblestone, 'S', "stickWood").setRegistryName(shovel));

        //Pickaxe
        recipes.remove(pickaxe);
        registry.register(new ShapedOreRecipe(pickaxe, Items.STONE_PICKAXE, "CCC", " S ", " S ", 'C', cobblestone, 'S', "stickWood").setRegistryName(pickaxe));

        //Hoe
        recipes.remove(hoe);
        registry.register(new ShapedOreRecipe(hoe, Items.STONE_HOE, "CC", " S", " S", 'C', cobblestone, 'S', "stickWood").setRegistryName(hoe));

        //Axe
        recipes.remove(axe);
        registry.register(new ShapedOreRecipe(axe, Items.STONE_AXE, "CC", "CS", " S", 'C', cobblestone, 'S', "stickWood").setRegistryName(axe));

        //Furnace
        recipes.remove(furnace);
        registry.register(new ShapedOreRecipe(furnace, Blocks.FURNACE, "CCC", "C C", "CCC", 'C', cobblestone).setRegistryName(furnace));
    }
}