package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class OasisGrassBlock extends BushBlock {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public OasisGrassBlock() {
        super(Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().hardnessAndResistance(0.0F).sound(SoundType.PLANT));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isReplaceable(BlockState state, @Nonnull BlockItemUseContext context) {
        return true;
    }

    @Override
    @Nonnull
    public Block.OffsetType getOffsetType() {
        return Block.OffsetType.XYZ;
    }
}