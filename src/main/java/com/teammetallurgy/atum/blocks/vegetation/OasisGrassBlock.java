package com.teammetallurgy.atum.blocks.vegetation;

import com.mojang.serialization.MapCodec;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class OasisGrassBlock extends BushBlock {
    public static final MapCodec<OasisGrassBlock> CODEC = simpleCodec(OasisGrassBlock::new);
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public OasisGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    protected MapCodec<? extends OasisGrassBlock> codec() {
        return CODEC;
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canBeReplaced(@Nonnull BlockState state, @Nonnull BlockPlaceContext context) {
        return true;
    }
}