package com.teammetallurgy.atum.blocks.stone.limestone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;

import javax.annotation.Nonnull;

public class RaStoneBlock extends HalfTransparentBlock {
    private static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public RaStoneBlock() {
        super(Properties.of(Material.STONE, MaterialColor.COLOR_RED).randomTicks().strength(0.5F).sound(SoundType.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
        return 3;
    }

    @Override
    @Nonnull
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return PushReaction.NORMAL;
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel world, @Nonnull BlockPos pos, RandomSource random) {
        if ((random.nextInt(3) == 0 || this.shouldDisappear(world, pos, 4)) && world.getMaxLocalRawBrightness(pos) > 11 - state.getValue(AGE) - state.getLightBlock(world, pos) && this.slightlyRemove(state, world, pos)) {
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (Direction direction : Direction.values()) {
                mutablePos.set(pos).move(direction);
                BlockState stateDirection = world.getBlockState(mutablePos);
                if (stateDirection.is(this) && !this.slightlyRemove(stateDirection, world, mutablePos)) {
                    world.scheduleTick(mutablePos, this, Mth.nextInt(random, 20, 40));
                }
            }
        } else {
            world.scheduleTick(pos, this, Mth.nextInt(random, 20, 40));
        }
    }

    private boolean slightlyRemove(BlockState state, Level world, BlockPos pos) {
        int age = state.getValue(AGE);
        if (age < 3) {
            world.setBlock(pos, state.setValue(AGE, age + 1), 2);
            return false;
        } else {
            this.turnIntoLava(world, pos);
            return true;
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        if (block == this && this.shouldDisappear(world, pos, 2)) {
            this.turnIntoLava(world, pos);
        }
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
    }

    private boolean shouldDisappear(BlockGetter world, BlockPos pos, int neighborsRequired) {
        int i = 0;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            mutablePos.set(pos).move(direction);
            if (world.getBlockState(mutablePos).getBlock() == this) {
                ++i;
                if (i >= neighborsRequired) {
                    return false;
                }
            }
        }
        return true;
    }

    private void turnIntoLava(Level world, BlockPos pos) {
        world.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
        world.neighborChanged(pos, Blocks.LAVA, pos);
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(AGE);
    }
}