package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.BlockLadder;
import net.minecraft.block.SoundType;

public class BlockAtumLadder extends BlockLadder {

    public BlockAtumLadder() {
        super();
        this.setHardness(0.4F);
        this.setSoundType(SoundType.LADDER);
    }
}