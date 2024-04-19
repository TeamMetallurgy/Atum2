package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;


public class LeavesAtumBlock extends LeavesBlock {

    public LeavesAtumBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(AtumBlocks::allowsSpawnOnLeaves).isSuffocating(AtumBlocks::never).isViewBlocking(AtumBlocks::never));
    }
}