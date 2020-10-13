package com.teammetallurgy.atum.init;

import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.wood.DeadwoodLogBlock;
import com.teammetallurgy.atum.world.gen.feature.*;
import com.teammetallurgy.atum.world.gen.feature.config.DoubleBlockStateFeatureConfig;
import com.teammetallurgy.atum.world.gen.feature.config.PalmConfig;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.blockplacer.ColumnBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumFeatures {
    private static final List<Feature<?>> FEATURES = new ArrayList<>();
    //Features
    public static final Feature<DoubleBlockStateFeatureConfig> OASIS_POND = register("oasis_pond", new OasisPondFeature(DoubleBlockStateFeatureConfig.DOUBLE_STATE_CODEC));
    public static final Feature<BlockStateFeatureConfig> SURFACE_LAVA_LAKE = register("surface_lava_lake", new LakeFeature(BlockStateFeatureConfig.field_236455_a_));
    public static final Feature<PalmConfig> PALM_TREE = register("palm_tree", new PalmFeature(PalmConfig.PALM_CODEC));
    public static final BonusCrateFeature BONUS_CRATE = register("bonus_crate", new BonusCrateFeature(NoFeatureConfig.field_236558_a_));
    public static final StartStructureFeature START_STRUCTURE = register("start_structure", new StartStructureFeature(NoFeatureConfig.field_236558_a_));
    public static final DirtyBoneFossilsFeature DIRTY_BONE_FOSSILS = register("dirty_bone_fossil", new DirtyBoneFossilsFeature(NoFeatureConfig.field_236558_a_));
    public static final LimestoneDungeonsFeature LIMESTONE_DUNGEONS = register("limestone_dungeon", new LimestoneDungeonsFeature(NoFeatureConfig.field_236558_a_));
    public static final DeadwoodFeature DEADWOOD_FEATURE = register("deadwood_tree", new DeadwoodFeature(BaseTreeFeatureConfig.CODEC));
    public static final Feature<NoFeatureConfig> LIMESTONE_SPIKE = register("limestone_spike", new LimestoneSpikeFeature(NoFeatureConfig.field_236558_a_));
    public static final Feature<BlockClusterFeatureConfig> ANPUTS_FINGERS = register("anputs_fingers", new AnputsFingersFeature(BlockClusterFeatureConfig.field_236587_a_));

    //Feature Configs
    public static final BlockClusterFeatureConfig OASIS_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.OASIS_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(30).build();
    public static final BlockClusterFeatureConfig DEAD_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEAD_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(2).build();
    public static final BlockClusterFeatureConfig ANPUTS_FINGERS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.ANPUTS_FINGERS.getDefaultState()), new SimpleBlockPlacer())).build();
    public static final BlockClusterFeatureConfig PAPYRUS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PAPYRUS.getDefaultState()), new ColumnBlockPlacer(1, 2))).tries(124).xSpread(1).ySpread(0).zSpread(1).func_227317_b_().requiresWater().build();
    public static final PalmConfig PALM_TREE_CONFIG = (new PalmConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PALM_LOG.getDefaultState()), new SimpleBlockStateProvider(AtumBlocks.PALM_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0)), new StraightTrunkPlacer(5, 2, 1), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().date(0.1D).ophidianTongue(0.6D).build(); //TODO TwoLayerFeature n'stuff
    public static final PalmConfig PALM_TREE_CONFIG_SAPLING = (new PalmConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PALM_LOG.getDefaultState()), new SimpleBlockStateProvider(AtumBlocks.PALM_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0)), new StraightTrunkPlacer(5, 2, 1), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().date(0.1D).ophidianTongue(0.0D).build(); //TODO TwoLayerFeature n'stuff
    public static final PalmConfig DEAD_PALM_TREE_CONFIG = (new PalmConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_LOG.getDefaultState().with(DeadwoodLogBlock.HAS_SCARAB, true)), new SimpleBlockStateProvider(AtumBlocks.DEADWOOD_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0)), new StraightTrunkPlacer(5, 2, 1), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().date(0.0D).ophidianTongue(0.0D).build(); //TODO TwoLayerFeature n'stuff
    public static final LiquidsConfig WATER_SPRING_CONFIG = new LiquidsConfig(Fluids.WATER.getDefaultState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));
    public static final LiquidsConfig LAVA_SPRING_CONFIG = new LiquidsConfig(Fluids.LAVA.getDefaultState(), true, 4, 1, ImmutableSet.of(AtumBlocks.LIMESTONE));
    public static final BlockClusterFeatureConfig SHRUB_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.SHRUB.getDefaultState()), new SimpleBlockPlacer())).tries(3).build();
    public static final BlockClusterFeatureConfig WEED_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.WEED.getDefaultState()), new SimpleBlockPlacer())).tries(3).build();

    private static <C extends IFeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        feature.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        FEATURES.add(feature);
        return feature;
    }

    @SubscribeEvent
    public static void registerFeature(RegistryEvent.Register<Feature<?>> event) {
        for (Feature<?> feature : FEATURES) {
            event.getRegistry().register(feature);
        }
    }
}