package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.gen.feature.config.PalmTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class PalmSaplingBlock extends SaplingBlock implements IGrowable {
    private static final VoxelShape PALM_SAPLING_AABB = Block.makeCuboidShape(0.4125D, 0.0D, 0.4125D, 0.6D, 0.5D, 0.6D);

    public PalmSaplingBlock() {
        super(new PalmTree(), Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.PLANT));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext selectionContext) {
        return PALM_SAPLING_AABB;
    }

    @Override
    protected boolean isValidGround(BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return state.getBlock() == AtumBlocks.FERTILE_SOIL || super.isValidGround(state, reader, pos);
    }
}