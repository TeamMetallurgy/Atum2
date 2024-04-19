package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;


public class AtumLadderBlock extends LadderBlock {

    public AtumLadderBlock() {
        super(Block.Properties.of().pushReaction(PushReaction.DESTROY).strength(0.4F).sound(SoundType.LADDER).noOcclusion());
    }
}