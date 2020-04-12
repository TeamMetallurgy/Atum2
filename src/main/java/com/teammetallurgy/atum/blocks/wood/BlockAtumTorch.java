package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;

public class BlockAtumTorch extends BlockTorch {

    public BlockAtumTorch() {
        super();
        this.setHardness(0.0F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(null);
        this.setLightLevel(0.9375F);
    }
}