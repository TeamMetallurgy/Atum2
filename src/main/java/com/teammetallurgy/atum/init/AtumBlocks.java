package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.blocks.*;
import com.teammetallurgy.atum.blocks.tileentity.TileEntityBurningTrap;
import com.teammetallurgy.atum.blocks.tileentity.chests.TileEntityChestSpawner;
import com.teammetallurgy.atum.blocks.tileentity.chests.TileEntityPharaohChest;
import com.teammetallurgy.atum.blocks.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.blocks.tileentity.furnace.TileEntityLimestoneFurnace;
import com.teammetallurgy.atum.items.ItemAtumSlab;
import com.teammetallurgy.atum.items.ItemSand;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.teammetallurgy.atum.utils.AtumRegistry.registerBlock;

@GameRegistry.ObjectHolder(value = Constants.MOD_ID)
public class AtumBlocks {
    public static final BlockPortal PORTAL = new BlockPortal();
    public static final BlockChestSpawner CHEST_SPAWNER = new BlockChestSpawner();
    public static final Block SAND = new BlockStrangeSand().setHardness(0.5F);
    public static final Block LIMESTONE_GRAVEL = new BlockAtumGravel();
    public static final Block LIMESTONE = new BlockLimestone();
    public static final Block LIMESTONE_CRACKED = new Block(Material.ROCK).setHardness(2.0F).setResistance(10.0F);
    public static final Block LIMESTONE_WALL = new BlockLimestoneWall();
    public static final Block LIMESTONE_CRACKED_WALL = new BlockLimestoneWall();
    public static final Block SMOOTH_STAIRS = new BlockAtumStairs(LIMESTONE.getDefaultState());
    public static final Block CRACKED_STAIRS = new BlockAtumStairs(LIMESTONE_CRACKED.getDefaultState());
    public static final Block SMOOTH_LIMESTONE_SLAB = new BlockAtumSlab(Material.ROCK);
    public static final Block CRACKED_LIMESTONE_SLAB = new BlockAtumSlab(Material.ROCK);
    public static final Block LIMESTONE_DOOR = new BlockAtumDoor(Material.ROCK);
    public static final Block LIMESTONE_CRACKED_DOOR = new BlockAtumDoor(Material.ROCK);
    public static final Block SAND_LAYERED = new BlockSandLayers().setHardness(0.1F).setLightOpacity(0);
    public static final Block CRYSTAL_GLASS = new BlockAtumGlass(Material.GLASS);
    public static final Block FRAMED_GLASS = new BlockAtumGlass(Material.GLASS);
    public static final Block DATE_BLOCK = new BlockDate();
    public static final BlockShrub SHRUB = new BlockShrub();
    public static final BlockShrub WEED = new BlockShrub();
    public static final Block PAPYRUS = new BlockPapyrus();
    public static final Block FLAX = new BlockFlax();
    public static final Block FERTILE_SOIL = new BlockFertileSoil();
    public static final Block FERTILE_SOIL_TILLED = new BlockFertileSoilTilled();
    public static final BlockAtumDoor PALM_DOOR = (BlockAtumDoor) new BlockAtumDoor(Material.WOOD);
    public static final Block PALM_FENCE = new BlockAtumFence(BlockAtumPlank.WoodType.PALM.getMapColor());
    public static final Block PALM_FENCE_GATE = new BlockAtumFenceGate();
    public static final Block PALM_HATCH = new BlockAtumTrapDoor();
    public static final Block PALM_LADDER = new BlockAtumLadder();
    public static final Block THIN_CRYSTAL_GLASS = new BlockAtumPane();
    public static final Block THIN_FRAMED_GLASS = new BlockAtumPane();
    public static final Block PALM_TORCH = new BlockAtumTorch();
    public static final Block DEADWOOD_TORCH = new BlockAtumTorch();
    public static final Block LIMESTONE_TORCH = new BlockAtumTorch();
    public static final Block PHARAOH_TORCH = new BlockAtumTorch();
    public static final Block BURNING_TRAP = new BlockBurningTrap();
    public static final Block PHARAOH_CHEST = new BlockPharaohChest();
    public static final Block GOLD_ORE = new BlockAtumOres();
    public static final Block IRON_ORE = new BlockAtumOres();
    public static final Block COAL_ORE = new BlockAtumOres();
    public static final Block LAPIS_ORE = new BlockAtumOres();
    public static final Block EMERALD_ORE = new BlockAtumOres();
    public static final Block DIAMOND_ORE = new BlockAtumOres();
    public static final Block REDSTONE_ORE = new BlockAtumRedstoneOre(false);
    public static final Block LIT_REDSTONE_ORE = new BlockAtumRedstoneOre(true);
    public static final Block BONE_ORE = new BlockAtumOres();
    public static final Block RELIC_ORE = new BlockAtumOres();
    public static final Block BONE_DIRTY = new BlockBone();
    public static final Block LIMESTONE_FURNACE = new BlockLimeStoneFurnace(false);
    public static final Block LIMESTONE_FURNACE_LIT = new BlockLimeStoneFurnace(true);
    public static final BlockAtumDoor DEADWOOD_DOOR = (BlockAtumDoor) new BlockAtumDoor(Material.WOOD);
    public static final Block DEADWOOD_FENCE = new BlockAtumFence(BlockAtumPlank.WoodType.DEADWOOD.getMapColor());
    public static final Block DEADWOOD_FENCE_GATE = new BlockAtumFenceGate();
    public static final Block DEADWOOD_HATCH = new BlockAtumTrapDoor();
    public static final Block DEADWOOD_LADDER = new BlockAtumLadder();

    public static void registerBlocks() {
        registerBlock(PORTAL, "portal", null);
        registerBlock(CHEST_SPAWNER, "chest_spawner", null);
        registerBlock(SAND, "sand");
        registerBlock(SAND_LAYERED, new ItemSand(SAND_LAYERED), "sand_layer");
        registerBlock(DATE_BLOCK, "date_block", null);
        registerBlock(SHRUB, "shrub");
        registerBlock(WEED, "weed");
        registerBlock(PAPYRUS, "papyrus");
        registerBlock(FLAX, "flax_block", null);
        registerBlock(FERTILE_SOIL, "fertile_soil");
        registerBlock(FERTILE_SOIL_TILLED, "fertile_soil_tilled");
        registerBlock(BURNING_TRAP, "burning_trap");
        registerBlock(PHARAOH_CHEST, "pharaoh_chest");
        registerBlock(GOLD_ORE, "gold_ore");
        registerBlock(IRON_ORE, "iron_ore");
        registerBlock(COAL_ORE, "coal_ore");
        registerBlock(LAPIS_ORE, "lapis_ore");
        registerBlock(DIAMOND_ORE, "diamond_ore");
        registerBlock(EMERALD_ORE, "emerald_ore");
        registerBlock(REDSTONE_ORE, "redstone_ore");
        registerBlock(LIT_REDSTONE_ORE, "lit_redstone_ore", null);
        registerBlock(BONE_ORE, "bone_ore");
        registerBlock(RELIC_ORE, "relic_ore");
        registerBlock(BONE_DIRTY, "dirty_bone_block");
        registerBlock(LIMESTONE_FURNACE, "limestone_furnace");
        registerBlock(LIMESTONE_FURNACE_LIT, "limestone_furnace_lit", null);
        registerBlock(PALM_TORCH, "palm_torch");
        registerBlock(DEADWOOD_TORCH, "deadwood_torch");
        registerBlock(LIMESTONE_TORCH, "limestone_torch");
        registerBlock(PHARAOH_TORCH, "pharaoh_torch");

        //Limestone
        BlockLimestoneBricks.registerBricks();
        registerBlock(SMOOTH_LIMESTONE_SLAB, new ItemAtumSlab(SMOOTH_LIMESTONE_SLAB, (BlockAtumSlab) SMOOTH_LIMESTONE_SLAB), "limestone_smooth_slab");
        registerBlock(CRACKED_LIMESTONE_SLAB, new ItemAtumSlab(CRACKED_LIMESTONE_SLAB, (BlockAtumSlab) CRACKED_LIMESTONE_SLAB), "limestone_cracked_slab");
        BlockLimestoneSlab.registerSlabs();
        registerBlock(LIMESTONE_GRAVEL, "limestone_gravel");
        registerBlock(LIMESTONE, "limestone");
        registerBlock(LIMESTONE_CRACKED, "limestone_cracked");
        registerBlock(SMOOTH_STAIRS, "smooth_stairs");
        registerBlock(CRACKED_STAIRS, "cracked_stairs");
        BlockAtumStairs.registerLimestoneStairs();
        registerBlock(LIMESTONE_WALL, "limestone_wall");
        registerBlock(LIMESTONE_CRACKED_WALL, "limestone_cracked_wall");
        BlockLimestoneWall.registerWalls();
        registerBlock(LIMESTONE_DOOR, new ItemDoor(LIMESTONE_DOOR), "limestone_door");
        registerBlock(LIMESTONE_CRACKED_DOOR, new ItemDoor(LIMESTONE_CRACKED_DOOR), "limestone_cracked_door");
        BlockLimestoneBricks.registerDoors();

        //Glass
        registerBlock(CRYSTAL_GLASS, "crystal_glass");
        BlockAtumStainedGlass.registerStainedGlass(CRYSTAL_GLASS);
        registerBlock(THIN_CRYSTAL_GLASS, "thin_crystal_glass");
        BlockAtumStainedGlassPane.registerStainedGlassPane(THIN_CRYSTAL_GLASS);
        registerBlock(FRAMED_GLASS, "framed_glass");
        BlockAtumStainedGlass.registerStainedGlass(FRAMED_GLASS);
        registerBlock(THIN_FRAMED_GLASS, "thin_framed_glass");
        BlockAtumStainedGlassPane.registerStainedGlassPane(THIN_FRAMED_GLASS);

        // Wood
        BlockAtumPlank.registerPlanks();
        BlockAtumLog.registerLogs();
        BlockAtumStairs.registerWoodStairs();
        BlockAtumWoodSlab.registerSlabs();
        BlockAtumSapling.registerSaplings();
        BlockLeave.registerLeaves();
        BlockCrate.registerCrates();
        registerBlock(PALM_LADDER, "palm_ladder");
        registerBlock(DEADWOOD_LADDER, "deadwood_ladder");
        registerBlock(PALM_FENCE, "palm_fence");
        registerBlock(DEADWOOD_FENCE, "deadwood_fence");
        registerBlock(PALM_FENCE_GATE, "palm_fence_gate");
        registerBlock(DEADWOOD_FENCE_GATE, "deadwood_fence_gate");
        registerBlock(PALM_HATCH, "palm_hatch");
        registerBlock(DEADWOOD_HATCH, "deadwood_hatch");
        registerBlock(PALM_DOOR, new ItemDoor(PALM_DOOR), "palm_door");
        registerBlock(DEADWOOD_DOOR, new ItemDoor(DEADWOOD_DOOR), "deadwood_door");
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityChestSpawner.class, new ResourceLocation(Constants.MOD_ID, "cursed_chest"));
        GameRegistry.registerTileEntity(TileEntityPharaohChest.class, new ResourceLocation(Constants.MOD_ID, "pharaoh_chest"));
        GameRegistry.registerTileEntity(TileEntityBurningTrap.class, new ResourceLocation(Constants.MOD_ID, "burning_trap"));
        GameRegistry.registerTileEntity(TileEntityLimestoneFurnace.class, new ResourceLocation(Constants.MOD_ID, "limestone_furnace"));
        GameRegistry.registerTileEntity(TileEntityCrate.class, new ResourceLocation(Constants.MOD_ID, "crate"));
    }

    public static void setBlockInfo() {
        SAND.setHarvestLevel("shovel", 0);
        SAND_LAYERED.setHarvestLevel("shovel", 0);
        LIMESTONE_GRAVEL.setHarvestLevel("shovel", 0);
        FERTILE_SOIL.setHarvestLevel("shovel", 0);
        FERTILE_SOIL_TILLED.setHarvestLevel("shovel", 0);
        COAL_ORE.setHarvestLevel("pickaxe", 0);
        BONE_ORE.setHarvestLevel("pickaxe", 0);
        IRON_ORE.setHarvestLevel("pickaxe", 1);
        RELIC_ORE.setHarvestLevel("pickaxe", 1);
        GOLD_ORE.setHarvestLevel("pickaxe", 2);
        LAPIS_ORE.setHarvestLevel("pickaxe", 1);
        EMERALD_ORE.setHarvestLevel("pickaxe", 1);
        DIAMOND_ORE.setHarvestLevel("pickaxe", 2);
        REDSTONE_ORE.setHarvestLevel("pickaxe", 2);

        Blocks.FIRE.setFireInfo(PALM_FENCE, 5, 20);
        Blocks.FIRE.setFireInfo(PALM_FENCE_GATE, 5, 20);
        Blocks.FIRE.setFireInfo(DEADWOOD_FENCE, 5, 20);
        Blocks.FIRE.setFireInfo(DEADWOOD_FENCE_GATE, 5, 20);
        Blocks.FIRE.setFireInfo(SHRUB, 60, 100);
        Blocks.FIRE.setFireInfo(WEED, 60, 100);
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            Blocks.FIRE.setFireInfo(BlockAtumWoodSlab.getSlab(type), 5, 20);
            Blocks.FIRE.setFireInfo(BlockAtumPlank.getPlank(type), 5, 20);
            Blocks.FIRE.setFireInfo(BlockLeave.getLeave(type), 30, 60);
            Blocks.FIRE.setFireInfo(BlockAtumLog.getLog(type), 5, 5);
            Blocks.FIRE.setFireInfo(BlockAtumStairs.getWoodStairs(type), 5, 20);
        }
    }
}