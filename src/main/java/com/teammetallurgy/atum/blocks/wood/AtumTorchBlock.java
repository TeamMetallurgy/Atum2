package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;

public class AtumTorchBlock extends TorchBlock {

    public AtumTorchBlock() {
        super();
        this.setHardness(0.0F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(null);
        this.setLightLevel(0.9375F);
    }
}