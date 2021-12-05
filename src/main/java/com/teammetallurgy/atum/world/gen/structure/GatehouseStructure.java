package com.teammetallurgy.atum.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class GatehouseStructure extends JigsawFeature {

    public GatehouseStructure(Codec<JigsawConfiguration> config) {
        super(config, 0, true, true);
    }

}