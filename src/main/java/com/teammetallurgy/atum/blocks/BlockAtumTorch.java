package com.teammetallurgy.atum.blocks;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;

public class BlockAtumTorch extends BlockTorch {
    public BlockAtumTorch() {
        super();
        this.setHardness(0.0F);
        this.setLightLevel(0.9375F);
        this.setSoundType(SoundType.WOOD);
    }
}