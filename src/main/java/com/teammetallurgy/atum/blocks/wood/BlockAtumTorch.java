package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;

public class BlockAtumTorch extends TorchBlock {

    public BlockAtumTorch() {
        super();
        this.setHardness(0.0F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(null);
        this.setLightLevel(0.9375F);
    }
}