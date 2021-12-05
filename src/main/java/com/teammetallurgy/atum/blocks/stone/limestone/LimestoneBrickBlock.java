package com.teammetallurgy.atum.blocks.stone.limestone;

import com.teammetallurgy.atum.blocks.base.IUnbreakable;
import com.teammetallurgy.atum.blocks.machines.KilnBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Random;

public class LimestoneBrickBlock extends FallingBlock implements IUnbreakable {
    public static final BooleanProperty CAN_FALL = BooleanProperty.create("can_fall");

    public LimestoneBrickBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(1.5F, 8.0F).setRequiresTool().harvestTool(ToolType.PICKAXE).harvestLevel(0));
        this.setDefaultState(this.stateContainer.getBaseState().with(UNBREAKABLE, false).with(CAN_FALL, false));
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (state.getBlock() == AtumBlocks.LIMESTONE_BRICK_SMALL) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos checkPos = pos.add(dx, dy, dz);
                        BlockState kilnState = world.getBlockState(checkPos);
                        if (kilnState.getBlock() == AtumBlocks.KILN) {
                            KilnBlock kiln = (KilnBlock) kilnState.getBlock();
                            kiln.tryMakeMultiblock(world, checkPos, kilnState);
                        }
                    }
                }
            }
        }
    }

    @Override
    public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
        return world.getBlockState(pos).get(UNBREAKABLE) ? 6000000.0F : super.getExplosionResistance(state, world, pos, explosion);
    }

    @Override
    public void onBlockAdded(BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
        if (state.get(CAN_FALL)) {
            super.onBlockAdded(state, world, pos, oldState, isMoving);
        }
    }

    @Override
    @Nonnull
    public BlockState updatePostPlacement(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull IWorld world, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        return state.get(CAN_FALL) ? super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos) : state;
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (state.hasProperty(CAN_FALL) && state.get(CAN_FALL)) {
            super.tick(state, world, pos, rand);
        }
    }

    @Override
    public void onEndFalling(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState fallingState, @Nonnull BlockState hitState, @Nonnull FallingBlockEntity fallingBlock) {
        super.onEndFalling(world, pos, fallingState, hitState, fallingBlock);
        if (fallingState.get(CAN_FALL)) {
            world.setBlockState(pos, fallingState.with(CAN_FALL, false), 2);
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_FALL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void animateTick(BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (state.hasProperty(CAN_FALL) && state.get(CAN_FALL)) {
            super.animateTick(state, world, pos, rand);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(UNBREAKABLE, CAN_FALL);
    }

    @Override
    public int getDustColor(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return -2370656;
    }
}