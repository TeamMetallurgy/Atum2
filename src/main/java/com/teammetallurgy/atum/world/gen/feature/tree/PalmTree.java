package com.teammetallurgy.atum.world.gen.feature.tree;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nonnull;
import java.util.Random;

public class PalmTree extends Tree {

    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(@Nonnull Random random, boolean largeHive) {
        return AtumFeatures.ATUM_TREE.withConfiguration(AtumFeatures.PALM_TREE_CONFIG_SAPLING);
    }
}