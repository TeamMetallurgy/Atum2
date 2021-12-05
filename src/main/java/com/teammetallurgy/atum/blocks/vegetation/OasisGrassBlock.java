package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class OasisGrassBlock extends BushBlock {
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public OasisGrassBlock() {
        super(Properties.of(Material.REPLACEABLE_PLANT).noCollission().strength(0.0F).sound(SoundType.GRASS));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canBeReplaced(@Nonnull BlockState state, @Nonnull BlockPlaceContext context) {
        return true;
    }

    @Override
    @Nonnull
    public Block.OffsetType getOffsetType() {
        return Block.OffsetType.XYZ;
    }
}