package com.teammetallurgy.atum.blocks.stone.limestone;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class RaStoneBlock extends BreakableBlock {
    private static final IntegerProperty AGE = BlockStateProperties.AGE_0_3;

    public RaStoneBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.RED).tickRandomly().hardnessAndResistance(0.5F).sound(SoundType.STONE));
        this.setDefaultState(this.stateContainer.getBaseState().with(AGE, 0));
    }

    @Override
    public int getOpacity(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
        return 3;
    }

    @Override
    @Nonnull
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    @Override
    @Nonnull
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void tick(BlockState state, World world, BlockPos pos, Random random) {
        if ((random.nextInt(3) == 0 || this.shouldDisappear(world, pos, 4)) && world.getLight(pos) > 11 - state.get(AGE) - state.getOpacity(world, pos) && this.slightlyRemove(state, world, pos)) {
            try (BlockPos.PooledMutableBlockPos mutablePos = BlockPos.PooledMutableBlockPos.retain()) {
                for (Direction direction : Direction.values()) {
                    mutablePos.setPos(pos).move(direction);
                    BlockState stateDirection = world.getBlockState(mutablePos);
                    if (stateDirection.getBlock() == this && !this.slightlyRemove(stateDirection, world, mutablePos)) {
                        world.getPendingBlockTicks().scheduleTick(mutablePos, this, MathHelper.nextInt(random, 20, 40));
                    }
                }
            }

        } else {
            world.getPendingBlockTicks().scheduleTick(pos, this, MathHelper.nextInt(random, 20, 40));
        }
    }

    private boolean slightlyRemove(BlockState state, World world, BlockPos pos) {
        int age = state.get(AGE);
        if (age < 3) {
            world.setBlockState(pos, state.with(AGE, age + 1), 2);
            return false;
        } else {
            this.turnIntoLava(world, pos);
            return true;
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (block == this && this.shouldDisappear(world, pos, 2)) {
            this.turnIntoLava(world, pos);
        }
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
    }

    private boolean shouldDisappear(IBlockReader world, BlockPos pos, int neighborsRequired) {
        int i = 0;
        try (BlockPos.PooledMutableBlockPos mutablePos = BlockPos.PooledMutableBlockPos.retain()) {
            for (Direction direction : Direction.values()) {
                mutablePos.setPos(pos).move(direction);
                if (world.getBlockState(mutablePos).getBlock() == this) {
                    ++i;
                    if (i >= neighborsRequired) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private void turnIntoLava(World world, BlockPos pos) {
        world.setBlockState(pos, Blocks.LAVA.getDefaultState());
        world.neighborChanged(pos, Blocks.LAVA, pos);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(AGE);
    }
}