package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.LadderBlock;
import net.minecraft.block.SoundType;

public class AtumLadderBlock extends LadderBlock {

    public AtumLadderBlock() {
        super();
        this.setHardness(0.4F);
        this.setSoundType(SoundType.LADDER);
    }
}