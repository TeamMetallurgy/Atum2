package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SandLayersBlock extends FallingBlock {
    private static final Material SAND_LAYER = new Material.Builder(MaterialColor.SAND).notSolid().pushDestroys().replaceable().build();
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS_1_8;
    private static final VoxelShape[] SAND_SHAPE = new VoxelShape[]{Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    public SandLayersBlock() {
        super(Block.Properties.create(SAND_LAYER).tickRandomly().hardnessAndResistance(0.1F).sound(SoundType.SAND).harvestTool(ToolType.SHOVEL).harvestLevel(0));
        this.setDefaultState(this.stateContainer.getBaseState().with(LAYERS, 1));
    }

    @Override
    @Nonnull
    public VoxelShape getRenderShape(BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return SAND_SHAPE[state.get(LAYERS)];
    }

    @Override
    @Nonnull
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, ISelectionContext context) {
        int layer = state.get(LAYERS) - 2;
        layer = MathHelper.clamp(layer, 0, 8);
        return SAND_SHAPE[state.get(LAYERS) - layer];
    }

    @Override
    public boolean allowsMovement(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, PathType pathType) {
        if (pathType == PathType.LAND) {
            return state.get(LAYERS) < 5;
        } else {
            return false;
        }
    }

    @Override
    public boolean func_220074_n(BlockState state) {
        return true;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader reader, BlockPos pos) {
        BlockState stateDown = reader.getBlockState(pos.down());
        Block blockDown = stateDown.getBlock();
        if (blockDown != Blocks.ICE && blockDown != Blocks.PACKED_ICE && blockDown != Blocks.BARRIER) {
            return Block.doesSideFillSquare(stateDown.getCollisionShape(reader, pos.down()), Direction.UP) || blockDown == this && stateDown.get(LAYERS) == 8;
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState facingState, IWorld world, @Nonnull BlockPos currentPos, BlockPos facingPos) {
        return !state.isValidPosition(world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, direction, facingState, world, currentPos, facingPos);
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext context) {
        int layer = state.get(LAYERS);
        if (context.getItem().getItem() == this.asItem() && layer < 8) {
            if (context.replacingClickedOnBlock()) {
                return context.getFace() == Direction.UP;
            } else {
                return true;
            }
        } else {
            return layer == 1;
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = context.getWorld().getBlockState(context.getPos());
        if (state.getBlock() == this) {
            int layer = state.get(LAYERS);
            return layer == 7 ? AtumBlocks.SAND.getDefaultState() : state.with(LAYERS, Math.min(7, layer + 1));
        } else {
            return super.getStateForPlacement(context);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(LAYERS);
    }
}