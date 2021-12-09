package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.*;
import com.teammetallurgy.atum.blocks.base.AtumPaneBlock;
import com.teammetallurgy.atum.blocks.base.DoorAtumBlock;
import com.teammetallurgy.atum.blocks.curio.CurioDisplayBlock;
import com.teammetallurgy.atum.blocks.curio.tileentity.*;
import com.teammetallurgy.atum.blocks.lighting.AtumLanternBlock;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchBlock;
import com.teammetallurgy.atum.blocks.linen.LinenBlock;
import com.teammetallurgy.atum.blocks.linen.LinenCarpetBlock;
import com.teammetallurgy.atum.blocks.machines.*;
import com.teammetallurgy.atum.blocks.stone.ceramic.CeramicBlock;
import com.teammetallurgy.atum.blocks.stone.ceramic.CeramicTileBlock;
import com.teammetallurgy.atum.blocks.stone.khnumite.KhnumiteBlock;
import com.teammetallurgy.atum.blocks.stone.khnumite.KhnumiteFaceBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.*;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.LimestoneChestBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.SarcophagusBlock;
import com.teammetallurgy.atum.blocks.trap.*;
import com.teammetallurgy.atum.blocks.vegetation.*;
import com.teammetallurgy.atum.blocks.wood.*;
import com.teammetallurgy.atum.client.render.ItemStackRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

import static com.teammetallurgy.atum.misc.AtumRegistry.*;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;

@ObjectHolder(value = Atum.MOD_ID)
public class AtumBlocks {
    public static final PortalBlock PORTAL = (PortalBlock) registerBlock(new PortalBlock(), null, "portal");
    public static final Block SAND = registerBlock(new StrangeSandBlock(), "sand");
    public static final Block SAND_LAYERED = registerBlock(new SandLayersBlock(), "sand_layer");
    public static final Block LIMESTONE_GRAVEL = registerBlock(new LimestoneGravelBlock(), "limestone_gravel");
    public static final Block DATE_BLOCK = registerBlock(new DateBlock(), null, "date_block");
    public static final Block EMMER_WHEAT = registerBlock(new EmmerBlock(), null, "emmer_wheat");
    public static final Block EMMER_BLOCK = registerBlock(new HayBlock(BlockBehaviour.Properties.of(Material.GRASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.GRASS)), "emmer_block");
    public static final Block ANPUTS_FINGERS = registerBlock(new AnputsFingersBlock(), null, "anputs_fingers");
    public static final Block OASIS_GRASS = registerBlock(new OasisGrassBlock(), "oasis_grass");
    public static final Block DRY_GRASS = registerBlock(new DryGrassBlock(), "dry_grass");
    public static final Block TALL_DRY_GRASS = registerBlock(new TallDryGrass(), "tall_dry_grass");
    public static final Block SHRUB = registerBlock(new ShrubBlock(), "shrub");
    public static final Block WEED = registerBlock(new ShrubBlock(), "weed");
    public static final Block PAPYRUS = registerBlock(new PapyrusBlock(), null, "papyrus");
    public static final Block OPHIDIAN_TONGUE = registerBlock(new OphidianTongueBlock(), "ophidian_tongue");
    public static final Block FLAX = registerBlock(new FlaxBlock(), null, "flax_block");
    public static final Block FERTILE_SOIL = registerBlock(new FertileSoilBlock(), "fertile_soil");
    public static final Block FERTILE_SOIL_TILLED = registerBlock(new FertileSoilTilledBlock(), "fertile_soil_tilled");
    public static final Block FERTILE_SOIL_PATH = registerBlock(new AtumPathBlock(FERTILE_SOIL), "fertile_soil_path");
    public static final Block STRANGE_SAND_PATH = registerBlock(new AtumPathBlock(SAND), "strange_sand_path");
    public static final Block QUERN = registerBlock(new QuernBlock(), "quern");
    public static final Block SPINNING_WHEEL = registerBlock(new SpinningWheelBlock(), "spinning_wheel");
    public static final Block KILN = registerBlock(new KilnBlock(), "kiln");
    public static final Block KILN_FAKE = registerBlock(new KilnFakeBlock(), null, "kiln_fake");
    public static final Block GODFORGE = registerBlock(new GodforgeBlock(), "godforge");
    public static final Block QUANDARY_BLOCK = registerBlock(new QuandaryBlock(), "quandary_block");
    public static final Block GLASSBLOWER_FURNACE = registerBlock(new GlassblowerFurnace(), "glassblower_furnace");
    public static final Block PALM_CURIO_DISPLAY = registerWithRenderer(new CurioDisplayBlock(Material.WOOD) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new PalmCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "palm_curio_display");
    public static final Block DEADWOOD_CURIO_DISPLAY = registerWithRenderer(new CurioDisplayBlock(Material.WOOD) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new DeadwoodCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "deadwood_curio_display");
    public static final Block ACACIA_CURIO_DISPLAY = registerWithRenderer(new CurioDisplayBlock(Material.WOOD) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new AcaciaCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "acacia_curio_display");
    public static final Block LIMESTONE_CURIO_DISPLAY = registerWithRenderer(new CurioDisplayBlock(Material.STONE) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new LimestoneCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "limestone_curio_display");
    public static final Block ALABASTER_CURIO_DISPLAY = registerWithRenderer(new CurioDisplayBlock(Material.STONE) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new AlabasterCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "alabaster_curio_display");
    public static final Block PORPHYRY_CURIO_DISPLAY = registerWithRenderer(new CurioDisplayBlock(Material.STONE) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new PorphyryCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "porphyry_curio_display");
    public static final Block NEBU_CURIO_DISPLAY = registerWithRenderer(new CurioDisplayBlock(Material.METAL) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new NebuCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "nebu_curio_display");
    public static final Block BURNING_TRAP = registerBlock(new BurningTrapBlock(), "burning_trap");
    public static final Block POISON_TRAP = registerBlock(new PoisonTrapBlock(), "poison_trap");
    public static final Block TAR_TRAP = registerBlock(new TarTrapBlock(), "tar_trap");
    public static final Block SMOKE_TRAP = registerBlock(new SmokeTrapBlock(), "smoke_trap");
    public static final Block ARROW_TRAP = registerBlock(new ArrowTrapBlock(), "arrow_trap");
    public static final Block SARCOPHAGUS = registerWithRenderer(new SarcophagusBlock(), new Item.Properties(), "sarcophagus");
    public static final Block LIMESTONE_CHEST = registerWithRenderer(new LimestoneChestBlock(), new Item.Properties(), "limestone_chest");
    public static final Block GOLD_ORE = registerBlock(new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)), "gold_ore");
    public static final Block IRON_ORE = registerBlock(new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)), "iron_ore");
    public static final Block COAL_ORE = registerBlock(new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 2)), "coal_ore");
    public static final Block LAPIS_ORE = registerBlock(new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(2, 5)), "lapis_ore");
    public static final Block DIAMOND_ORE = registerBlock(new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)), "diamond_ore");
    public static final Block EMERALD_ORE = registerBlock(new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)), "emerald_ore");
    public static final Block REDSTONE_ORE = registerBlock(new RedStoneOreBlock(of(Material.STONE).requiresCorrectToolForDrops().randomTicks().lightLevel(s -> 9).strength(3.0F, 3.0F)), "redstone_ore");
    public static final Block KHNUMITE_RAW = registerBlock(new Block(of(Material.CLAY).strength(0.6F).sound(SoundType.GRAVEL)), "khnumite_raw");
    public static final Block BONE_ORE = registerBlock(new OreBlock(of(Material.STONE).strength(3.0F, 3.0F), UniformInt.of(0, 2)), "bone_ore");
    public static final Block RELIC_ORE = registerBlock(new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 2)), "relic_ore");
    public static final Block NEBU_ORE = registerBlock(new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)), "nebu_ore");
    public static final Block NEBU_BLOCK = registerBlock(new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)), "nebu_block");
    public static final Block GODFORGED_BLOCK = registerBlock(new GodforgedBlock(), "godforged_block");
    public static final Block ANPUT_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.ANPUT), "anput_godforged_block");
    public static final Block ANUBIS_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.ANUBIS), "anubis_godforged_block");
    public static final Block ATEM_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.ATEM), "atem_godforged_block");
    public static final Block GEB_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.GEB), "geb_godforged_block");
    public static final Block HORUS_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.HORUS), "horus_godforged_block");
    public static final Block ISIS_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.ISIS), "isis_godforged_block");
    public static final Block MONTU_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.MONTU), "montu_godforged_block");
    public static final Block NEPTHYS_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.NEPTHYS), "nepthys_godforged_block");
    public static final Block NUIT_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.NUIT), "nuit_godforged_block");
    public static final Block OSIRIS_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.OSIRIS), "osiris_godforged_block");
    public static final Block PTAH_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.PTAH), "ptah_godforged_block");
    public static final Block RA_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.RA), "ra_godforged_block");
    public static final Block SETH_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.SETH), "seth_godforged_block");
    public static final Block SHU_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.SHU), "shu_godforged_block");
    public static final Block TEFNUT_GODFORGED_BLOCK = registerBlock(new GodGodforgedBlock(God.TEFNUT), "tefnut_godforged_block");
    public static final Block DIRTY_BONE = registerBlock(new RotatedPillarBlock(of(Material.STONE, MaterialColor.SAND).strength(2.0F)), "dirty_bone_block");
    public static final Block DIRTY_BONE_SLAB = registerBlock(new SlabBlock(of(Material.STONE, MaterialColor.SAND).strength(2.0F)), "dirty_bone_slab");
    public static final Block BONE_LADDER = registerBlock(new AtumLadderBlock(), "bone_ladder");
    public static final Block LIMESTONE_FURNACE = registerBlock(new LimestoneFurnaceBlock(), "limestone_furnace");
    public static final Block PALM_TORCH = registerTorchWithUnlit(new AtumTorchBlock(14), "palm_torch");
    public static final Block DEADWOOD_TORCH = registerTorchWithUnlit(new AtumTorchBlock(14), "deadwood_torch");
    public static final Block LIMESTONE_TORCH = registerTorchWithUnlit(new AtumTorchBlock(14), "limestone_torch");
    public static final Block BONE_TORCH = registerTorchWithUnlit(new AtumTorchBlock(14), "bone_torch");
    public static final Block NEBU_TORCH = registerTorchWithUnlit(new AtumTorchBlock(null), "nebu_torch");
    public static final Block TORCH_OF_ANPUT = registerTorch(new AtumTorchBlock(God.ANPUT), "torch_of_anput");
    public static final Block TORCH_OF_ANUBIS = registerTorch(new AtumTorchBlock(God.ANUBIS), "torch_of_anubis");
    public static final Block TORCH_OF_ATEM = registerTorch(new AtumTorchBlock(God.ATEM), "torch_of_atem");
    public static final Block TORCH_OF_GEB = registerTorch(new AtumTorchBlock(God.GEB), "torch_of_geb");
    public static final Block TORCH_OF_HORUS = registerTorch(new AtumTorchBlock(God.HORUS), "torch_of_horus");
    public static final Block TORCH_OF_ISIS = registerTorch(new AtumTorchBlock(God.ISIS), "torch_of_isis");
    public static final Block TORCH_OF_MONTU = registerTorch(new AtumTorchBlock(God.MONTU), "torch_of_montu");
    public static final Block TORCH_OF_NEPTHYS = registerTorch(new AtumTorchBlock(God.NEPTHYS), "torch_of_nepthys");
    public static final Block TORCH_OF_NUIT = registerTorch(new AtumTorchBlock(God.NUIT), "torch_of_nuit");
    public static final Block TORCH_OF_OSIRIS = registerTorch(new AtumTorchBlock(God.OSIRIS), "torch_of_osiris");
    public static final Block TORCH_OF_PTAH = registerTorch(new AtumTorchBlock(God.PTAH), "torch_of_ptah");
    public static final Block TORCH_OF_RA = registerTorch(new AtumTorchBlock(God.RA), "torch_of_ra");
    public static final Block TORCH_OF_SETH = registerTorch(new AtumTorchBlock(God.SETH), "torch_of_seth");
    public static final Block TORCH_OF_SHU = registerTorch(new AtumTorchBlock(God.SHU), "torch_of_shu");
    public static final Block TORCH_OF_TEFNUT = registerTorch(new AtumTorchBlock(God.TEFNUT), "torch_of_tefnut");
    public static final Block NEBU_LANTERN = registerBlock(new AtumLanternBlock(), "nebu_lantern");
    public static final Block LANTERN_OF_ANPUT = registerBlock(new AtumLanternBlock(), "lantern_of_anput");
    public static final Block LANTERN_OF_ANUBIS = registerBlock(new AtumLanternBlock(), "lantern_of_anubis");
    public static final Block LANTERN_OF_ATEM = registerBlock(new AtumLanternBlock(), "lantern_of_atem");
    public static final Block LANTERN_OF_GEB = registerBlock(new AtumLanternBlock(), "lantern_of_geb");
    public static final Block LANTERN_OF_HORUS = registerBlock(new AtumLanternBlock(), "lantern_of_horus");
    public static final Block LANTERN_OF_ISIS = registerBlock(new AtumLanternBlock(), "lantern_of_isis");
    public static final Block LANTERN_OF_MONTU = registerBlock(new AtumLanternBlock(), "lantern_of_montu");
    public static final Block LANTERN_OF_NEPTHYS = registerBlock(new AtumLanternBlock(), "lantern_of_nepthys");
    public static final Block LANTERN_OF_NUIT = registerBlock(new AtumLanternBlock(), "lantern_of_nuit");
    public static final Block LANTERN_OF_OSIRIS = registerBlock(new AtumLanternBlock(), "lantern_of_osiris");
    public static final Block LANTERN_OF_PTAH = registerBlock(new AtumLanternBlock(), "lantern_of_ptah");
    public static final Block LANTERN_OF_RA = registerBlock(new AtumLanternBlock(), "lantern_of_ra");
    public static final Block LANTERN_OF_SETH = registerBlock(new AtumLanternBlock(), "lantern_of_seth");
    public static final Block LANTERN_OF_SHU = registerBlock(new AtumLanternBlock(), "lantern_of_shu");
    public static final Block LANTERN_OF_TEFNUT = registerBlock(new AtumLanternBlock(), "lantern_of_tefnut");
    public static final Block NEBU_CHAIN = registerBlock(new ChainBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.NONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.CHAIN).noOcclusion()), "nebu_chain");
    public static final Block MARL = registerBlock(new Block(of(Material.CLAY).strength(0.6F).sound(SoundType.GRAVEL)), "marl");
    public static final Block RA_STONE = registerBlock(new RaStoneBlock(), null, "ra_stone");
    public static final Block LIMESTONE = registerBlock(new LimestoneBlock(), "limestone");
    public static final Block LIMESTONE_CRACKED = registerBlock(new Block(of(Material.STONE, MaterialColor.SAND).strength(1.5F, 10.0F)), "limestone_cracked");
    public static final Block LIMESTONE_BRICK_SMALL = registerBlock(new LimestoneBrickBlock(), "limestone_brick_small");
    public static final Block LIMESTONE_BRICK_LARGE = registerBlock(new LimestoneBrickBlock(), "limestone_brick_large");
    public static final Block LIMESTONE_BRICK_CRACKED_BRICK = registerBlock(new LimestoneBrickBlock(), "limestone_brick_cracked_brick");
    public static final Block LIMESTONE_BRICK_CHISELED = registerBlock(new LimestoneBrickBlock(), "limestone_brick_chiseled");
    public static final Block LIMESTONE_BRICK_CARVED = registerBlock(new LimestoneBrickBlock(), "limestone_brick_carved");
    public static final Block LIMESTONE_SLAB = registerBlock(new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_slab");
    public static final Block LIMESTONE_CRACKED_SLAB = registerBlock(new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_cracked_slab");
    public static final Block LIMESTONE_BRICK_SMALL_SLAB = registerBlock(new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_small_slab");
    public static final Block LIMESTONE_BRICK_LARGE_SLAB = registerBlock(new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_large_slab");
    public static final Block LIMESTONE_CRACKED_BRICK_SLAB = registerBlock(new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_cracked_brick_slab");
    public static final Block LIMESTONE_BRICK_CHISELED_SLAB = registerBlock(new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_chiseled_slab");
    public static final Block LIMESTONE_BRICK_CARVED_SLAB = registerBlock(new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_carved_slab");
    public static final Block KHNUMITE_BLOCK = registerBlock(new KhnumiteBlock(), "khnumite_block");
    public static final Block KHNUMITE_FACE = registerBlock(new KhnumiteFaceBlock(), "khnumite_face");
    public static final Block SMOOTH_STAIRS = registerBlock(new StairBlock(LIMESTONE::defaultBlockState, copy(LIMESTONE)), "smooth_stairs");
    public static final Block CRACKED_STAIRS = registerBlock(new StairBlock(LIMESTONE_CRACKED::defaultBlockState, copy(LIMESTONE_CRACKED)), "cracked_stairs");
    public static final Block SMALL_STAIRS = registerBlock(new StairBlock(LIMESTONE_BRICK_SMALL::defaultBlockState, copy(LIMESTONE_BRICK_SMALL)), "small_stairs");
    public static final Block LARGE_STAIRS = registerBlock(new StairBlock(LIMESTONE_BRICK_LARGE::defaultBlockState, copy(LIMESTONE_BRICK_LARGE)), "large_stairs");
    public static final Block CRACKED_BRICK_STAIRS = registerBlock(new StairBlock(LIMESTONE_BRICK_CRACKED_BRICK::defaultBlockState, copy(LIMESTONE_BRICK_CRACKED_BRICK)), "cracked_brick_stairs");
    public static final Block CHISELED_STAIRS = registerBlock(new StairBlock(LIMESTONE_BRICK_CHISELED::defaultBlockState, copy(LIMESTONE_BRICK_CHISELED)), "chiseled_stairs");
    public static final Block CARVED_STAIRS = registerBlock(new StairBlock(LIMESTONE_BRICK_CARVED::defaultBlockState, copy(LIMESTONE_BRICK_CARVED)), "carved_stairs");
    public static final WallBlock LIMESTONE_WALL = (WallBlock) registerBlock(new WallBlock(copy(LIMESTONE)), "limestone_wall");
    public static final WallBlock LIMESTONE_CRACKED_WALL = (WallBlock) registerBlock(new WallBlock(copy(LIMESTONE_CRACKED)), "limestone_cracked_wall");
    public static final WallBlock SMALL_WALL = (WallBlock) registerBlock(new WallBlock(copy(LIMESTONE_BRICK_SMALL)), "small_wall");
    public static final WallBlock LARGE_WALL = (WallBlock) registerBlock(new WallBlock(copy(LIMESTONE_BRICK_LARGE)), "large_wall");
    public static final WallBlock CRACKED_BRICK_WALL = (WallBlock) registerBlock(new WallBlock(copy(LIMESTONE_BRICK_CRACKED_BRICK)), "cracked_brick_wall");
    public static final WallBlock CHISELED_WALL = (WallBlock) registerBlock(new WallBlock(copy(LIMESTONE_BRICK_CHISELED)), "chiseled_wall");
    public static final WallBlock CARVED_WALL = (WallBlock) registerBlock(new WallBlock(copy(LIMESTONE_BRICK_CARVED)), "carved_wall");
    public static final Block LIMESTONE_DOOR = registerBlock(new DoorAtumBlock(copy(LIMESTONE)), "limestone_door");
    public static final Block LIMESTONE_CRACKED_DOOR = registerBlock(new DoorAtumBlock(copy(LIMESTONE_CRACKED)), "limestone_cracked_door");
    public static final Block LIMESTONE_BRICK_SMALL_DOOR = registerBlock(new DoorAtumBlock(copy(LIMESTONE_BRICK_SMALL)), "limestone_brick_small_door");
    public static final Block LIMESTONE_BRICK_LARGE_DOOR = registerBlock(new DoorAtumBlock(copy(LIMESTONE_BRICK_LARGE)), "limestone_brick_large_door");
    public static final Block LIMESTONE_CRACKED_BRICK_DOOR = registerBlock(new DoorAtumBlock(copy(LIMESTONE_BRICK_CRACKED_BRICK)), "limestone_brick_cracked_brick_door");
    public static final Block LIMESTONE_BRICK_CHISELED_DOOR = registerBlock(new DoorAtumBlock(copy(LIMESTONE_BRICK_CHISELED)), "limestone_brick_chiseled_door");
    public static final Block LIMESTONE_BRICK_CARVED_DOOR = registerBlock(new DoorAtumBlock(copy(LIMESTONE_BRICK_CARVED)), "limestone_brick_carved_door");
    public static final Block ALABASTER = registerBlock(new Block(of(Material.STONE, MaterialColor.QUARTZ).strength(2.0F, 8.0F)), "alabaster");
    public static final Block ALABASTER_BRICK_SMOOTH = registerBlock(new Block(copy(ALABASTER)), "alabaster_brick_smooth");
    public static final Block ALABASTER_BRICK_POLISHED = registerBlock(new Block(copy(ALABASTER)), "alabaster_brick_polished");
    public static final Block ALABASTER_BRICK_CARVED = registerBlock(new Block(copy(ALABASTER)), "alabaster_brick_carved");
    public static final Block ALABASTER_BRICK_TILED = registerBlock(new Block(copy(ALABASTER)), "alabaster_brick_tiled");
    public static final Block ALABASTER_BRICK_PILLAR = registerBlock(new Block(copy(ALABASTER)), "alabaster_brick_pillar");
    public static final Block ALABASTER_BRICK_SMOOTH_SLAB = registerBlock(new SlabBlock(copy(ALABASTER)), "alabaster_smooth_slab");
    public static final Block ALABASTER_BRICK_POLISHED_SLAB = registerBlock(new SlabBlock(copy(ALABASTER)), "alabaster_polished_slab");
    public static final Block ALABASTER_BRICK_CARVED_SLAB = registerBlock(new SlabBlock(copy(ALABASTER)), "alabaster_carved_slab");
    public static final Block ALABASTER_BRICK_TILED_SLAB = registerBlock(new SlabBlock(copy(ALABASTER)), "alabaster_tiled_slab");
    public static final Block ALABASTER_BRICK_PILLAR_SLAB = registerBlock(new SlabBlock(copy(ALABASTER)), "alabaster_pillar_slab");
    public static final Block ALABASTER_BRICK_SMOOTH_STAIRS = registerBlock(new StairBlock(ALABASTER_BRICK_SMOOTH::defaultBlockState, copy(ALABASTER)), "alabaster_smooth_stairs");
    public static final Block ALABASTER_BRICK_POLISHED_STAIRS = registerBlock(new StairBlock(ALABASTER_BRICK_POLISHED::defaultBlockState, copy(ALABASTER)), "alabaster_polished_stairs");
    public static final Block ALABASTER_BRICK_CARVED_STAIRS = registerBlock(new StairBlock(ALABASTER_BRICK_CARVED::defaultBlockState, copy(ALABASTER)), "alabaster_carved_stairs");
    public static final Block ALABASTER_BRICK_TILED_STAIRS = registerBlock(new StairBlock(ALABASTER_BRICK_TILED::defaultBlockState, copy(ALABASTER)), "alabaster_tiled_stairs");
    public static final Block ALABASTER_BRICK_PILLAR_STARS = registerBlock(new StairBlock(ALABASTER_BRICK_PILLAR::defaultBlockState, copy(ALABASTER)), "alabaster_pillar_stairs");
    public static final WallBlock ALABASTER_BRICK_SMOOTH_WALL = (WallBlock) registerBlock(new WallBlock(copy(ALABASTER)), "alabaster_smooth_wall");
    public static final WallBlock ALABASTER_BRICK_POLISHED_WALL = (WallBlock) registerBlock(new WallBlock(copy(ALABASTER)), "alabaster_polished_wall");
    public static final WallBlock ALABASTER_BRICK_CARVED_WALL = (WallBlock) registerBlock(new WallBlock(copy(ALABASTER)), "alabaster_carved_wall");
    public static final WallBlock ALABASTER_BRICK_TILED_WALL = (WallBlock) registerBlock(new WallBlock(copy(ALABASTER)), "alabaster_tiled_wall");
    public static final WallBlock ALABASTER_BRICK_PILLAR_WALL = (WallBlock) registerBlock(new WallBlock(copy(ALABASTER)), "alabaster_pillar_wall");
    public static final Block PORPHYRY = registerBlock(new Block(of(Material.STONE, MaterialColor.COLOR_BLACK).strength(1.5F, 5.0F).sound(SoundType.STONE)), "porphyry");
    public static final Block PORPHYRY_BRICK_SMOOTH = registerBlock(new Block(copy(PORPHYRY)), "porphyry_brick_smooth");
    public static final Block PORPHYRY_BRICK_POLISHED = registerBlock(new Block(copy(PORPHYRY)), "porphyry_brick_polished");
    public static final Block PORPHYRY_BRICK_CARVED = registerBlock(new Block(copy(PORPHYRY)), "porphyry_brick_carved");
    public static final Block PORPHYRY_BRICK_TILED = registerBlock(new Block(copy(PORPHYRY)), "porphyry_brick_tiled");
    public static final Block PORPHYRY_BRICK_PILLAR = registerBlock(new Block(copy(PORPHYRY)), "porphyry_brick_pillar");
    public static final Block PORPHYRY_BRICK_SMOOTH_SLAB = registerBlock(new SlabBlock(copy(PORPHYRY)), "porphyry_smooth_slab");
    public static final Block PORPHYRY_BRICK_POLISHED_SLAB = registerBlock(new SlabBlock(copy(PORPHYRY)), "porphyry_polished_slab");
    public static final Block PORPHYRY_BRICK_CARVED_SLAB = registerBlock(new SlabBlock(copy(PORPHYRY)), "porphyry_carved_slab");
    public static final Block PORPHYRY_BRICK_TILED_SLAB = registerBlock(new SlabBlock(copy(PORPHYRY)), "porphyry_tiled_slab");
    public static final Block PORPHYRY_BRICK_PILLAR_SLAB = registerBlock(new SlabBlock(copy(PORPHYRY)), "porphyry_pillar_slab");
    public static final Block PORPHYRY_BRICK_SMOOTH_STAIRS = registerBlock(new StairBlock(PORPHYRY_BRICK_SMOOTH::defaultBlockState, copy(PORPHYRY)), "porphyry_smooth_stairs");
    public static final Block PORPHYRY_BRICK_POLISHED_STAIRS = registerBlock(new StairBlock(PORPHYRY_BRICK_POLISHED::defaultBlockState, copy(PORPHYRY)), "porphyry_polished_stairs");
    public static final Block PORPHYRY_BRICK_CARVED_STAIRS = registerBlock(new StairBlock(PORPHYRY_BRICK_CARVED::defaultBlockState, copy(PORPHYRY)), "porphyry_carved_stairs");
    public static final Block PORPHYRY_BRICK_TILED_STAIRS = registerBlock(new StairBlock(PORPHYRY_BRICK_TILED::defaultBlockState, copy(PORPHYRY)), "porphyry_tiled_stairs");
    public static final Block PORPHYRY_BRICK_PILLAR_STARS = registerBlock(new StairBlock(PORPHYRY_BRICK_PILLAR::defaultBlockState, copy(PORPHYRY)), "porphyry_pillar_stairs");
    public static final WallBlock PORPHYRY_BRICK_SMOOTH_WALL = (WallBlock) registerBlock(new WallBlock(copy(PORPHYRY)), "porphyry_smooth_wall");
    public static final WallBlock PORPHYRY_BRICK_POLISHED_WALL = (WallBlock) registerBlock(new WallBlock(copy(PORPHYRY)), "porphyry_polished_wall");
    public static final WallBlock PORPHYRY_BRICK_CARVED_WALL = (WallBlock) registerBlock(new WallBlock(copy(PORPHYRY)), "porphyry_carved_wall");
    public static final WallBlock PORPHYRY_BRICK_TILED_WALL = (WallBlock) registerBlock(new WallBlock(copy(PORPHYRY)), "porphyry_tiled_wall");
    public static final WallBlock PORPHYRY_BRICK_PILLAR_WALL = (WallBlock) registerBlock(new WallBlock(copy(PORPHYRY)), "porphyry_pillar_wall");
    public static final Block CERAMIC_WHITE = registerBlock(new CeramicBlock(DyeColor.WHITE), "ceramic_white");
    public static final Block CERAMIC_ORANGE = registerBlock(new CeramicBlock(DyeColor.ORANGE), "ceramic_orange");
    public static final Block CERAMIC_MAGENTA = registerBlock(new CeramicBlock(DyeColor.MAGENTA), "ceramic_magenta");
    public static final Block CERAMIC_LIGHT_BLUE = registerBlock(new CeramicBlock(DyeColor.LIGHT_BLUE), "ceramic_light_blue");
    public static final Block CERAMIC_YELLOW = registerBlock(new CeramicBlock(DyeColor.YELLOW), "ceramic_yellow");
    public static final Block CERAMIC_LIME = registerBlock(new CeramicBlock(DyeColor.LIME), "ceramic_lime");
    public static final Block CERAMIC_PINK = registerBlock(new CeramicBlock(DyeColor.PINK), "ceramic_pink");
    public static final Block CERAMIC_GRAY = registerBlock(new CeramicBlock(DyeColor.GRAY), "ceramic_gray");
    public static final Block CERAMIC_LIGHT_GRAY = registerBlock(new CeramicBlock(DyeColor.LIGHT_GRAY), "ceramic_light_gray");
    public static final Block CERAMIC_CYAN = registerBlock(new CeramicBlock(DyeColor.CYAN), "ceramic_cyan");
    public static final Block CERAMIC_PURPLE = registerBlock(new CeramicBlock(DyeColor.PURPLE), "ceramic_purple");
    public static final Block CERAMIC_BLUE = registerBlock(new CeramicBlock(DyeColor.BLUE), "ceramic_blue");
    public static final Block CERAMIC_BROWN = registerBlock(new CeramicBlock(DyeColor.BROWN), "ceramic_brown");
    public static final Block CERAMIC_GREEN = registerBlock(new CeramicBlock(DyeColor.GREEN), "ceramic_green");
    public static final Block CERAMIC_RED = registerBlock(new CeramicBlock(DyeColor.RED), "ceramic_red");
    public static final Block CERAMIC_BLACK = registerBlock(new CeramicBlock(DyeColor.BLACK), "ceramic_black");
    public static final Block CERAMIC_WHITE_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_WHITE)), "ceramic_slab_white");
    public static final Block CERAMIC_ORANGE_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_ORANGE)), "ceramic_slab_orange");
    public static final Block CERAMIC_MAGENTA_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_MAGENTA)), "ceramic_slab_magenta");
    public static final Block CERAMIC_LIGHT_BLUE_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_LIGHT_BLUE)), "ceramic_slab_light_blue");
    public static final Block CERAMIC_YELLOW_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_YELLOW)), "ceramic_slab_yellow");
    public static final Block CERAMIC_LIME_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_LIME)), "ceramic_slab_lime");
    public static final Block CERAMIC_PINK_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_PINK)), "ceramic_slab_pink");
    public static final Block CERAMIC_GRAY_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_GRAY)), "ceramic_slab_gray");
    public static final Block CERAMIC_LIGHT_GRAY_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_LIGHT_GRAY)), "ceramic_slab_light_gray");
    public static final Block CERAMIC_CYAN_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_CYAN)), "ceramic_slab_cyan");
    public static final Block CERAMIC_PURPLE_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_PURPLE)), "ceramic_slab_purple");
    public static final Block CERAMIC_BLUE_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_BLUE)), "ceramic_slab_blue");
    public static final Block CERAMIC_BROWN_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_BROWN)), "ceramic_slab_brown");
    public static final Block CERAMIC_GREEN_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_GREEN)), "ceramic_slab_green");
    public static final Block CERAMIC_RED_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_RED)), "ceramic_slab_red");
    public static final Block CERAMIC_BLACK_SLAB = registerBlock(new SlabBlock(copy(CERAMIC_BLACK)), "ceramic_slab_black");
    public static final Block CERAMIC_WHITE_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_WHITE)), "ceramic_tile_white");
    public static final Block CERAMIC_ORANGE_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_ORANGE)), "ceramic_tile_orange");
    public static final Block CERAMIC_MAGENTA_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_MAGENTA)), "ceramic_tile_magenta");
    public static final Block CERAMIC_LIGHT_BLUE_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_LIGHT_BLUE)), "ceramic_tile_light_blue");
    public static final Block CERAMIC_YELLOW_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_YELLOW)), "ceramic_tile_yellow");
    public static final Block CERAMIC_LIME_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_LIME)), "ceramic_tile_lime");
    public static final Block CERAMIC_PINK_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_PINK)), "ceramic_tile_pink");
    public static final Block CERAMIC_GRAY_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_GRAY)), "ceramic_tile_gray");
    public static final Block CERAMIC_LIGHT_GRAY_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_LIGHT_GRAY)), "ceramic_tile_light_gray");
    public static final Block CERAMIC_CYAN_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_CYAN)), "ceramic_tile_cyan");
    public static final Block CERAMIC_PURPLE_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_PURPLE)), "ceramic_tile_purple");
    public static final Block CERAMIC_BLUE_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_BLUE)), "ceramic_tile_blue");
    public static final Block CERAMIC_BROWN_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_BROWN)), "ceramic_tile_brown");
    public static final Block CERAMIC_GREEN_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_GREEN)), "ceramic_tile_green");
    public static final Block CERAMIC_RED_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_RED)), "ceramic_tile_red");
    public static final Block CERAMIC_BLACK_TILE = registerBlock(new CeramicTileBlock(copy(CERAMIC_BLACK)), "ceramic_tile_black");
    public static final Block CERAMIC_WHITE_STAIRS = registerBlock(new StairBlock(CERAMIC_WHITE::defaultBlockState, copy(CERAMIC_WHITE)), "ceramic_stairs_white");
    public static final Block CERAMIC_ORANGE_STAIRS = registerBlock(new StairBlock(CERAMIC_ORANGE::defaultBlockState, copy(CERAMIC_ORANGE)), "ceramic_stairs_orange");
    public static final Block CERAMIC_MAGENTA_STAIRS = registerBlock(new StairBlock(CERAMIC_MAGENTA::defaultBlockState, copy(CERAMIC_MAGENTA)), "ceramic_stairs_magenta");
    public static final Block CERAMIC_LIGHT_BLUE_STAIRS = registerBlock(new StairBlock(CERAMIC_LIGHT_BLUE::defaultBlockState, copy(CERAMIC_LIGHT_BLUE)), "ceramic_stairs_light_blue");
    public static final Block CERAMIC_YELLOW_STAIRS = registerBlock(new StairBlock(CERAMIC_YELLOW::defaultBlockState, copy(CERAMIC_YELLOW)), "ceramic_stairs_yellow");
    public static final Block CERAMIC_LIME_STAIRS = registerBlock(new StairBlock(CERAMIC_LIME::defaultBlockState, copy(CERAMIC_LIME)), "ceramic_stairs_lime");
    public static final Block CERAMIC_PINK_STAIRS = registerBlock(new StairBlock(CERAMIC_PINK::defaultBlockState, copy(CERAMIC_PINK)), "ceramic_stairs_pink");
    public static final Block CERAMIC_GRAY_STAIRS = registerBlock(new StairBlock(CERAMIC_GRAY::defaultBlockState, copy(CERAMIC_GRAY)), "ceramic_stairs_gray");
    public static final Block CERAMIC_LIGHT_GRAY_STAIRS = registerBlock(new StairBlock(CERAMIC_LIGHT_GRAY::defaultBlockState, copy(CERAMIC_LIGHT_GRAY)), "ceramic_stairs_light_gray");
    public static final Block CERAMIC_CYAN_STAIRS = registerBlock(new StairBlock(CERAMIC_CYAN::defaultBlockState, copy(CERAMIC_CYAN)), "ceramic_stairs_cyan");
    public static final Block CERAMIC_PURPLE_STAIRS = registerBlock(new StairBlock(CERAMIC_PURPLE::defaultBlockState, copy(CERAMIC_PURPLE)), "ceramic_stairs_purple");
    public static final Block CERAMIC_BLUE_STAIRS = registerBlock(new StairBlock(CERAMIC_BLUE::defaultBlockState, copy(CERAMIC_BLUE)), "ceramic_stairs_blue");
    public static final Block CERAMIC_BROWN_STAIRS = registerBlock(new StairBlock(CERAMIC_BROWN::defaultBlockState, copy(CERAMIC_BROWN)), "ceramic_stairs_brown");
    public static final Block CERAMIC_GREEN_STAIRS = registerBlock(new StairBlock(CERAMIC_GREEN::defaultBlockState, copy(CERAMIC_GREEN)), "ceramic_stairs_green");
    public static final Block CERAMIC_RED_STAIRS = registerBlock(new StairBlock(CERAMIC_RED::defaultBlockState, copy(CERAMIC_RED)), "ceramic_stairs_red");
    public static final Block CERAMIC_BLACK_STAIRS = registerBlock(new StairBlock(CERAMIC_BLACK::defaultBlockState, copy(CERAMIC_BLACK)), "ceramic_stairs_black");
    public static final WallBlock CERAMIC_WHITE_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_WHITE)), "ceramic_wall_white");
    public static final WallBlock CERAMIC_ORANGE_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_ORANGE)), "ceramic_wall_orange");
    public static final WallBlock CERAMIC_MAGENTA_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_MAGENTA)), "ceramic_wall_magenta");
    public static final WallBlock CERAMIC_LIGHT_BLUE_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_LIGHT_BLUE)), "ceramic_wall_light_blue");
    public static final WallBlock CERAMIC_YELLOW_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_YELLOW)), "ceramic_wall_yellow");
    public static final WallBlock CERAMIC_LIME_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_LIME)), "ceramic_wall_lime");
    public static final WallBlock CERAMIC_PINK_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_PINK)), "ceramic_wall_pink");
    public static final WallBlock CERAMIC_GRAY_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_GRAY)), "ceramic_wall_gray");
    public static final WallBlock CERAMIC_LIGHT_GRAY_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_LIGHT_GRAY)), "ceramic_wall_light_gray");
    public static final WallBlock CERAMIC_CYAN_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_CYAN)), "ceramic_wall_cyan");
    public static final WallBlock CERAMIC_PURPLE_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_PURPLE)), "ceramic_wall_purple");
    public static final WallBlock CERAMIC_BLUE_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_BLUE)), "ceramic_wall_blue");
    public static final WallBlock CERAMIC_BROWN_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_BROWN)), "ceramic_wall_brown");
    public static final WallBlock CERAMIC_GREEN_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_GREEN)), "ceramic_wall_green");
    public static final WallBlock CERAMIC_RED_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_RED)), "ceramic_wall_red");
    public static final WallBlock CERAMIC_BLACK_WALL = (WallBlock) registerBlock(new WallBlock(copy(CERAMIC_BLACK)), "ceramic_wall_black");
    //public static final Block RADIANT_BEACON = registerBlock(new RadiantBeaconBlock(), "radiant_beacon");
    //public static final Block RADIANT_BEACON_FRAMED = registerBlock(new FramedRadiantBeaconBlock(), null, "radiant_beacon_framed");
    public static final Block CRYSTAL_GLASS = registerBlock(new GlassBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(AtumBlocks::neverAllowSpawn).isRedstoneConductor(AtumBlocks::isntSolid).isSuffocating(AtumBlocks::isntSolid).isViewBlocking(AtumBlocks::isntSolid)), "crystal_glass");
    public static final Block WHITE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.WHITE), "white_stained_crystal_glass");
    public static final Block ORANGE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.ORANGE), "orange_stained_crystal_glass");
    public static final Block MAGENTA_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.MAGENTA), "magenta_stained_crystal_glass");
    public static final Block LIGHT_BLUE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_BLUE), "light_blue_stained_crystal_glass");
    public static final Block YELLOW_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.YELLOW), "yellow_stained_crystal_glass");
    public static final Block LIME_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIME), "lime_stained_crystal_glass");
    public static final Block PINK_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PINK), "pink_stained_crystal_glass");
    public static final Block GRAY_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GRAY), "gray_stained_crystal_glass");
    public static final Block LIGHT_GRAY_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_GRAY), "light_gray_stained_crystal_glass");
    public static final Block CYAN_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.CYAN), "cyan_stained_crystal_glass");
    public static final Block PURPLE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PURPLE), "purple_stained_crystal_glass");
    public static final Block BLUE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLUE), "blue_stained_crystal_glass");
    public static final Block BROWN_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BROWN), "brown_stained_crystal_glass");
    public static final Block GREEN_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GREEN), "green_stained_crystal_glass");
    public static final Block RED_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.RED), "red_stained_crystal_glass");
    public static final Block BLACK_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLACK), "black_stained_crystal_glass");
    public static final Block CRYSTAL_GLASS_PANE = registerBlock(new AtumPaneBlock(), "crystal_glass_pane");
    public static final Block WHITE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.WHITE, copy(CRYSTAL_GLASS_PANE)), "white_stained_crystal_glass_pane");
    public static final Block ORANGE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.ORANGE, copy(CRYSTAL_GLASS_PANE)), "orange_stained_crystal_glass_pane");
    public static final Block MAGENTA_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.MAGENTA, copy(CRYSTAL_GLASS_PANE)), "magenta_stained_crystal_glass_pane");
    public static final Block LIGHT_BLUE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.LIGHT_BLUE, copy(CRYSTAL_GLASS_PANE)), "light_blue_stained_crystal_glass_pane");
    public static final Block YELLOW_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.YELLOW, copy(CRYSTAL_GLASS_PANE)), "yellow_stained_crystal_glass_pane");
    public static final Block LIME_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.LIME, copy(CRYSTAL_GLASS_PANE)), "lime_stained_crystal_glass_pane");
    public static final Block PINK_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.PINK, copy(CRYSTAL_GLASS_PANE)), "pink_stained_crystal_glass_pane");
    public static final Block GRAY_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.GRAY, copy(CRYSTAL_GLASS_PANE)), "gray_stained_crystal_glass_pane");
    public static final Block LIGHT_GRAY_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.LIGHT_GRAY, copy(CRYSTAL_GLASS_PANE)), "light_gray_stained_crystal_glass_pane");
    public static final Block CYAN_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.CYAN, copy(CRYSTAL_GLASS_PANE)), "cyan_stained_crystal_glass_pane");
    public static final Block PURPLE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.PURPLE, copy(CRYSTAL_GLASS_PANE)), "purple_stained_crystal_glass_pane");
    public static final Block BLUE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.BLUE, copy(CRYSTAL_GLASS_PANE)), "blue_stained_crystal_glass_pane");
    public static final Block BROWN_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.BROWN, copy(CRYSTAL_GLASS_PANE)), "brown_stained_crystal_glass_pane");
    public static final Block GREEN_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.GREEN, copy(CRYSTAL_GLASS_PANE)), "green_stained_crystal_glass_pane");
    public static final Block RED_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.RED, copy(CRYSTAL_GLASS_PANE)), "red_stained_crystal_glass_pane");
    public static final Block BLACK_STAINED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.BLACK, copy(CRYSTAL_GLASS_PANE)), "black_stained_crystal_glass_pane");
    public static final Block PALM_FRAMED_CRYSTAL_GLASS = registerBlock(new GlassBlock(of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()), "palm_framed_crystal_glass");
    public static final Block WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.WHITE), "white_stained_palm_framed_crystal_glass");
    public static final Block ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.ORANGE), "orange_stained_palm_framed_crystal_glass");
    public static final Block MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.MAGENTA), "magenta_stained_palm_framed_crystal_glass");
    public static final Block LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_BLUE), "light_blue_stained_palm_framed_crystal_glass");
    public static final Block YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.YELLOW), "yellow_stained_palm_framed_crystal_glass");
    public static final Block LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIME), "lime_stained_palm_framed_crystal_glass");
    public static final Block PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PINK), "pink_stained_palm_framed_crystal_glass");
    public static final Block GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GRAY), "gray_stained_palm_framed_crystal_glass");
    public static final Block LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_GRAY), "light_gray_stained_palm_framed_crystal_glass");
    public static final Block CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.CYAN), "cyan_stained_palm_framed_crystal_glass");
    public static final Block PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PURPLE), "purple_stained_palm_framed_crystal_glass");
    public static final Block BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLUE), "blue_stained_palm_framed_crystal_glass");
    public static final Block BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BROWN), "brown_stained_palm_framed_crystal_glass");
    public static final Block GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GREEN), "green_stained_palm_framed_crystal_glass");
    public static final Block RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.RED), "red_stained_palm_framed_crystal_glass");
    public static final Block BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLACK), "black_stained_palm_framed_crystal_glass");
    public static final Block DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(new GlassBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(AtumBlocks::neverAllowSpawn).isRedstoneConductor(AtumBlocks::isntSolid).isSuffocating(AtumBlocks::isntSolid).isViewBlocking(AtumBlocks::isntSolid)), "deadwood_framed_crystal_glass");
    public static final Block WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.WHITE), "white_stained_deadwood_framed_crystal_glass");
    public static final Block ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.ORANGE), "orange_stained_deadwood_framed_crystal_glass");
    public static final Block MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.MAGENTA), "magenta_stained_deadwood_framed_crystal_glass");
    public static final Block LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_BLUE), "light_blue_stained_deadwood_framed_crystal_glass");
    public static final Block YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.YELLOW), "yellow_stained_deadwood_framed_crystal_glass");
    public static final Block LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIME), "lime_stained_deadwood_framed_crystal_glass");
    public static final Block PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PINK), "pink_stained_deadwood_framed_crystal_glass");
    public static final Block GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GRAY), "gray_stained_deadwood_framed_crystal_glass");
    public static final Block LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_GRAY), "light_gray_stained_deadwood_framed_crystal_glass");
    public static final Block CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.CYAN), "cyan_stained_deadwood_framed_crystal_glass");
    public static final Block PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PURPLE), "purple_stained_deadwood_framed_crystal_glass");
    public static final Block BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLUE), "blue_stained_deadwood_framed_crystal_glass");
    public static final Block BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BROWN), "brown_stained_deadwood_framed_crystal_glass");
    public static final Block GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GREEN), "green_stained_deadwood_framed_crystal_glass");
    public static final Block RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.RED), "red_stained_deadwood_framed_crystal_glass");
    public static final Block BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLACK), "black_stained_deadwood_framed_crystal_glass");
    public static final Block PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new AtumPaneBlock(), "palm_framed_crystal_glass_pane");
    public static final Block WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.WHITE, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "white_stained_palm_framed_crystal_glass_pane");
    public static final Block ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.ORANGE, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "orange_stained_palm_framed_crystal_glass_pane");
    public static final Block MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.MAGENTA, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "magenta_stained_palm_framed_crystal_glass_pane");
    public static final Block LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.LIGHT_BLUE, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "light_blue_stained_palm_framed_crystal_glass_pane");
    public static final Block YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.YELLOW, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "yellow_stained_palm_framed_crystal_glass_pane");
    public static final Block LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.LIME, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "lime_stained_palm_framed_crystal_glass_pane");
    public static final Block PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.PINK, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "pink_stained_palm_framed_crystal_glass_pane");
    public static final Block GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.GRAY, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "gray_stained_palm_framed_crystal_glass_pane");
    public static final Block LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.LIGHT_GRAY, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "light_gray_stained_palm_framed_crystal_glass_pane");
    public static final Block CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.CYAN, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "cyan_stained_palm_framed_crystal_glass_pane");
    public static final Block PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.PURPLE, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "purple_stained_palm_framed_crystal_glass_pane");
    public static final Block BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.BLUE, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "blue_stained_palm_framed_crystal_glass_pane");
    public static final Block BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.BROWN, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "brown_stained_palm_framed_crystal_glass_pane");
    public static final Block GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.GREEN, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "green_stained_palm_framed_crystal_glass_pane");
    public static final Block RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.RED, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "red_stained_palm_framed_crystal_glass_pane");
    public static final Block BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.BLACK, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE)), "black_stained_palm_framed_crystal_glass_pane");
    public static final Block DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new AtumPaneBlock(), "deadwood_framed_crystal_glass_pane");
    public static final Block WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.WHITE, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "white_stained_deadwood_framed_crystal_glass_pane");
    public static final Block ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.ORANGE, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "orange_stained_deadwood_framed_crystal_glass_pane");
    public static final Block MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.MAGENTA, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "magenta_stained_deadwood_framed_crystal_glass_pane");
    public static final Block LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.LIGHT_BLUE, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "light_blue_stained_deadwood_framed_crystal_glass_pane");
    public static final Block YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.YELLOW, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "yellow_stained_deadwood_framed_crystal_glass_pane");
    public static final Block LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.LIME, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "lime_stained_deadwood_framed_crystal_glass_pane");
    public static final Block PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.PINK, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "pink_stained_deadwood_framed_crystal_glass_pane");
    public static final Block GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.GRAY, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "gray_stained_deadwood_framed_crystal_glass_pane");
    public static final Block LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.LIGHT_GRAY, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "light_gray_stained_deadwood_framed_crystal_glass_pane");
    public static final Block CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.CYAN, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "cyan_stained_deadwood_framed_crystal_glass_pane");
    public static final Block PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.PURPLE, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "purple_stained_deadwood_framed_crystal_glass_pane");
    public static final Block BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.BLUE, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "blue_stained_deadwood_framed_crystal_glass_pane");
    public static final Block BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.BROWN, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "brown_stained_deadwood_framed_crystal_glass_pane");
    public static final Block GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.GREEN, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "green_stained_deadwood_framed_crystal_glass_pane");
    public static final Block RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.RED, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "red_stained_deadwood_framed_crystal_glass_pane");
    public static final Block BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(new StainedGlassPaneBlock(DyeColor.BLACK, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE)), "black_stained_deadwood_framed_crystal_glass_pane");
    public static final Block LINEN_WHITE = registerBlock(new LinenBlock(DyeColor.WHITE), "linen_white");
    public static final Block LINEN_ORANGE = registerBlock(new LinenBlock(DyeColor.ORANGE), "linen_orange");
    public static final Block LINEN_MAGENTA = registerBlock(new LinenBlock(DyeColor.MAGENTA), "linen_magenta");
    public static final Block LINEN_LIGHT_BLUE = registerBlock(new LinenBlock(DyeColor.LIGHT_BLUE), "linen_light_blue");
    public static final Block LINEN_YELLOW = registerBlock(new LinenBlock(DyeColor.YELLOW), "linen_yellow");
    public static final Block LINEN_LIME = registerBlock(new LinenBlock(DyeColor.LIME), "linen_lime");
    public static final Block LINEN_PINK = registerBlock(new LinenBlock(DyeColor.PINK), "linen_pink");
    public static final Block LINEN_GRAY = registerBlock(new LinenBlock(DyeColor.GRAY), "linen_gray");
    public static final Block LINEN_LIGHT_GRAY = registerBlock(new LinenBlock(DyeColor.LIGHT_GRAY), "linen_light_gray");
    public static final Block LINEN_CYAN = registerBlock(new LinenBlock(DyeColor.CYAN), "linen_cyan");
    public static final Block LINEN_PURPLE = registerBlock(new LinenBlock(DyeColor.PURPLE), "linen_purple");
    public static final Block LINEN_BLUE = registerBlock(new LinenBlock(DyeColor.BLUE), "linen_blue");
    public static final Block LINEN_BROWN = registerBlock(new LinenBlock(DyeColor.BROWN), "linen_brown");
    public static final Block LINEN_GREEN = registerBlock(new LinenBlock(DyeColor.GREEN), "linen_green");
    public static final Block LINEN_RED = registerBlock(new LinenBlock(DyeColor.RED), "linen_red");
    public static final Block LINEN_BLACK = registerBlock(new LinenBlock(DyeColor.BLACK), "linen_black");
    public static final Block LINEN_CARPET_WHITE = registerBlock(new LinenCarpetBlock(DyeColor.WHITE), "linen_carpet_white");
    public static final Block LINEN_CARPET_ORANGE = registerBlock(new LinenCarpetBlock(DyeColor.ORANGE), "linen_carpet_orange");
    public static final Block LINEN_CARPET_MAGENTA = registerBlock(new LinenCarpetBlock(DyeColor.MAGENTA), "linen_carpet_magenta");
    public static final Block LINEN_CARPET_LIGHT_BLUE = registerBlock(new LinenCarpetBlock(DyeColor.LIGHT_BLUE), "linen_carpet_light_blue");
    public static final Block LINEN_CARPET_YELLOW = registerBlock(new LinenCarpetBlock(DyeColor.YELLOW), "linen_carpet_yellow");
    public static final Block LINEN_CARPET_LIME = registerBlock(new LinenCarpetBlock(DyeColor.LIME), "linen_carpet_lime");
    public static final Block LINEN_CARPET_PINK = registerBlock(new LinenCarpetBlock(DyeColor.PINK), "linen_carpet_pink");
    public static final Block LINEN_CARPET_GRAY = registerBlock(new LinenCarpetBlock(DyeColor.GRAY), "linen_carpet_gray");
    public static final Block LINEN_CARPET_LIGHT_GRAY = registerBlock(new LinenCarpetBlock(DyeColor.LIGHT_GRAY), "linen_carpet_light_gray");
    public static final Block LINEN_CARPET_CYAN = registerBlock(new LinenCarpetBlock(DyeColor.CYAN), "linen_carpet_cyan");
    public static final Block LINEN_CARPET_PURPLE = registerBlock(new LinenCarpetBlock(DyeColor.PURPLE), "linen_carpet_purple");
    public static final Block LINEN_CARPET_BLUE = registerBlock(new LinenCarpetBlock(DyeColor.BLUE), "linen_carpet_blue");
    public static final Block LINEN_CARPET_BROWN = registerBlock(new LinenCarpetBlock(DyeColor.BROWN), "linen_carpet_brown");
    public static final Block LINEN_CARPET_GREEN = registerBlock(new LinenCarpetBlock(DyeColor.GREEN), "linen_carpet_green");
    public static final Block LINEN_CARPET_RED = registerBlock(new LinenCarpetBlock(DyeColor.RED), "linen_carpet_red");
    public static final Block LINEN_CARPET_BLACK = registerBlock(new LinenCarpetBlock(DyeColor.BLACK), "linen_carpet_black");
    public static final Block PALM_PLANKS = registerBlock(new Block(of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)), "palm_planks");
    public static final Block DEADWOOD_PLANKS = registerBlock(new Block(of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F, 3.0F).sound(SoundType.WOOD)), "deadwood_planks");
    public static final Block PALM_LOG = registerBlock(new PalmLog(), "palm_log");
    public static final Block STRIPPED_PALM_LOG = registerBlock(new RotatedPillarBlock(copy(PALM_LOG)), "stripped_palm_log");
    public static final Block PALM_WOOD = registerBlock(new RotatedPillarBlock(copy(PALM_LOG)), "palm_wood");
    public static final Block STRIPPED_PALM_WOOD = registerBlock(new RotatedPillarBlock(copy(PALM_LOG)), "stripped_palm_wood");
    public static final Block DEADWOOD_LOG = registerBlock(new DeadwoodLogBlock().setCanBeStripped(), "deadwood_log");
    public static final Block STRIPPED_DEADWOOD_LOG = registerBlock(new DeadwoodLogBlock(), "stripped_deadwood_log");
    public static final Block DEADWOOD_WOOD = registerBlock(new DeadwoodLogBlock(), "deadwood_wood");
    public static final Block STRIPPED_DEADWOOD_WOOD = registerBlock(new DeadwoodLogBlock(), "stripped_deadwood_wood");
    public static final Block DEADWOOD_BRANCH = registerBlock(new DeadwoodBranchBlock(), null, "deadwood_branch");
    public static final Block PALM_STAIRS = registerBlock(new StairBlock(PALM_PLANKS::defaultBlockState, copy(PALM_PLANKS)), "palm_stairs");
    public static final Block DEADWOOD_STAIRS = registerBlock(new StairBlock(DEADWOOD_PLANKS::defaultBlockState, copy(DEADWOOD_PLANKS)), "deadwood_stairs");
    public static final Block PALM_SLAB = registerBlock(new SlabBlock(copy(PALM_PLANKS)), "palm_slab");
    public static final Block DEADWOOD_SLAB = registerBlock(new SlabBlock(copy(DEADWOOD_PLANKS)), "deadwood_slab");
    public static final Block PALM_SAPLING = registerBlock(new PalmSaplingBlock(), "palm_sapling");
    public static final Block POTTED_PALM_SAPLING = registerBaseBlock(new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT.delegate.get(), () -> PALM_SAPLING, BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()), "potted_palm_sapling");
    public static final Block PALM_LEAVES = registerBlock(new PalmLeavesBlock(), "palm_leaves");
    public static final Block DRY_LEAVES = registerBlock(new LeavesAtumBlock(), null, "dry_leaves");
    public static final Block PALM_CRATE = registerBlock(new CrateBlock(copy(PALM_PLANKS)), "palm_crate");
    public static final Block DEADWOOD_CRATE = registerBlock(new CrateBlock(copy(PALM_PLANKS)), "deadwood_crate");
    public static final Block PALM_LADDER = registerBlock(new AtumLadderBlock(), "palm_ladder");
    public static final Block DEADWOOD_LADDER = registerBlock(new AtumLadderBlock(), "deadwood_ladder");
    public static final Block PALM_FENCE = registerBlock(new FenceBlock(copy(PALM_PLANKS)), "palm_fence");
    public static final Block DEADWOOD_FENCE = registerBlock(new FenceBlock(copy(DEADWOOD_PLANKS)), "deadwood_fence");
    public static final Block PALM_FENCE_GATE = registerBlock(new FenceGateBlock(copy(PALM_PLANKS)), "palm_fence_gate");
    public static final Block DEADWOOD_FENCE_GATE = registerBlock(new FenceGateBlock(copy(DEADWOOD_PLANKS)), "deadwood_fence_gate");
    public static final Block PALM_HATCH = registerBlock(new AtumTrapDoorBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(AtumBlocks::neverAllowSpawn)), "palm_hatch");
    public static final Block DEADWOOD_HATCH = registerBlock(new AtumTrapDoorBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(AtumBlocks::neverAllowSpawn)), "deadwood_hatch");
    public static final Block PALM_DOOR = registerBlock(new DoorAtumBlock(copy(PALM_PLANKS)), "palm_door");
    public static final Block DEADWOOD_DOOR = registerBlock(new DoorAtumBlock(copy(DEADWOOD_PLANKS)), "deadwood_door");
    public static final Block LIMESTONE_BUTTON = registerBlock(new StoneButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F)), "limestone_button");
    public static final Block PALM_BUTTON = registerBlock(new WoodButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD)), "palm_button");
    public static final Block DEADWOOD_BUTTON = registerBlock(new WoodButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD)), "deadwood_button");
    public static final Block LIMESTONE_PRESSURE_PLATE = registerBlock(new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().noCollission().strength(0.5F)), "limestone_pressure_plate");
    public static final Block PALM_PRESSURE_PLATE = registerBlock(new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of(Material.WOOD, PALM_PLANKS.defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD)), "palm_pressure_plate");
    public static final Block DEADWOOD_PRESSURE_PLATE = registerBlock(new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of(Material.WOOD, DEADWOOD_PLANKS.defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD)), "deadwood_pressure_plate");
    public static final Block PALM_SIGN = registerSign(new AtumStandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD), Atum.PALM), Atum.PALM);
    public static final Block DEADWOOD_SIGN = registerSign(new AtumStandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD), Atum.DEADWOOD), Atum.DEADWOOD);
    public static final Block PALM_SCAFFOLDING = registerScaffolding(new AtumScaffoldingBlock(BlockBehaviour.Properties.of(Material.DECORATION, MaterialColor.SAND).noCollission().sound(SoundType.SCAFFOLDING).dynamicShape()), "palm_scaffolding");
    public static final Block DEADWOOD_SCAFFOLDING = registerScaffolding(new AtumScaffoldingBlock(BlockBehaviour.Properties.of(Material.DECORATION, MaterialColor.SAND).noCollission().sound(SoundType.SCAFFOLDING).dynamicShape()), "deadwood_scaffolding");

    public static void setBlockInfo() {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(PALM_SAPLING.getRegistryName(), () -> POTTED_PALM_SAPLING);
        //Fire Info
        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFlammable(PALM_PLANKS, 5, 20);
        fire.setFlammable(PALM_FENCE, 5, 20);
        fire.setFlammable(PALM_FENCE_GATE, 5, 20);
        fire.setFlammable(DEADWOOD_FENCE, 5, 20);
        fire.setFlammable(DEADWOOD_FENCE_GATE, 5, 20);
        fire.setFlammable(SHRUB, 60, 100);
        fire.setFlammable(WEED, 60, 100);
        fire.setFlammable(SPINNING_WHEEL, 2, 1);
        fire.setFlammable(OPHIDIAN_TONGUE, 15, 100);
        fire.setFlammable(PALM_PLANKS, 5, 20);
        fire.setFlammable(DEADWOOD_PLANKS, 5, 20);
        fire.setFlammable(PALM_LOG, 5, 5);
        fire.setFlammable(DEADWOOD_LOG, 5, 5);
        fire.setFlammable(PALM_SLAB, 5, 20);
        fire.setFlammable(DEADWOOD_SLAB, 5, 20);
        fire.setFlammable(PALM_LEAVES, 30, 60);
        fire.setFlammable(DRY_LEAVES, 30, 60);
        fire.setFlammable(DRY_GRASS, 60, 10);
        fire.setFlammable(TALL_DRY_GRASS, 60, 10);
        fire.setFlammable(PALM_STAIRS, 5, 20);
        fire.setFlammable(DEADWOOD_STAIRS, 5, 20);
        fire.setFlammable(EMMER_BLOCK, 60, 20);
        fire.setFlammable(PALM_SCAFFOLDING, 60, 60);
        fire.setFlammable(DEADWOOD_SCAFFOLDING, 60, 60);
    }

    public static StainedGlassBlock createStainedGlassFromColor(DyeColor color) {
        return new StainedGlassBlock(color, BlockBehaviour.Properties.of(Material.GLASS, color).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(AtumBlocks::neverAllowSpawn).isRedstoneConductor(AtumBlocks::isntSolid).isSuffocating(AtumBlocks::isntSolid).isViewBlocking(AtumBlocks::isntSolid));
    }

    //Copied from vanilla Block class
    public static Boolean neverAllowSpawn(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
        return false;
    }

    public static Boolean alwaysAllowSpawn(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
        return true;
    }

    public static Boolean allowsSpawnOnLeaves(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
        return entity == EntityType.OCELOT || entity == EntityType.PARROT;
    }

    public static boolean needsPostProcessing(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    public static boolean isntSolid(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }
}