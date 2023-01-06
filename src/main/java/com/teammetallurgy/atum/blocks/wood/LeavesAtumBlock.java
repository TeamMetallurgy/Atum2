package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nonnull;

public class LeavesAtumBlock extends LeavesBlock {

    public LeavesAtumBlock() {
        super(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(AtumBlocks::allowsSpawnOnLeaves).isSuffocating(AtumBlocks::never).isViewBlocking(AtumBlocks::never));
    }

    @Override
    public void randomTick(BlockState state, @Nonnull ServerLevel world, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (!state.getValue(PERSISTENT) && state.getValue(DISTANCE) >= 3) {
            dropResources(state, world, pos);
            world.removeBlock(pos, false);
        }
    }
}