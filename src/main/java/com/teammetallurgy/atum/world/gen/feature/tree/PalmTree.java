package com.teammetallurgy.atum.world.gen.feature.tree;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class PalmTree extends AbstractTreeGrower {

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@Nonnull RandomSource random, boolean largeHive) {
        return AtumFeatures.PALM_TREE;
    }
}
