package com.teammetallurgy.atum.blocks.stone.limestone;

import com.mojang.serialization.MapCodec;
import com.teammetallurgy.atum.blocks.base.IUnbreakable;
import com.teammetallurgy.atum.blocks.machines.KilnBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nonnull;

public class LimestoneBrickBlock extends FallingBlock implements IUnbreakable {
    public static final MapCodec<LimestoneBrickBlock> CODEC = simpleCodec(LimestoneBrickBlock::new);
    public static final BooleanProperty CAN_FALL = BooleanProperty.create("can_fall");

    public LimestoneBrickBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(UNBREAKABLE, false).setValue(CAN_FALL, false));
    }

    @Override
    @Nonnull
    protected MapCodec<? extends LimestoneBrickBlock> codec() {
        return CODEC;
    }

    @Override
    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (state.getBlock() == AtumBlocks.LIMESTONE_BRICK_SMALL.get()) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos checkPos = pos.offset(dx, dy, dz);
                        BlockState kilnState = level.getBlockState(checkPos);
                        if (kilnState.getBlock() == AtumBlocks.KILN.get()) {
                            KilnBlock kiln = (KilnBlock) kilnState.getBlock();
                            kiln.tryMakeMultiblock(level, checkPos, kilnState);
                        }
                    }
                }
            }
        }
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return level.getBlockState(pos).getValue(UNBREAKABLE) ? 6000000.0F : super.getExplosionResistance(state, level, pos, explosion);
    }

    @Override
    public void onPlace(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
        if (state.getValue(CAN_FALL)) {
            super.onPlace(state, level, pos, oldState, isMoving);
        }
    }

    @Override
    @Nonnull
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        return state.getValue(CAN_FALL) ? super.updateShape(state, facing, facingState, level, currentPos, facingPos) : state;
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        if (state.hasProperty(CAN_FALL) && state.getValue(CAN_FALL)) {
            super.tick(state, level, pos, rand);
        }
    }

    @Override
    public void onLand(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState fallingState, @Nonnull BlockState hitState, @Nonnull FallingBlockEntity fallingBlock) {
        super.onLand(level, pos, fallingState, hitState, fallingBlock);
        if (fallingState.getValue(CAN_FALL)) {
            level.setBlock(pos, fallingState.setValue(CAN_FALL, false), 2);
            level.playSound(null, pos, SoundEvents.STONE_FALL, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void animateTick(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        if (state.hasProperty(CAN_FALL) && state.getValue(CAN_FALL)) {
            super.animateTick(state, level, pos, rand);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(UNBREAKABLE, CAN_FALL);
    }

    @Override
    public int getDustColor(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return -2370656;
    }
}