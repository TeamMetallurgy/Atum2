package com.teammetallurgy.atum.handler;

import com.teammetallurgy.atum.blocks.*;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.init.Blocks;
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
        RecipeToJson.addShapedRecipe(new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE), 4), "XX", "XX", 'X', AtumBlocks.LIMESTONE);
        RecipeToJson.addShapedRecipe(new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.SMALL), 4), "XX", "XX", 'X', AtumBlocks.LIMESTONE_CRACKED);
        RecipeToJson.addShapelessRecipe(new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CHISELED)), AtumBlocks.LIMESTONE);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.SMOOTH_STAIRS, 4), "X  ", "XX ", "XXX", 'X', AtumBlocks.LIMESTONE);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.CRACKED_STAIRS, 4), "X  ", "XX ", "XXX", 'X', AtumBlocks.LIMESTONE_CRACKED);
        RecipeToJson.addShapedRecipe(new ItemStack(BlockAtumStairs.getBrickStairs(BlockLimestoneBricks.BrickType.LARGE), 4), "X  ", "XX ", "XXX", 'X', new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE)));
        RecipeToJson.addShapedRecipe(new ItemStack(BlockAtumStairs.getBrickStairs(BlockLimestoneBricks.BrickType.SMALL), 4), "X  ", "XX ", "XXX", 'X', new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.SMALL)));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.SMOOTH_LIMESTONE_SLAB, 6), "XXX", 'X', AtumBlocks.LIMESTONE);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.CRACKED_LIMESTONE_SLAB, 6), "XXX", 'X', AtumBlocks.LIMESTONE_CRACKED);
        RecipeToJson.addShapedRecipe(new ItemStack(BlockLimestoneSlab.getSlab(BlockLimestoneBricks.BrickType.LARGE), 6), "XXX", 'X', new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE)));
        RecipeToJson.addShapedRecipe(new ItemStack(BlockLimestoneSlab.getSlab(BlockLimestoneBricks.BrickType.SMALL), 6), "XXX", 'X', new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.SMALL)));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.LIMESTONE_WALL, 6), "XXX", "XXX", 'X', AtumBlocks.LIMESTONE);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.LIMESTONE_CRACKED_WALL, 6), "XXX", "XXX", 'X', AtumBlocks.LIMESTONE_CRACKED);
        RecipeToJson.addShapedRecipe(new ItemStack(BlockLimestoneWall.getWall(BlockLimestoneBricks.BrickType.LARGE), 6), "XXX", "XXX", 'X', new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE)));
        RecipeToJson.addShapedRecipe(new ItemStack(BlockLimestoneWall.getWall(BlockLimestoneBricks.BrickType.SMALL), 6), "XXX", "XXX", 'X', new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.SMALL), 1));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.FRAMED_GLASS), " X ", "XSX", " X ", 'X', Items.STICK, 'S', AtumBlocks.CRYSTAL_GLASS);
        RecipeToJson.addShapedRecipe(new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CRACKED), 4), "XX", "XX", 'X', AtumItems.STONE_CHUNK);
        RecipeToJson.addShapedRecipe(new ItemStack(Items.EXPERIENCE_BOTTLE), " X ", "XBX", " X ", 'X', AtumItems.ECTOPLASM, 'B', Items.POTIONITEM);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.LIMESTONE_SWORD), "L", "L", "S", 'L', AtumBlocks.LIMESTONE_CRACKED, 'S', Items.STICK);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.LIMESTONE_SHOVEL), "L", "S", "S", 'L', AtumBlocks.LIMESTONE_CRACKED, 'S', Items.STICK);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.LIMESTONE_PICKAXE), "LLL", " S ", " S ", 'L', AtumBlocks.LIMESTONE_CRACKED, 'S', Items.STICK);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.LIMESTONE_AXE), "LL", "LS", " S", 'L', AtumBlocks.LIMESTONE_CRACKED, 'S', Items.STICK);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.LIMESTONE_HOE), "LL", " S", " S", 'L', AtumBlocks.LIMESTONE_CRACKED, 'S', Items.STICK);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.MUMMY_HELMET), "XXX", "X X", 'X', AtumItems.SCRAP);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.MUMMY_CHEST), "X X", "XXX", "XXX", 'X', AtumItems.SCRAP);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.MUMMY_LEGS), "XXX", "X X", "X X", 'X', AtumItems.SCRAP);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.MUMMY_BOOTS), "X X", "X X", 'X', AtumItems.SCRAP);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.WANDERER_HELMET), "XXX", "X X", 'X', AtumItems.LINEN);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.WANDERER_CHEST), "X X", "XXX", "XXX", 'X', AtumItems.LINEN);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.WANDERER_LEGS), "XXX", "X X", "X X", 'X', AtumItems.LINEN);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.WANDERER_BOOTS), "X X", "X X", 'X', AtumItems.LINEN);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.LINEN), "XXX", 'X', AtumItems.FLAX);
        RecipeToJson.addShapedRecipe(new ItemStack(Items.GLASS_BOTTLE, 3), "X X", " X ", 'X', AtumBlocks.CRYSTAL_GLASS);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.THIN_CRYSTAL_GLASS, 16), "XXX", "XXX", 'X', AtumBlocks.CRYSTAL_GLASS);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.THIN_FRAMED_GLASS, 16), "XXX", "XXX", 'X', AtumBlocks.FRAMED_GLASS);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.SCROLL), "XXX", "SXS", "XXX", 'X', AtumItems.PAPYRUS_PLANT, 'S', Items.STICK);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.SCARAB), " G ", "GDG", " G ", 'G', Items.GOLD_INGOT, 'D', Items.DIAMOND);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.LIMESTONE_FURNACE), "XXX", "X X", "XXX", 'X', AtumBlocks.LIMESTONE_CRACKED);
        RecipeToJson.addShapelessRecipe(new ItemStack(Items.DYE, 1, 15), new ItemStack(AtumItems.DUSTY_BONE));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.GOLDEN_DATE, 1, 0), "###", "#X#", "###", '#', Items.GOLD_INGOT, 'X', AtumItems.DATE);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumItems.GOLDEN_DATE, 1, 1), "###", "#X#", "###", '#', Blocks.GOLD_BLOCK, 'X', AtumItems.DATE);
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.PALM_DOOR), "pp ", "pp ", "pp ", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.PALM)));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.PALM_FENCE), "sss", "sss", 's', new ItemStack(BlockAtumPlank.getStick(BlockAtumPlank.WoodType.PALM), 1));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.PALM_FENCE_GATE), "sps", "sps", 's', new ItemStack(BlockAtumPlank.getStick(BlockAtumPlank.WoodType.PALM), 1), 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.PALM)));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.PALM_HATCH, 2), "ppp", "ppp", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.PALM)));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.PALM_LADDER, 3), "s s", "sss", "s s", 's', new ItemStack(BlockAtumPlank.getStick(BlockAtumPlank.WoodType.PALM), 1));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.DEADWOOD_DOOR), "pp ", "pp ", "pp ", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.DEADWOOD)));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.DEADWOOD_FENCE), "sss", "sss", 's', new ItemStack(BlockAtumPlank.getStick(BlockAtumPlank.WoodType.DEADWOOD), 1));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.DEADWOOD_FENCE_GATE), "sps", "sps", 's', new ItemStack(BlockAtumPlank.getStick(BlockAtumPlank.WoodType.DEADWOOD), 1), 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.DEADWOOD)));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.DEADWOOD_HATCH, 2), "ppp", "ppp", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.DEADWOOD)));
        RecipeToJson.addShapedRecipe(new ItemStack(AtumBlocks.DEADWOOD_LADDER, 3), "s s", "sss", "s s", 's', new ItemStack(BlockAtumPlank.getStick(BlockAtumPlank.WoodType.DEADWOOD), 1));
        RecipeToJson.addShapedRecipe(new ItemStack(BlockAtumWoodSlab.getSlab(BlockAtumPlank.WoodType.PALM), 6), "ppp", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.PALM)));
        RecipeToJson.addShapedRecipe(new ItemStack(BlockAtumWoodSlab.getSlab(BlockAtumPlank.WoodType.DEADWOOD), 6), "ppp", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.DEADWOOD)));
        RecipeToJson.addShapedRecipe(new ItemStack(BlockAtumStairs.getWoodStairs(BlockAtumPlank.WoodType.PALM), 4), "p  ", "pp ", "ppp", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.PALM)));
        RecipeToJson.addShapedRecipe(new ItemStack(BlockAtumStairs.getWoodStairs(BlockAtumPlank.WoodType.DEADWOOD), 4), "p  ", "pp ", "ppp", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.DEADWOOD)));
        RecipeToJson.addShapedRecipe(new ItemStack(BlockCrate.getCrate(BlockAtumPlank.WoodType.PALM)), "ppp", "p p", "ppp", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.PALM)));
        RecipeToJson.addShapedRecipe(new ItemStack(BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD), 1), "ppp", "p p", "ppp", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.DEADWOOD)));
        RecipeToJson.addShapedRecipe(new ItemStack(BlockAtumPlank.getStick(BlockAtumPlank.WoodType.PALM), 4), "p", "p", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.PALM)));
        RecipeToJson.addShapedRecipe(new ItemStack(BlockAtumPlank.getStick(BlockAtumPlank.WoodType.DEADWOOD), 4), "p", "p", 'p', new ItemStack(BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.DEADWOOD)));

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