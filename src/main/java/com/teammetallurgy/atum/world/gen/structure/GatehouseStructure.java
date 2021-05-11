package com.teammetallurgy.atum.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.structure.JigsawStructure;
import net.minecraft.world.gen.feature.structure.VillageConfig;

public class GatehouseStructure extends JigsawStructure {

    public GatehouseStructure(Codec<VillageConfig> config) {
        super(config, 0, true, true);
    }

}