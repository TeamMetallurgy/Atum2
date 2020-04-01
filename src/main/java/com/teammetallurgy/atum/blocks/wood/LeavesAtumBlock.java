package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;

public class LeavesAtumBlock extends LeavesBlock {

    public LeavesAtumBlock() {
        super(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid());
    }

    @Override
    public void randomTick(BlockState state, @Nonnull ServerWorld world, @Nonnull BlockPos pos, Random random) { //TODO Check if leaves stays when they should
        if (!state.get(PERSISTENT) && state.get(DISTANCE) >= 3) {
            spawnDrops(state, world, pos);
            world.removeBlock(pos, false);
        }
    }
}