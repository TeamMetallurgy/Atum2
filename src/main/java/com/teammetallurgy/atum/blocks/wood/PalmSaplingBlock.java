package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.gen.feature.tree.PalmTree;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class PalmSaplingBlock extends SaplingBlock implements BonemealableBlock {
    private static final VoxelShape PALM_SAPLING_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D);

    public PalmSaplingBlock() {
        super(new PalmTree(), Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0.0F).sound(SoundType.GRASS));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos, @Nonnull CollisionContext selectionContext) {
        return PALM_SAPLING_AABB;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return state.getBlock() == AtumBlocks.FERTILE_SOIL.get() || super.mayPlaceOn(state, reader, pos);
    }
}