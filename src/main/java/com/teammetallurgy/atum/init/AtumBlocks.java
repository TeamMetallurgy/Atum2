package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.*;
import com.teammetallurgy.atum.blocks.base.AtumPaneBlock;
import com.teammetallurgy.atum.blocks.base.DoorAtumBlock;
import com.teammetallurgy.atum.blocks.curio.CurioDisplayBlock;
import com.teammetallurgy.atum.blocks.curio.tileentity.*;
import com.teammetallurgy.atum.blocks.lighting.*;
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
import com.teammetallurgy.atum.items.AtumScaffoldingItem;
import com.teammetallurgy.atum.items.BlockItemWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;

public class AtumBlocks {
    public static final DeferredRegister<Block> BLOCK_DEFERRED = DeferredRegister.create(ForgeRegistries.BLOCKS, Atum.MOD_ID);
    public static final RegistryObject<Block> PORTAL = registerBlock(PortalBlock::new, null, "portal");
    public static final RegistryObject<Block> SAND = registerBlock(StrangeSandBlock::new, "sand");
    public static final RegistryObject<Block> SAND_LAYERED = registerBlock(SandLayersBlock::new, "sand_layer");
    public static final RegistryObject<Block> LIMESTONE_GRAVEL = registerBlock(LimestoneGravelBlock::new, "limestone_gravel");
    public static final RegistryObject<Block> DATE_BLOCK = registerBlock(DateBlock::new, null, "date_block");
    public static final RegistryObject<Block> EMMER_WHEAT = registerBlock(EmmerBlock::new, null, "emmer_wheat");
    public static final RegistryObject<Block> EMMER_BLOCK = registerBlock(() -> new HayBlock(BlockBehaviour.Properties.of(Material.GRASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.GRASS)), "emmer_block");
    public static final RegistryObject<Block> ANPUTS_FINGERS = registerBlock(AnputsFingersBlock::new, null, "anputs_fingers");
    public static final RegistryObject<Block> OASIS_GRASS = registerBlock(OasisGrassBlock::new, "oasis_grass");
    public static final RegistryObject<Block> DRY_GRASS = registerBlock(DryGrassBlock::new, "dry_grass");
    public static final RegistryObject<Block> TALL_DRY_GRASS = registerBlock(TallDryGrass::new, "tall_dry_grass");
    public static final RegistryObject<Block> SHRUB = registerBlock(ShrubBlock::new, "shrub");
    public static final RegistryObject<Block> WEED = registerBlock(ShrubBlock::new, "weed");
    public static final RegistryObject<Block> PAPYRUS = registerBlock(PapyrusBlock::new, null, "papyrus");
    public static final RegistryObject<Block> OPHIDIAN_TONGUE = registerBlock(OphidianTongueBlock::new, "ophidian_tongue");
    public static final RegistryObject<Block> FLAX = registerBlock(FlaxBlock::new, null, "flax_block");
    public static final RegistryObject<Block> FERTILE_SOIL = registerBlock(FertileSoilBlock::new, "fertile_soil");
    public static final RegistryObject<Block> FERTILE_SOIL_TILLED = registerBlock(FertileSoilTilledBlock::new, "fertile_soil_tilled");
    public static final RegistryObject<Block> FERTILE_SOIL_PATH = registerBlock(() -> new AtumPathBlock(FERTILE_SOIL.get()), "fertile_soil_path");
    public static final RegistryObject<Block> STRANGE_SAND_PATH = registerBlock(() -> new AtumPathBlock(SAND.get()), "strange_sand_path");
    public static final RegistryObject<Block> QUERN = registerBlock(QuernBlock::new, "quern");
    public static final RegistryObject<Block> SPINNING_WHEEL = registerBlock(SpinningWheelBlock::new, "spinning_wheel");
    public static final RegistryObject<Block> KILN = registerBlock(KilnBlock::new, "kiln");
    public static final RegistryObject<Block> KILN_FAKE = registerBlock(KilnFakeBlock::new, null, "kiln_fake");
    public static final RegistryObject<Block> GODFORGE = registerBlock(GodforgeBlock::new, "godforge");
    public static final RegistryObject<Block> QUANDARY_BLOCK = registerBlock(QuandaryBlock::new, "quandary_block");
    public static final RegistryObject<Block> GLASSBLOWER_FURNACE = registerBlock(GlassblowerFurnace::new, "glassblower_furnace");
    public static final RegistryObject<Block> PALM_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(Material.WOOD) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new PalmCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "palm_curio_display");
    public static final RegistryObject<Block> DEADWOOD_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(Material.WOOD) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new DeadwoodCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "deadwood_curio_display");
    public static final RegistryObject<Block> ACACIA_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(Material.WOOD) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new AcaciaCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "acacia_curio_display");
    public static final RegistryObject<Block> LIMESTONE_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(Material.STONE) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new LimestoneCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "limestone_curio_display");
    public static final RegistryObject<Block> ALABASTER_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(Material.STONE) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new AlabasterCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "alabaster_curio_display");
    public static final RegistryObject<Block> PORPHYRY_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(Material.STONE) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new PorphyryCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "porphyry_curio_display");
    public static final RegistryObject<Block> NEBU_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(Material.METAL) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new NebuCurioDisplayTileEntity(pos, state);
        }
    }, new Item.Properties(), "nebu_curio_display");
    public static final RegistryObject<Block> BURNING_TRAP = registerBlock(BurningTrapBlock::new, "burning_trap");
    public static final RegistryObject<Block> POISON_TRAP = registerBlock(PoisonTrapBlock::new, "poison_trap");
    public static final RegistryObject<Block> TAR_TRAP = registerBlock(TarTrapBlock::new, "tar_trap");
    public static final RegistryObject<Block> SMOKE_TRAP = registerBlock(SmokeTrapBlock::new, "smoke_trap");
    public static final RegistryObject<Block> ARROW_TRAP = registerBlock(ArrowTrapBlock::new, "arrow_trap");
    public static final RegistryObject<Block> SARCOPHAGUS = registerWithRenderer(SarcophagusBlock::new, new Item.Properties(), "sarcophagus");
    public static final RegistryObject<Block> LIMESTONE_CHEST = registerWithRenderer(LimestoneChestBlock::new, new Item.Properties(), "limestone_chest");
    public static final RegistryObject<Block> GOLD_ORE = registerBlock(() -> new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)), "gold_ore");
    public static final RegistryObject<Block> IRON_ORE = registerBlock(() -> new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)), "iron_ore");
    public static final RegistryObject<Block> COAL_ORE = registerBlock(() -> new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 2)), "coal_ore");
    public static final RegistryObject<Block> LAPIS_ORE = registerBlock(() -> new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(2, 5)), "lapis_ore");
    public static final RegistryObject<Block> DIAMOND_ORE = registerBlock(() -> new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)), "diamond_ore");
    public static final RegistryObject<Block> EMERALD_ORE = registerBlock(() -> new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)), "emerald_ore");
    public static final RegistryObject<Block> REDSTONE_ORE = registerBlock(() -> new RedStoneOreBlock(of(Material.STONE).requiresCorrectToolForDrops().randomTicks().lightLevel(s -> 9).strength(3.0F, 3.0F)), "redstone_ore");
    public static final RegistryObject<Block> KHNUMITE_RAW = registerBlock(() -> new Block(of(Material.CLAY).strength(0.6F).sound(SoundType.GRAVEL)), "khnumite_raw");
    public static final RegistryObject<Block> BONE_ORE = registerBlock(() -> new OreBlock(of(Material.STONE).strength(3.0F, 3.0F), UniformInt.of(0, 2)), "bone_ore");
    public static final RegistryObject<Block> RELIC_ORE = registerBlock(() -> new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 2)), "relic_ore");
    public static final RegistryObject<Block> NEBU_ORE = registerBlock(() -> new OreBlock(of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(2, 6)), "nebu_ore");
    public static final RegistryObject<Block> NEBU_BLOCK = registerBlock(() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)), "nebu_block");
    public static final RegistryObject<Block> GODFORGED_BLOCK = registerBlock(GodforgedBlock::new, "godforged_block");
    public static final RegistryObject<Block> ANPUT_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.ANPUT), "anput_godforged_block");
    public static final RegistryObject<Block> ANUBIS_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.ANUBIS), "anubis_godforged_block");
    public static final RegistryObject<Block> ATEM_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.ATEM), "atem_godforged_block");
    public static final RegistryObject<Block> GEB_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.GEB), "geb_godforged_block");
    public static final RegistryObject<Block> HORUS_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.HORUS), "horus_godforged_block");
    public static final RegistryObject<Block> ISIS_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.ISIS), "isis_godforged_block");
    public static final RegistryObject<Block> MONTU_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.MONTU), "montu_godforged_block");
    public static final RegistryObject<Block> NEPTHYS_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.NEPTHYS), "nepthys_godforged_block");
    public static final RegistryObject<Block> NUIT_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.NUIT), "nuit_godforged_block");
    public static final RegistryObject<Block> OSIRIS_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.OSIRIS), "osiris_godforged_block");
    public static final RegistryObject<Block> PTAH_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.PTAH), "ptah_godforged_block");
    public static final RegistryObject<Block> RA_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.RA), "ra_godforged_block");
    public static final RegistryObject<Block> SETH_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.SETH), "seth_godforged_block");
    public static final RegistryObject<Block> SHU_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.SHU), "shu_godforged_block");
    public static final RegistryObject<Block> TEFNUT_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.TEFNUT), "tefnut_godforged_block");
    public static final RegistryObject<Block> DIRTY_BONE = registerBlock(() -> new RotatedPillarBlock(of(Material.STONE, MaterialColor.SAND).strength(2.0F)), "dirty_bone_block");
    public static final RegistryObject<Block> DIRTY_BONE_SLAB = registerBlock(() -> new SlabBlock(of(Material.STONE, MaterialColor.SAND).strength(2.0F)), "dirty_bone_slab");
    public static final RegistryObject<Block> BONE_LADDER = registerBlock(AtumLadderBlock::new, "bone_ladder");
    public static final RegistryObject<Block> LIMESTONE_FURNACE = registerBlock(LimestoneFurnaceBlock::new, "limestone_furnace");
    public static final RegistryObject<Block> PALM_TORCH = registerTorchWithUnlit(() -> new AtumTorchBlock(14), "palm_torch");
    public static final RegistryObject<Block> DEADWOOD_TORCH = registerTorchWithUnlit(() -> new AtumTorchBlock(14), "deadwood_torch");
    public static final RegistryObject<Block> LIMESTONE_TORCH = registerTorchWithUnlit(() -> new AtumTorchBlock(14), "limestone_torch");
    public static final RegistryObject<Block> BONE_TORCH = registerTorchWithUnlit(() -> new AtumTorchBlock(14), "bone_torch");
    public static final RegistryObject<Block> NEBU_TORCH = registerTorchWithUnlit(() -> new AtumTorchBlock(null), "nebu_torch");
    public static final RegistryObject<Block> TORCH_OF_ANPUT = registerTorch(() -> new AtumTorchBlock(God.ANPUT), "torch_of_anput");
    public static final RegistryObject<Block> TORCH_OF_ANUBIS = registerTorch(() -> new AtumTorchBlock(God.ANUBIS), "torch_of_anubis");
    public static final RegistryObject<Block> TORCH_OF_ATEM = registerTorch(() -> new AtumTorchBlock(God.ATEM), "torch_of_atem");
    public static final RegistryObject<Block> TORCH_OF_GEB = registerTorch(() -> new AtumTorchBlock(God.GEB), "torch_of_geb");
    public static final RegistryObject<Block> TORCH_OF_HORUS = registerTorch(() -> new AtumTorchBlock(God.HORUS), "torch_of_horus");
    public static final RegistryObject<Block> TORCH_OF_ISIS = registerTorch(() -> new AtumTorchBlock(God.ISIS), "torch_of_isis");
    public static final RegistryObject<Block> TORCH_OF_MONTU = registerTorch(() -> new AtumTorchBlock(God.MONTU), "torch_of_montu");
    public static final RegistryObject<Block> TORCH_OF_NEPTHYS = registerTorch(() -> new AtumTorchBlock(God.NEPTHYS), "torch_of_nepthys");
    public static final RegistryObject<Block> TORCH_OF_NUIT = registerTorch(() -> new AtumTorchBlock(God.NUIT), "torch_of_nuit");
    public static final RegistryObject<Block> TORCH_OF_OSIRIS = registerTorch(() -> new AtumTorchBlock(God.OSIRIS), "torch_of_osiris");
    public static final RegistryObject<Block> TORCH_OF_PTAH = registerTorch(() -> new AtumTorchBlock(God.PTAH), "torch_of_ptah");
    public static final RegistryObject<Block> TORCH_OF_RA = registerTorch(() -> new AtumTorchBlock(God.RA), "torch_of_ra");
    public static final RegistryObject<Block> TORCH_OF_SETH = registerTorch(() -> new AtumTorchBlock(God.SETH), "torch_of_seth");
    public static final RegistryObject<Block> TORCH_OF_SHU = registerTorch(() -> new AtumTorchBlock(God.SHU), "torch_of_shu");
    public static final RegistryObject<Block> TORCH_OF_TEFNUT = registerTorch(() -> new AtumTorchBlock(God.TEFNUT), "torch_of_tefnut");
    public static final RegistryObject<Block> NEBU_LANTERN = registerBlock(AtumLanternBlock::new, "nebu_lantern");
    public static final RegistryObject<Block> LANTERN_OF_ANPUT = registerBlock(AtumLanternBlock::new, "lantern_of_anput");
    public static final RegistryObject<Block> LANTERN_OF_ANUBIS = registerBlock(AtumLanternBlock::new, "lantern_of_anubis");
    public static final RegistryObject<Block> LANTERN_OF_ATEM = registerBlock(AtumLanternBlock::new, "lantern_of_atem");
    public static final RegistryObject<Block> LANTERN_OF_GEB = registerBlock(AtumLanternBlock::new, "lantern_of_geb");
    public static final RegistryObject<Block> LANTERN_OF_HORUS = registerBlock(AtumLanternBlock::new, "lantern_of_horus");
    public static final RegistryObject<Block> LANTERN_OF_ISIS = registerBlock(AtumLanternBlock::new, "lantern_of_isis");
    public static final RegistryObject<Block> LANTERN_OF_MONTU = registerBlock(AtumLanternBlock::new, "lantern_of_montu");
    public static final RegistryObject<Block> LANTERN_OF_NEPTHYS = registerBlock(AtumLanternBlock::new, "lantern_of_nepthys");
    public static final RegistryObject<Block> LANTERN_OF_NUIT = registerBlock(AtumLanternBlock::new, "lantern_of_nuit");
    public static final RegistryObject<Block> LANTERN_OF_OSIRIS = registerBlock(AtumLanternBlock::new, "lantern_of_osiris");
    public static final RegistryObject<Block> LANTERN_OF_PTAH = registerBlock(AtumLanternBlock::new, "lantern_of_ptah");
    public static final RegistryObject<Block> LANTERN_OF_RA = registerBlock(AtumLanternBlock::new, "lantern_of_ra");
    public static final RegistryObject<Block> LANTERN_OF_SETH = registerBlock(AtumLanternBlock::new, "lantern_of_seth");
    public static final RegistryObject<Block> LANTERN_OF_SHU = registerBlock(AtumLanternBlock::new, "lantern_of_shu");
    public static final RegistryObject<Block> LANTERN_OF_TEFNUT = registerBlock(AtumLanternBlock::new, "lantern_of_tefnut");
    public static final RegistryObject<Block> NEBU_CHAIN = registerBlock(() -> new ChainBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.NONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.CHAIN).noOcclusion()), "nebu_chain");
    public static final RegistryObject<Block> MARL = registerBlock(() -> new Block(of(Material.CLAY).strength(0.6F).sound(SoundType.GRAVEL)), "marl");
    public static final RegistryObject<Block> RA_STONE = registerBlock(RaStoneBlock::new, null, "ra_stone");
    public static final RegistryObject<Block> LIMESTONE = registerBlock(LimestoneBlock::new, "limestone");
    public static final RegistryObject<Block> LIMESTONE_CRACKED = registerBlock(() -> new Block(of(Material.STONE, MaterialColor.SAND).strength(1.5F, 10.0F)), "limestone_cracked");
    public static final RegistryObject<Block> LIMESTONE_BRICK_SMALL = registerBlock(LimestoneBrickBlock::new, "limestone_brick_small");
    public static final RegistryObject<Block> LIMESTONE_BRICK_LARGE = registerBlock(LimestoneBrickBlock::new, "limestone_brick_large");
    public static final RegistryObject<Block> LIMESTONE_BRICK_CRACKED_BRICK = registerBlock(LimestoneBrickBlock::new, "limestone_brick_cracked_brick");
    public static final RegistryObject<Block> LIMESTONE_BRICK_CHISELED = registerBlock(LimestoneBrickBlock::new, "limestone_brick_chiseled");
    public static final RegistryObject<Block> LIMESTONE_BRICK_CARVED = registerBlock(LimestoneBrickBlock::new, "limestone_brick_carved");
    public static final RegistryObject<Block> LIMESTONE_SLAB = registerBlock(() -> new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_slab");
    public static final RegistryObject<Block> LIMESTONE_CRACKED_SLAB = registerBlock(() -> new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_cracked_slab");
    public static final RegistryObject<Block> LIMESTONE_BRICK_SMALL_SLAB = registerBlock(() -> new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_small_slab");
    public static final RegistryObject<Block> LIMESTONE_BRICK_LARGE_SLAB = registerBlock(() -> new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_large_slab");
    public static final RegistryObject<Block> LIMESTONE_CRACKED_BRICK_SLAB = registerBlock(() -> new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_cracked_brick_slab");
    public static final RegistryObject<Block> LIMESTONE_BRICK_CHISELED_SLAB = registerBlock(() -> new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_chiseled_slab");
    public static final RegistryObject<Block> LIMESTONE_BRICK_CARVED_SLAB = registerBlock(() -> new SlabBlock(of(Material.STONE).strength(2.0F, 6.0F)), "limestone_carved_slab");
    public static final RegistryObject<Block> KHNUMITE_BLOCK = registerBlock(KhnumiteBlock::new, "khnumite_block");
    public static final RegistryObject<Block> KHNUMITE_FACE = registerBlock(KhnumiteFaceBlock::new, "khnumite_face");
    public static final RegistryObject<Block> SMOOTH_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE.get().defaultBlockState(), copy(LIMESTONE.get())), "smooth_stairs");
    public static final RegistryObject<Block> CRACKED_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_CRACKED.get().defaultBlockState(), copy(LIMESTONE_CRACKED.get())), "cracked_stairs");
    public static final RegistryObject<Block> SMALL_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_BRICK_SMALL.get().defaultBlockState(), copy(LIMESTONE_BRICK_SMALL.get())), "small_stairs");
    public static final RegistryObject<Block> LARGE_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_BRICK_LARGE.get().defaultBlockState(), copy(LIMESTONE_BRICK_LARGE.get())), "large_stairs");
    public static final RegistryObject<Block> CRACKED_BRICK_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_BRICK_CRACKED_BRICK.get().defaultBlockState(), copy(LIMESTONE_BRICK_CRACKED_BRICK.get())), "cracked_brick_stairs");
    public static final RegistryObject<Block> CHISELED_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_BRICK_CHISELED.get().defaultBlockState(), copy(LIMESTONE_BRICK_CHISELED.get())), "chiseled_stairs");
    public static final RegistryObject<Block> CARVED_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_BRICK_CARVED.get().defaultBlockState(), copy(LIMESTONE_BRICK_CARVED.get())), "carved_stairs");
    public static final RegistryObject<Block> LIMESTONE_WALL = registerBlock(() -> new WallBlock(copy(LIMESTONE.get())), "limestone_wall");
    public static final RegistryObject<Block> LIMESTONE_CRACKED_WALL = registerBlock(() -> new WallBlock(copy(LIMESTONE_CRACKED.get())), "limestone_cracked_wall");
    public static final RegistryObject<Block> SMALL_WALL = registerBlock(() -> new WallBlock(copy(LIMESTONE_BRICK_SMALL.get())), "small_wall");
    public static final RegistryObject<Block> LARGE_WALL = registerBlock(() -> new WallBlock(copy(LIMESTONE_BRICK_LARGE.get())), "large_wall");
    public static final RegistryObject<Block> CRACKED_BRICK_WALL = registerBlock(() -> new WallBlock(copy(LIMESTONE_BRICK_CRACKED_BRICK.get())), "cracked_brick_wall");
    public static final RegistryObject<Block> CHISELED_WALL = registerBlock(() -> new WallBlock(copy(LIMESTONE_BRICK_CHISELED.get())), "chiseled_wall");
    public static final RegistryObject<Block> CARVED_WALL = registerBlock(() -> new WallBlock(copy(LIMESTONE_BRICK_CARVED.get())), "carved_wall");
    public static final RegistryObject<Block> LIMESTONE_DOOR = registerBlock(() -> new DoorAtumBlock(copy(LIMESTONE.get())), "limestone_door");
    public static final RegistryObject<Block> LIMESTONE_CRACKED_DOOR = registerBlock(() -> new DoorAtumBlock(copy(LIMESTONE_CRACKED.get())), "limestone_cracked_door");
    public static final RegistryObject<Block> LIMESTONE_BRICK_SMALL_DOOR = registerBlock(() -> new DoorAtumBlock(copy(LIMESTONE_BRICK_SMALL.get())), "limestone_brick_small_door");
    public static final RegistryObject<Block> LIMESTONE_BRICK_LARGE_DOOR = registerBlock(() -> new DoorAtumBlock(copy(LIMESTONE_BRICK_LARGE.get())), "limestone_brick_large_door");
    public static final RegistryObject<Block> LIMESTONE_CRACKED_BRICK_DOOR = registerBlock(() -> new DoorAtumBlock(copy(LIMESTONE_BRICK_CRACKED_BRICK.get())), "limestone_brick_cracked_brick_door");
    public static final RegistryObject<Block> LIMESTONE_BRICK_CHISELED_DOOR = registerBlock(() -> new DoorAtumBlock(copy(LIMESTONE_BRICK_CHISELED.get())), "limestone_brick_chiseled_door");
    public static final RegistryObject<Block> LIMESTONE_BRICK_CARVED_DOOR = registerBlock(() -> new DoorAtumBlock(copy(LIMESTONE_BRICK_CARVED.get())), "limestone_brick_carved_door");
    public static final RegistryObject<Block> ALABASTER = registerBlock(() -> new Block(of(Material.STONE, MaterialColor.QUARTZ).strength(2.0F, 8.0F)), "alabaster");
    public static final RegistryObject<Block> ALABASTER_BRICK_SMOOTH = registerBlock(() -> new Block(copy(ALABASTER.get())), "alabaster_brick_smooth");
    public static final RegistryObject<Block> ALABASTER_BRICK_POLISHED = registerBlock(() -> new Block(copy(ALABASTER.get())), "alabaster_brick_polished");
    public static final RegistryObject<Block> ALABASTER_BRICK_CARVED = registerBlock(() -> new Block(copy(ALABASTER.get())), "alabaster_brick_carved");
    public static final RegistryObject<Block> ALABASTER_BRICK_TILED = registerBlock(() -> new Block(copy(ALABASTER.get())), "alabaster_brick_tiled");
    public static final RegistryObject<Block> ALABASTER_BRICK_PILLAR = registerBlock(() -> new Block(copy(ALABASTER.get())), "alabaster_brick_pillar");
    public static final RegistryObject<Block> ALABASTER_BRICK_SMOOTH_SLAB = registerBlock(() -> new SlabBlock(copy(ALABASTER.get())), "alabaster_smooth_slab");
    public static final RegistryObject<Block> ALABASTER_BRICK_POLISHED_SLAB = registerBlock(() -> new SlabBlock(copy(ALABASTER.get())), "alabaster_polished_slab");
    public static final RegistryObject<Block> ALABASTER_BRICK_CARVED_SLAB = registerBlock(() -> new SlabBlock(copy(ALABASTER.get())), "alabaster_carved_slab");
    public static final RegistryObject<Block> ALABASTER_BRICK_TILED_SLAB = registerBlock(() -> new SlabBlock(copy(ALABASTER.get())), "alabaster_tiled_slab");
    public static final RegistryObject<Block> ALABASTER_BRICK_PILLAR_SLAB = registerBlock(() -> new SlabBlock(copy(ALABASTER.get())), "alabaster_pillar_slab");
    public static final RegistryObject<Block> ALABASTER_BRICK_SMOOTH_STAIRS = registerBlock(() -> new StairBlock(() -> ALABASTER_BRICK_SMOOTH.get().defaultBlockState(), copy(ALABASTER.get())), "alabaster_smooth_stairs");
    public static final RegistryObject<Block> ALABASTER_BRICK_POLISHED_STAIRS = registerBlock(() -> new StairBlock(() -> ALABASTER_BRICK_POLISHED.get().defaultBlockState(), copy(ALABASTER.get())), "alabaster_polished_stairs");
    public static final RegistryObject<Block> ALABASTER_BRICK_CARVED_STAIRS = registerBlock(() -> new StairBlock(() -> ALABASTER_BRICK_CARVED.get().defaultBlockState(), copy(ALABASTER.get())), "alabaster_carved_stairs");
    public static final RegistryObject<Block> ALABASTER_BRICK_TILED_STAIRS = registerBlock(() -> new StairBlock(() -> ALABASTER_BRICK_TILED.get().defaultBlockState(), copy(ALABASTER.get())), "alabaster_tiled_stairs");
    public static final RegistryObject<Block> ALABASTER_BRICK_PILLAR_STARS = registerBlock(() -> new StairBlock(() -> ALABASTER_BRICK_PILLAR.get().defaultBlockState(), copy(ALABASTER.get())), "alabaster_pillar_stairs");
    public static final RegistryObject<Block> ALABASTER_BRICK_SMOOTH_WALL = registerBlock(() -> new WallBlock(copy(ALABASTER.get())), "alabaster_smooth_wall");
    public static final RegistryObject<Block> ALABASTER_BRICK_POLISHED_WALL = registerBlock(() -> new WallBlock(copy(ALABASTER.get())), "alabaster_polished_wall");
    public static final RegistryObject<Block> ALABASTER_BRICK_CARVED_WALL = registerBlock(() -> new WallBlock(copy(ALABASTER.get())), "alabaster_carved_wall");
    public static final RegistryObject<Block> ALABASTER_BRICK_TILED_WALL = registerBlock(() -> new WallBlock(copy(ALABASTER.get())), "alabaster_tiled_wall");
    public static final RegistryObject<Block> ALABASTER_BRICK_PILLAR_WALL = registerBlock(() -> new WallBlock(copy(ALABASTER.get())), "alabaster_pillar_wall");
    public static final RegistryObject<Block> PORPHYRY = registerBlock(() -> new Block(of(Material.STONE, MaterialColor.COLOR_BLACK).strength(1.5F, 5.0F).sound(SoundType.STONE)), "porphyry");
    public static final RegistryObject<Block> PORPHYRY_BRICK_SMOOTH = registerBlock(() -> new Block(copy(PORPHYRY.get())), "porphyry_brick_smooth");
    public static final RegistryObject<Block> PORPHYRY_BRICK_POLISHED = registerBlock(() -> new Block(copy(PORPHYRY.get())), "porphyry_brick_polished");
    public static final RegistryObject<Block> PORPHYRY_BRICK_CARVED = registerBlock(() -> new Block(copy(PORPHYRY.get())), "porphyry_brick_carved");
    public static final RegistryObject<Block> PORPHYRY_BRICK_TILED = registerBlock(() -> new Block(copy(PORPHYRY.get())), "porphyry_brick_tiled");
    public static final RegistryObject<Block> PORPHYRY_BRICK_PILLAR = registerBlock(() -> new Block(copy(PORPHYRY.get())), "porphyry_brick_pillar");
    public static final RegistryObject<Block> PORPHYRY_BRICK_SMOOTH_SLAB = registerBlock(() -> new SlabBlock(copy(PORPHYRY.get())), "porphyry_smooth_slab");
    public static final RegistryObject<Block> PORPHYRY_BRICK_POLISHED_SLAB = registerBlock(() -> new SlabBlock(copy(PORPHYRY.get())), "porphyry_polished_slab");
    public static final RegistryObject<Block> PORPHYRY_BRICK_CARVED_SLAB = registerBlock(() -> new SlabBlock(copy(PORPHYRY.get())), "porphyry_carved_slab");
    public static final RegistryObject<Block> PORPHYRY_BRICK_TILED_SLAB = registerBlock(() -> new SlabBlock(copy(PORPHYRY.get())), "porphyry_tiled_slab");
    public static final RegistryObject<Block> PORPHYRY_BRICK_PILLAR_SLAB = registerBlock(() -> new SlabBlock(copy(PORPHYRY.get())), "porphyry_pillar_slab");
    public static final RegistryObject<Block> PORPHYRY_BRICK_SMOOTH_STAIRS = registerBlock(() -> new StairBlock(() -> PORPHYRY_BRICK_SMOOTH.get().defaultBlockState(), copy(PORPHYRY.get())), "porphyry_smooth_stairs");
    public static final RegistryObject<Block> PORPHYRY_BRICK_POLISHED_STAIRS = registerBlock(() -> new StairBlock(() -> PORPHYRY_BRICK_POLISHED.get().defaultBlockState(), copy(PORPHYRY.get())), "porphyry_polished_stairs");
    public static final RegistryObject<Block> PORPHYRY_BRICK_CARVED_STAIRS = registerBlock(() -> new StairBlock(() -> PORPHYRY_BRICK_CARVED.get().defaultBlockState(), copy(PORPHYRY.get())), "porphyry_carved_stairs");
    public static final RegistryObject<Block> PORPHYRY_BRICK_TILED_STAIRS = registerBlock(() -> new StairBlock(() -> PORPHYRY_BRICK_TILED.get().defaultBlockState(), copy(PORPHYRY.get())), "porphyry_tiled_stairs");
    public static final RegistryObject<Block> PORPHYRY_BRICK_PILLAR_STARS = registerBlock(() -> new StairBlock(() -> PORPHYRY_BRICK_PILLAR.get().defaultBlockState(), copy(PORPHYRY.get())), "porphyry_pillar_stairs");
    public static final RegistryObject<Block> PORPHYRY_BRICK_SMOOTH_WALL = registerBlock(() -> new WallBlock(copy(PORPHYRY.get())), "porphyry_smooth_wall");
    public static final RegistryObject<Block> PORPHYRY_BRICK_POLISHED_WALL = registerBlock(() -> new WallBlock(copy(PORPHYRY.get())), "porphyry_polished_wall");
    public static final RegistryObject<Block> PORPHYRY_BRICK_CARVED_WALL = registerBlock(() -> new WallBlock(copy(PORPHYRY.get())), "porphyry_carved_wall");
    public static final RegistryObject<Block> PORPHYRY_BRICK_TILED_WALL = registerBlock(() -> new WallBlock(copy(PORPHYRY.get())), "porphyry_tiled_wall");
    public static final RegistryObject<Block> PORPHYRY_BRICK_PILLAR_WALL = registerBlock(() -> new WallBlock(copy(PORPHYRY.get())), "porphyry_pillar_wall");
    public static final RegistryObject<Block> CERAMIC_WHITE = registerBlock(() -> new CeramicBlock(DyeColor.WHITE), "ceramic_white");
    public static final RegistryObject<Block> CERAMIC_ORANGE = registerBlock(() -> new CeramicBlock(DyeColor.ORANGE), "ceramic_orange");
    public static final RegistryObject<Block> CERAMIC_MAGENTA = registerBlock(() -> new CeramicBlock(DyeColor.MAGENTA), "ceramic_magenta");
    public static final RegistryObject<Block> CERAMIC_LIGHT_BLUE = registerBlock(() -> new CeramicBlock(DyeColor.LIGHT_BLUE), "ceramic_light_blue");
    public static final RegistryObject<Block> CERAMIC_YELLOW = registerBlock(() -> new CeramicBlock(DyeColor.YELLOW), "ceramic_yellow");
    public static final RegistryObject<Block> CERAMIC_LIME = registerBlock(() -> new CeramicBlock(DyeColor.LIME), "ceramic_lime");
    public static final RegistryObject<Block> CERAMIC_PINK = registerBlock(() -> new CeramicBlock(DyeColor.PINK), "ceramic_pink");
    public static final RegistryObject<Block> CERAMIC_GRAY = registerBlock(() -> new CeramicBlock(DyeColor.GRAY), "ceramic_gray");
    public static final RegistryObject<Block> CERAMIC_LIGHT_GRAY = registerBlock(() -> new CeramicBlock(DyeColor.LIGHT_GRAY), "ceramic_light_gray");
    public static final RegistryObject<Block> CERAMIC_CYAN = registerBlock(() -> new CeramicBlock(DyeColor.CYAN), "ceramic_cyan");
    public static final RegistryObject<Block> CERAMIC_PURPLE = registerBlock(() -> new CeramicBlock(DyeColor.PURPLE), "ceramic_purple");
    public static final RegistryObject<Block> CERAMIC_BLUE = registerBlock(() -> new CeramicBlock(DyeColor.BLUE), "ceramic_blue");
    public static final RegistryObject<Block> CERAMIC_BROWN = registerBlock(() -> new CeramicBlock(DyeColor.BROWN), "ceramic_brown");
    public static final RegistryObject<Block> CERAMIC_GREEN = registerBlock(() -> new CeramicBlock(DyeColor.GREEN), "ceramic_green");
    public static final RegistryObject<Block> CERAMIC_RED = registerBlock(() -> new CeramicBlock(DyeColor.RED), "ceramic_red");
    public static final RegistryObject<Block> CERAMIC_BLACK = registerBlock(() -> new CeramicBlock(DyeColor.BLACK), "ceramic_black");
    public static final RegistryObject<Block> CERAMIC_WHITE_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_WHITE.get())), "ceramic_slab_white");
    public static final RegistryObject<Block> CERAMIC_ORANGE_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_ORANGE.get())), "ceramic_slab_orange");
    public static final RegistryObject<Block> CERAMIC_MAGENTA_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_MAGENTA.get())), "ceramic_slab_magenta");
    public static final RegistryObject<Block> CERAMIC_LIGHT_BLUE_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_LIGHT_BLUE.get())), "ceramic_slab_light_blue");
    public static final RegistryObject<Block> CERAMIC_YELLOW_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_YELLOW.get())), "ceramic_slab_yellow");
    public static final RegistryObject<Block> CERAMIC_LIME_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_LIME.get())), "ceramic_slab_lime");
    public static final RegistryObject<Block> CERAMIC_PINK_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_PINK.get())), "ceramic_slab_pink");
    public static final RegistryObject<Block> CERAMIC_GRAY_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_GRAY.get())), "ceramic_slab_gray");
    public static final RegistryObject<Block> CERAMIC_LIGHT_GRAY_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_LIGHT_GRAY.get())), "ceramic_slab_light_gray");
    public static final RegistryObject<Block> CERAMIC_CYAN_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_CYAN.get())), "ceramic_slab_cyan");
    public static final RegistryObject<Block> CERAMIC_PURPLE_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_PURPLE.get())), "ceramic_slab_purple");
    public static final RegistryObject<Block> CERAMIC_BLUE_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_BLUE.get())), "ceramic_slab_blue");
    public static final RegistryObject<Block> CERAMIC_BROWN_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_BROWN.get())), "ceramic_slab_brown");
    public static final RegistryObject<Block> CERAMIC_GREEN_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_GREEN.get())), "ceramic_slab_green");
    public static final RegistryObject<Block> CERAMIC_RED_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_RED.get())), "ceramic_slab_red");
    public static final RegistryObject<Block> CERAMIC_BLACK_SLAB = registerBlock(() -> new SlabBlock(copy(CERAMIC_BLACK.get())), "ceramic_slab_black");
    public static final RegistryObject<Block> CERAMIC_WHITE_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_WHITE.get())), "ceramic_tile_white");
    public static final RegistryObject<Block> CERAMIC_ORANGE_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_ORANGE.get())), "ceramic_tile_orange");
    public static final RegistryObject<Block> CERAMIC_MAGENTA_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_MAGENTA.get())), "ceramic_tile_magenta");
    public static final RegistryObject<Block> CERAMIC_LIGHT_BLUE_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_LIGHT_BLUE.get())), "ceramic_tile_light_blue");
    public static final RegistryObject<Block> CERAMIC_YELLOW_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_YELLOW.get())), "ceramic_tile_yellow");
    public static final RegistryObject<Block> CERAMIC_LIME_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_LIME.get())), "ceramic_tile_lime");
    public static final RegistryObject<Block> CERAMIC_PINK_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_PINK.get())), "ceramic_tile_pink");
    public static final RegistryObject<Block> CERAMIC_GRAY_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_GRAY.get())), "ceramic_tile_gray");
    public static final RegistryObject<Block> CERAMIC_LIGHT_GRAY_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_LIGHT_GRAY.get())), "ceramic_tile_light_gray");
    public static final RegistryObject<Block> CERAMIC_CYAN_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_CYAN.get())), "ceramic_tile_cyan");
    public static final RegistryObject<Block> CERAMIC_PURPLE_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_PURPLE.get())), "ceramic_tile_purple");
    public static final RegistryObject<Block> CERAMIC_BLUE_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_BLUE.get())), "ceramic_tile_blue");
    public static final RegistryObject<Block> CERAMIC_BROWN_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_BROWN.get())), "ceramic_tile_brown");
    public static final RegistryObject<Block> CERAMIC_GREEN_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_GREEN.get())), "ceramic_tile_green");
    public static final RegistryObject<Block> CERAMIC_RED_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_RED.get())), "ceramic_tile_red");
    public static final RegistryObject<Block> CERAMIC_BLACK_TILE = registerBlock(() -> new CeramicTileBlock(copy(CERAMIC_BLACK.get())), "ceramic_tile_black");
    public static final RegistryObject<Block> CERAMIC_WHITE_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_WHITE.get().defaultBlockState(), copy(CERAMIC_WHITE.get())), "ceramic_stairs_white");
    public static final RegistryObject<Block> CERAMIC_ORANGE_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_ORANGE.get().defaultBlockState(), copy(CERAMIC_ORANGE.get())), "ceramic_stairs_orange");
    public static final RegistryObject<Block> CERAMIC_MAGENTA_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_MAGENTA.get().defaultBlockState(), copy(CERAMIC_MAGENTA.get())), "ceramic_stairs_magenta");
    public static final RegistryObject<Block> CERAMIC_LIGHT_BLUE_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_LIGHT_BLUE.get().defaultBlockState(), copy(CERAMIC_LIGHT_BLUE.get())), "ceramic_stairs_light_blue");
    public static final RegistryObject<Block> CERAMIC_YELLOW_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_YELLOW.get().defaultBlockState(), copy(CERAMIC_YELLOW.get())), "ceramic_stairs_yellow");
    public static final RegistryObject<Block> CERAMIC_LIME_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_LIME.get().defaultBlockState(), copy(CERAMIC_LIME.get())), "ceramic_stairs_lime");
    public static final RegistryObject<Block> CERAMIC_PINK_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_PINK.get().defaultBlockState(), copy(CERAMIC_PINK.get())), "ceramic_stairs_pink");
    public static final RegistryObject<Block> CERAMIC_GRAY_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_GRAY.get().defaultBlockState(), copy(CERAMIC_GRAY.get())), "ceramic_stairs_gray");
    public static final RegistryObject<Block> CERAMIC_LIGHT_GRAY_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_LIGHT_GRAY.get().defaultBlockState(), copy(CERAMIC_LIGHT_GRAY.get())), "ceramic_stairs_light_gray");
    public static final RegistryObject<Block> CERAMIC_CYAN_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_CYAN.get().defaultBlockState(), copy(CERAMIC_CYAN.get())), "ceramic_stairs_cyan");
    public static final RegistryObject<Block> CERAMIC_PURPLE_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_PURPLE.get().defaultBlockState(), copy(CERAMIC_PURPLE.get())), "ceramic_stairs_purple");
    public static final RegistryObject<Block> CERAMIC_BLUE_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_BLUE.get().defaultBlockState(), copy(CERAMIC_BLUE.get())), "ceramic_stairs_blue");
    public static final RegistryObject<Block> CERAMIC_BROWN_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_BROWN.get().defaultBlockState(), copy(CERAMIC_BROWN.get())), "ceramic_stairs_brown");
    public static final RegistryObject<Block> CERAMIC_GREEN_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_GREEN.get().defaultBlockState(), copy(CERAMIC_GREEN.get())), "ceramic_stairs_green");
    public static final RegistryObject<Block> CERAMIC_RED_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_RED.get().defaultBlockState(), copy(CERAMIC_RED.get())), "ceramic_stairs_red");
    public static final RegistryObject<Block> CERAMIC_BLACK_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_BLACK.get().defaultBlockState(), copy(CERAMIC_BLACK.get())), "ceramic_stairs_black");
    public static final RegistryObject<Block> CERAMIC_WHITE_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_WHITE.get())), "ceramic_wall_white");
    public static final RegistryObject<Block> CERAMIC_ORANGE_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_ORANGE.get())), "ceramic_wall_orange");
    public static final RegistryObject<Block> CERAMIC_MAGENTA_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_MAGENTA.get())), "ceramic_wall_magenta");
    public static final RegistryObject<Block> CERAMIC_LIGHT_BLUE_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_LIGHT_BLUE.get())), "ceramic_wall_light_blue");
    public static final RegistryObject<Block> CERAMIC_YELLOW_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_YELLOW.get())), "ceramic_wall_yellow");
    public static final RegistryObject<Block> CERAMIC_LIME_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_LIME.get())), "ceramic_wall_lime");
    public static final RegistryObject<Block> CERAMIC_PINK_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_PINK.get())), "ceramic_wall_pink");
    public static final RegistryObject<Block> CERAMIC_GRAY_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_GRAY.get())), "ceramic_wall_gray");
    public static final RegistryObject<Block> CERAMIC_LIGHT_GRAY_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_LIGHT_GRAY.get())), "ceramic_wall_light_gray");
    public static final RegistryObject<Block> CERAMIC_CYAN_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_CYAN.get())), "ceramic_wall_cyan");
    public static final RegistryObject<Block> CERAMIC_PURPLE_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_PURPLE.get())), "ceramic_wall_purple");
    public static final RegistryObject<Block> CERAMIC_BLUE_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_BLUE.get())), "ceramic_wall_blue");
    public static final RegistryObject<Block> CERAMIC_BROWN_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_BROWN.get())), "ceramic_wall_brown");
    public static final RegistryObject<Block> CERAMIC_GREEN_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_GREEN.get())), "ceramic_wall_green");
    public static final RegistryObject<Block> CERAMIC_RED_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_RED.get())), "ceramic_wall_red");
    public static final RegistryObject<Block> CERAMIC_BLACK_WALL = registerBlock(() -> new WallBlock(copy(CERAMIC_BLACK.get())), "ceramic_wall_black");
    public static final RegistryObject<Block> CRYSTAL_GLASS = registerBlock(() -> new GlassBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(AtumBlocks::never).isRedstoneConductor(AtumBlocks::never).isSuffocating(AtumBlocks::never).isViewBlocking(AtumBlocks::never)), "crystal_glass");
    public static final RegistryObject<Block> WHITE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.WHITE), "white_stained_crystal_glass");
    public static final RegistryObject<Block> ORANGE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.ORANGE), "orange_stained_crystal_glass");
    public static final RegistryObject<Block> MAGENTA_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.MAGENTA), "magenta_stained_crystal_glass");
    public static final RegistryObject<Block> LIGHT_BLUE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_BLUE), "light_blue_stained_crystal_glass");
    public static final RegistryObject<Block> YELLOW_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.YELLOW), "yellow_stained_crystal_glass");
    public static final RegistryObject<Block> LIME_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIME), "lime_stained_crystal_glass");
    public static final RegistryObject<Block> PINK_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PINK), "pink_stained_crystal_glass");
    public static final RegistryObject<Block> GRAY_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GRAY), "gray_stained_crystal_glass");
    public static final RegistryObject<Block> LIGHT_GRAY_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_GRAY), "light_gray_stained_crystal_glass");
    public static final RegistryObject<Block> CYAN_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.CYAN), "cyan_stained_crystal_glass");
    public static final RegistryObject<Block> PURPLE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PURPLE), "purple_stained_crystal_glass");
    public static final RegistryObject<Block> BLUE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLUE), "blue_stained_crystal_glass");
    public static final RegistryObject<Block> BROWN_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BROWN), "brown_stained_crystal_glass");
    public static final RegistryObject<Block> GREEN_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GREEN), "green_stained_crystal_glass");
    public static final RegistryObject<Block> RED_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.RED), "red_stained_crystal_glass");
    public static final RegistryObject<Block> BLACK_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLACK), "black_stained_crystal_glass");
    public static final RegistryObject<Block> CRYSTAL_GLASS_PANE = registerBlock(AtumPaneBlock::new, "crystal_glass_pane");
    public static final RegistryObject<Block> WHITE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.WHITE, copy(CRYSTAL_GLASS_PANE.get())), "white_stained_crystal_glass_pane");
    public static final RegistryObject<Block> ORANGE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.ORANGE, copy(CRYSTAL_GLASS_PANE.get())), "orange_stained_crystal_glass_pane");
    public static final RegistryObject<Block> MAGENTA_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.MAGENTA, copy(CRYSTAL_GLASS_PANE.get())), "magenta_stained_crystal_glass_pane");
    public static final RegistryObject<Block> LIGHT_BLUE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_BLUE, copy(CRYSTAL_GLASS_PANE.get())), "light_blue_stained_crystal_glass_pane");
    public static final RegistryObject<Block> YELLOW_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.YELLOW, copy(CRYSTAL_GLASS_PANE.get())), "yellow_stained_crystal_glass_pane");
    public static final RegistryObject<Block> LIME_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIME, copy(CRYSTAL_GLASS_PANE.get())), "lime_stained_crystal_glass_pane");
    public static final RegistryObject<Block> PINK_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PINK, copy(CRYSTAL_GLASS_PANE.get())), "pink_stained_crystal_glass_pane");
    public static final RegistryObject<Block> GRAY_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GRAY, copy(CRYSTAL_GLASS_PANE.get())), "gray_stained_crystal_glass_pane");
    public static final RegistryObject<Block> LIGHT_GRAY_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_GRAY, copy(CRYSTAL_GLASS_PANE.get())), "light_gray_stained_crystal_glass_pane");
    public static final RegistryObject<Block> CYAN_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.CYAN, copy(CRYSTAL_GLASS_PANE.get())), "cyan_stained_crystal_glass_pane");
    public static final RegistryObject<Block> PURPLE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PURPLE, copy(CRYSTAL_GLASS_PANE.get())), "purple_stained_crystal_glass_pane");
    public static final RegistryObject<Block> BLUE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLUE, copy(CRYSTAL_GLASS_PANE.get())), "blue_stained_crystal_glass_pane");
    public static final RegistryObject<Block> BROWN_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BROWN, copy(CRYSTAL_GLASS_PANE.get())), "brown_stained_crystal_glass_pane");
    public static final RegistryObject<Block> GREEN_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GREEN, copy(CRYSTAL_GLASS_PANE.get())), "green_stained_crystal_glass_pane");
    public static final RegistryObject<Block> RED_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.RED, copy(CRYSTAL_GLASS_PANE.get())), "red_stained_crystal_glass_pane");
    public static final RegistryObject<Block> BLACK_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLACK, copy(CRYSTAL_GLASS_PANE.get())), "black_stained_crystal_glass_pane");
    public static final RegistryObject<Block> PALM_FRAMED_CRYSTAL_GLASS = registerBlock(() -> new GlassBlock(of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()), "palm_framed_crystal_glass");
    public static final RegistryObject<Block> WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.WHITE), "white_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.ORANGE), "orange_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.MAGENTA), "magenta_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_BLUE), "light_blue_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.YELLOW), "yellow_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIME), "lime_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PINK), "pink_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GRAY), "gray_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_GRAY), "light_gray_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.CYAN), "cyan_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PURPLE), "purple_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLUE), "blue_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BROWN), "brown_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GREEN), "green_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.RED), "red_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLACK), "black_stained_palm_framed_crystal_glass");
    public static final RegistryObject<Block> DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(() -> new GlassBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(AtumBlocks::never).isRedstoneConductor(AtumBlocks::never).isSuffocating(AtumBlocks::never).isViewBlocking(AtumBlocks::never)), "deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.WHITE), "white_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.ORANGE), "orange_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.MAGENTA), "magenta_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_BLUE), "light_blue_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.YELLOW), "yellow_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIME), "lime_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PINK), "pink_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GRAY), "gray_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_GRAY), "light_gray_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.CYAN), "cyan_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PURPLE), "purple_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLUE), "blue_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BROWN), "brown_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GREEN), "green_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.RED), "red_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLACK), "black_stained_deadwood_framed_crystal_glass");
    public static final RegistryObject<Block> PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(AtumPaneBlock::new, "palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.WHITE, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "white_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.ORANGE, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "orange_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.MAGENTA, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "magenta_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_BLUE, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "light_blue_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.YELLOW, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "yellow_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIME, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "lime_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PINK, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "pink_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GRAY, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "gray_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_GRAY, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "light_gray_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.CYAN, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "cyan_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PURPLE, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "purple_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLUE, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "blue_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BROWN, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "brown_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GREEN, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "green_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.RED, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "red_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLACK, copy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "black_stained_palm_framed_crystal_glass_pane");
    public static final RegistryObject<Block> DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(AtumPaneBlock::new, "deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.WHITE, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "white_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.ORANGE, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "orange_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.MAGENTA, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "magenta_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_BLUE, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "light_blue_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.YELLOW, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "yellow_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIME, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "lime_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PINK, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "pink_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GRAY, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "gray_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_GRAY, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "light_gray_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.CYAN, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "cyan_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PURPLE, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "purple_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLUE, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "blue_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BROWN, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "brown_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GREEN, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "green_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.RED, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "red_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLACK, copy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "black_stained_deadwood_framed_crystal_glass_pane");
    public static final RegistryObject<Block> LINEN_WHITE = registerBlock(() -> new LinenBlock(DyeColor.WHITE), "linen_white");
    public static final RegistryObject<Block> LINEN_ORANGE = registerBlock(() -> new LinenBlock(DyeColor.ORANGE), "linen_orange");
    public static final RegistryObject<Block> LINEN_MAGENTA = registerBlock(() -> new LinenBlock(DyeColor.MAGENTA), "linen_magenta");
    public static final RegistryObject<Block> LINEN_LIGHT_BLUE = registerBlock(() -> new LinenBlock(DyeColor.LIGHT_BLUE), "linen_light_blue");
    public static final RegistryObject<Block> LINEN_YELLOW = registerBlock(() -> new LinenBlock(DyeColor.YELLOW), "linen_yellow");
    public static final RegistryObject<Block> LINEN_LIME = registerBlock(() -> new LinenBlock(DyeColor.LIME), "linen_lime");
    public static final RegistryObject<Block> LINEN_PINK = registerBlock(() -> new LinenBlock(DyeColor.PINK), "linen_pink");
    public static final RegistryObject<Block> LINEN_GRAY = registerBlock(() -> new LinenBlock(DyeColor.GRAY), "linen_gray");
    public static final RegistryObject<Block> LINEN_LIGHT_GRAY = registerBlock(() -> new LinenBlock(DyeColor.LIGHT_GRAY), "linen_light_gray");
    public static final RegistryObject<Block> LINEN_CYAN = registerBlock(() -> new LinenBlock(DyeColor.CYAN), "linen_cyan");
    public static final RegistryObject<Block> LINEN_PURPLE = registerBlock(() -> new LinenBlock(DyeColor.PURPLE), "linen_purple");
    public static final RegistryObject<Block> LINEN_BLUE = registerBlock(() -> new LinenBlock(DyeColor.BLUE), "linen_blue");
    public static final RegistryObject<Block> LINEN_BROWN = registerBlock(() -> new LinenBlock(DyeColor.BROWN), "linen_brown");
    public static final RegistryObject<Block> LINEN_GREEN = registerBlock(() -> new LinenBlock(DyeColor.GREEN), "linen_green");
    public static final RegistryObject<Block> LINEN_RED = registerBlock(() -> new LinenBlock(DyeColor.RED), "linen_red");
    public static final RegistryObject<Block> LINEN_BLACK = registerBlock(() -> new LinenBlock(DyeColor.BLACK), "linen_black");
    public static final RegistryObject<Block> LINEN_CARPET_WHITE = registerBlock(() -> new LinenCarpetBlock(DyeColor.WHITE), "linen_carpet_white");
    public static final RegistryObject<Block> LINEN_CARPET_ORANGE = registerBlock(() -> new LinenCarpetBlock(DyeColor.ORANGE), "linen_carpet_orange");
    public static final RegistryObject<Block> LINEN_CARPET_MAGENTA = registerBlock(() -> new LinenCarpetBlock(DyeColor.MAGENTA), "linen_carpet_magenta");
    public static final RegistryObject<Block> LINEN_CARPET_LIGHT_BLUE = registerBlock(() -> new LinenCarpetBlock(DyeColor.LIGHT_BLUE), "linen_carpet_light_blue");
    public static final RegistryObject<Block> LINEN_CARPET_YELLOW = registerBlock(() -> new LinenCarpetBlock(DyeColor.YELLOW), "linen_carpet_yellow");
    public static final RegistryObject<Block> LINEN_CARPET_LIME = registerBlock(() -> new LinenCarpetBlock(DyeColor.LIME), "linen_carpet_lime");
    public static final RegistryObject<Block> LINEN_CARPET_PINK = registerBlock(() -> new LinenCarpetBlock(DyeColor.PINK), "linen_carpet_pink");
    public static final RegistryObject<Block> LINEN_CARPET_GRAY = registerBlock(() -> new LinenCarpetBlock(DyeColor.GRAY), "linen_carpet_gray");
    public static final RegistryObject<Block> LINEN_CARPET_LIGHT_GRAY = registerBlock(() -> new LinenCarpetBlock(DyeColor.LIGHT_GRAY), "linen_carpet_light_gray");
    public static final RegistryObject<Block> LINEN_CARPET_CYAN = registerBlock(() -> new LinenCarpetBlock(DyeColor.CYAN), "linen_carpet_cyan");
    public static final RegistryObject<Block> LINEN_CARPET_PURPLE = registerBlock(() -> new LinenCarpetBlock(DyeColor.PURPLE), "linen_carpet_purple");
    public static final RegistryObject<Block> LINEN_CARPET_BLUE = registerBlock(() -> new LinenCarpetBlock(DyeColor.BLUE), "linen_carpet_blue");
    public static final RegistryObject<Block> LINEN_CARPET_BROWN = registerBlock(() -> new LinenCarpetBlock(DyeColor.BROWN), "linen_carpet_brown");
    public static final RegistryObject<Block> LINEN_CARPET_GREEN = registerBlock(() -> new LinenCarpetBlock(DyeColor.GREEN), "linen_carpet_green");
    public static final RegistryObject<Block> LINEN_CARPET_RED = registerBlock(() -> new LinenCarpetBlock(DyeColor.RED), "linen_carpet_red");
    public static final RegistryObject<Block> LINEN_CARPET_BLACK = registerBlock(() -> new LinenCarpetBlock(DyeColor.BLACK), "linen_carpet_black");
    public static final RegistryObject<Block> PALM_PLANKS = registerBlock(() -> new Block(of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)), "palm_planks");
    public static final RegistryObject<Block> DEADWOOD_PLANKS = registerBlock(() -> new Block(of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F, 3.0F).sound(SoundType.WOOD)), "deadwood_planks");
    public static final RegistryObject<Block> PALM_LOG = registerBlock(PalmLog::new, "palm_log");
    public static final RegistryObject<Block> STRIPPED_PALM_LOG = registerBlock(() -> new RotatedPillarBlock(copy(PALM_LOG.get())), "stripped_palm_log");
    public static final RegistryObject<Block> PALM_WOOD = registerBlock(() -> new RotatedPillarBlock(copy(PALM_LOG.get())), "palm_wood");
    public static final RegistryObject<Block> STRIPPED_PALM_WOOD = registerBlock(() -> new RotatedPillarBlock(copy(PALM_LOG.get())), "stripped_palm_wood");
    public static final RegistryObject<Block> DEADWOOD_LOG = registerBlock(() -> new DeadwoodLogBlock().setCanBeStripped(), "deadwood_log");
    public static final RegistryObject<Block> STRIPPED_DEADWOOD_LOG = registerBlock(DeadwoodLogBlock::new, "stripped_deadwood_log");
    public static final RegistryObject<Block> DEADWOOD_WOOD = registerBlock(DeadwoodLogBlock::new, "deadwood_wood");
    public static final RegistryObject<Block> STRIPPED_DEADWOOD_WOOD = registerBlock(DeadwoodLogBlock::new, "stripped_deadwood_wood");
    public static final RegistryObject<Block> DEADWOOD_BRANCH = registerBlock(DeadwoodBranchBlock::new, null, "deadwood_branch");
    public static final RegistryObject<Block> PALM_STAIRS = registerBlock(() -> new StairBlock(() -> PALM_PLANKS.get().defaultBlockState(), copy(PALM_PLANKS.get())), "palm_stairs");
    public static final RegistryObject<Block> DEADWOOD_STAIRS = registerBlock(() -> new StairBlock(() -> DEADWOOD_PLANKS.get().defaultBlockState(), copy(DEADWOOD_PLANKS.get())), "deadwood_stairs");
    public static final RegistryObject<Block> PALM_SLAB = registerBlock(() -> new SlabBlock(copy(PALM_PLANKS.get())), "palm_slab");
    public static final RegistryObject<Block> DEADWOOD_SLAB = registerBlock(() -> new SlabBlock(copy(DEADWOOD_PLANKS.get())), "deadwood_slab");
    public static final RegistryObject<Block> PALM_SAPLING = registerBlock(PalmSaplingBlock::new, "palm_sapling");
    public static final RegistryObject<Block> POTTED_PALM_SAPLING = registerBaseBlock(() -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT.delegate.get(), PALM_SAPLING, BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()), "potted_palm_sapling");
    public static final RegistryObject<Block> PALM_LEAVES = registerBlock(PalmLeavesBlock::new, "palm_leaves");
    public static final RegistryObject<Block> DRY_LEAVES = registerBlock(LeavesAtumBlock::new, null, "dry_leaves");
    public static final RegistryObject<Block> PALM_CRATE = registerBlock(() -> new CrateBlock(copy(PALM_PLANKS.get())), "palm_crate");
    public static final RegistryObject<Block> DEADWOOD_CRATE = registerBlock(() -> new CrateBlock(copy(PALM_PLANKS.get())), "deadwood_crate");
    public static final RegistryObject<Block> PALM_LADDER = registerBlock(AtumLadderBlock::new, "palm_ladder");
    public static final RegistryObject<Block> DEADWOOD_LADDER = registerBlock(AtumLadderBlock::new, "deadwood_ladder");
    public static final RegistryObject<Block> PALM_FENCE = registerBlock(() -> new FenceBlock(copy(PALM_PLANKS.get())), "palm_fence");
    public static final RegistryObject<Block> DEADWOOD_FENCE = registerBlock(() -> new FenceBlock(copy(DEADWOOD_PLANKS.get())), "deadwood_fence");
    public static final RegistryObject<Block> PALM_FENCE_GATE = registerBlock(() -> new FenceGateBlock(copy(PALM_PLANKS.get())), "palm_fence_gate");
    public static final RegistryObject<Block> DEADWOOD_FENCE_GATE = registerBlock(() -> new FenceGateBlock(copy(DEADWOOD_PLANKS.get())), "deadwood_fence_gate");
    public static final RegistryObject<Block> PALM_HATCH = registerBlock(() -> new AtumTrapDoorBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(AtumBlocks::never)), "palm_hatch");
    public static final RegistryObject<Block> DEADWOOD_HATCH = registerBlock(() -> new AtumTrapDoorBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(AtumBlocks::never)), "deadwood_hatch");
    public static final RegistryObject<Block> PALM_DOOR = registerBlock(() -> new DoorAtumBlock(copy(PALM_PLANKS.get())), "palm_door");
    public static final RegistryObject<Block> DEADWOOD_DOOR = registerBlock(() -> new DoorAtumBlock(copy(DEADWOOD_PLANKS.get())), "deadwood_door");
    public static final RegistryObject<Block> LIMESTONE_BUTTON = registerBlock(() -> new StoneButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F)), "limestone_button");
    public static final RegistryObject<Block> PALM_BUTTON = registerBlock(() -> new WoodButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD)), "palm_button");
    public static final RegistryObject<Block> DEADWOOD_BUTTON = registerBlock(() -> new WoodButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD)), "deadwood_button");
    public static final RegistryObject<Block> LIMESTONE_PRESSURE_PLATE = registerBlock(() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().noCollission().strength(0.5F)), "limestone_pressure_plate");
    public static final RegistryObject<Block> PALM_PRESSURE_PLATE = registerBlock(() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of(Material.WOOD, PALM_PLANKS.get().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD)), "palm_pressure_plate");
    public static final RegistryObject<Block> DEADWOOD_PRESSURE_PLATE = registerBlock(() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of(Material.WOOD, DEADWOOD_PLANKS.get().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD)), "deadwood_pressure_plate");
    public static final RegistryObject<Block> PALM_SIGN = registerSign(() -> new AtumStandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD), Atum.PALM), Atum.PALM);
    public static final RegistryObject<Block> DEADWOOD_SIGN = registerSign(() -> new AtumStandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD), Atum.DEADWOOD), Atum.DEADWOOD);
    public static final RegistryObject<Block> PALM_SCAFFOLDING = registerScaffolding(() -> new AtumScaffoldingBlock(BlockBehaviour.Properties.of(Material.DECORATION, MaterialColor.SAND).noCollission().sound(SoundType.SCAFFOLDING).dynamicShape()), "palm_scaffolding");
    public static final RegistryObject<Block> DEADWOOD_SCAFFOLDING = registerScaffolding(() -> new AtumScaffoldingBlock(BlockBehaviour.Properties.of(Material.DECORATION, MaterialColor.SAND).noCollission().sound(SoundType.SCAFFOLDING).dynamicShape()), "deadwood_scaffolding");

    public static void setBlockInfo() {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(PALM_SAPLING.get().getRegistryName(), () -> POTTED_PALM_SAPLING.get());
        //Fire Info
        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFlammable(PALM_PLANKS.get(), 5, 20);
        fire.setFlammable(PALM_FENCE.get(), 5, 20);
        fire.setFlammable(PALM_FENCE_GATE.get(), 5, 20);
        fire.setFlammable(DEADWOOD_FENCE.get(), 5, 20);
        fire.setFlammable(DEADWOOD_FENCE_GATE.get(), 5, 20);
        fire.setFlammable(SHRUB.get(), 60, 100);
        fire.setFlammable(WEED.get(), 60, 100);
        fire.setFlammable(SPINNING_WHEEL.get(), 2, 1);
        fire.setFlammable(OPHIDIAN_TONGUE.get(), 15, 100);
        fire.setFlammable(PALM_PLANKS.get(), 5, 20);
        fire.setFlammable(DEADWOOD_PLANKS.get(), 5, 20);
        fire.setFlammable(PALM_LOG.get(), 5, 5);
        fire.setFlammable(DEADWOOD_LOG.get(), 5, 5);
        fire.setFlammable(PALM_SLAB.get(), 5, 20);
        fire.setFlammable(DEADWOOD_SLAB.get(), 5, 20);
        fire.setFlammable(PALM_LEAVES.get(), 30, 60);
        fire.setFlammable(DRY_LEAVES.get(), 30, 60);
        fire.setFlammable(DRY_GRASS.get(), 60, 10);
        fire.setFlammable(TALL_DRY_GRASS.get(), 60, 10);
        fire.setFlammable(PALM_STAIRS.get(), 5, 20);
        fire.setFlammable(DEADWOOD_STAIRS.get(), 5, 20);
        fire.setFlammable(EMMER_BLOCK.get(), 60, 20);
        fire.setFlammable(PALM_SCAFFOLDING.get(), 60, 60);
        fire.setFlammable(DEADWOOD_SCAFFOLDING.get(), 60, 60);
    }

    public static Supplier<Block> createStainedGlassFromColor(DyeColor color) {
        return () -> new StainedGlassBlock(color, BlockBehaviour.Properties.of(Material.GLASS, color).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(AtumBlocks::never).isRedstoneConductor(AtumBlocks::never).isSuffocating(AtumBlocks::never).isViewBlocking(AtumBlocks::never));
    }

    //Copied from vanilla Block class
    public static Boolean allowsSpawnOnLeaves(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
        return entity == EntityType.OCELOT || entity == EntityType.PARROT;
    }

    public static boolean always(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    public static boolean never(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }

    public static boolean always(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entityType) {
        return true;
    }

    public static boolean never(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entityType) {
        return false;
    }

    /**
     * Helper method for easily registering torches with unlit torches
     *
     * @param lit The lit torch block to be registered
     * @param name  The name to register the block with
     * @return The Block that was registered
     */
    public static <T extends Block> RegistryObject<T> registerTorchWithUnlit(@Nonnull Supplier<T> lit, @Nonnull String name) {
        Supplier<Block> unlitTorch = () -> new AtumTorchUnlitBlock(lit);
        Supplier<Block> wallTorchLit = () -> new AtumWallTorch(Block.Properties.copy(lit.get()).lootFrom(lit), ((AtumTorchBlock) lit.get()).getParticleType());
        Supplier<Block> wallTorchUnlit = () -> new AtumWallTorchUnlitBlock(wallTorchLit.get(), Block.Properties.copy(unlitTorch.get()).lootFrom(unlitTorch));
        registerBaseBlock(wallTorchLit, "wall_" + name);
        registerBaseBlock(wallTorchUnlit, "wall_" + name + "_unlit");
        registerBlockWithItem(unlitTorch, () -> new StandingAndWallBlockItem(unlitTorch.get(), wallTorchUnlit.get(), new Item.Properties()), name + "_unlit");
        AtumTorchUnlitBlock.ALL_TORCHES.add(lit);
        AtumTorchUnlitBlock.ALL_TORCHES.add(unlitTorch);
        AtumTorchUnlitBlock.ALL_TORCHES.add(wallTorchLit);
        AtumTorchUnlitBlock.ALL_TORCHES.add(wallTorchUnlit);
        return registerBlockWithItem(lit, () -> new StandingAndWallBlockItem(lit.get(), wallTorchLit.get(), new Item.Properties().tab(Atum.GROUP)), name);
    }

    public static <T extends Block> RegistryObject<T> registerTorch(@Nonnull Supplier<T> torch, @Nonnull String name) {
        Supplier<Block> wallTorchLit = () -> new AtumWallTorch(Block.Properties.copy(torch.get()).lootFrom(torch), ((AtumTorchBlock) torch.get()).getParticleType());
        registerBaseBlock(wallTorchLit, "wall_" + name);
        AtumTorchUnlitBlock.ALL_TORCHES.add(torch);
        AtumTorchUnlitBlock.ALL_TORCHES.add(wallTorchLit);
        return registerBlockWithItem(torch, () -> new StandingAndWallBlockItem(torch.get(), wallTorchLit.get(), new Item.Properties().tab(Atum.GROUP)), name);
    }

    /**
     * Helper method for easily registering scaffolding
     *
     * @param scaffolding The scaffolding block to be registered
     * @param name        The name to register the block with
     * @return The Block that was registered
     */
    public static <T extends Block> RegistryObject<T> registerScaffolding(@Nonnull Supplier<T> scaffolding, @Nonnull String name) {
        return registerBlockWithItem(scaffolding, () -> new AtumScaffoldingItem(scaffolding.get()), name);
    }

    /**
     * Allows for easy registering of signs, that handles Ground Sign, Wall Sign and Item sign registration
     */
    public static <T extends Block> RegistryObject<T> registerSign(@Nonnull Supplier<T> signBlock, @Nonnull WoodType woodType) {
        String typeName = woodType.name().replace("atum_", "");
        RegistryObject<Block> wallSignBlock = registerBaseBlock(() -> new AtumWallSignBlock(of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD).lootFrom(signBlock), woodType), typeName + "_wall_sign");
        AtumItems.registerItem(() -> new SignItem((new Item.Properties()).stacksTo(16).tab(Atum.GROUP), signBlock.get(), wallSignBlock.get()), typeName + "_sign");
        RegistryObject<Block> signBlockObject = registerBaseBlock((Supplier<Block>) signBlock, typeName + "_sign");
        AtumWallSignBlock.WALL_SIGN_BLOCKS.put(signBlockObject, wallSignBlock);
        return (RegistryObject<T>) signBlockObject;
    }

    /**
     * Same as {@link AtumBlocks#registerBlock(Supplier, Item.Properties, String)}, but have an empty Item.Properties set
     */
    public static <T extends Block> RegistryObject<T> registerBlock(@Nonnull Supplier<T> block, @Nonnull String name) {
        return registerBlock(block, new Item.Properties(), name);
    }

    /**
     * Same as {@link AtumBlocks#registerBlock(Supplier, Item.Properties, String)} and have an easy way to set a ISTER
     *
     * @param properties BlockItem properties, can be set to null to not have any ItemGroup
     */
    public static <T extends Block> RegistryObject<T> registerWithRenderer(@Nonnull Supplier<T> block, @Nullable Item.Properties properties, @Nonnull String name) {
        return registerBlockWithItem(block, () -> new BlockItemWithoutLevelRenderer(block.get(), properties == null ? new Item.Properties() : properties.tab(Atum.GROUP)), name);
    }

    /**
     * Same as {@link AtumBlocks#registerBlockWithItem(Supplier, Supplier, String)}, but have a basic BlockItem
     *
     * @param properties BlockItem properties, can be set to null to not have any ItemGroup
     */
    public static <T extends Block> RegistryObject<T> registerBlock(@Nonnull Supplier<T> block, @Nullable Item.Properties properties, @Nonnull String name) {
        return registerBlockWithItem(block, () -> new BlockItem(block.get(), properties == null ? new Item.Properties() : properties.tab(Atum.GROUP)), name);
    }

    /**
     * Same as {@link AtumBlocks#registerBaseBlock(Supplier, String)}, but allows for registering an BlockItem at the same time
     */
    public static <T extends Block> RegistryObject<T> registerBlockWithItem(@Nonnull Supplier<T> block, Supplier<Item> blockItem, @Nonnull String name) {
        AtumItems.registerItem(blockItem, name);
        return registerBaseBlock(block, name);
    }

    /**
     * Registers a block
     *
     * @param block The block to be registered
     * @param name  The name to register the block with
     * @return The Block that was registered
     */
    public static <T extends Block> RegistryObject<T> registerBaseBlock(@Nonnull Supplier<T> block, @Nonnull String name) {
        return BLOCK_DEFERRED.register(name, block);
    }
}