package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.api.AtumAPI;
import net.minecraft.block.BlockState;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;

public class AtumScaffoldingBlock extends ScaffoldingBlock {

    public AtumScaffoldingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        World world = context.getWorld();
        int i = getDistance(world, blockpos);
        return this.getDefaultState().with(WATERLOGGED, world.getFluidState(blockpos).getFluid() == Fluids.WATER).with(DISTANCE, i).with(BOTTOM, this.hasScaffoldingBelow(world, blockpos, i));
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        int i = getDistance(world, pos);
        BlockState blockstate = state.with(DISTANCE, i).with(BOTTOM, this.hasScaffoldingBelow(world, pos, i));
        if (blockstate.get(DISTANCE) == 7) {
            if (state.get(DISTANCE) == 7) {
                world.addEntity(new FallingBlockEntity(world, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, blockstate.with(WATERLOGGED, Boolean.FALSE)));
            } else {
                world.destroyBlock(pos, true);
            }
        } else if (state != blockstate) {
            world.setBlockState(pos, blockstate, 3);
        }

    }

    @Override
    public boolean isValidPosition(@Nonnull BlockState state, @Nonnull IWorldReader world, @Nonnull BlockPos pos) {
        return getDistance(world, pos) < 7;
    }

    private boolean hasScaffoldingBelow(IBlockReader blockReader, BlockPos pos, int distance) {
        return distance > 0 && !blockReader.getBlockState(pos.down()).isIn(this);
    }

    public static int getDistance(IBlockReader reader, BlockPos pos) {
        BlockPos.Mutable mutablePos = pos.toMutable().move(Direction.DOWN);
        BlockState state = reader.getBlockState(mutablePos);
        int distance = 7;
        if (state.isIn(AtumAPI.Tags.SCAFFOLDING)) {
            distance = state.get(DISTANCE);
        } else if (state.isSolidSide(reader, mutablePos, Direction.UP)) {
            return 0;
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState blockstate1 = reader.getBlockState(mutablePos.setAndMove(pos, direction));
            if (blockstate1.isIn(AtumAPI.Tags.SCAFFOLDING)) {
                distance = Math.min(distance, blockstate1.get(DISTANCE) + 1);
                if (distance == 1) {
                    break;
                }
            }
        }
        return distance;
    }

    @Override
    public boolean isScaffolding(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }
}