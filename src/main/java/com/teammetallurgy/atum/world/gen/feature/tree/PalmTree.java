package com.teammetallurgy.atum.world.gen.feature.tree;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nonnull;
import java.util.Random;

public class PalmTree extends AbstractTreeGrower {

    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(@Nonnull Random random, boolean largeHive) {
        return AtumFeatures.ATUM_TREE.configured(AtumFeatures.PALM_TREE_CONFIG_SAPLING);
    }
}