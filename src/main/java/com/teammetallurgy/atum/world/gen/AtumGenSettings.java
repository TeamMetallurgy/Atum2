package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationSettings;

public class AtumGenSettings extends GenerationSettings {

    public AtumGenSettings() {
        this.setDefaultBlock(AtumBlocks.LIMESTONE.getDefaultState());
        this.setDefaultFluid(Blocks.AIR.getDefaultState());
    }

    public int getBiomeSize() {
        return 4;
    }

    public int getRiverSize() {
        return 4;
    }

    @Override
    public int getBedrockFloorHeight() {
        return 0;
    }
}