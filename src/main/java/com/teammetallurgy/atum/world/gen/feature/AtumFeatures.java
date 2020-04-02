package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.gen.feature.config.DoubleBlockStateFeatureConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.blockplacer.ColumnBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumFeatures {
    private static final List<Feature<?>> FEATURES = new ArrayList<>();
    //Features
    public static final Feature<DoubleBlockStateFeatureConfig> OASIS_POND = register("oasis_pond", new OasisPondFeature(DoubleBlockStateFeatureConfig::deserializeDouble));

    //Feature Configs
    public static final BlockClusterFeatureConfig OASIS_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.OASIS_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(30).build();
    public static final BlockClusterFeatureConfig DEAD_OASIS_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.DEAD_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(2).build();
    public static final BlockClusterFeatureConfig PAPYRUS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AtumBlocks.PAPYRUS.getDefaultState()), new ColumnBlockPlacer(1, 2))).tries(124).func_227318_b_(1).func_227321_c_(0).func_227323_d_(1).func_227317_b_().func_227320_c_().build();

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