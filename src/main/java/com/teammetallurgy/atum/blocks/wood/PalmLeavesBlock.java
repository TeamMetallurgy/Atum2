package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.Random;

public class PalmLeavesBlock extends LeavesAtumBlock implements BonemealableBlock {

    public PalmLeavesBlock() {
        super();
    }

    @Override
    public void randomTick(BlockState state, @Nonnull ServerLevel world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        super.randomTick(state, world, pos, rand);
        if (!world.isClientSide) {
            if (world.random.nextDouble() <= 0.05F) {
                if (isValidBonemealTarget(world, pos, state, false)) {
                    world.setBlockAndUpdate(pos.below(), AtumBlocks.DATE_BLOCK.defaultBlockState());
                }
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(@Nonnull BlockGetter reader, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return !state.getValue(PERSISTENT) && isValidLocation(reader, pos.below()) && reader.getBlockState(pos.below()).isAir();
    }

    private boolean isValidLocation(@Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        for (int i = 0; i < 4; i++) {
            Direction horizontal = Direction.from2DDataValue((5 - i) % 4); //[W, S, E, N]
            BlockPos check = pos.relative(horizontal);
            if (reader.getBlockState(check).getBlock() == AtumBlocks.PALM_LOG) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBonemealSuccess(@Nonnull Level world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@Nonnull ServerLevel world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        if (isValidBonemealTarget(world, pos, state, false) && rand.nextDouble() <= 0.5D) {
            world.setBlockAndUpdate(pos.below(), AtumBlocks.DATE_BLOCK.defaultBlockState());
        }
    }
}