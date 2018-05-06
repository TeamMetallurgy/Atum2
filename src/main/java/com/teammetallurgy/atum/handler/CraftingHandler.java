package com.teammetallurgy.atum.handler;

import com.teammetallurgy.atum.blocks.BlockAtumLog;
import com.teammetallurgy.atum.blocks.BlockAtumPlank;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CraftingHandler {
    private static void register() {
        addRecipes();
        addSmeltingRecipes();
    }

    private static void addRecipes() {
        /*String[] oreColours = Constants.ORE_DIC_COLOURS;

        for (int i = 0; i < oreColours.length; i++) {
            RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.CRYSTAL_STAINED_GLASS, 8, i), "GGG", "GDG", "GGG", 'G', AtumBlocks.CRYSTAL_GLASS, 'D', "dye" + oreColours[i]));
            RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.THIN_CRYSTAL_STAINED_GLASS, 16, i), "GGG", "GGG", 'G', new ItemStack(AtumBlocks.CRYSTAL_STAINED_GLASS, 1, i));

            RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.FRAMED_STAINED_GLASS, 8, i), "GGG", "GDG", "GGG", 'G', AtumBlocks.FRAMED_GLASS, 'D', "dye" + oreColours[i]));
            RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.THIN_FRAMED_STAINED_GLASS, 16, i), "GGG", "GGG", 'G', new ItemStack(AtumBlocks.FRAMED_STAINED_GLASS, 1, i));

            RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.FRAMED_STAINED_GLASS, 1, i), " S ", "SGS", " S ", 'S', Items.STICK, 'G', new ItemStack(AtumBlocks.CRYSTAL_STAINED_GLASS, 1, i));
        }*/
    }

    private static void addSmeltingRecipes() {
        GameRegistry.addSmelting(AtumBlocks.IRON_ORE, new ItemStack(Items.IRON_INGOT), 0.7F);
        GameRegistry.addSmelting(AtumBlocks.COAL_ORE, new ItemStack(Items.COAL), 0.1F);
        GameRegistry.addSmelting(AtumBlocks.REDSTONE_ORE, new ItemStack(Items.REDSTONE), 0.7F);
        GameRegistry.addSmelting(AtumBlocks.LAPIS_ORE, new ItemStack(Items.DYE, 1, 4), 0.2F);
        GameRegistry.addSmelting(AtumBlocks.GOLD_ORE, new ItemStack(Items.GOLD_INGOT), 1.0F);
        GameRegistry.addSmelting(AtumBlocks.DIAMOND_ORE, new ItemStack(Items.DIAMOND), 1.0F);
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()){
            GameRegistry.addSmelting(BlockAtumLog.getLog(type), new ItemStack(Items.COAL, 1, 1), 0.15F);
        }
        GameRegistry.addSmelting(AtumBlocks.LIMESTONE_CRACKED, new ItemStack(AtumBlocks.LIMESTONE), 0.1F);
        GameRegistry.addSmelting(AtumBlocks.SAND, new ItemStack(AtumBlocks.CRYSTAL_GLASS), 0.1F);
        GameRegistry.addSmelting(AtumBlocks.SAND, new ItemStack(AtumBlocks.CRYSTAL_GLASS), 0.1F);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        CraftingHandler.register();
    }
}