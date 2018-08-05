package com.teammetallurgy.atum.blocks.limestone;

import net.minecraft.block.BlockClay;
import net.minecraft.block.SoundType;

public class BlockSandyClay extends BlockClay {

    public BlockSandyClay() {
        super();
        this.setHardness(0.6F);
        this.setSoundType(SoundType.GROUND);
    }
}