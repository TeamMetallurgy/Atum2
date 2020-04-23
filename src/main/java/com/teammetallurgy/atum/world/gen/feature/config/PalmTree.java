package com.teammetallurgy.atum.world.gen.feature.config;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class PalmTree extends PalmTreeBase {

    @Nullable
    @Override
    protected ConfiguredFeature<PalmConfig, ?> getPalmFeature(Random random, boolean b) {
        return AtumFeatures.PALM_TREE.withConfiguration(AtumFeatures.PALM_TREE_CONFIG_SAPLING);
    }
}