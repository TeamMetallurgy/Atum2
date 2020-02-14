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
    private static final VoxelShape TALL_GRASS_AABB = Block.makeCuboidShape(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public OasisGrassBlock() {
        super(Properties.create(Material.TALL_PLANTS).sound(SoundType.PLANT));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return TALL_GRASS_AABB;
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