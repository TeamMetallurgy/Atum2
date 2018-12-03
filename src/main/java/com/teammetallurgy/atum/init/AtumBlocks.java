package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.blocks.*;
import com.teammetallurgy.atum.blocks.base.BlockAtumDoor;
import com.teammetallurgy.atum.blocks.base.BlockAtumSlab;
import com.teammetallurgy.atum.blocks.base.BlockAtumStairs;
import com.teammetallurgy.atum.blocks.base.ItemDoubleChest;
import com.teammetallurgy.atum.blocks.beacon.BlockFramedRadiantBeacon;
import com.teammetallurgy.atum.blocks.beacon.BlockHeartOfRa;
import com.teammetallurgy.atum.blocks.beacon.BlockRadiantBeacon;
import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityHeartOfRa;
import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityRadiantBeacon;
import com.teammetallurgy.atum.blocks.glass.BlockAtumGlass;
import com.teammetallurgy.atum.blocks.glass.BlockAtumPane;
import com.teammetallurgy.atum.blocks.glass.BlockAtumStainedGlass;
import com.teammetallurgy.atum.blocks.glass.BlockAtumStainedGlassPane;
import com.teammetallurgy.atum.blocks.machines.BlockQuern;
import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityQuern;
import com.teammetallurgy.atum.blocks.stone.alabaster.BlockAlabaster;
import com.teammetallurgy.atum.blocks.stone.alabaster.BlockAlabasterBricks;
import com.teammetallurgy.atum.blocks.stone.alabaster.BlockAlabasterSlab;
import com.teammetallurgy.atum.blocks.stone.alabaster.BlockAlabasterWall;
import com.teammetallurgy.atum.blocks.stone.khnumite.BlockKhnumite;
import com.teammetallurgy.atum.blocks.stone.khnumite.BlockKhnumiteFace;
import com.teammetallurgy.atum.blocks.stone.khnumite.BlockKhnumiteRaw;
import com.teammetallurgy.atum.blocks.stone.limestone.*;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.BlockChestSpawner;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.BlockLimestoneChest;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.BlockSarcophagus;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntityChestSpawner;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntityLimestoneChest;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.furnace.TileEntityLimestoneFurnace;
import com.teammetallurgy.atum.blocks.stone.porphyry.BlockPorphyry;
import com.teammetallurgy.atum.blocks.stone.porphyry.BlockPorphyryBricks;
import com.teammetallurgy.atum.blocks.stone.porphyry.BlockPorphyrySlab;
import com.teammetallurgy.atum.blocks.stone.porphyry.BlockPorphyryWall;
import com.teammetallurgy.atum.blocks.trap.*;
import com.teammetallurgy.atum.blocks.trap.tileentity.*;
import com.teammetallurgy.atum.blocks.vegetation.*;
import com.teammetallurgy.atum.blocks.wood.*;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.items.ItemAtumSlab;
import com.teammetallurgy.atum.items.ItemSand;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.teammetallurgy.atum.utils.AtumRegistry.registerBlock;

@GameRegistry.ObjectHolder(value = Constants.MOD_ID)
public class AtumBlocks {
    public static final BlockPortal PORTAL = new BlockPortal();
    public static final BlockChestSpawner CHEST_SPAWNER = new BlockChestSpawner();
    public static final Block SAND = new BlockStrangeSand();
    public static final Block LIMESTONE_GRAVEL = new BlockLimestoneGravel();
    public static final Block SANDY_CLAY = new BlockSandyClay();
    public static final Block LIMESTONE = new BlockLimestone();
    public static final Block LIMESTONE_CRACKED = new Block(Material.ROCK, MapColor.SAND).setHardness(1.5F).setResistance(10.0F);
    public static final Block LIMESTONE_WALL = new BlockLimestoneWall();
    public static final Block LIMESTONE_CRACKED_WALL = new BlockLimestoneWall();
    public static final Block ALABASTER = new BlockAlabaster();
    public static final Block PORPHYRY = new BlockPorphyry();
    public static final Block RA_STONE = new BlockRaStone();
    public static final Block KHNUMITE_BLOCK = new BlockKhnumite();
    public static final Block KHNUMITE_FACE = new BlockKhnumiteFace();
    public static final Block SMOOTH_STAIRS = new BlockAtumStairs(LIMESTONE.getDefaultState());
    public static final Block CRACKED_STAIRS = new BlockAtumStairs(LIMESTONE_CRACKED.getDefaultState());
    public static final Block SMOOTH_LIMESTONE_SLAB = new BlockAtumSlab(Material.ROCK);
    public static final Block CRACKED_LIMESTONE_SLAB = new BlockAtumSlab(Material.ROCK);
    public static final Block LIMESTONE_DOOR = new BlockAtumDoor(Material.ROCK);
    public static final Block LIMESTONE_CRACKED_DOOR = new BlockAtumDoor(Material.ROCK);
    public static final Block SAND_LAYERED = new BlockSandLayers();
    public static final Block RADIANT_BEACON = new BlockRadiantBeacon();
    public static final Block RADIANT_BEACON_FRAMED = new BlockFramedRadiantBeacon();
    public static final Block CRYSTAL_GLASS = new BlockAtumGlass(Material.GLASS);
    public static final Block FRAMED_GLASS = new BlockAtumGlass(Material.GLASS);
    public static final Block DATE_BLOCK = new BlockDate();
    public static final Block EMMER_WHEAT = new BlockEmmer();
    public static final BlockOasisGrass OASIS_GRASS = new BlockOasisGrass();
    public static final BlockShrub SHRUB = new BlockShrub();
    public static final BlockShrub WEED = new BlockShrub();
    public static final BlockPapyrus PAPYRUS = new BlockPapyrus();
    public static final Block FLAX = new BlockFlax();
    public static final Block FERTILE_SOIL = new BlockFertileSoil();
    public static final Block FERTILE_SOIL_TILLED = new BlockFertileSoilTilled();
    public static final BlockAtumDoor PALM_DOOR = new BlockAtumDoor(Material.WOOD);
    public static final Block PALM_FENCE = new BlockAtumFence(BlockAtumPlank.WoodType.PALM.getMapColor());
    public static final Block PALM_FENCE_GATE = new BlockAtumFenceGate();
    public static final Block PALM_HATCH = new BlockAtumTrapDoor();
    public static final Block PALM_LADDER = new BlockAtumLadder();
    public static final Block THIN_CRYSTAL_GLASS = new BlockAtumPane();
    public static final Block THIN_FRAMED_GLASS = new BlockAtumPane();
    public static final Block PALM_LOG = new BlockAtumLog();
    public static final Block DEADWOOD_LOG = new BlockDeadwood();
    public static final BlockAtumTorch PALM_TORCH = new BlockAtumTorch();
    public static final BlockAtumTorch DEADWOOD_TORCH = new BlockAtumTorch();
    public static final BlockAtumTorch LIMESTONE_TORCH = new BlockAtumTorch();
    public static final BlockAtumTorch BONE_TORCH = new BlockAtumTorch();
    public static final BlockAtumTorch PHARAOH_TORCH = new BlockAtumTorch();
    public static final Block QUERN = new BlockQuern();
    public static final Block BURNING_TRAP = new BlockBurningTrap();
    public static final Block POISON_TRAP = new BlockPoisonTrap();
    public static final Block TAR_TRAP = new BlockTarTrap();
    public static final Block SMOKE_TRAP = new BlockSmokeTrap();
    public static final Block ARROW_TRAP = new BlockArrowTrap();
    public static final Block SARCOPHAGUS = new BlockSarcophagus();
    public static final Block LIMESTONE_CHEST = new BlockLimestoneChest();
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
    public static final Block KHNUMITE_RAW = new BlockKhnumiteRaw();
    public static final Block BONE_DIRTY = new BlockBone();
    public static final Block BONE_DIRTY_SLAB = new BlockAtumSlab(Material.ROCK, MapColor.SAND);
    public static final Block BONE_LADDER = new BlockAtumLadder();
    public static final Block LIMESTONE_FURNACE = new BlockLimestoneFurnace(false);
    public static final Block LIMESTONE_FURNACE_LIT = new BlockLimestoneFurnace(true);
    public static final BlockAtumDoor DEADWOOD_DOOR = new BlockAtumDoor(Material.WOOD);
    public static final Block DEADWOOD_FENCE = new BlockAtumFence(BlockAtumPlank.WoodType.DEADWOOD.getMapColor());
    public static final Block DEADWOOD_FENCE_GATE = new BlockAtumFenceGate();
    public static final Block DEADWOOD_HATCH = new BlockAtumTrapDoor();
    public static final Block DEADWOOD_LADDER = new BlockAtumLadder();
    public static final Block HEART_OF_RA = new BlockHeartOfRa();

    public static void registerBlocks() {
        registerBlock(PORTAL, "portal", null);
        registerBlock(CHEST_SPAWNER, "chest_spawner", null);
        registerBlock(SAND, "sand");
        registerBlock(SAND_LAYERED, new ItemSand(SAND_LAYERED), "sand_layer");
        registerBlock(DATE_BLOCK, "date_block", null);
        registerBlock(EMMER_WHEAT, "emmer_wheat", null);
        registerBlock(OASIS_GRASS, "oasis_grass");
        registerBlock(SHRUB, "shrub");
        registerBlock(WEED, "weed");
        registerBlock(PAPYRUS, "papyrus", null);
        registerBlock(FLAX, "flax_block", null);
        registerBlock(FERTILE_SOIL, "fertile_soil");
        registerBlock(FERTILE_SOIL_TILLED, "fertile_soil_tilled");
        registerBlock(QUERN, "quern");
        registerBlock(BURNING_TRAP, "burning_trap");
        registerBlock(POISON_TRAP, "poison_trap");
        registerBlock(TAR_TRAP, "tar_trap");
        registerBlock(SMOKE_TRAP, "smoke_trap");
        registerBlock(ARROW_TRAP, "arrow_trap");
        registerBlock(SARCOPHAGUS, new ItemDoubleChest(SARCOPHAGUS), "sarcophagus");
        registerBlock(LIMESTONE_CHEST, "limestone_chest");
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
        registerBlock(KHNUMITE_RAW, "khnumite_raw");
        registerBlock(BONE_DIRTY, "dirty_bone_block");
        registerBlock(BONE_DIRTY_SLAB, new ItemAtumSlab(BONE_DIRTY_SLAB, (BlockAtumSlab) BONE_DIRTY_SLAB), "dirty_bone_slab");
        registerBlock(BONE_LADDER, "bone_ladder");
        registerBlock(LIMESTONE_FURNACE, "limestone_furnace");
        registerBlock(LIMESTONE_FURNACE_LIT, "limestone_furnace_lit", null);
        registerBlock(PALM_TORCH, "palm_torch");
        registerBlock(DEADWOOD_TORCH, "deadwood_torch");
        registerBlock(LIMESTONE_TORCH, "limestone_torch");
        registerBlock(BONE_TORCH, "bone_torch");
        registerBlock(PHARAOH_TORCH, "pharaoh_torch");
        BlockAtumTorchUnlit.registerUnlitTorches();

        //Limestone
        BlockLimestoneBricks.registerBricks();
        registerBlock(SMOOTH_LIMESTONE_SLAB, new ItemAtumSlab(SMOOTH_LIMESTONE_SLAB, (BlockAtumSlab) SMOOTH_LIMESTONE_SLAB), "limestone_smooth_slab");
        registerBlock(CRACKED_LIMESTONE_SLAB, new ItemAtumSlab(CRACKED_LIMESTONE_SLAB, (BlockAtumSlab) CRACKED_LIMESTONE_SLAB), "limestone_cracked_slab");
        BlockLimestoneSlab.registerSlabs();
        registerBlock(LIMESTONE_GRAVEL, "limestone_gravel");
        registerBlock(SANDY_CLAY, "sandy_clay");
        registerBlock(RA_STONE, "ra_stone", null);
        registerBlock(KHNUMITE_BLOCK, "khnumite_block");
        registerBlock(KHNUMITE_FACE, "khnumite_face");
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

        //Alabaster
        registerBlock(ALABASTER, "alabaster");
        BlockAlabasterBricks.registerBricks();
        BlockAlabasterSlab.registerSlabs();
        BlockAtumStairs.registerAlabasterStairs();
        BlockAlabasterWall.registerWalls();

        //Porphyry
        registerBlock(PORPHYRY, "porphyry");
        BlockPorphyryBricks.registerBricks();
        BlockPorphyrySlab.registerSlabs();
        BlockAtumStairs.registerPorphyryStairs();
        BlockPorphyryWall.registerWalls();

        registerBlock(RADIANT_BEACON, "radiant_beacon");
        registerBlock(RADIANT_BEACON_FRAMED, "radiant_beacon_framed", null);

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
        registerBlock(PALM_LOG, "palm_log");
        registerBlock(DEADWOOD_LOG, "deadwood_log");
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
        GameRegistry.registerTileEntity(TileEntityLimestoneChest.class, new ResourceLocation(Constants.MOD_ID, "limestone_chest"));
        GameRegistry.registerTileEntity(TileEntityChestSpawner.class, new ResourceLocation(Constants.MOD_ID, "cursed_chest"));
        GameRegistry.registerTileEntity(TileEntitySarcophagus.class, new ResourceLocation(Constants.MOD_ID, "sarcophagus"));
        GameRegistry.registerTileEntity(TileEntityBurningTrap.class, new ResourceLocation(Constants.MOD_ID, "burning_trap"));
        GameRegistry.registerTileEntity(TileEntityPoisonTrap.class, new ResourceLocation(Constants.MOD_ID, "poison_trap"));
        GameRegistry.registerTileEntity(TileEntityTarTrap.class, new ResourceLocation(Constants.MOD_ID, "tar_trap"));
        GameRegistry.registerTileEntity(TileEntitySmokeTrap.class, new ResourceLocation(Constants.MOD_ID, "smoke_trap"));
        GameRegistry.registerTileEntity(TileEntityArrowTrap.class, new ResourceLocation(Constants.MOD_ID, "arrow_trap"));
        GameRegistry.registerTileEntity(TileEntityLimestoneFurnace.class, new ResourceLocation(Constants.MOD_ID, "limestone_furnace"));
        GameRegistry.registerTileEntity(TileEntityCrate.class, new ResourceLocation(Constants.MOD_ID, "crate"));
        GameRegistry.registerTileEntity(TileEntityHeartOfRa.class, new ResourceLocation(Constants.MOD_ID, "heart_of_ra"));
        GameRegistry.registerTileEntity(TileEntityRadiantBeacon.class, new ResourceLocation(Constants.MOD_ID, "radiant_beacon"));
        GameRegistry.registerTileEntity(TileEntityQuern.class, new ResourceLocation(Constants.MOD_ID, "quern"));
    }

    public static void setBlockInfo() {
        //Harvest Levels
        SAND.setHarvestLevel("shovel", 0);
        SAND_LAYERED.setHarvestLevel("shovel", 0);
        LIMESTONE_GRAVEL.setHarvestLevel("shovel", 0);
        SANDY_CLAY.setHarvestLevel("shovel", 0);
        KHNUMITE_RAW.setHarvestLevel("shovel", 0);
        FERTILE_SOIL.setHarvestLevel("shovel", 0);
        FERTILE_SOIL_TILLED.setHarvestLevel("shovel", 0);
        LIMESTONE_CRACKED.setHarvestLevel("pickaxe", 0);
        KHNUMITE_BLOCK.setHarvestLevel("pickaxe", 0);
        KHNUMITE_BLOCK.setHarvestLevel("pickaxe", 1);
        KHNUMITE_FACE.setHarvestLevel("pickaxe", 1);
        COAL_ORE.setHarvestLevel("pickaxe", 0);
        BONE_ORE.setHarvestLevel("pickaxe", 0);
        IRON_ORE.setHarvestLevel("pickaxe", 1);
        RELIC_ORE.setHarvestLevel("pickaxe", 1);
        GOLD_ORE.setHarvestLevel("pickaxe", 2);
        LAPIS_ORE.setHarvestLevel("pickaxe", 1);
        EMERALD_ORE.setHarvestLevel("pickaxe", 1);
        DIAMOND_ORE.setHarvestLevel("pickaxe", 2);
        REDSTONE_ORE.setHarvestLevel("pickaxe", 2);

        //Fire Info
        Blocks.FIRE.setFireInfo(PALM_FENCE, 5, 20);
        Blocks.FIRE.setFireInfo(PALM_FENCE_GATE, 5, 20);
        Blocks.FIRE.setFireInfo(DEADWOOD_FENCE, 5, 20);
        Blocks.FIRE.setFireInfo(DEADWOOD_FENCE_GATE, 5, 20);
        Blocks.FIRE.setFireInfo(SHRUB, 60, 100);
        Blocks.FIRE.setFireInfo(WEED, 60, 100);
        Blocks.FIRE.setFireInfo(PALM_LOG, 5, 5);
        Blocks.FIRE.setFireInfo(DEADWOOD_LOG, 5, 5);
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            Blocks.FIRE.setFireInfo(BlockAtumWoodSlab.getSlab(type), 5, 20);
            Blocks.FIRE.setFireInfo(BlockAtumPlank.getPlank(type), 5, 20);
            Blocks.FIRE.setFireInfo(BlockLeave.getLeave(type), 30, 60);
            Blocks.FIRE.setFireInfo(BlockAtumStairs.getWoodStairs(type), 5, 20);
        }

        BlockKhnumiteFace.addDispenerSupport();

        //Ore Dictionary
        OreDictHelper.add(LIMESTONE_CRACKED, "cobblestone");
        OreDictHelper.add(LIMESTONE_GRAVEL, "gravel");
        OreDictHelper.add(Item.REGISTRY.getObject(PALM_DOOR.getRegistryName()), "doorWood");
        OreDictHelper.add(Item.REGISTRY.getObject(DEADWOOD_DOOR.getRegistryName()), "doorWood");
        OreDictHelper.add(KHNUMITE_RAW, "stoneKhnumite");
    }
}