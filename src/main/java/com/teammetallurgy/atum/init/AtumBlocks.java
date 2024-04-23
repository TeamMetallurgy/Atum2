package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.*;
import com.teammetallurgy.atum.blocks.base.AtumGlassBlock;
import com.teammetallurgy.atum.blocks.base.AtumPaneBlock;
import com.teammetallurgy.atum.blocks.base.DoorAtumBlock;
import com.teammetallurgy.atum.blocks.curio.CurioDisplayBlock;
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
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy;

public class AtumBlocks {
    public static final DeferredRegister.Blocks BLOCK_DEFERRED = DeferredRegister.createBlocks(Atum.MOD_ID);
    public static final DeferredBlock<Block> PORTAL = registerBlock(PortalBlock::new, null, "portal");
    public static final DeferredBlock<Block> STRANGE_SAND = registerBlock(() -> new StrangeSandBlock(Block.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND).randomTicks()), "strange_sand");
    public static final DeferredBlock<Block> STRANGE_SAND_LAYERED = registerBlock(() -> new SandLayersBlock(Block.Properties.of().mapColor(MapColor.SAND).replaceable().noCollission().forceSolidOff().strength(0.1F).sound(SoundType.SAND).pushReaction(PushReaction.DESTROY)), "strange_sand_layer");
    public static final DeferredBlock<Block> LIMESTONE_GRAVEL = registerBlock(LimestoneGravelBlock::new, "limestone_gravel");
    public static final DeferredBlock<Block> DATE_BLOCK = registerBlock(() -> new DateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS).strength(0.35F).noOcclusion().randomTicks()), null, "date_block");
    public static final DeferredBlock<Block> EMMER_WHEAT = registerBlock(EmmerBlock::new, null, "emmer_wheat");
    public static final DeferredBlock<Block> EMMER_BLOCK = registerBlock(() -> new HayBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.GRASS)), "emmer_block");
    public static final DeferredBlock<Block> ANPUTS_FINGERS = registerBlock(AnputsFingersBlock::new, null, "anputs_fingers");
    public static final DeferredBlock<Block> OASIS_GRASS = registerBlock(() -> new OasisGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY).noCollission().strength(0.0F).sound(SoundType.GRASS).offsetType(Block.OffsetType.XYZ)), "oasis_grass");
    public static final DeferredBlock<Block> DRY_GRASS = registerBlock(() -> new DryGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY).noCollission().strength(0.0F).sound(SoundType.GRASS).offsetType(Block.OffsetType.XYZ)), "dry_grass");
    public static final DeferredBlock<Block> TALL_DRY_GRASS = registerBlock(TallDryGrass::new, "tall_dry_grass");
    public static final DeferredBlock<Block> SHRUB = registerBlock(ShrubBlock::new, "shrub");
    public static final DeferredBlock<Block> WEED = registerBlock(ShrubBlock::new, "weed");
    public static final DeferredBlock<Block> PAPYRUS = registerBlock(PapyrusBlock::new, null, "papyrus");
    public static final DeferredBlock<Block> OPHIDIAN_TONGUE = registerBlock(OphidianTongueBlock::new, "ophidian_tongue");
    public static final DeferredBlock<Block> FLAX = registerBlock(FlaxBlock::new, null, "flax_block");
    public static final DeferredBlock<Block> FERTILE_SOIL = registerBlock(FertileSoilBlock::new, "fertile_soil");
    public static final DeferredBlock<Block> FERTILE_SOIL_TILLED = registerBlock(FertileSoilTilledBlock::new, "fertile_soil_tilled");
    public static final DeferredBlock<Block> FERTILE_SOIL_PATH = registerBlock(() -> new AtumPathBlock(FERTILE_SOIL.get(), MapColor.DIRT), "fertile_soil_path");
    public static final DeferredBlock<Block> STRANGE_SAND_PATH = registerBlock(() -> new AtumPathBlock(STRANGE_SAND.get(), MapColor.SAND), "strange_sand_path");
    public static final DeferredBlock<Block> QUERN = registerBlock(() -> new QuernBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F)), "quern");
    public static final DeferredBlock<Block> SPINNING_WHEEL = registerBlock(() -> new SpinningWheelBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.2F)), "spinning_wheel");
    public static final DeferredBlock<Block> KILN = registerBlock(() -> new KilnBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).strength(3.5F).lightLevel(s -> s.getValue(BlockStateProperties.LIT) ? 13 : 0).sound(SoundType.STONE)), "kiln");
    public static final DeferredBlock<Block> KILN_FAKE = registerBlock(() -> new KilnFakeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 10.0F).sound(SoundType.STONE)), null, "kiln_fake");
    public static final DeferredBlock<Block> GODFORGE = registerBlock(() -> new GodforgeBlock((BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.5F).lightLevel((state) -> state.getValue(GodforgeBlock.LIT) ? 13 : 0))), "godforge");
    public static final DeferredBlock<Block> QUANDARY_BLOCK = registerBlock(QuandaryBlock::new, "quandary_block");
    public static final DeferredBlock<Block> GLASSBLOWER_FURNACE = registerBlock(() -> new GlassblowerFurnace(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.5F).lightLevel(s -> s.getValue(BlockStateProperties.LIT) ? 13 : 0)), "glassblower_furnace");
    public static final DeferredBlock<Block> PALM_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.5F, 1.0F).sound(SoundType.GLASS)) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return AtumTileEntities.PALM_CURIO_DISPLAY.get().create(pos, state);
        }
    }, new Item.Properties(), "palm_curio_display");
    public static final DeferredBlock<Block> DEADWOOD_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(BlockBehaviour.Properties.ofFullCopy(PALM_CURIO_DISPLAY.get())) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return AtumTileEntities.DEADWOOD_CURIO_DISPLAY.get().create(pos, state);
        }
    }, new Item.Properties(), "deadwood_curio_display");
    public static final DeferredBlock<Block> ACACIA_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(BlockBehaviour.Properties.ofFullCopy(PALM_CURIO_DISPLAY.get())) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return AtumTileEntities.ACACIA_CURIO_DISPLAY.get().create(pos, state);
        }
    }, new Item.Properties(), "acacia_curio_display");
    public static final DeferredBlock<Block> LIMESTONE_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 1.0F).sound(SoundType.GLASS)) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return AtumTileEntities.LIMESTONE_CURIO_DISPLAY.get().create(pos, state);
        }
    }, new Item.Properties(), "limestone_curio_display");
    public static final DeferredBlock<Block> ALABASTER_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(BlockBehaviour.Properties.ofFullCopy(LIMESTONE_CURIO_DISPLAY.get())) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return AtumTileEntities.ALABASTER_CURIO_DISPLAY.get().create(pos, state);
        }
    }, new Item.Properties(), "alabaster_curio_display");
    public static final DeferredBlock<Block> PORPHYRY_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(BlockBehaviour.Properties.ofFullCopy(LIMESTONE_CURIO_DISPLAY.get())) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return AtumTileEntities.PORPHYRY_CURIO_DISPLAY.get().create(pos, state);
        }
    }, new Item.Properties(), "porphyry_curio_display");
    public static final DeferredBlock<Block> NEBU_CURIO_DISPLAY = registerWithRenderer(() -> new CurioDisplayBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(1.5F, 1.0F).sound(SoundType.GLASS)) {
        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return AtumTileEntities.NEBU_CURIO_DISPLAY.get().create(pos, state);
        }
    }, new Item.Properties(), "nebu_curio_display");
    public static final DeferredBlock<Block> BURNING_TRAP = registerBlock(() -> new BurningTrapBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F)), "burning_trap");
    public static final DeferredBlock<Block> POISON_TRAP = registerBlock(() -> new PoisonTrapBlock(ofFullCopy(BURNING_TRAP.get())), "poison_trap");
    public static final DeferredBlock<Block> TAR_TRAP = registerBlock(() -> new TarTrapBlock(ofFullCopy(BURNING_TRAP.get())), "tar_trap");
    public static final DeferredBlock<Block> SMOKE_TRAP = registerBlock(() -> new SmokeTrapBlock(ofFullCopy(BURNING_TRAP.get())), "smoke_trap");
    public static final DeferredBlock<Block> ARROW_TRAP = registerBlock(() -> new ArrowTrapBlock(ofFullCopy(BURNING_TRAP.get())), "arrow_trap");
    public static final DeferredBlock<Block> SARCOPHAGUS = registerWithRenderer(SarcophagusBlock::new, new Item.Properties(), "sarcophagus");
    public static final DeferredBlock<Block> LIMESTONE_CHEST = registerWithRenderer(LimestoneChestBlock::new, new Item.Properties(), "limestone_chest");
    public static final DeferredBlock<Block> GOLD_ORE = registerBlock(() -> new DropExperienceBlock(ConstantInt.of(0), ofFullCopy(Blocks.GOLD_ORE)), "gold_ore");
    public static final DeferredBlock<Block> IRON_ORE = registerBlock(() -> new DropExperienceBlock(ConstantInt.of(0), ofFullCopy(Blocks.IRON_ORE)), "iron_ore");
    public static final DeferredBlock<Block> COAL_ORE = registerBlock(() -> new DropExperienceBlock(UniformInt.of(0, 2), ofFullCopy(Blocks.COAL_ORE)), "coal_ore");
    public static final DeferredBlock<Block> LAPIS_ORE = registerBlock(() -> new DropExperienceBlock(UniformInt.of(2, 5), ofFullCopy(Blocks.LAPIS_ORE)), "lapis_ore");
    public static final DeferredBlock<Block> DIAMOND_ORE = registerBlock(() -> new DropExperienceBlock(UniformInt.of(3, 7), ofFullCopy(Blocks.DIAMOND_ORE)), "diamond_ore");
    public static final DeferredBlock<Block> EMERALD_ORE = registerBlock(() -> new DropExperienceBlock(UniformInt.of(3, 7), ofFullCopy(Blocks.EMERALD_ORE)), "emerald_ore");
    public static final DeferredBlock<Block> REDSTONE_ORE = registerBlock(() -> new RedStoneOreBlock(ofFullCopy(Blocks.REDSTONE_ORE)), "redstone_ore");
    public static final DeferredBlock<Block> KHNUMITE_RAW = registerBlock(() -> new Block(of().mapColor(MapColor.CLAY).strength(0.6F).sound(SoundType.GRAVEL)), "khnumite_raw");
    public static final DeferredBlock<Block> BONE_ORE = registerBlock(() -> new DropExperienceBlock(UniformInt.of(0, 2), of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F)), "bone_ore");
    public static final DeferredBlock<Block> RELIC_ORE = registerBlock(() -> new DropExperienceBlock(UniformInt.of(0, 2), of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)), "relic_ore");
    public static final DeferredBlock<Block> NEBU_ORE = registerBlock(() -> new DropExperienceBlock(UniformInt.of(2, 6), of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)), "nebu_ore");
    public static final DeferredBlock<Block> NEBU_BLOCK = registerBlock(() -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)), "nebu_block");
    public static final DeferredBlock<Block> GODFORGED_BLOCK = registerBlock(GodforgedBlock::new, "godforged_block");
    public static final DeferredBlock<Block> ANPUT_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.ANPUT), "anput_godforged_block");
    public static final DeferredBlock<Block> ANUBIS_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.ANUBIS), "anubis_godforged_block");
    public static final DeferredBlock<Block> ATEM_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.ATEM), "atem_godforged_block");
    public static final DeferredBlock<Block> GEB_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.GEB), "geb_godforged_block");
    public static final DeferredBlock<Block> HORUS_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.HORUS), "horus_godforged_block");
    public static final DeferredBlock<Block> ISIS_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.ISIS), "isis_godforged_block");
    public static final DeferredBlock<Block> MONTU_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.MONTU), "montu_godforged_block");
    public static final DeferredBlock<Block> NEPTHYS_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.NEPTHYS), "nepthys_godforged_block");
    public static final DeferredBlock<Block> NUIT_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.NUIT), "nuit_godforged_block");
    public static final DeferredBlock<Block> OSIRIS_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.OSIRIS), "osiris_godforged_block");
    public static final DeferredBlock<Block> PTAH_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.PTAH), "ptah_godforged_block");
    public static final DeferredBlock<Block> RA_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.RA), "ra_godforged_block");
    public static final DeferredBlock<Block> SETH_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.SETH), "seth_godforged_block");
    public static final DeferredBlock<Block> SHU_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.SHU), "shu_godforged_block");
    public static final DeferredBlock<Block> TEFNUT_GODFORGED_BLOCK = registerBlock(() -> new GodGodforgedBlock(God.TEFNUT), "tefnut_godforged_block");
    public static final DeferredBlock<Block> DIRTY_BONE = registerBlock(() -> new RotatedPillarBlock(of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F)), "dirty_bone_block");
    public static final DeferredBlock<Block> DIRTY_BONE_SLAB = registerBlock(() -> new SlabBlock(of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F)), "dirty_bone_slab");
    public static final DeferredBlock<Block> BONE_LADDER = registerBlock(AtumLadderBlock::new, "bone_ladder");
    public static final DeferredBlock<Block> LIMESTONE_FURNACE = registerBlock(LimestoneFurnaceBlock::new, "limestone_furnace");
    public static final DeferredBlock<Block> PALM_TORCH = registerTorchWithUnlit(() -> new AtumTorchBlock(14), "palm_torch");
    public static final DeferredBlock<Block> DEADWOOD_TORCH = registerTorchWithUnlit(() -> new AtumTorchBlock(14), "deadwood_torch");
    public static final DeferredBlock<Block> LIMESTONE_TORCH = registerTorchWithUnlit(() -> new AtumTorchBlock(14), "limestone_torch");
    public static final DeferredBlock<Block> BONE_TORCH = registerTorchWithUnlit(() -> new AtumTorchBlock(14), "bone_torch");
    public static final DeferredBlock<Block> NEBU_TORCH = registerTorchWithUnlit(() -> new AtumTorchBlock(null), "nebu_torch");
    public static final DeferredBlock<Block> TORCH_OF_ANPUT = registerTorch(() -> new AtumTorchBlock(God.ANPUT), "torch_of_anput");
    public static final DeferredBlock<Block> TORCH_OF_ANUBIS = registerTorch(() -> new AtumTorchBlock(God.ANUBIS), "torch_of_anubis");
    public static final DeferredBlock<Block> TORCH_OF_ATEM = registerTorch(() -> new AtumTorchBlock(God.ATEM), "torch_of_atem");
    public static final DeferredBlock<Block> TORCH_OF_GEB = registerTorch(() -> new AtumTorchBlock(God.GEB), "torch_of_geb");
    public static final DeferredBlock<Block> TORCH_OF_HORUS = registerTorch(() -> new AtumTorchBlock(God.HORUS), "torch_of_horus");
    public static final DeferredBlock<Block> TORCH_OF_ISIS = registerTorch(() -> new AtumTorchBlock(God.ISIS), "torch_of_isis");
    public static final DeferredBlock<Block> TORCH_OF_MONTU = registerTorch(() -> new AtumTorchBlock(God.MONTU), "torch_of_montu");
    public static final DeferredBlock<Block> TORCH_OF_NEPTHYS = registerTorch(() -> new AtumTorchBlock(God.NEPTHYS), "torch_of_nepthys");
    public static final DeferredBlock<Block> TORCH_OF_NUIT = registerTorch(() -> new AtumTorchBlock(God.NUIT), "torch_of_nuit");
    public static final DeferredBlock<Block> TORCH_OF_OSIRIS = registerTorch(() -> new AtumTorchBlock(God.OSIRIS), "torch_of_osiris");
    public static final DeferredBlock<Block> TORCH_OF_PTAH = registerTorch(() -> new AtumTorchBlock(God.PTAH), "torch_of_ptah");
    public static final DeferredBlock<Block> TORCH_OF_RA = registerTorch(() -> new AtumTorchBlock(God.RA), "torch_of_ra");
    public static final DeferredBlock<Block> TORCH_OF_SETH = registerTorch(() -> new AtumTorchBlock(God.SETH), "torch_of_seth");
    public static final DeferredBlock<Block> TORCH_OF_SHU = registerTorch(() -> new AtumTorchBlock(God.SHU), "torch_of_shu");
    public static final DeferredBlock<Block> TORCH_OF_TEFNUT = registerTorch(() -> new AtumTorchBlock(God.TEFNUT), "torch_of_tefnut");
    public static final DeferredBlock<Block> NEBU_LANTERN = registerBlock(AtumLanternBlock::new, "nebu_lantern");
    public static final DeferredBlock<Block> LANTERN_OF_ANPUT = registerBlock(AtumLanternBlock::new, "lantern_of_anput");
    public static final DeferredBlock<Block> LANTERN_OF_ANUBIS = registerBlock(AtumLanternBlock::new, "lantern_of_anubis");
    public static final DeferredBlock<Block> LANTERN_OF_ATEM = registerBlock(AtumLanternBlock::new, "lantern_of_atem");
    public static final DeferredBlock<Block> LANTERN_OF_GEB = registerBlock(AtumLanternBlock::new, "lantern_of_geb");
    public static final DeferredBlock<Block> LANTERN_OF_HORUS = registerBlock(AtumLanternBlock::new, "lantern_of_horus");
    public static final DeferredBlock<Block> LANTERN_OF_ISIS = registerBlock(AtumLanternBlock::new, "lantern_of_isis");
    public static final DeferredBlock<Block> LANTERN_OF_MONTU = registerBlock(AtumLanternBlock::new, "lantern_of_montu");
    public static final DeferredBlock<Block> LANTERN_OF_NEPTHYS = registerBlock(AtumLanternBlock::new, "lantern_of_nepthys");
    public static final DeferredBlock<Block> LANTERN_OF_NUIT = registerBlock(AtumLanternBlock::new, "lantern_of_nuit");
    public static final DeferredBlock<Block> LANTERN_OF_OSIRIS = registerBlock(AtumLanternBlock::new, "lantern_of_osiris");
    public static final DeferredBlock<Block> LANTERN_OF_PTAH = registerBlock(AtumLanternBlock::new, "lantern_of_ptah");
    public static final DeferredBlock<Block> LANTERN_OF_RA = registerBlock(AtumLanternBlock::new, "lantern_of_ra");
    public static final DeferredBlock<Block> LANTERN_OF_SETH = registerBlock(AtumLanternBlock::new, "lantern_of_seth");
    public static final DeferredBlock<Block> LANTERN_OF_SHU = registerBlock(AtumLanternBlock::new, "lantern_of_shu");
    public static final DeferredBlock<Block> LANTERN_OF_TEFNUT = registerBlock(AtumLanternBlock::new, "lantern_of_tefnut");
    public static final DeferredBlock<Block> NEBU_CHAIN = registerBlock(() -> new ChainBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.CHAIN).noOcclusion()), "nebu_chain");
    public static final DeferredBlock<Block> MARL = registerBlock(() -> new Block(of().mapColor(MapColor.CLAY).strength(0.6F).sound(SoundType.GRAVEL)), "marl");
    public static final DeferredBlock<Block> RA_STONE = registerBlock(RaStoneBlock::new, null, "ra_stone");
    public static final DeferredBlock<Block> LIMESTONE = registerBlock(LimestoneBlock::new, "limestone");
    public static final DeferredBlock<Block> LIMESTONE_CRACKED = registerBlock(() -> new Block(of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 10.0F)), "limestone_cracked");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_SMALL = registerBlock(() -> new LimestoneBrickBlock(Block.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 8.0F).requiresCorrectToolForDrops()), "limestone_brick_small");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_LARGE = registerBlock(() -> new LimestoneBrickBlock(ofFullCopy(LIMESTONE_BRICK_SMALL.get())), "limestone_brick_large");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_CRACKED_BRICK = registerBlock(() -> new LimestoneBrickBlock(ofFullCopy(LIMESTONE_BRICK_SMALL.get())), "limestone_brick_cracked_brick");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_CHISELED = registerBlock(() -> new LimestoneBrickBlock(ofFullCopy(LIMESTONE_BRICK_SMALL.get())), "limestone_brick_chiseled");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_CARVED = registerBlock(() -> new LimestoneBrickBlock(ofFullCopy(LIMESTONE_BRICK_SMALL.get())), "limestone_brick_carved");
    public static final DeferredBlock<Block> LIMESTONE_SLAB = registerBlock(() -> new SlabBlock(of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 6.0F)), "limestone_slab");
    public static final DeferredBlock<Block> LIMESTONE_CRACKED_SLAB = registerBlock(() -> new SlabBlock(of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 6.0F)), "limestone_cracked_slab");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_SMALL_SLAB = registerBlock(() -> new SlabBlock(of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 6.0F)), "limestone_small_slab");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_LARGE_SLAB = registerBlock(() -> new SlabBlock(of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 6.0F)), "limestone_large_slab");
    public static final DeferredBlock<Block> LIMESTONE_CRACKED_BRICK_SLAB = registerBlock(() -> new SlabBlock(of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 6.0F)), "limestone_cracked_brick_slab");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_CHISELED_SLAB = registerBlock(() -> new SlabBlock(of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 6.0F)), "limestone_chiseled_slab");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_CARVED_SLAB = registerBlock(() -> new SlabBlock(of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 6.0F)), "limestone_carved_slab");
    public static final DeferredBlock<Block> KHNUMITE_BLOCK = registerBlock(KhnumiteBlock::new, "khnumite_block");
    public static final DeferredBlock<Block> KHNUMITE_FACE = registerBlock(() -> new KhnumiteFaceBlock(Block.Properties.of().mapColor(MapColor.CLAY).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F)), "khnumite_face");
    public static final DeferredBlock<Block> SMOOTH_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE.get().defaultBlockState(), ofFullCopy(LIMESTONE.get())), "smooth_stairs");
    public static final DeferredBlock<Block> CRACKED_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_CRACKED.get().defaultBlockState(), ofFullCopy(LIMESTONE_CRACKED.get())), "cracked_stairs");
    public static final DeferredBlock<Block> SMALL_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_BRICK_SMALL.get().defaultBlockState(), ofFullCopy(LIMESTONE_BRICK_SMALL.get())), "small_stairs");
    public static final DeferredBlock<Block> LARGE_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_BRICK_LARGE.get().defaultBlockState(), ofFullCopy(LIMESTONE_BRICK_LARGE.get())), "large_stairs");
    public static final DeferredBlock<Block> CRACKED_BRICK_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_BRICK_CRACKED_BRICK.get().defaultBlockState(), ofFullCopy(LIMESTONE_BRICK_CRACKED_BRICK.get())), "cracked_brick_stairs");
    public static final DeferredBlock<Block> CHISELED_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_BRICK_CHISELED.get().defaultBlockState(), ofFullCopy(LIMESTONE_BRICK_CHISELED.get())), "chiseled_stairs");
    public static final DeferredBlock<Block> CARVED_STAIRS = registerBlock(() -> new StairBlock(() -> LIMESTONE_BRICK_CARVED.get().defaultBlockState(), ofFullCopy(LIMESTONE_BRICK_CARVED.get())), "carved_stairs");
    public static final DeferredBlock<Block> LIMESTONE_WALL = registerBlock(() -> new WallBlock(ofFullCopy(LIMESTONE.get())), "limestone_wall");
    public static final DeferredBlock<Block> LIMESTONE_CRACKED_WALL = registerBlock(() -> new WallBlock(ofFullCopy(LIMESTONE_CRACKED.get())), "limestone_cracked_wall");
    public static final DeferredBlock<Block> SMALL_WALL = registerBlock(() -> new WallBlock(ofFullCopy(LIMESTONE_BRICK_SMALL.get())), "small_wall");
    public static final DeferredBlock<Block> LARGE_WALL = registerBlock(() -> new WallBlock(ofFullCopy(LIMESTONE_BRICK_LARGE.get())), "large_wall");
    public static final DeferredBlock<Block> CRACKED_BRICK_WALL = registerBlock(() -> new WallBlock(ofFullCopy(LIMESTONE_BRICK_CRACKED_BRICK.get())), "cracked_brick_wall");
    public static final DeferredBlock<Block> CHISELED_WALL = registerBlock(() -> new WallBlock(ofFullCopy(LIMESTONE_BRICK_CHISELED.get())), "chiseled_wall");
    public static final DeferredBlock<Block> CARVED_WALL = registerBlock(() -> new WallBlock(ofFullCopy(LIMESTONE_BRICK_CARVED.get())), "carved_wall");
    public static final DeferredBlock<Block> LIMESTONE_DOOR = registerBlock(() -> new DoorAtumBlock(ofFullCopy(LIMESTONE.get()), AtumBlockSetType.LIMESTONE), "limestone_door");
    public static final DeferredBlock<Block> LIMESTONE_CRACKED_DOOR = registerBlock(() -> new DoorAtumBlock(ofFullCopy(LIMESTONE_CRACKED.get()), AtumBlockSetType.LIMESTONE), "limestone_cracked_door");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_SMALL_DOOR = registerBlock(() -> new DoorAtumBlock(ofFullCopy(LIMESTONE_BRICK_SMALL.get()), AtumBlockSetType.LIMESTONE), "limestone_brick_small_door");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_LARGE_DOOR = registerBlock(() -> new DoorAtumBlock(ofFullCopy(LIMESTONE_BRICK_LARGE.get()), AtumBlockSetType.LIMESTONE), "limestone_brick_large_door");
    public static final DeferredBlock<Block> LIMESTONE_CRACKED_BRICK_DOOR = registerBlock(() -> new DoorAtumBlock(ofFullCopy(LIMESTONE_BRICK_CRACKED_BRICK.get()), AtumBlockSetType.LIMESTONE), "limestone_brick_cracked_brick_door");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_CHISELED_DOOR = registerBlock(() -> new DoorAtumBlock(ofFullCopy(LIMESTONE_BRICK_CHISELED.get()), AtumBlockSetType.LIMESTONE), "limestone_brick_chiseled_door");
    public static final DeferredBlock<Block> LIMESTONE_BRICK_CARVED_DOOR = registerBlock(() -> new DoorAtumBlock(ofFullCopy(LIMESTONE_BRICK_CARVED.get()), AtumBlockSetType.LIMESTONE), "limestone_brick_carved_door");
    public static final DeferredBlock<Block> KARST = registerBlock(() -> new Block(ofFullCopy(LIMESTONE.get()).strength(2.0F, 6.0F)), "karst");
    public static final DeferredBlock<Block> ALABASTER = registerBlock(() -> new Block(of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 8.0F)), "alabaster");
    public static final DeferredBlock<Block> ALABASTER_BRICK_SMOOTH = registerBlock(() -> new Block(ofFullCopy(ALABASTER.get())), "alabaster_brick_smooth");
    public static final DeferredBlock<Block> ALABASTER_BRICK_POLISHED = registerBlock(() -> new Block(ofFullCopy(ALABASTER.get())), "alabaster_brick_polished");
    public static final DeferredBlock<Block> ALABASTER_BRICK_CARVED = registerBlock(() -> new Block(ofFullCopy(ALABASTER.get())), "alabaster_brick_carved");
    public static final DeferredBlock<Block> ALABASTER_BRICK_TILED = registerBlock(() -> new Block(ofFullCopy(ALABASTER.get())), "alabaster_brick_tiled");
    public static final DeferredBlock<Block> ALABASTER_BRICK_PILLAR = registerBlock(() -> new Block(ofFullCopy(ALABASTER.get())), "alabaster_brick_pillar");
    public static final DeferredBlock<Block> ALABASTER_BRICK_SMOOTH_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(ALABASTER.get())), "alabaster_smooth_slab");
    public static final DeferredBlock<Block> ALABASTER_BRICK_POLISHED_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(ALABASTER.get())), "alabaster_polished_slab");
    public static final DeferredBlock<Block> ALABASTER_BRICK_CARVED_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(ALABASTER.get())), "alabaster_carved_slab");
    public static final DeferredBlock<Block> ALABASTER_BRICK_TILED_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(ALABASTER.get())), "alabaster_tiled_slab");
    public static final DeferredBlock<Block> ALABASTER_BRICK_PILLAR_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(ALABASTER.get())), "alabaster_pillar_slab");
    public static final DeferredBlock<Block> ALABASTER_BRICK_SMOOTH_STAIRS = registerBlock(() -> new StairBlock(() -> ALABASTER_BRICK_SMOOTH.get().defaultBlockState(), ofFullCopy(ALABASTER.get())), "alabaster_smooth_stairs");
    public static final DeferredBlock<Block> ALABASTER_BRICK_POLISHED_STAIRS = registerBlock(() -> new StairBlock(() -> ALABASTER_BRICK_POLISHED.get().defaultBlockState(), ofFullCopy(ALABASTER.get())), "alabaster_polished_stairs");
    public static final DeferredBlock<Block> ALABASTER_BRICK_CARVED_STAIRS = registerBlock(() -> new StairBlock(() -> ALABASTER_BRICK_CARVED.get().defaultBlockState(), ofFullCopy(ALABASTER.get())), "alabaster_carved_stairs");
    public static final DeferredBlock<Block> ALABASTER_BRICK_TILED_STAIRS = registerBlock(() -> new StairBlock(() -> ALABASTER_BRICK_TILED.get().defaultBlockState(), ofFullCopy(ALABASTER.get())), "alabaster_tiled_stairs");
    public static final DeferredBlock<Block> ALABASTER_BRICK_PILLAR_STARS = registerBlock(() -> new StairBlock(() -> ALABASTER_BRICK_PILLAR.get().defaultBlockState(), ofFullCopy(ALABASTER.get())), "alabaster_pillar_stairs");
    public static final DeferredBlock<Block> ALABASTER_BRICK_SMOOTH_WALL = registerBlock(() -> new WallBlock(ofFullCopy(ALABASTER.get())), "alabaster_smooth_wall");
    public static final DeferredBlock<Block> ALABASTER_BRICK_POLISHED_WALL = registerBlock(() -> new WallBlock(ofFullCopy(ALABASTER.get())), "alabaster_polished_wall");
    public static final DeferredBlock<Block> ALABASTER_BRICK_CARVED_WALL = registerBlock(() -> new WallBlock(ofFullCopy(ALABASTER.get())), "alabaster_carved_wall");
    public static final DeferredBlock<Block> ALABASTER_BRICK_TILED_WALL = registerBlock(() -> new WallBlock(ofFullCopy(ALABASTER.get())), "alabaster_tiled_wall");
    public static final DeferredBlock<Block> ALABASTER_BRICK_PILLAR_WALL = registerBlock(() -> new WallBlock(ofFullCopy(ALABASTER.get())), "alabaster_pillar_wall");
    public static final DeferredBlock<Block> PORPHYRY = registerBlock(() -> new Block(of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 5.0F).sound(SoundType.STONE)), "porphyry");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_SMOOTH = registerBlock(() -> new Block(ofFullCopy(PORPHYRY.get())), "porphyry_brick_smooth");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_POLISHED = registerBlock(() -> new Block(ofFullCopy(PORPHYRY.get())), "porphyry_brick_polished");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_CARVED = registerBlock(() -> new Block(ofFullCopy(PORPHYRY.get())), "porphyry_brick_carved");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_TILED = registerBlock(() -> new Block(ofFullCopy(PORPHYRY.get())), "porphyry_brick_tiled");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_PILLAR = registerBlock(() -> new Block(ofFullCopy(PORPHYRY.get())), "porphyry_brick_pillar");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_SMOOTH_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(PORPHYRY.get())), "porphyry_smooth_slab");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_POLISHED_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(PORPHYRY.get())), "porphyry_polished_slab");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_CARVED_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(PORPHYRY.get())), "porphyry_carved_slab");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_TILED_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(PORPHYRY.get())), "porphyry_tiled_slab");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_PILLAR_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(PORPHYRY.get())), "porphyry_pillar_slab");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_SMOOTH_STAIRS = registerBlock(() -> new StairBlock(() -> PORPHYRY_BRICK_SMOOTH.get().defaultBlockState(), ofFullCopy(PORPHYRY.get())), "porphyry_smooth_stairs");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_POLISHED_STAIRS = registerBlock(() -> new StairBlock(() -> PORPHYRY_BRICK_POLISHED.get().defaultBlockState(), ofFullCopy(PORPHYRY.get())), "porphyry_polished_stairs");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_CARVED_STAIRS = registerBlock(() -> new StairBlock(() -> PORPHYRY_BRICK_CARVED.get().defaultBlockState(), ofFullCopy(PORPHYRY.get())), "porphyry_carved_stairs");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_TILED_STAIRS = registerBlock(() -> new StairBlock(() -> PORPHYRY_BRICK_TILED.get().defaultBlockState(), ofFullCopy(PORPHYRY.get())), "porphyry_tiled_stairs");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_PILLAR_STARS = registerBlock(() -> new StairBlock(() -> PORPHYRY_BRICK_PILLAR.get().defaultBlockState(), ofFullCopy(PORPHYRY.get())), "porphyry_pillar_stairs");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_SMOOTH_WALL = registerBlock(() -> new WallBlock(ofFullCopy(PORPHYRY.get())), "porphyry_smooth_wall");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_POLISHED_WALL = registerBlock(() -> new WallBlock(ofFullCopy(PORPHYRY.get())), "porphyry_polished_wall");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_CARVED_WALL = registerBlock(() -> new WallBlock(ofFullCopy(PORPHYRY.get())), "porphyry_carved_wall");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_TILED_WALL = registerBlock(() -> new WallBlock(ofFullCopy(PORPHYRY.get())), "porphyry_tiled_wall");
    public static final DeferredBlock<Block> PORPHYRY_BRICK_PILLAR_WALL = registerBlock(() -> new WallBlock(ofFullCopy(PORPHYRY.get())), "porphyry_pillar_wall");
    public static final DeferredBlock<Block> CERAMIC_WHITE = registerBlock(() -> new CeramicBlock(DyeColor.WHITE), "ceramic_white");
    public static final DeferredBlock<Block> CERAMIC_ORANGE = registerBlock(() -> new CeramicBlock(DyeColor.ORANGE), "ceramic_orange");
    public static final DeferredBlock<Block> CERAMIC_MAGENTA = registerBlock(() -> new CeramicBlock(DyeColor.MAGENTA), "ceramic_magenta");
    public static final DeferredBlock<Block> CERAMIC_LIGHT_BLUE = registerBlock(() -> new CeramicBlock(DyeColor.LIGHT_BLUE), "ceramic_light_blue");
    public static final DeferredBlock<Block> CERAMIC_YELLOW = registerBlock(() -> new CeramicBlock(DyeColor.YELLOW), "ceramic_yellow");
    public static final DeferredBlock<Block> CERAMIC_LIME = registerBlock(() -> new CeramicBlock(DyeColor.LIME), "ceramic_lime");
    public static final DeferredBlock<Block> CERAMIC_PINK = registerBlock(() -> new CeramicBlock(DyeColor.PINK), "ceramic_pink");
    public static final DeferredBlock<Block> CERAMIC_GRAY = registerBlock(() -> new CeramicBlock(DyeColor.GRAY), "ceramic_gray");
    public static final DeferredBlock<Block> CERAMIC_LIGHT_GRAY = registerBlock(() -> new CeramicBlock(DyeColor.LIGHT_GRAY), "ceramic_light_gray");
    public static final DeferredBlock<Block> CERAMIC_CYAN = registerBlock(() -> new CeramicBlock(DyeColor.CYAN), "ceramic_cyan");
    public static final DeferredBlock<Block> CERAMIC_PURPLE = registerBlock(() -> new CeramicBlock(DyeColor.PURPLE), "ceramic_purple");
    public static final DeferredBlock<Block> CERAMIC_BLUE = registerBlock(() -> new CeramicBlock(DyeColor.BLUE), "ceramic_blue");
    public static final DeferredBlock<Block> CERAMIC_BROWN = registerBlock(() -> new CeramicBlock(DyeColor.BROWN), "ceramic_brown");
    public static final DeferredBlock<Block> CERAMIC_GREEN = registerBlock(() -> new CeramicBlock(DyeColor.GREEN), "ceramic_green");
    public static final DeferredBlock<Block> CERAMIC_RED = registerBlock(() -> new CeramicBlock(DyeColor.RED), "ceramic_red");
    public static final DeferredBlock<Block> CERAMIC_BLACK = registerBlock(() -> new CeramicBlock(DyeColor.BLACK), "ceramic_black");
    public static final DeferredBlock<Block> CERAMIC_WHITE_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_WHITE.get())), "ceramic_slab_white");
    public static final DeferredBlock<Block> CERAMIC_ORANGE_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_ORANGE.get())), "ceramic_slab_orange");
    public static final DeferredBlock<Block> CERAMIC_MAGENTA_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_MAGENTA.get())), "ceramic_slab_magenta");
    public static final DeferredBlock<Block> CERAMIC_LIGHT_BLUE_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_LIGHT_BLUE.get())), "ceramic_slab_light_blue");
    public static final DeferredBlock<Block> CERAMIC_YELLOW_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_YELLOW.get())), "ceramic_slab_yellow");
    public static final DeferredBlock<Block> CERAMIC_LIME_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_LIME.get())), "ceramic_slab_lime");
    public static final DeferredBlock<Block> CERAMIC_PINK_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_PINK.get())), "ceramic_slab_pink");
    public static final DeferredBlock<Block> CERAMIC_GRAY_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_GRAY.get())), "ceramic_slab_gray");
    public static final DeferredBlock<Block> CERAMIC_LIGHT_GRAY_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_LIGHT_GRAY.get())), "ceramic_slab_light_gray");
    public static final DeferredBlock<Block> CERAMIC_CYAN_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_CYAN.get())), "ceramic_slab_cyan");
    public static final DeferredBlock<Block> CERAMIC_PURPLE_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_PURPLE.get())), "ceramic_slab_purple");
    public static final DeferredBlock<Block> CERAMIC_BLUE_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_BLUE.get())), "ceramic_slab_blue");
    public static final DeferredBlock<Block> CERAMIC_BROWN_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_BROWN.get())), "ceramic_slab_brown");
    public static final DeferredBlock<Block> CERAMIC_GREEN_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_GREEN.get())), "ceramic_slab_green");
    public static final DeferredBlock<Block> CERAMIC_RED_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_RED.get())), "ceramic_slab_red");
    public static final DeferredBlock<Block> CERAMIC_BLACK_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(CERAMIC_BLACK.get())), "ceramic_slab_black");
    public static final DeferredBlock<Block> CERAMIC_WHITE_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_WHITE.get())), "ceramic_tile_white");
    public static final DeferredBlock<Block> CERAMIC_ORANGE_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_ORANGE.get())), "ceramic_tile_orange");
    public static final DeferredBlock<Block> CERAMIC_MAGENTA_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_MAGENTA.get())), "ceramic_tile_magenta");
    public static final DeferredBlock<Block> CERAMIC_LIGHT_BLUE_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_LIGHT_BLUE.get())), "ceramic_tile_light_blue");
    public static final DeferredBlock<Block> CERAMIC_YELLOW_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_YELLOW.get())), "ceramic_tile_yellow");
    public static final DeferredBlock<Block> CERAMIC_LIME_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_LIME.get())), "ceramic_tile_lime");
    public static final DeferredBlock<Block> CERAMIC_PINK_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_PINK.get())), "ceramic_tile_pink");
    public static final DeferredBlock<Block> CERAMIC_GRAY_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_GRAY.get())), "ceramic_tile_gray");
    public static final DeferredBlock<Block> CERAMIC_LIGHT_GRAY_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_LIGHT_GRAY.get())), "ceramic_tile_light_gray");
    public static final DeferredBlock<Block> CERAMIC_CYAN_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_CYAN.get())), "ceramic_tile_cyan");
    public static final DeferredBlock<Block> CERAMIC_PURPLE_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_PURPLE.get())), "ceramic_tile_purple");
    public static final DeferredBlock<Block> CERAMIC_BLUE_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_BLUE.get())), "ceramic_tile_blue");
    public static final DeferredBlock<Block> CERAMIC_BROWN_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_BROWN.get())), "ceramic_tile_brown");
    public static final DeferredBlock<Block> CERAMIC_GREEN_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_GREEN.get())), "ceramic_tile_green");
    public static final DeferredBlock<Block> CERAMIC_RED_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_RED.get())), "ceramic_tile_red");
    public static final DeferredBlock<Block> CERAMIC_BLACK_TILE = registerBlock(() -> new CeramicTileBlock(ofFullCopy(CERAMIC_BLACK.get())), "ceramic_tile_black");
    public static final DeferredBlock<Block> CERAMIC_WHITE_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_WHITE.get().defaultBlockState(), ofFullCopy(CERAMIC_WHITE.get())), "ceramic_stairs_white");
    public static final DeferredBlock<Block> CERAMIC_ORANGE_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_ORANGE.get().defaultBlockState(), ofFullCopy(CERAMIC_ORANGE.get())), "ceramic_stairs_orange");
    public static final DeferredBlock<Block> CERAMIC_MAGENTA_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_MAGENTA.get().defaultBlockState(), ofFullCopy(CERAMIC_MAGENTA.get())), "ceramic_stairs_magenta");
    public static final DeferredBlock<Block> CERAMIC_LIGHT_BLUE_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_LIGHT_BLUE.get().defaultBlockState(), ofFullCopy(CERAMIC_LIGHT_BLUE.get())), "ceramic_stairs_light_blue");
    public static final DeferredBlock<Block> CERAMIC_YELLOW_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_YELLOW.get().defaultBlockState(), ofFullCopy(CERAMIC_YELLOW.get())), "ceramic_stairs_yellow");
    public static final DeferredBlock<Block> CERAMIC_LIME_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_LIME.get().defaultBlockState(), ofFullCopy(CERAMIC_LIME.get())), "ceramic_stairs_lime");
    public static final DeferredBlock<Block> CERAMIC_PINK_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_PINK.get().defaultBlockState(), ofFullCopy(CERAMIC_PINK.get())), "ceramic_stairs_pink");
    public static final DeferredBlock<Block> CERAMIC_GRAY_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_GRAY.get().defaultBlockState(), ofFullCopy(CERAMIC_GRAY.get())), "ceramic_stairs_gray");
    public static final DeferredBlock<Block> CERAMIC_LIGHT_GRAY_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_LIGHT_GRAY.get().defaultBlockState(), ofFullCopy(CERAMIC_LIGHT_GRAY.get())), "ceramic_stairs_light_gray");
    public static final DeferredBlock<Block> CERAMIC_CYAN_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_CYAN.get().defaultBlockState(), ofFullCopy(CERAMIC_CYAN.get())), "ceramic_stairs_cyan");
    public static final DeferredBlock<Block> CERAMIC_PURPLE_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_PURPLE.get().defaultBlockState(), ofFullCopy(CERAMIC_PURPLE.get())), "ceramic_stairs_purple");
    public static final DeferredBlock<Block> CERAMIC_BLUE_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_BLUE.get().defaultBlockState(), ofFullCopy(CERAMIC_BLUE.get())), "ceramic_stairs_blue");
    public static final DeferredBlock<Block> CERAMIC_BROWN_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_BROWN.get().defaultBlockState(), ofFullCopy(CERAMIC_BROWN.get())), "ceramic_stairs_brown");
    public static final DeferredBlock<Block> CERAMIC_GREEN_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_GREEN.get().defaultBlockState(), ofFullCopy(CERAMIC_GREEN.get())), "ceramic_stairs_green");
    public static final DeferredBlock<Block> CERAMIC_RED_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_RED.get().defaultBlockState(), ofFullCopy(CERAMIC_RED.get())), "ceramic_stairs_red");
    public static final DeferredBlock<Block> CERAMIC_BLACK_STAIRS = registerBlock(() -> new StairBlock(() -> CERAMIC_BLACK.get().defaultBlockState(), ofFullCopy(CERAMIC_BLACK.get())), "ceramic_stairs_black");
    public static final DeferredBlock<Block> CERAMIC_WHITE_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_WHITE.get())), "ceramic_wall_white");
    public static final DeferredBlock<Block> CERAMIC_ORANGE_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_ORANGE.get())), "ceramic_wall_orange");
    public static final DeferredBlock<Block> CERAMIC_MAGENTA_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_MAGENTA.get())), "ceramic_wall_magenta");
    public static final DeferredBlock<Block> CERAMIC_LIGHT_BLUE_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_LIGHT_BLUE.get())), "ceramic_wall_light_blue");
    public static final DeferredBlock<Block> CERAMIC_YELLOW_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_YELLOW.get())), "ceramic_wall_yellow");
    public static final DeferredBlock<Block> CERAMIC_LIME_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_LIME.get())), "ceramic_wall_lime");
    public static final DeferredBlock<Block> CERAMIC_PINK_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_PINK.get())), "ceramic_wall_pink");
    public static final DeferredBlock<Block> CERAMIC_GRAY_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_GRAY.get())), "ceramic_wall_gray");
    public static final DeferredBlock<Block> CERAMIC_LIGHT_GRAY_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_LIGHT_GRAY.get())), "ceramic_wall_light_gray");
    public static final DeferredBlock<Block> CERAMIC_CYAN_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_CYAN.get())), "ceramic_wall_cyan");
    public static final DeferredBlock<Block> CERAMIC_PURPLE_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_PURPLE.get())), "ceramic_wall_purple");
    public static final DeferredBlock<Block> CERAMIC_BLUE_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_BLUE.get())), "ceramic_wall_blue");
    public static final DeferredBlock<Block> CERAMIC_BROWN_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_BROWN.get())), "ceramic_wall_brown");
    public static final DeferredBlock<Block> CERAMIC_GREEN_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_GREEN.get())), "ceramic_wall_green");
    public static final DeferredBlock<Block> CERAMIC_RED_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_RED.get())), "ceramic_wall_red");
    public static final DeferredBlock<Block> CERAMIC_BLACK_WALL = registerBlock(() -> new WallBlock(ofFullCopy(CERAMIC_BLACK.get())), "ceramic_wall_black");
    public static final DeferredBlock<Block> CRYSTAL_GLASS = registerBlock(() -> new AtumGlassBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(AtumBlocks::never).isRedstoneConductor(AtumBlocks::never).isSuffocating(AtumBlocks::never).isViewBlocking(AtumBlocks::never)), "crystal_glass");
    public static final DeferredBlock<Block> WHITE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.WHITE), "white_stained_crystal_glass");
    public static final DeferredBlock<Block> ORANGE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.ORANGE), "orange_stained_crystal_glass");
    public static final DeferredBlock<Block> MAGENTA_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.MAGENTA), "magenta_stained_crystal_glass");
    public static final DeferredBlock<Block> LIGHT_BLUE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_BLUE), "light_blue_stained_crystal_glass");
    public static final DeferredBlock<Block> YELLOW_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.YELLOW), "yellow_stained_crystal_glass");
    public static final DeferredBlock<Block> LIME_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIME), "lime_stained_crystal_glass");
    public static final DeferredBlock<Block> PINK_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PINK), "pink_stained_crystal_glass");
    public static final DeferredBlock<Block> GRAY_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GRAY), "gray_stained_crystal_glass");
    public static final DeferredBlock<Block> LIGHT_GRAY_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_GRAY), "light_gray_stained_crystal_glass");
    public static final DeferredBlock<Block> CYAN_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.CYAN), "cyan_stained_crystal_glass");
    public static final DeferredBlock<Block> PURPLE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PURPLE), "purple_stained_crystal_glass");
    public static final DeferredBlock<Block> BLUE_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLUE), "blue_stained_crystal_glass");
    public static final DeferredBlock<Block> BROWN_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BROWN), "brown_stained_crystal_glass");
    public static final DeferredBlock<Block> GREEN_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GREEN), "green_stained_crystal_glass");
    public static final DeferredBlock<Block> RED_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.RED), "red_stained_crystal_glass");
    public static final DeferredBlock<Block> BLACK_STAINED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLACK), "black_stained_crystal_glass");
    public static final DeferredBlock<Block> CRYSTAL_GLASS_PANE = registerBlock(AtumPaneBlock::new, "crystal_glass_pane");
    public static final DeferredBlock<Block> WHITE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.WHITE, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "white_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> ORANGE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.ORANGE, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "orange_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> MAGENTA_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.MAGENTA, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "magenta_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> LIGHT_BLUE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_BLUE, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "light_blue_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> YELLOW_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.YELLOW, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "yellow_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> LIME_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIME, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "lime_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> PINK_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PINK, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "pink_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> GRAY_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GRAY, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "gray_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> LIGHT_GRAY_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_GRAY, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "light_gray_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> CYAN_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.CYAN, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "cyan_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> PURPLE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PURPLE, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "purple_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> BLUE_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLUE, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "blue_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> BROWN_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BROWN, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "brown_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> GREEN_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GREEN, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "green_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> RED_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.RED, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "red_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> BLACK_STAINED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLACK, ofFullCopy(CRYSTAL_GLASS_PANE.get())), "black_stained_crystal_glass_pane");
    public static final DeferredBlock<Block> PALM_FRAMED_CRYSTAL_GLASS = registerBlock(() -> new AtumGlassBlock(of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion()), "palm_framed_crystal_glass");
    public static final DeferredBlock<Block> WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.WHITE), "white_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.ORANGE), "orange_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.MAGENTA), "magenta_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_BLUE), "light_blue_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.YELLOW), "yellow_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIME), "lime_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PINK), "pink_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GRAY), "gray_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_GRAY), "light_gray_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.CYAN), "cyan_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PURPLE), "purple_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLUE), "blue_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BROWN), "brown_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GREEN), "green_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.RED), "red_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLACK), "black_stained_palm_framed_crystal_glass");
    public static final DeferredBlock<Block> DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(() -> new AtumGlassBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(AtumBlocks::never).isRedstoneConductor(AtumBlocks::never).isSuffocating(AtumBlocks::never).isViewBlocking(AtumBlocks::never)), "deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.WHITE), "white_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.ORANGE), "orange_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.MAGENTA), "magenta_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_BLUE), "light_blue_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.YELLOW), "yellow_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIME), "lime_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PINK), "pink_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GRAY), "gray_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.LIGHT_GRAY), "light_gray_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.CYAN), "cyan_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.PURPLE), "purple_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLUE), "blue_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BROWN), "brown_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.GREEN), "green_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.RED), "red_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS = registerBlock(createStainedGlassFromColor(DyeColor.BLACK), "black_stained_deadwood_framed_crystal_glass");
    public static final DeferredBlock<Block> PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(AtumPaneBlock::new, "palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.WHITE, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "white_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.ORANGE, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "orange_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.MAGENTA, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "magenta_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_BLUE, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "light_blue_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.YELLOW, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "yellow_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIME, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "lime_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PINK, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "pink_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GRAY, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "gray_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_GRAY, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "light_gray_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.CYAN, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "cyan_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PURPLE, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "purple_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLUE, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "blue_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BROWN, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "brown_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GREEN, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "green_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.RED, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "red_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLACK, ofFullCopy(PALM_FRAMED_CRYSTAL_GLASS_PANE.get())), "black_stained_palm_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(AtumPaneBlock::new, "deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.WHITE, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "white_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.ORANGE, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "orange_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.MAGENTA, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "magenta_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_BLUE, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "light_blue_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.YELLOW, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "yellow_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIME, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "lime_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PINK, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "pink_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GRAY, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "gray_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.LIGHT_GRAY, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "light_gray_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.CYAN, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "cyan_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.PURPLE, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "purple_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLUE, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "blue_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BROWN, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "brown_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.GREEN, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "green_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.RED, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "red_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE = registerBlock(() -> new StainedGlassPaneBlock(DyeColor.BLACK, ofFullCopy(DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get())), "black_stained_deadwood_framed_crystal_glass_pane");
    public static final DeferredBlock<Block> LINEN_WHITE = registerBlock(() -> new LinenBlock(DyeColor.WHITE), "linen_white");
    public static final DeferredBlock<Block> LINEN_ORANGE = registerBlock(() -> new LinenBlock(DyeColor.ORANGE), "linen_orange");
    public static final DeferredBlock<Block> LINEN_MAGENTA = registerBlock(() -> new LinenBlock(DyeColor.MAGENTA), "linen_magenta");
    public static final DeferredBlock<Block> LINEN_LIGHT_BLUE = registerBlock(() -> new LinenBlock(DyeColor.LIGHT_BLUE), "linen_light_blue");
    public static final DeferredBlock<Block> LINEN_YELLOW = registerBlock(() -> new LinenBlock(DyeColor.YELLOW), "linen_yellow");
    public static final DeferredBlock<Block> LINEN_LIME = registerBlock(() -> new LinenBlock(DyeColor.LIME), "linen_lime");
    public static final DeferredBlock<Block> LINEN_PINK = registerBlock(() -> new LinenBlock(DyeColor.PINK), "linen_pink");
    public static final DeferredBlock<Block> LINEN_GRAY = registerBlock(() -> new LinenBlock(DyeColor.GRAY), "linen_gray");
    public static final DeferredBlock<Block> LINEN_LIGHT_GRAY = registerBlock(() -> new LinenBlock(DyeColor.LIGHT_GRAY), "linen_light_gray");
    public static final DeferredBlock<Block> LINEN_CYAN = registerBlock(() -> new LinenBlock(DyeColor.CYAN), "linen_cyan");
    public static final DeferredBlock<Block> LINEN_PURPLE = registerBlock(() -> new LinenBlock(DyeColor.PURPLE), "linen_purple");
    public static final DeferredBlock<Block> LINEN_BLUE = registerBlock(() -> new LinenBlock(DyeColor.BLUE), "linen_blue");
    public static final DeferredBlock<Block> LINEN_BROWN = registerBlock(() -> new LinenBlock(DyeColor.BROWN), "linen_brown");
    public static final DeferredBlock<Block> LINEN_GREEN = registerBlock(() -> new LinenBlock(DyeColor.GREEN), "linen_green");
    public static final DeferredBlock<Block> LINEN_RED = registerBlock(() -> new LinenBlock(DyeColor.RED), "linen_red");
    public static final DeferredBlock<Block> LINEN_BLACK = registerBlock(() -> new LinenBlock(DyeColor.BLACK), "linen_black");
    public static final DeferredBlock<Block> LINEN_CARPET_WHITE = registerBlock(() -> new LinenCarpetBlock(DyeColor.WHITE), "linen_carpet_white");
    public static final DeferredBlock<Block> LINEN_CARPET_ORANGE = registerBlock(() -> new LinenCarpetBlock(DyeColor.ORANGE), "linen_carpet_orange");
    public static final DeferredBlock<Block> LINEN_CARPET_MAGENTA = registerBlock(() -> new LinenCarpetBlock(DyeColor.MAGENTA), "linen_carpet_magenta");
    public static final DeferredBlock<Block> LINEN_CARPET_LIGHT_BLUE = registerBlock(() -> new LinenCarpetBlock(DyeColor.LIGHT_BLUE), "linen_carpet_light_blue");
    public static final DeferredBlock<Block> LINEN_CARPET_YELLOW = registerBlock(() -> new LinenCarpetBlock(DyeColor.YELLOW), "linen_carpet_yellow");
    public static final DeferredBlock<Block> LINEN_CARPET_LIME = registerBlock(() -> new LinenCarpetBlock(DyeColor.LIME), "linen_carpet_lime");
    public static final DeferredBlock<Block> LINEN_CARPET_PINK = registerBlock(() -> new LinenCarpetBlock(DyeColor.PINK), "linen_carpet_pink");
    public static final DeferredBlock<Block> LINEN_CARPET_GRAY = registerBlock(() -> new LinenCarpetBlock(DyeColor.GRAY), "linen_carpet_gray");
    public static final DeferredBlock<Block> LINEN_CARPET_LIGHT_GRAY = registerBlock(() -> new LinenCarpetBlock(DyeColor.LIGHT_GRAY), "linen_carpet_light_gray");
    public static final DeferredBlock<Block> LINEN_CARPET_CYAN = registerBlock(() -> new LinenCarpetBlock(DyeColor.CYAN), "linen_carpet_cyan");
    public static final DeferredBlock<Block> LINEN_CARPET_PURPLE = registerBlock(() -> new LinenCarpetBlock(DyeColor.PURPLE), "linen_carpet_purple");
    public static final DeferredBlock<Block> LINEN_CARPET_BLUE = registerBlock(() -> new LinenCarpetBlock(DyeColor.BLUE), "linen_carpet_blue");
    public static final DeferredBlock<Block> LINEN_CARPET_BROWN = registerBlock(() -> new LinenCarpetBlock(DyeColor.BROWN), "linen_carpet_brown");
    public static final DeferredBlock<Block> LINEN_CARPET_GREEN = registerBlock(() -> new LinenCarpetBlock(DyeColor.GREEN), "linen_carpet_green");
    public static final DeferredBlock<Block> LINEN_CARPET_RED = registerBlock(() -> new LinenCarpetBlock(DyeColor.RED), "linen_carpet_red");
    public static final DeferredBlock<Block> LINEN_CARPET_BLACK = registerBlock(() -> new LinenCarpetBlock(DyeColor.BLACK), "linen_carpet_black");
    public static final DeferredBlock<Block> PALM_PLANKS = registerBlock(() -> new Block(of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)), "palm_planks");
    public static final DeferredBlock<Block> DEADWOOD_PLANKS = registerBlock(() -> new Block(of().mapColor(MapColor.PODZOL).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)), "deadwood_planks");
    public static final DeferredBlock<Block> PALM_LOG = registerBlock(PalmLog::new, "palm_log");
    public static final DeferredBlock<Block> STRIPPED_PALM_LOG = registerBlock(() -> new RotatedPillarBlock(ofFullCopy(PALM_LOG.get())), "stripped_palm_log");
    public static final DeferredBlock<Block> PALM_WOOD = registerBlock(() -> new RotatedPillarBlock(ofFullCopy(PALM_LOG.get())), "palm_wood");
    public static final DeferredBlock<Block> STRIPPED_PALM_WOOD = registerBlock(() -> new RotatedPillarBlock(ofFullCopy(PALM_LOG.get())), "stripped_palm_wood");
    public static final DeferredBlock<Block> DEADWOOD_LOG = registerBlock(() -> new DeadwoodLogBlock().setCanBeStripped(), "deadwood_log");
    public static final DeferredBlock<Block> STRIPPED_DEADWOOD_LOG = registerBlock(DeadwoodLogBlock::new, "stripped_deadwood_log");
    public static final DeferredBlock<Block> DEADWOOD_WOOD = registerBlock(DeadwoodLogBlock::new, "deadwood_wood");
    public static final DeferredBlock<Block> STRIPPED_DEADWOOD_WOOD = registerBlock(DeadwoodLogBlock::new, "stripped_deadwood_wood");
    public static final DeferredBlock<Block> DEADWOOD_BRANCH = registerBlock(DeadwoodBranchBlock::new, null, "deadwood_branch");
    public static final DeferredBlock<Block> PALM_STAIRS = registerBlock(() -> new StairBlock(() -> PALM_PLANKS.get().defaultBlockState(), ofFullCopy(PALM_PLANKS.get())), "palm_stairs");
    public static final DeferredBlock<Block> DEADWOOD_STAIRS = registerBlock(() -> new StairBlock(() -> DEADWOOD_PLANKS.get().defaultBlockState(), ofFullCopy(DEADWOOD_PLANKS.get())), "deadwood_stairs");
    public static final DeferredBlock<Block> PALM_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(PALM_PLANKS.get())), "palm_slab");
    public static final DeferredBlock<Block> DEADWOOD_SLAB = registerBlock(() -> new SlabBlock(ofFullCopy(DEADWOOD_PLANKS.get())), "deadwood_slab");
    public static final DeferredBlock<Block> PALM_SAPLING = registerBlock(PalmSaplingBlock::new, "palm_sapling");
    public static final DeferredBlock<Block> POTTED_PALM_SAPLING = registerBaseBlock(() -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, PALM_SAPLING, BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).instabreak().noOcclusion()), "potted_palm_sapling");
    public static final DeferredBlock<Block> PALM_LEAVES = registerBlock(PalmLeavesBlock::new, "palm_leaves");
    public static final DeferredBlock<Block> DRY_LEAVES = registerBlock(LeavesAtumBlock::new, null, "dry_leaves");
    public static final DeferredBlock<Block> PALM_CRATE = registerBlock(() -> new CrateBlock(ofFullCopy(PALM_PLANKS.get())), "palm_crate");
    public static final DeferredBlock<Block> DEADWOOD_CRATE = registerBlock(() -> new CrateBlock(ofFullCopy(PALM_PLANKS.get())), "deadwood_crate");
    public static final DeferredBlock<Block> PALM_LADDER = registerBlock(AtumLadderBlock::new, "palm_ladder");
    public static final DeferredBlock<Block> DEADWOOD_LADDER = registerBlock(AtumLadderBlock::new, "deadwood_ladder");
    public static final DeferredBlock<Block> PALM_FENCE = registerBlock(() -> new FenceBlock(ofFullCopy(PALM_PLANKS.get())), "palm_fence");
    public static final DeferredBlock<Block> DEADWOOD_FENCE = registerBlock(() -> new FenceBlock(ofFullCopy(DEADWOOD_PLANKS.get())), "deadwood_fence");
    public static final DeferredBlock<Block> PALM_FENCE_GATE = registerBlock(() -> new FenceGateBlock(ofFullCopy(PALM_PLANKS.get()), SoundEvents.FENCE_GATE_CLOSE, SoundEvents.FENCE_GATE_OPEN), "palm_fence_gate");
    public static final DeferredBlock<Block> DEADWOOD_FENCE_GATE = registerBlock(() -> new FenceGateBlock(ofFullCopy(DEADWOOD_PLANKS.get()), SoundEvents.FENCE_GATE_CLOSE, SoundEvents.FENCE_GATE_OPEN), "deadwood_fence_gate");
    public static final DeferredBlock<Block> PALM_HATCH = registerBlock(() -> new TrapDoorBlock(AtumBlockSetType.PALM, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(AtumBlocks::never)), "palm_hatch");
    public static final DeferredBlock<Block> DEADWOOD_HATCH = registerBlock(() -> new TrapDoorBlock(AtumBlockSetType.DEADWOOD, Block.Properties.of().mapColor(MapColor.PODZOL).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(AtumBlocks::never)), "deadwood_hatch");
    public static final DeferredBlock<Block> PALM_DOOR = registerBlock(() -> new DoorAtumBlock(ofFullCopy(PALM_PLANKS.get()), AtumBlockSetType.PALM), "palm_door");
    public static final DeferredBlock<Block> DEADWOOD_DOOR = registerBlock(() -> new DoorAtumBlock(ofFullCopy(DEADWOOD_PLANKS.get()), AtumBlockSetType.DEADWOOD), "deadwood_door");
    public static final DeferredBlock<Block> LIMESTONE_BUTTON = registerBlock(() -> new ButtonBlock(AtumBlockSetType.LIMESTONE, 20, BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).noCollission().strength(0.5F)), "limestone_button");
    public static final DeferredBlock<Block> PALM_BUTTON = registerBlock(() -> new ButtonBlock(AtumBlockSetType.PALM, 30, BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).noCollission().strength(0.5F).sound(SoundType.WOOD)), "palm_button");
    public static final DeferredBlock<Block> DEADWOOD_BUTTON = registerBlock(() -> new ButtonBlock(AtumBlockSetType.DEADWOOD, 30, BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).noCollission().strength(0.5F).sound(SoundType.WOOD)), "deadwood_button");
    public static final DeferredBlock<Block> LIMESTONE_PRESSURE_PLATE = registerBlock(() -> new PressurePlateBlock(AtumBlockSetType.LIMESTONE, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().strength(0.5F)), "limestone_pressure_plate");
    public static final DeferredBlock<Block> PALM_PRESSURE_PLATE = registerBlock(() -> new PressurePlateBlock(AtumBlockSetType.PALM, BlockBehaviour.Properties.of().mapColor(PALM_PLANKS.get().defaultMapColor()).ignitedByLava().instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5F).sound(SoundType.WOOD)), "palm_pressure_plate");
    public static final DeferredBlock<Block> DEADWOOD_PRESSURE_PLATE = registerBlock(() -> new PressurePlateBlock(AtumBlockSetType.DEADWOOD, BlockBehaviour.Properties.of().mapColor(DEADWOOD_PLANKS.get().defaultMapColor()).ignitedByLava().instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5F).sound(SoundType.WOOD)), "deadwood_pressure_plate");
    public static final DeferredBlock<Block> PALM_SIGN = registerSign(() -> new AtumStandingSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).sound(SoundType.WOOD), Atum.PALM), Atum.PALM);
    public static final DeferredBlock<Block> DEADWOOD_SIGN = registerSign(() -> new AtumStandingSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).sound(SoundType.WOOD), Atum.DEADWOOD), Atum.DEADWOOD);
    public static final DeferredBlock<Block> PALM_SCAFFOLDING = registerScaffolding(() -> new AtumScaffoldingBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).pushReaction(PushReaction.DESTROY).noCollission().sound(SoundType.SCAFFOLDING).dynamicShape()), "palm_scaffolding");
    public static final DeferredBlock<Block> DEADWOOD_SCAFFOLDING = registerScaffolding(() -> new AtumScaffoldingBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).pushReaction(PushReaction.DESTROY).noCollission().sound(SoundType.SCAFFOLDING).dynamicShape()), "deadwood_scaffolding");
    public static final DeferredBlock<Block> LIFELESS_SAND = registerBlock(() -> new Block(of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.SNARE).strength(1.5F, 5.0F).sound(SoundType.SAND)), "lifeless_sand");
    public static final DeferredBlock<Block> PACKED_STRANGE_SAND = registerBlock(() -> new Block(of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.SNARE).strength(1.5F, 5.0F).sound(SoundType.SAND)), "packed_strange_sand");

    public static void setBlockInfo() {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BuiltInRegistries.BLOCK.getKey(PALM_SAPLING.get()), () -> POTTED_PALM_SAPLING.get());
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
        return () -> new StainedGlassBlock(color, BlockBehaviour.Properties.of().mapColor(color).instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(AtumBlocks::never).isRedstoneConductor(AtumBlocks::never).isSuffocating(AtumBlocks::never).isViewBlocking(AtumBlocks::never));
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
     * @param lit  The lit torch block to be registered
     * @param name The name to register the block with
     * @return The Block that was registered
     */
    public static DeferredBlock<Block> registerTorchWithUnlit(@Nonnull Supplier<Block> lit, @Nonnull String name) {
        DeferredBlock<Block> litTorch = registerBaseBlock(lit, name);
        DeferredBlock<Block> unlitTorch = registerBaseBlock(() -> new AtumTorchUnlitBlock(litTorch), name + "_unlit");
        DeferredBlock<Block> wallTorchLit = registerBaseBlock(() -> new AtumWallTorch(Block.Properties.ofFullCopy(litTorch.get()).lootFrom(litTorch), ((AtumTorchBlock) litTorch.get()).getParticleType()), "wall_" + name);
        DeferredBlock<Block> wallTorchUnlit = registerBaseBlock(() -> new AtumWallTorchUnlitBlock(wallTorchLit.get(), Block.Properties.ofFullCopy(unlitTorch.get()).lootFrom(unlitTorch)), "wall_" + name + "_unlit");
        AtumItems.registerItem(() -> new StandingAndWallBlockItem(unlitTorch.get(), wallTorchUnlit.get(), new Item.Properties(), Direction.DOWN), name + "_unlit");
        DeferredItem<Item> litTorchItem = AtumItems.registerItem(() -> new StandingAndWallBlockItem(litTorch.get(), wallTorchLit.get(), new Item.Properties(), Direction.DOWN), name);
        AtumTorchUnlitBlock.UNLIT.put(litTorch, unlitTorch);
        AtumItems.ITEMS_FOR_TAB_LIST.add(litTorchItem);
        return litTorch;
    }

    public static DeferredBlock<Block> registerTorch(@Nonnull Supplier<Block> torch, @Nonnull String name) {
        DeferredBlock<Block> torchList = registerBaseBlock(torch, name);
        DeferredBlock<Block> wallTorchLit = registerBaseBlock(() -> new AtumWallTorch(Block.Properties.ofFullCopy(torchList.get()).lootFrom(torchList), ((AtumTorchBlock) torchList.get()).getParticleType()), "wall_" + name);
        DeferredItem<Item> litTorchItem = AtumItems.registerItem(() -> new StandingAndWallBlockItem(torchList.get(), wallTorchLit.get(), new Item.Properties(), Direction.DOWN), name);
        AtumItems.ITEMS_FOR_TAB_LIST.add(litTorchItem);
        return torchList;
    }

    /**
     * Helper method for easily registering scaffolding
     *
     * @param scaffolding The scaffolding block to be registered
     * @param name        The name to register the block with
     * @return The Block that was registered
     */
    public static DeferredBlock<Block> registerScaffolding(@Nonnull Supplier<Block> scaffolding, @Nonnull String name) {
        DeferredBlock<Block> scaffoldingBlock = registerBaseBlock(scaffolding, name);
        AtumItems.registerItem(() -> new AtumScaffoldingItem(scaffoldingBlock.get()), name);
        return scaffoldingBlock;
    }

    /**
     * Allows for easy registering of signs, that handles Ground Sign, Wall Sign and Item sign registration
     */
    public static DeferredBlock<Block> registerSign(@Nonnull Supplier<Block> signBlock, @Nonnull WoodType woodType) {
        String typeName = woodType.name().replace("atum_", "");
        DeferredBlock<Block> signBlockObject = registerBaseBlock(signBlock, typeName + "_sign");
        DeferredBlock<Block> wallSignBlock = registerBaseBlock(() -> new AtumWallSignBlock(of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).sound(SoundType.WOOD).lootFrom(signBlockObject), woodType), typeName + "_wall_sign");
        DeferredItem<Item> signItem = AtumItems.registerItem(() -> new SignItem((new Item.Properties()).stacksTo(16), signBlockObject.get(), wallSignBlock.get()), typeName + "_sign");
        AtumWallSignBlock.WALL_SIGN_BLOCKS.put(signBlockObject, wallSignBlock);
        AtumItems.ITEMS_FOR_TAB_LIST.add(signItem);
        return signBlockObject;
    }

    /**
     * Same as {@link AtumBlocks#registerBlock(Supplier, Item.Properties, String)}, but have an empty Item.Properties set
     */
    public static DeferredBlock<Block> registerBlock(@Nonnull Supplier<Block> supplier, @Nonnull String name) {
        return registerBlock(supplier, new Item.Properties(), name);
    }

    /**
     * Same as {@link AtumBlocks#registerBlock(Supplier, Item.Properties, String)} and have an easy way to set a ISTER
     *
     * @param properties BlockItem properties, can be set to null to not have any ItemGroup
     */
    public static DeferredBlock<Block> registerWithRenderer(@Nonnull Supplier<Block> supplier, @Nullable Item.Properties properties, @Nonnull String name) {
        DeferredBlock<Block> block = registerBaseBlock(supplier, name);

        if (properties == null) {
            AtumItems.registerItem(() -> new BlockItemWithoutLevelRenderer(block.get(), new Item.Properties()), name);
        } else {
            AtumItems.registerItemWithTab(() -> new BlockItemWithoutLevelRenderer(block.get(), properties), name);
        }
        return block;
    }

    /**
     * Same as {@link AtumBlocks#registerBaseBlock(Supplier, String)}, but registers a basic BlockItem at the same time
     *
     * @param properties BlockItem properties, can be set to null to not have any ItemGroup
     */
    public static DeferredBlock<Block> registerBlock(@Nonnull Supplier<Block> supplier, @Nullable Item.Properties properties, @Nonnull String name) {
        DeferredBlock<Block> block = registerBaseBlock(supplier, name);

        if (properties == null) {
            AtumItems.registerItem(() -> new BlockItem(block.get(), new Item.Properties()), name);
        } else {
            AtumItems.registerItemWithTab(() -> new BlockItem(block.get(), properties), name);
        }
        return block;
    }

    /**
     * Registers a block
     *
     * @param supplier The supplier of the block to be registered
     * @param name  The name to register the block with
     * @return The Block that was registered
     */
    public static DeferredBlock<Block> registerBaseBlock(@Nonnull Supplier<Block> supplier, @Nonnull String name) {
        return BLOCK_DEFERRED.register(name, supplier);
    }
}