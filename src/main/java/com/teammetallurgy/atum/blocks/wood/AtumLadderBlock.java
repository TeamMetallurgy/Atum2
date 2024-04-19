package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.SoundType;


public class AtumLadderBlock extends LadderBlock {

    public AtumLadderBlock() {
        super(Block.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER).noOcclusion());
    }
}