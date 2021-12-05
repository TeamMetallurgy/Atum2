package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.api.AtumAPI;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class AtumScaffoldingBlock extends ScaffoldingBlock {

    public AtumScaffoldingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level world = context.getLevel();
        int i = getDistance(world, blockpos);
        return this.defaultBlockState().setValue(WATERLOGGED, world.getFluidState(blockpos).getType() == Fluids.WATER).setValue(DISTANCE, i).setValue(BOTTOM, this.hasScaffoldingBelow(world, blockpos, i));
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerLevel world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        int i = getDistance(world, pos);
        BlockState blockstate = state.setValue(DISTANCE, i).setValue(BOTTOM, this.hasScaffoldingBelow(world, pos, i));
        if (blockstate.getValue(DISTANCE) == 7) {
            if (state.getValue(DISTANCE) == 7) {
                world.addFreshEntity(new FallingBlockEntity(world, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, blockstate.setValue(WATERLOGGED, Boolean.FALSE)));
            } else {
                world.destroyBlock(pos, true);
            }
        } else if (state != blockstate) {
            world.setBlock(pos, blockstate, 3);
        }

    }

    @Override
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader world, @Nonnull BlockPos pos) {
        return getDistance(world, pos) < 7;
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
    public boolean isScaffolding(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }
}