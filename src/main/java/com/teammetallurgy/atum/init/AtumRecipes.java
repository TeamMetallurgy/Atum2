package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import com.teammetallurgy.atum.api.recipe.kiln.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.api.recipe.quern.QuernRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.misc.AtumRegistry;
import net.minecraft.block.Blocks;
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
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import net.minecraftforge.registries.RegistryManager;

import static com.teammetallurgy.atum.misc.recipe.RecipeHelper.*;
import static net.minecraft.potion.PotionUtils.addPotionToItemStack;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumRecipes {

    @SubscribeEvent
    public static void registerRegistries(RegistryEvent.NewRegistry event) {
        RecipeHandlers.quernRecipes = (IForgeRegistryModifiable<IQuernRecipe>) AtumRegistry.makeRegistry("quern_recipes", IQuernRecipe.class);
        RecipeHandlers.kilnRecipes = (IForgeRegistryModifiable<IKilnRecipe>) AtumRegistry.makeRegistry("kiln_recipes", IKilnRecipe.class);
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
        addQuernRecipe("flowers_white", new QuernRecipe(AtumAPI.Tags.FLOWERS_WHITE, new ItemStack(Items.WHITE_DYE, 2), 1), event);
        addQuernRecipe("flowers_orange", new QuernRecipe(AtumAPI.Tags.FLOWERS_ORANGE, new ItemStack(Items.ORANGE_DYE, 2), 1), event);
        addQuernRecipe("flowers_magenta", new QuernRecipe(AtumAPI.Tags.FLOWERS_MAGENTA, new ItemStack(Items.MAGENTA_DYE, 2), 1), event);
        addQuernRecipe("flowers_light_blue", new QuernRecipe(AtumAPI.Tags.FLOWERS_LIGHT_BLUE, new ItemStack(Items.LIGHT_BLUE_DYE, 2), 1), event);
        addQuernRecipe("flowers_yellow", new QuernRecipe(AtumAPI.Tags.FLOWERS_YELLOW, new ItemStack(Items.YELLOW_DYE, 2), 1), event);
        addQuernRecipe("flowers_lime", new QuernRecipe(AtumAPI.Tags.FLOWERS_LIME, new ItemStack(Items.LIME_DYE, 2), 1), event);
        addQuernRecipe("flowers_pink", new QuernRecipe(AtumAPI.Tags.FLOWERS_PINK, new ItemStack(Items.PINK_DYE, 2), 1), event);
        addQuernRecipe("flowers_gray", new QuernRecipe(AtumAPI.Tags.FLOWERS_GRAY, new ItemStack(Items.GRAY_DYE, 2), 1), event);
        addQuernRecipe("flowers_light_gray", new QuernRecipe(AtumAPI.Tags.FLOWERS_LIGHT_GRAY, new ItemStack(Items.LIGHT_GRAY_DYE, 2), 1), event);
        addQuernRecipe("flowers_cyan", new QuernRecipe(AtumAPI.Tags.FLOWERS_CYAN, new ItemStack(Items.CYAN_DYE, 2), 1), event);
        addQuernRecipe("flowers_purple", new QuernRecipe(AtumAPI.Tags.FLOWERS_PURPLE, new ItemStack(Items.PURPLE_DYE, 2), 1), event);
        addQuernRecipe("flowers_blue", new QuernRecipe(AtumAPI.Tags.FLOWERS_BLUE, new ItemStack(Items.BLUE_DYE, 2), 1), event);
        addQuernRecipe("flowers_brown", new QuernRecipe(AtumAPI.Tags.FLOWERS_BROWN, new ItemStack(Items.BROWN_DYE, 2), 1), event);
        addQuernRecipe("flowers_green", new QuernRecipe(AtumAPI.Tags.FLOWERS_GREEN, new ItemStack(Items.GREEN_DYE, 2), 1), event);
        addQuernRecipe("flowers_red", new QuernRecipe(AtumAPI.Tags.FLOWERS_RED, new ItemStack(Items.RED_DYE, 2), 1), event);
        addQuernRecipe("flowers_black", new QuernRecipe(AtumAPI.Tags.FLOWERS_BLACK, new ItemStack(Items.BLACK_DYE, 2), 1), event);

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
        AtumRegistry.registerRecipe("marl", new KilnRecipe(AtumBlocks.MARL, new ItemStack(AtumBlocks.CERAMIC_WHITE), 0.1F), event);
    }

    public static void addKilnRecipes(MinecraftServer server) {
        //Add valid vanilla & modded recipes based on Furnace recipes
        ServerWorld world = DimensionManager.getWorld(server, DimensionType.OVERWORLD, true, true);
        if (world != null) {
            ForgeRegistry<?> kilnRegistry = RegistryManager.ACTIVE.getRegistry(RecipeHandlers.kilnRecipes.getRegistryName());
            kilnRegistry.unfreeze();
            for (FurnaceRecipe furnaceRecipe : getRecipes(world.getRecipeManager(), IRecipeType.SMELTING)) {
                for (Ingredient input : furnaceRecipe.getIngredients()) {
                    ItemStack output = furnaceRecipe.getRecipeOutput();
                    if (input != null && !output.isEmpty()) {
                        if (!KilnTileEntity.canKilnNotSmelt(input) && !KilnTileEntity.canKilnNotSmelt(output)) {
                            ResourceLocation id = new ResourceLocation("atum.kiln_" + furnaceRecipe.getId().getPath()); //Will be registered with "minecraft" as prefix, to prevent log spam
                            RecipeHandlers.kilnRecipes.register(new KilnRecipe(input, output, furnaceRecipe.getExperience()).setRegistryName(id));
                        }
                    }
                }
            }
            kilnRegistry.freeze();
        }
    }

    public static void kilnMissingMappings(RegistryEvent.MissingMappings<IKilnRecipe> event) {
        //Ignore missing mappings for kiln recipes, as they're first registered on world load. So will always be missing, until the world is fully loaded
        for (RegistryEvent.MissingMappings.Mapping<IKilnRecipe> mapping : event.getAllMappings()) {
            mapping.ignore();
        }
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        AtumRecipes.addBrewingRecipes();
    }
}