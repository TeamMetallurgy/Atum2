package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class PalmLeavesBlock extends LeavesAtumBlock implements BonemealableBlock {

    public PalmLeavesBlock() {
        super();
    }

    @Override
    public void randomTick(BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        super.randomTick(state, level, pos, rand);
        if (!level.isClientSide) {
            if (level.random.nextDouble() <= 0.05F) {
                if (isValidBonemealTarget(level, pos, state, false)) {
                    level.setBlockAndUpdate(pos.below(), AtumBlocks.DATE_BLOCK.get().defaultBlockState());
                }
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(@Nonnull LevelReader reader, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return !state.getValue(PERSISTENT) && isValidLocation(reader, pos.below()) && reader.getBlockState(pos.below()).isAir();
    }

    private boolean isValidLocation(@Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        for (int i = 0; i < 4; i++) {
            Direction horizontal = Direction.from2DDataValue((5 - i) % 4); //[W, S, E, N]
            BlockPos check = pos.relative(horizontal);
            if (reader.getBlockState(check).getBlock() == AtumBlocks.PALM_LOG.get()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBonemealSuccess(@Nonnull Level level, @Nonnull RandomSource rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@Nonnull ServerLevel level, @Nonnull RandomSource rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        if (isValidBonemealTarget(level, pos, state, false) && rand.nextDouble() <= 0.5D) {
            level.setBlockAndUpdate(pos.below(), AtumBlocks.DATE_BLOCK.get().defaultBlockState());
        }
    }
}