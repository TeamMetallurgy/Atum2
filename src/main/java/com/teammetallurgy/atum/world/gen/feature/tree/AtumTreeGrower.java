package com.teammetallurgy.atum.world.gen.feature.tree;

import com.teammetallurgy.atum.init.AtumFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class AtumTreeGrower {
    public static final TreeGrower PALM = new TreeGrower("atum:palm", Optional.empty(), Optional.of(AtumFeatures.PALM_TREE), Optional.empty());

}
