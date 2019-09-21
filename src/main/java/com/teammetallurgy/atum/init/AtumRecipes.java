package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import com.teammetallurgy.atum.api.recipe.kiln.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.api.recipe.quern.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.spinningwheel.ISpinningWheelRecipe;
import com.teammetallurgy.atum.api.recipe.spinningwheel.SpinningWheelRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityKiln;
import com.teammetallurgy.atum.blocks.stone.ceramic.BlockCeramic;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import static com.teammetallurgy.atum.utils.recipe.RecipeHelper.*;
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

    private static void addSmeltingRecipes() { //TODO Move to json
        /*GameRegistry.addSmelting(AtumBlocks.IRON_ORE, new ItemStack(Items.IRON_INGOT), 0.7F);
        GameRegistry.addSmelting(AtumBlocks.COAL_ORE, new ItemStack(Items.COAL), 0.1F);
        GameRegistry.addSmelting(AtumBlocks.REDSTONE_ORE, new ItemStack(Items.REDSTONE), 0.7F);
        GameRegistry.addSmelting(AtumBlocks.LAPIS_ORE, new ItemStack(Items.DYE, 1, DyeColor.BLUE.getDyeDamage()), 0.2F);
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
        GameRegistry.addSmelting(AtumItems.ECTOPLASM, new ItemStack(Items.SLIME_BALL), 0.1F);*/
    }

    private static void addBrewingRecipes() {
        addBrewingRecipeWithSubPotions(AtumAPI.Tags.DUSTS_BLAZE, Potions.STRENGTH);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.DUSTY_BONE), Potions.FIRE_RESISTANCE);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.ECTOPLASM), Potions.INVISIBILITY);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.GLISTERING_DATE), Potions.REGENERATION);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.KHNUMITE), Potions.SLOWNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.MANDIBLES), Potions.WEAKNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.SKELETAL_FISH), Potions.WATER_BREATHING);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.WOLF_PELT), Potions.SWIFTNESS);
        addBrewingRecipeWithSubPotions(new ItemStack(AtumItems.OPHIDIAN_TONGUE_FLOWER), Potions.POISON);

        //Anput's Fingers //TODO Check if this is needed anymore, due to it being Tagged by Forge now
        Ingredient cropNetherWart = Ingredient.fromTag(Tags.Items.CROPS_NETHER_WART);
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER), cropNetherWart, addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.WATER), cropNetherWart, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.AWKWARD));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), Potions.WATER), cropNetherWart, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), Potions.AWKWARD));

        //Fertile Soil modifier (Glowstone)
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.LEAPING), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_LEAPING));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.SWIFTNESS), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_SWIFTNESS));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.HEALING), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_HEALING));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.HARMING), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_HARMING));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.POISON), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_POISON));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.REGENERATION), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_REGENERATION));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRENGTH), new ItemStack(AtumItems.FERTILE_SOIL_PILE), addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRONG_STRENGTH));
    }

    @SubscribeEvent
    public static void registerQuernRecipes(RegistryEvent.Register<IQuernRecipe> event) {
        addQuernRecipe("emmer_wheat", new QuernRecipe(AtumAPI.Tags.CROPS_EMMER, new ItemStack(AtumItems.EMMER_FLOUR), 2), event);
        addQuernRecipe("rod_blaze", new QuernRecipe(Tags.Items.RODS_BLAZE, new ItemStack(Items.BLAZE_POWDER, 3), 4), event);
        addQuernRecipe("marl", new QuernRecipe(AtumBlocks.MARL, new ItemStack(Items.CLAY_BALL, 3), 2), event);
        addQuernRecipe("gravel", new QuernRecipe(Tags.Items.GRAVEL, new ItemStack(Items.FLINT), 3), event);
        addQuernRecipe("sugarcane", new QuernRecipe(AtumAPI.Tags.SUGAR_CANE, new ItemStack(Items.SUGAR, 2), 2), event);

        ////Dyes
        addQuernRecipe("black", new QuernRecipe(AtumItems.ANPUTS_FINGERS_SPORES, new ItemStack(Items.BLACK_DYE, 2), 1), event);
        addQuernRecipe("brown_shrub", new QuernRecipe(AtumBlocks.SHRUB, new ItemStack(Items.BROWN_DYE, 2), 1), event);
        addQuernRecipe("brown_weed", new QuernRecipe(AtumBlocks.WEED, new ItemStack(Items.BROWN_DYE, 2), 1), event);
        addQuernRecipe("green", new QuernRecipe(AtumBlocks.OPHIDIAN_TONGUE, new ItemStack(Items.GREEN_DYE, 2), 1), event);
        addQuernRecipe("red", new QuernRecipe(AtumItems.DATE, new ItemStack(Items.RED_DYE, 2), 1), event);
        addQuernRecipe("flowersWhite", new QuernRecipe(AtumAPI.Tags.FLOWERS_WHITE, new ItemStack(Items.WHITE_DYE, 2), 1), event);
        addQuernRecipe("flowersOrange", new QuernRecipe(AtumAPI.Tags.FLOWERS_ORANGE, new ItemStack(Items.ORANGE_DYE, 2), 1), event);
        addQuernRecipe("flowersMagenta", new QuernRecipe(AtumAPI.Tags.FLOWERS_MAGENTA, new ItemStack(Items.MAGENTA_DYE, 2), 1), event);
        addQuernRecipe("flowersLightBlue", new QuernRecipe(AtumAPI.Tags.FLOWERS_LIGHT_BLUE, new ItemStack(Items.LIGHT_BLUE_DYE, 2), 1), event);
        addQuernRecipe("flowersYellow", new QuernRecipe(AtumAPI.Tags.FLOWERS_YELLOW, new ItemStack(Items.YELLOW_DYE, 2), 1), event);
        addQuernRecipe("flowersLime", new QuernRecipe(AtumAPI.Tags.FLOWERS_LIME, new ItemStack(Items.LIME_DYE, 2), 1), event);
        addQuernRecipe("flowersPink", new QuernRecipe(AtumAPI.Tags.FLOWERS_PINK, new ItemStack(Items.PINK_DYE, 2), 1), event);
        addQuernRecipe("flowersGray", new QuernRecipe(AtumAPI.Tags.FLOWERS_GRAY, new ItemStack(Items.GRAY_DYE, 2), 1), event);
        addQuernRecipe("flowersLightGray", new QuernRecipe(AtumAPI.Tags.FLOWERS_LIGHT_GRAY, new ItemStack(Items.LIGHT_GRAY_DYE, 2), 1), event);
        addQuernRecipe("flowersCyan", new QuernRecipe(AtumAPI.Tags.FLOWERS_CYAN, new ItemStack(Items.CYAN_DYE, 2), 1), event);
        addQuernRecipe("flowersPurple", new QuernRecipe(AtumAPI.Tags.FLOWERS_PURPLE, new ItemStack(Items.PURPLE_DYE, 2), 1), event);
        addQuernRecipe("flowersBlue", new QuernRecipe(AtumAPI.Tags.FLOWERS_BLUE, new ItemStack(Items.BLUE_DYE, 2), 1), event);
        addQuernRecipe("flowersBrown", new QuernRecipe(AtumAPI.Tags.FLOWERS_BROWN, new ItemStack(Items.BROWN_DYE, 2), 1), event);
        addQuernRecipe("flowersGreen", new QuernRecipe(AtumAPI.Tags.FLOWERS_GREEN, new ItemStack(Items.GREEN_DYE, 2), 1), event);
        addQuernRecipe("flowersRed", new QuernRecipe(AtumAPI.Tags.FLOWERS_RED, new ItemStack(Items.RED_DYE, 2), 1), event);
        addQuernRecipe("flowersBlack", new QuernRecipe(AtumAPI.Tags.FLOWERS_BLACK, new ItemStack(Items.BLACK_DYE, 2), 1), event);

        addQuernRecipe("beetroot", new QuernRecipe(Items.BEETROOT, new ItemStack(Items.RED_DYE, 2), 2), event);
        addQuernRecipe("sunflower", new QuernRecipe(Blocks.SUNFLOWER, new ItemStack(Items.YELLOW_DYE, 4), 2), event);
        addQuernRecipe("lilac", new QuernRecipe(Blocks.LILAC, new ItemStack(Items.MAGENTA_DYE, 4), 2), event);
        addQuernRecipe("rose_bush", new QuernRecipe(Blocks.ROSE_BUSH, new ItemStack(Items.RED_DYE, 4), 2), event);
        addQuernRecipe("peony", new QuernRecipe(Blocks.PEONY, new ItemStack(Items.PINK_DYE, 4), 2), event);

        //BoP
        //TODO. BoP don't have tags for this yet
        /*addQuernRecipe("rafflesia", new QuernRecipe("plantRafflesia", new ItemStack(Items.RED_DYE, 3), 2), event);
        addQuernRecipe("plant_flax", new QuernRecipe("plantFlax", new ItemStack(Items.LIGHT_BLUE_DYE, 3), 2), event);
        addQuernRecipe("clover", new QuernRecipe("flowerClover", new ItemStack(Items.LIGHT_GRAY_DYE, 3), 2), event);
        addQuernRecipe("swampflower", new QuernRecipe("flowerSwampflower", new ItemStack(Items.CYAN_DYE, 3), 2), event);
        addQuernRecipe("glowflower", new QuernRecipe("flowerGlowflower", new ItemStack(Items.CYAN_DYE, 3), 2), event);
        addQuernRecipe("blue_hydrangea", new QuernRecipe("flowerBlueHydrangea", new ItemStack(Items.LIGHT_BLUE_DYE, 3), 2), event);
        addQuernRecipe("orange_cosmos", new QuernRecipe("flowerOrangeCosmos", new ItemStack(Items.ORANGE_DYE, 3), 2), event);
        addQuernRecipe("pink_daffodil", new QuernRecipe("flowerPinkDaffodil", new ItemStack(Items.PINK_DYE, 3), 2), event);
        addQuernRecipe("wildflower", new QuernRecipe("flowerWildflower", new ItemStack(Items.MAGENTA_DYE, 3), 2), event);
        addQuernRecipe("violet_flower", new QuernRecipe("flowerViolet", new ItemStack(Items.PURPLE_DYE, 3), 2), event);
        addQuernRecipe("bromeliad", new QuernRecipe("flowerBromeliad", new ItemStack(Items.RED_DYE, 3), 2), event);
        addQuernRecipe("wilted_lily", new QuernRecipe("flowerWiltedLily", new ItemStack(Items.LIGHT_GRAY_DYE, 3), 2), event);
        addQuernRecipe("pink_hibiscus", new QuernRecipe("flowerPinkHibiscus", new ItemStack(Items.PINK_DYE, 3), 2), event);
        addQuernRecipe("burning_blossom", new QuernRecipe("flowerBurningBlossom", new ItemStack(Items.ORANGE_DYE, 3), 2), event);
        addQuernRecipe("miners_delight", new QuernRecipe("flowerMinersDelight", new ItemStack(Items.PINK_DYE, 3), 2), event);
        addQuernRecipe("icy_iris", new QuernRecipe("flowerIcyIris", new ItemStack(Items.LIGHT_BLUE_DYE, 3), 2), event);
        addQuernRecipe("rose", new QuernRecipe("flowerRose", new ItemStack(Items.RED_DYE, 3), 2), event);*/
    }

    @SubscribeEvent
    public static void registerKilnRecipes(RegistryEvent.Register<IKilnRecipe> event) {
        AtumRegistry.registerRecipe("marl", new KilnRecipe(AtumBlocks.MARL, new ItemStack(BlockCeramic.getCeramicBlocks(DyeColor.WHITE)), 0.1F), event);
    }

    public static void addKilnRecipes(MinecraftServer server) {
        //Add valid vanilla & modded recipes based on Furnace recipes
        ServerWorld world = DimensionManager.getWorld(server, DimensionType.OVERWORLD, true, true);
        if (world != null) {
            for (FurnaceRecipe furnaceRecipe : getRecipes(world.getRecipeManager(), IRecipeType.SMELTING)) {
                for (Ingredient input : furnaceRecipe.getIngredients()) {
                    ItemStack output = furnaceRecipe.getRecipeOutput();
                    if (input != null && !output.isEmpty()) {
                        if (!TileEntityKiln.canKilnNotSmelt(input) && !TileEntityKiln.canKilnNotSmelt(output)) {
                            ResourceLocation id = new ResourceLocation(Constants.MOD_ID, furnaceRecipe.getId().getPath());
                            RecipeHandlers.kilnRecipes.register(new KilnRecipe(input, output, furnaceRecipe.getExperience()).setRegistryName(id));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerSpinningwheelRecipes(RegistryEvent.Register<ISpinningWheelRecipe> event) {
        AtumRegistry.registerRecipe("flax", new SpinningWheelRecipe(AtumAPI.Tags.CROPS_FLAX, new ItemStack(AtumItems.LINEN_THREAD, 3), 4), event);
        AtumRegistry.registerRecipe("wolf_pelt", new SpinningWheelRecipe(AtumItems.WOLF_PELT, new ItemStack(Items.STRING, 2), 5), event);
        AtumRegistry.registerRecipe("cloth_scrap", new SpinningWheelRecipe(AtumItems.SCRAP, new ItemStack(AtumItems.LINEN_THREAD), 2), event);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        AtumRecipes.register();
    }


    /*@SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) { //TODO Move to json
        IForgeRegistry<IRecipe> registry = event.getRegistry();
        final ResourceLocation crystal = new ResourceLocation(Constants.MOD_ID, "crystal_glass");
        final ResourceLocation framed = new ResourceLocation(Constants.MOD_ID, "framed_glass");
        final ResourceLocation linen = new ResourceLocation(Constants.MOD_ID, "linen");
        final ResourceLocation ceramic = new ResourceLocation(Constants.MOD_ID, "ceramic");
        final ResourceLocation concretePowder = new ResourceLocation(Constants.MOD_ID, "concrete_powder");

        for (DyeColor color : DyeColor.values()) {
            String colorName = StringUtils.capitalize(color.getTranslationKey().replace("silver", "lightGray"));
            AtumRegistry.registerRecipe("crystal_" + colorName, new ShapedOreRecipe(crystal, new ItemStack(BlockAtumStainedGlass.getGlass(AtumBlocks.CRYSTAL_GLASS, color), 8), "GGG", "GDG", "GGG", 'G', AtumBlocks.CRYSTAL_GLASS, 'D', "dye" + colorName), event);
            AtumRegistry.registerRecipe("framed_" + colorName, new ShapedOreRecipe(framed, new ItemStack(BlockAtumStainedGlass.getGlass(AtumBlocks.FRAMED_GLASS, color), 8), "GGG", "GDG", "GGG", 'G', AtumBlocks.FRAMED_GLASS, 'D', "dye" + colorName), event);
            AtumRegistry.registerRecipe("crystal_to_framed_" + colorName, new ShapedOreRecipe(framed, BlockAtumStainedGlass.getGlass(AtumBlocks.FRAMED_GLASS, color), " S ", "SGS", " S ", 'S', "stickWood", 'G', BlockAtumStainedGlass.getGlass(AtumBlocks.CRYSTAL_GLASS, color)), event);
            AtumRegistry.registerRecipe("thin_crystal_" + colorName, new ShapedOreRecipe(crystal, new ItemStack(BlockAtumStainedGlassPane.getGlass(AtumBlocks.CRYSTAL_GLASS, color), 16), "GGG", "GGG", 'G', BlockAtumStainedGlass.getGlass(AtumBlocks.CRYSTAL_GLASS, color)), event);
            AtumRegistry.registerRecipe("thin_framed_" + colorName, new ShapedOreRecipe(framed, new ItemStack(BlockAtumStainedGlassPane.getGlass(AtumBlocks.FRAMED_GLASS, color), 16), "GGG", "GGG", 'G', BlockAtumStainedGlass.getGlass(AtumBlocks.FRAMED_GLASS, color)), event);
            if (color != DyeColor.WHITE) {
                AtumRegistry.registerRecipe("linen_" + colorName, new ShapelessOreRecipe(linen, new ItemStack(BlockLinen.getLinen(color)), "dye" + colorName, BlockLinen.getLinen(DyeColor.WHITE)), event);
                AtumRegistry.registerRecipe("ceramic_" + colorName, new ShapelessOreRecipe(ceramic, new ItemStack(BlockCeramic.getCeramicBlocks(color)), "dye" + colorName, BlockCeramic.getCeramicBlocks(DyeColor.WHITE)), event);
            }
            AtumRegistry.registerRecipe("ceramic_tile_" + colorName, new ShapedOreRecipe(ceramic, new ItemStack(BlockCeramicTile.getTile(color), 3), "CC", 'C', BlockCeramic.getCeramicBlocks(color)), event);
            AtumRegistry.registerRecipe("ceramic_slab_" + colorName, new ShapedOreRecipe(ceramic, new ItemStack(BlockCeramicSlab.getSlab(color), 6), "CCC", 'C', BlockCeramic.getCeramicBlocks(color)), event);
            AtumRegistry.registerRecipe("ceramic_stairs_" + colorName, new ShapedOreRecipe(ceramic, new ItemStack(BlockAtumStairs.getCeramicStairs(color), 4), "C  ", "CC ", "CCC", 'C', BlockCeramic.getCeramicBlocks(color)).setMirrored(true), event);
            AtumRegistry.registerRecipe("ceramic_wall_" + colorName, new ShapedOreRecipe(ceramic, new ItemStack(BlockCeramicWall.getWall(color), 6), "CCC", "CCC", 'C', BlockCeramic.getCeramicBlocks(color)), event);
            AtumRegistry.registerRecipe("linen_carpet_" + colorName, new ShapedOreRecipe(linen, new ItemStack(BlockLinenCarpet.getLinenBlock(color), 5), "LLL", 'L', BlockLinen.getLinen(color)), event);
            //Concrete Powder //TODO Check if Forge have this now
            AtumRegistry.registerRecipe("concrete_powder" + colorName, new ShapelessOreRecipe(concretePowder, new ItemStack(Blocks.CONCRETE_POWDER, 8, color.getMetadata()), "dye" + colorName, "sand", "sand", "sand", "sand", "gravel", "gravel", "gravel", "gravel"), event);
        }
        if (AtumConfig.RECIPE_OVERRIDING) { //TODO Check if is needed
            fixOreDictEntries(registry);
        }
    }*/

    /*private static void fixOreDictEntries(IForgeRegistry<IRecipe> registry) {
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
        if (!Constants.IS_QUARK_LOADED || !ForgeRegistries.BLOCKS.containsKey(new ResourceLocation("quark", "custom_chest"))) { //Check if Quark Varied Chests is enabled
            recipes.remove(chest);
            registry.register(new ShapedOreRecipe(chest, new ItemStack(Blocks.CHEST), "PPP", "P P", "PPP", 'P', "plankWood").setRegistryName(chest));
        }

        //Trapdoor
        recipes.remove(trapdoor);
        registry.register(new ShapedOreRecipe(trapdoor, new ItemStack(Blocks.TRAPDOOR, 2), "PPP", "PPP", 'P', "plankWood").setRegistryName(trapdoor));


        if (ForgeVersion.getBuildVersion() >= 2831) {
            //Wool
            for (DyeColor color : DyeColor.values()) {
                if (color != DyeColor.WHITE) {
                    ResourceLocation location = new ResourceLocation(color.getName().replace("silver", "light_gray") + "_wool");
                    recipes.remove(location);
                    String colorName = StringUtils.capitalize(color.getTranslationKey().replace("silver", "lightGray"));
                    registry.register(new ShapelessOreRecipe(location, new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, color.getMetadata()), "dye" + colorName, "woolWhite").setRegistryName(location));
                }
            }
        }

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
    }*/
}