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
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SandLayersBlock extends FallingBlock {
    private static final Material SAND_LAYER = new Material.Builder(MaterialColor.SAND).doesNotBlockMovement().notOpaque().notSolid().pushDestroys().replaceable().build();
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS_1_8;
    private static final VoxelShape[] SAND_SHAPE = new VoxelShape[]{VoxelShapes.empty(), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public SandLayersBlock() {
        super(Block.Properties.create(SAND_LAYER).hardnessAndResistance(0.1F).sound(SoundType.SAND).harvestTool(ToolType.SHOVEL).harvestLevel(0));
        this.setDefaultState(this.stateContainer.getBaseState().with(LAYERS, 1));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return SAND_SHAPE[state.get(LAYERS)];
    }

    @Override
    @Nonnull
    public VoxelShape getCollisionShape(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return SAND_SHAPE[state.get(LAYERS) - 1];
    }

    @Override
    @Nonnull
    public VoxelShape getCollisionShape(BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return SAND_SHAPE[state.get(LAYERS)];
    }

    @Override
    @Nonnull
    public VoxelShape getRayTraceShape(BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return SAND_SHAPE[state.get(LAYERS)];
    }

    @Override
    public boolean allowsMovement(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull PathType pathType) {
        if (pathType == PathType.LAND) {
            return state.get(LAYERS) < 5;
        } else {
            return false;
        }
    }

    @Override
    public boolean isTransparent(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public boolean isValidPosition(@Nonnull BlockState state, @Nonnull IWorldReader world, BlockPos pos) {
        BlockState stateDown = world.getBlockState(pos.down());
        if (!stateDown.isIn(Blocks.ICE) && !stateDown.isIn(Blocks.PACKED_ICE) && !stateDown.isIn(Blocks.BARRIER)) {
            if (!stateDown.isIn(Blocks.HONEY_BLOCK) && !stateDown.isIn(Blocks.SOUL_SAND)) {
                return Block.doesSideFillSquare(stateDown.getCollisionShape(world, pos.down()), Direction.UP) || stateDown.getBlock() == this && stateDown.get(LAYERS) == 8;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    public BlockState updatePostPlacement(BlockState state, @Nonnull Direction direction, @Nonnull BlockState facingState, @Nonnull IWorld world, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        return !state.isValidPosition(world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, direction, facingState, world, currentPos, facingPos);
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        int layers = state.get(LAYERS);
        if (useContext.getItem().getItem() == this.asItem() && layers < 8) {
            if (useContext.replacingClickedOnBlock()) {
                return useContext.getFace() == Direction.UP;
            } else {
                return true;
            }
        } else {
            return layers == 1;
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