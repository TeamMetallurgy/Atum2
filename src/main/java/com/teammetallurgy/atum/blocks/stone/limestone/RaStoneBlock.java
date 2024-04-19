package com.teammetallurgy.atum.blocks.stone.limestone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;

public class RaStoneBlock extends HalfTransparentBlock {
    private static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public RaStoneBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).randomTicks().strength(0.5F).sound(SoundType.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return 3;
    }

    @Override
    @Nonnull
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return PushReaction.NORMAL;
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, RandomSource random) {
        if ((random.nextInt(3) == 0 || this.shouldDisappear(level, pos, 4)) && level.getMaxLocalRawBrightness(pos) > 11 - state.getValue(AGE) - state.getLightBlock(level, pos) && this.slightlyRemove(state, level, pos)) {
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (Direction direction : Direction.values()) {
                mutablePos.set(pos).move(direction);
                BlockState stateDirection = level.getBlockState(mutablePos);
                if (stateDirection.is(this) && !this.slightlyRemove(stateDirection, level, mutablePos)) {
                    level.scheduleTick(mutablePos, this, Mth.nextInt(random, 20, 40));
                }
            }
        } else {
            level.scheduleTick(pos, this, Mth.nextInt(random, 20, 40));
        }
    }

    private boolean slightlyRemove(BlockState state, Level level, BlockPos pos) {
        int age = state.getValue(AGE);
        if (age < 3) {
            level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            return false;
        } else {
            this.turnIntoLava(level, pos);
            return true;
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        if (block == this && this.shouldDisappear(level, pos, 2)) {
            this.turnIntoLava(level, pos);
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    private boolean shouldDisappear(BlockGetter level, BlockPos pos, int neighborsRequired) {
        int i = 0;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            mutablePos.set(pos).move(direction);
            if (level.getBlockState(mutablePos).getBlock() == this) {
                ++i;
                if (i >= neighborsRequired) {
                    return false;
                }
            }
        }
        return true;
    }

    private void turnIntoLava(Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
        level.neighborChanged(pos, Blocks.LAVA, pos);
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull BlockState state, @Nonnull HitResult target, @Nonnull LevelReader level, @Nonnull BlockPos pos, @Nonnull Player player) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(AGE);
    }
}