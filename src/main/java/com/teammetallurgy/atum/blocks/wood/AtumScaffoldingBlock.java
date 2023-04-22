package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.api.AtumAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nonnull;

public class AtumScaffoldingBlock extends ScaffoldingBlock {

    public AtumScaffoldingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        int i = getDistance(level, blockpos);
        return this.defaultBlockState().setValue(WATERLOGGED, level.getFluidState(blockpos).getType() == Fluids.WATER).setValue(DISTANCE, i).setValue(BOTTOM, this.hasScaffoldingBelow(level, blockpos, i));
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        int i = getDistance(level, pos);
        BlockState blockstate = state.setValue(DISTANCE, i).setValue(BOTTOM, this.hasScaffoldingBelow(level, pos, i));
        if (blockstate.getValue(DISTANCE) == 7) {
            if (state.getValue(DISTANCE) == 7) {
                FallingBlockEntity.fall(level, pos, blockstate);
            } else {
                level.destroyBlock(pos, true);
            }
        } else if (state != blockstate) {
            level.setBlock(pos, blockstate, 3);
        }

    }

    @Override
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
        return getDistance(level, pos) < 7;
    }

    private boolean hasScaffoldingBelow(BlockGetter blockReader, BlockPos pos, int distance) {
        return distance > 0 && !blockReader.getBlockState(pos.below()).is(this);
    }

    public static int getDistance(BlockGetter reader, BlockPos pos) {
        BlockPos.MutableBlockPos mutablePos = pos.mutable().move(Direction.DOWN);
        BlockState state = reader.getBlockState(mutablePos);
        int distance = 7;
        if (state.is(AtumAPI.Tags.SCAFFOLDING)) {
            distance = state.getValue(DISTANCE);
        } else if (state.isFaceSturdy(reader, mutablePos, Direction.UP)) {
            return 0;
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState blockstate1 = reader.getBlockState(mutablePos.setWithOffset(pos, direction));
            if (blockstate1.is(AtumAPI.Tags.SCAFFOLDING)) {
                distance = Math.min(distance, blockstate1.getValue(DISTANCE) + 1);
                if (distance == 1) {
                    break;
                }
            }
        }
        return distance;
    }

    @Override
    public boolean isScaffolding(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }
}