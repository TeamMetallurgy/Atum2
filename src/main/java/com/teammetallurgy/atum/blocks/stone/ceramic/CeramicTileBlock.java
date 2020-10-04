package com.teammetallurgy.atum.blocks.stone.ceramic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nonnull;

public class CeramicTileBlock extends CeramicBlock {
    private static final VoxelShape TILE_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public CeramicTileBlock(Properties properties) {
        super(properties.hardnessAndResistance(0.5F));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext selectionContext) {
        return TILE_SHAPE;
    }

    @Override
    @Nonnull
    public BlockState updatePostPlacement(BlockState state, @Nonnull Direction direction, @Nonnull BlockState facingState, @Nonnull IWorld world, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        return !state.isValidPosition(world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, direction, facingState, world, currentPos, facingPos);
    }

    @Override
    public boolean isValidPosition(@Nonnull BlockState state, IWorldReader reader, BlockPos pos) {
        return !reader.isAirBlock(pos.down());
    }
}