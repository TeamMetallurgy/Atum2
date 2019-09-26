package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.blocks.BlockAtumOres;
import com.teammetallurgy.atum.blocks.BlockPortal;
import com.teammetallurgy.atum.blocks.BlockSandLayers;
import com.teammetallurgy.atum.blocks.BlockStrangeSand;
import com.teammetallurgy.atum.blocks.base.BlockAtumDoor;
import com.teammetallurgy.atum.blocks.base.BlockAtumStairs;
import com.teammetallurgy.atum.blocks.beacon.BlockFramedRadiantBeacon;
import com.teammetallurgy.atum.blocks.beacon.BlockRadiantBeacon;
import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityHeartOfRa;
import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityRadiantBeacon;
import com.teammetallurgy.atum.blocks.glass.BlockAtumGlass;
import com.teammetallurgy.atum.blocks.glass.BlockAtumPane;
import com.teammetallurgy.atum.blocks.machines.BlockKiln;
import com.teammetallurgy.atum.blocks.machines.BlockKilnFake;
import com.teammetallurgy.atum.blocks.machines.BlockQuern;
import com.teammetallurgy.atum.blocks.machines.BlockSpinningWheel;
import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityKiln;
import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityQuern;
import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntitySpinningWheel;
import com.teammetallurgy.atum.blocks.stone.alabaster.BlockAlabaster;
import com.teammetallurgy.atum.blocks.stone.khnumite.BlockKhnumite;
import com.teammetallurgy.atum.blocks.stone.khnumite.BlockKhnumiteFace;
import com.teammetallurgy.atum.blocks.stone.limestone.*;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.BlockChestSpawner;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.BlockLimestoneChest;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.BlockSarcophagus;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntityChestSpawner;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntityLimestoneChest;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.furnace.TileEntityLimestoneFurnace;
import com.teammetallurgy.atum.blocks.stone.porphyry.BlockPorphyry;
import com.teammetallurgy.atum.blocks.trap.*;
import com.teammetallurgy.atum.blocks.trap.tileentity.*;
import com.teammetallurgy.atum.blocks.vegetation.*;
import com.teammetallurgy.atum.blocks.wood.*;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ObjectHolder;

import static com.teammetallurgy.atum.utils.AtumRegistry.registerBlock;
import static com.teammetallurgy.atum.utils.AtumRegistry.registerTileEntity;

@ObjectHolder(value = Constants.MOD_ID)
public class AtumBlocks {
    public static final Block PORTAL = registerBlock(new BlockPortal(), null, "portal");
    public static final Block CHEST_SPAWNER = registerBlock(new BlockChestSpawner(), null, "chest_spawner");
    public static final Block SAND = registerBlock(new BlockStrangeSand(), "sand");
    public static final Block SAND_LAYERED = registerBlock(new BlockSandLayers(), "sand_layer");
    public static final Block DATE_BLOCK = registerBlock(new BlockDate(), null, "date_block");
    public static final Block EMMER_WHEAT = registerBlock(new BlockEmmer(), null, "emmer_wheat");
    public static final Block ANPUTS_FINGERS = registerBlock(new BlockAnputsFingers(), null, "anputs_fingers");
    public static final Block OASIS_GRASS = registerBlock(new BlockOasisGrass(), "oasis_grass");
    public static final Block DEAD_GRASS = registerBlock(new BlockDeadGrass(), "dead_grass");
    public static final Block SHRUB = registerBlock(new BlockShrub(), "shrub");
    public static final Block WEED = registerBlock(new BlockShrub(), "weed");
    public static final Block PAPYRUS = registerBlock(new BlockPapyrus(), null, "papyrus");
    public static final Block OPHIDIAN_TONGUE = registerBlock(new BlockOphidianTongue(), "ophidian_tongue");
    public static final Block FLAX = registerBlock(new BlockFlax(), null, "flax_block");
    public static final Block FERTILE_SOIL = registerBlock(new BlockFertileSoil(), "fertile_soil");
    public static final Block FERTILE_SOIL_TILLED = registerBlock(new BlockFertileSoilTilled(), "fertile_soil_tilled");
    public static final Block QUERN = registerBlock(new BlockQuern(), "quern");
    public static final Block SPINNING_WHEEL = registerBlock(new BlockSpinningWheel(), "spinning_wheel");
    public static final Block KILN = registerBlock(new BlockKiln(), "kiln");
    public static final Block KILN_FAKE = registerBlock(new BlockKilnFake(), null, "kiln_fake");
    public static final Block BURNING_TRAP = registerBlock(new BlockBurningTrap(), "burning_trap");
    public static final Block POISON_TRAP = registerBlock(new BlockPoisonTrap(), "poison_trap");
    public static final Block TAR_TRAP = registerBlock(new BlockTarTrap(), "tar_trap");
    public static final Block SMOKE_TRAP = registerBlock(new BlockSmokeTrap(), "smoke_trap");
    public static final Block ARROW_TRAP = registerBlock(new BlockArrowTrap(), "arrow_trap");
    public static final Block SARCOPHAGUS = registerBlock(new BlockSarcophagus(), "sarcophagus");
    public static final Block LIMESTONE_CHEST = registerBlock(new BlockLimestoneChest(), "limestone_chest");
    public static final Block GOLD_ORE = registerBlock(new BlockAtumOres(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2)), "gold_ore");
    public static final Block IRON_ORE = registerBlock(new BlockAtumOres(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)), "iron_ore");
    public static final Block COAL_ORE = registerBlock(new BlockAtumOres(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)), "coal_ore");
    public static final Block LAPIS_ORE = registerBlock(new BlockAtumOres(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)), "lapis_ore");
    public static final Block DIAMOND_ORE = registerBlock(new BlockAtumOres(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2)), "diamond_ore");
    public static final Block EMERALD_ORE = registerBlock(new BlockAtumOres(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2)), "emerald_ore");
    public static final Block REDSTONE_ORE = registerBlock(new RedstoneOreBlock(Block.Properties.create(Material.ROCK).tickRandomly().lightValue(9).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2)), "redstone_ore");
    public static final Block BONE_ORE = registerBlock(new BlockAtumOres(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)), "bone_ore");
    public static final Block RELIC_ORE = registerBlock(new BlockAtumOres(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)), "relic_ore");
    public static final Block KHNUMITE_RAW = registerBlock(new Block(Block.Properties.create(Material.CLAY).hardnessAndResistance(0.6F).sound(SoundType.GROUND).harvestTool(ToolType.SHOVEL).harvestLevel(0)), "khnumite_raw");
    public static final Block DIRTY_BONE = registerBlock(new RotatedPillarBlock(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(2.0F)), "dirty_bone_block");
    public static final Block DIRTY_BONE_SLAB = registerBlock(new SlabBlock(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(2.0F)), "dirty_bone_slab");
    public static final Block BONE_LADDER = registerBlock(new BlockAtumLadder(), "bone_ladder");
    public static final Block LIMESTONE_FURNACE = registerBlock(new BlockLimestoneFurnace(false), "limestone_furnace");
    public static final Block PALM_TORCH = registerBlock(new BlockAtumTorch(), "palm_torch");
    public static final Block DEADWOOD_TORCH = registerBlock(new BlockAtumTorch(), "deadwood_torch");
    public static final Block LIMESTONE_TORCH = registerBlock(new BlockAtumTorch(), "limestone_torch");
    public static final Block BONE_TORCH = registerBlock(new BlockAtumTorch(), "bone_torch");
    public static final Block PHARAOH_TORCH = registerBlock(new BlockAtumTorch(), "pharaoh_torch");
    //BlockAtumTorchUnlit.registerUnlitTorches() //TODO
    public static final Block LIMESTONE_GRAVEL = registerBlock(new BlockLimestoneGravel(), "limestone_gravel");
    public static final Block MARL = registerBlock(new Block(Block.Properties.create(Material.CLAY).hardnessAndResistance(0.6F).sound(SoundType.GROUND).harvestTool(ToolType.SHOVEL).harvestLevel(0)), "marl");
    public static final Block RA_STONE = registerBlock(new BlockRaStone(), null, "ra_stone");
    public static final Block LIMESTONE = registerBlock(new BlockLimestone(), "limestone");
    public static final Block LIMESTONE_CRACKED = registerBlock(new Block(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(1.5F, 10.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)), "limestone_cracked");
    //BlockLimestoneBricks.registerBricks() //TODO
    public static final Block SMOOTH_LIMESTONE_SLAB = registerBlock(new SlabBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 6.0F)), "limestone_smooth_slab");
    public static final Block CRACKED_LIMESTONE_SLAB = registerBlock(new SlabBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 6.0F)), "limestone_cracked_slab");
    //BlockLimestoneSlab.registerSlabs() //TODO
    public static final Block KHNUMITE_BLOCK = registerBlock(new BlockKhnumite(), "khnumite_block");
    public static final Block KHNUMITE_FACE = registerBlock(new BlockKhnumiteFace(), "khnumite_face");
    public static final Block SMOOTH_STAIRS = registerBlock(new BlockAtumStairs(LIMESTONE::getDefaultState, Block.Properties.from(LIMESTONE)), "smooth_stairs");
    public static final Block CRACKED_STAIRS = registerBlock(new BlockAtumStairs(LIMESTONE_CRACKED::getDefaultState, Block.Properties.from(LIMESTONE_CRACKED)), "cracked_stairs");
    //BlockAtumStairs.registerLimestoneStairs() //TODO
    public static final Block LIMESTONE_WALL = registerBlock(new BlockLimestoneWall(), "limestone_wall");
    public static final Block LIMESTONE_CRACKED_WALL = registerBlock(new BlockLimestoneWall(), "limestone_cracked_wall");
    //BlockLimestoneWall.registerWalls() //TODO
    public static final Block LIMESTONE_DOOR = registerBlock(new BlockAtumDoor(Material.ROCK), "limestone_door");
    public static final Block LIMESTONE_CRACKED_DOOR = registerBlock(new BlockAtumDoor(Material.ROCK), "limestone_cracked_door");
    //BlockLimestoneBricks.registerDoors() //TODO
    public static final Block ALABASTER = registerBlock(new BlockAlabaster(), "alabaster");
    //BlockAlabasterBricks.registerBricks() //TODO
    //BlockAlabasterSlab.registerSlabs() //TODO
    //BlockAtumStairs.registerAlabasterStairs() //TODO
    //BlockAlabasterWall.registerWalls() //TODO
    public static final Block PORPHYRY = registerBlock(new BlockPorphyry(), "porphyry");
    //BlockPorphyryBricks.registerBricks() //TODO
    //BlockPorphyrySlab.registerSlabs() //TODO
    //BlockAtumStairs.registerPorphyryStairs() //TODO
    //BlockPorphyryWall.registerWalls() //TODO
    //BlockCeramic.registerCeramicBlocks() //TODO
    //BlockCeramicSlab.registerSlabs() //TODO
    //BlockCeramicTile.registerTile() //TODO
    //BlockAtumStairs.registerCeramicStairs() //TODO
    //BlockCeramicWall.registerWalls() //TODO
    public static final Block RADIANT_BEACON = registerBlock(new BlockRadiantBeacon(), "radiant_beacon");
    public static final Block RADIANT_BEACON_FRAMED = registerBlock(new BlockFramedRadiantBeacon(), null, "radiant_beacon_framed");
    public static final Block CRYSTAL_GLASS = registerBlock(new BlockAtumGlass(), "crystal_glass");
    //BlockAtumStainedGlass.registerStainedGlass(CRYSTAL_GLASS) //TODO
    public static final Block THIN_CRYSTAL_GLASS = registerBlock(new BlockAtumPane(), "thin_crystal_glass");
    //BlockAtumStainedGlassPane.registerStainedGlassPane(THIN_CRYSTAL_GLASS) //TODO
    public static final Block FRAMED_GLASS = registerBlock(new BlockAtumGlass(), "framed_glass");
    //BlockAtumStainedGlass.registerStainedGlass(FRAMED_GLASS) //TODO
    public static final Block THIN_FRAMED_GLASS = registerBlock(new BlockAtumPane(), "thin_framed_glass");
    //BlockAtumStainedGlassPane.registerStainedGlassPane(THIN_FRAMED_GLASS) //TODO
    //BlockLinen.registerLinenBlocks() //TODO
    //BlockLinenCarpet.registerLinenCarpets() //TODO
    //BlockAtumPlank.registerPlanks() //TODO
    public static final Block PALM_LOG = registerBlock(new BlockAtumLog(), "palm_log");
    public static final Block DEADWOOD_LOG = registerBlock(new BlockDeadwood(), "deadwood_log");
    public static final Block DEADWOOD_BRANCH = registerBlock(new BlockBranch(), null, "deadwood_branch");
    //BlockAtumStairs.registerWoodStairs() //TODO
    //BlockAtumWoodSlab.registerSlabs() //TODO
    //BlockAtumSapling.registerSaplings() //TODO
    //BlockLeave.registerLeaves() //TODO
    public static final Block PALM_CRATE = registerBlock(new BlockCrate(), "palm_crate");
    public static final Block DEADWOOD_CRATE = registerBlock(new BlockCrate(), "deadwood_crate");
    public static final Block PALM_LADDER = registerBlock(new BlockAtumLadder(), "palm_ladder");
    public static final Block DEADWOOD_LADDER = registerBlock(new BlockAtumLadder(), "deadwood_ladder");
    public static final Block PALM_FENCE = registerBlock(new BlockAtumFence(BlockAtumPlank.WoodType.PALM.getMapColor()), "palm_fence");
    public static final Block DEADWOOD_FENCE = registerBlock(new BlockAtumFence(BlockAtumPlank.WoodType.DEADWOOD.getMapColor()), "deadwood_fence");
    public static final Block PALM_FENCE_GATE = registerBlock(new BlockAtumFenceGate(), "palm_fence_gate");
    public static final Block DEADWOOD_FENCE_GATE = registerBlock(new BlockAtumFenceGate(), "deadwood_fence_gate");
    public static final Block PALM_HATCH = registerBlock(new BlockAtumTrapDoor(), "palm_hatch");
    public static final Block DEADWOOD_HATCH = registerBlock(new BlockAtumTrapDoor(), "deadwood_hatch");
    public static final Block PALM_DOOR = registerBlock(new BlockAtumDoor(Material.WOOD), "palm_door");
    public static final Block DEADWOOD_DOOR = registerBlock(new BlockAtumDoor(Material.WOOD), "deadwood_door");
    public static final Block HEART_OF_RA = AtumItems.HEART_OF_RA;

    public static void setBlockInfo() {
        //Fire Info
        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFireInfo(PALM_FENCE, 5, 20);
        fire.setFireInfo(PALM_FENCE_GATE, 5, 20);
        fire.setFireInfo(DEADWOOD_FENCE, 5, 20);
        fire.setFireInfo(DEADWOOD_FENCE_GATE, 5, 20);
        fire.setFireInfo(SHRUB, 60, 100);
        fire.setFireInfo(WEED, 60, 100);
        fire.setFireInfo(PALM_LOG, 5, 5);
        fire.setFireInfo(DEADWOOD_LOG, 5, 5);
        fire.setFireInfo(SPINNING_WHEEL, 2, 1);
        fire.setFireInfo(OPHIDIAN_TONGUE, 15, 100);
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            fire.setFireInfo(BlockAtumWoodSlab.getSlab(type), 5, 20);
            fire.setFireInfo(BlockAtumPlank.getPlank(type), 5, 20);
            fire.setFireInfo(BlockLeave.getLeave(type), 30, 60);
            fire.setFireInfo(BlockAtumStairs.getWoodStairs(type), 5, 20);
        }
        BlockKhnumiteFace.addDispenerSupport();
    }

    @ObjectHolder(Constants.MOD_ID)
    public static class AtumTileEntities {
        //Chests
        public static final TileEntityType<TileEntityLimestoneChest> LIMESTONE_CHEST = registerTileEntity("limestone_chest", TileEntityType.Builder.create(TileEntityLimestoneChest::new, AtumBlocks.LIMESTONE_CHEST));
        public static final TileEntityType<TileEntityChestSpawner> CHEST_SPAWNER = registerTileEntity("chest_spawner", TileEntityType.Builder.create(TileEntityChestSpawner::new, AtumBlocks.CHEST_SPAWNER));
        public static final TileEntityType<TileEntitySarcophagus> SARCOPHAGUS = registerTileEntity("sarcophagus", TileEntityType.Builder.create(TileEntitySarcophagus::new, AtumBlocks.SARCOPHAGUS));
        public static final TileEntityType<TileEntityLimestoneChest> CRATE = registerTileEntity("crate", TileEntityType.Builder.create(TileEntityLimestoneChest::new, AtumBlocks.PALM_CRATE, AtumBlocks.DEADWOOD_CRATE));
        //Traps
        public static final TileEntityType<TileEntityBurningTrap> BURNING_TRAP = registerTileEntity("burning_trap", TileEntityType.Builder.create(TileEntityBurningTrap::new, AtumBlocks.BURNING_TRAP));
        public static final TileEntityType<TileEntityPoisonTrap> POISON_TRAP = registerTileEntity("poison_trap", TileEntityType.Builder.create(TileEntityPoisonTrap::new, AtumBlocks.POISON_TRAP));
        public static final TileEntityType<TileEntityTarTrap> TAR_TRAP = registerTileEntity("tar_trap", TileEntityType.Builder.create(TileEntityTarTrap::new, AtumBlocks.TAR_TRAP));
        public static final TileEntityType<TileEntitySmokeTrap> SMOKE_TRAP = registerTileEntity("smoke_trap", TileEntityType.Builder.create(TileEntitySmokeTrap::new, AtumBlocks.SMOKE_TRAP));
        public static final TileEntityType<TileEntityArrowTrap> ARROW_TRAP = registerTileEntity("arrow_trap", TileEntityType.Builder.create(TileEntityArrowTrap::new, AtumBlocks.ARROW_TRAP));

        public static final TileEntityType<TileEntityHeartOfRa> HEART_OF_RA = registerTileEntity("heart_of_ra", TileEntityType.Builder.create(TileEntityHeartOfRa::new, AtumBlocks.HEART_OF_RA));
        public static final TileEntityType<TileEntityRadiantBeacon> RADIANT_BEACON = registerTileEntity("radiant_beacon", TileEntityType.Builder.create(TileEntityRadiantBeacon::new, AtumBlocks.RADIANT_BEACON, AtumBlocks.RADIANT_BEACON_FRAMED));
        public static final TileEntityType<TileEntityLimestoneFurnace> LIMESTONE_FURNACE = registerTileEntity("limestone_furnace", TileEntityType.Builder.create(TileEntityLimestoneFurnace::new, AtumBlocks.LIMESTONE_FURNACE));
        public static final TileEntityType<TileEntityQuern> QUERN = registerTileEntity("quern", TileEntityType.Builder.create(TileEntityQuern::new, AtumBlocks.QUERN));
        public static final TileEntityType<TileEntitySpinningWheel> SPINNING_WHEEL = registerTileEntity("spinning_wheel", TileEntityType.Builder.create(TileEntitySpinningWheel::new, AtumBlocks.SPINNING_WHEEL));
        public static final TileEntityType<TileEntityKiln> KILN = registerTileEntity("kiln", TileEntityType.Builder.create(TileEntityKiln::new, AtumBlocks.KILN));
    }
}