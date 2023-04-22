package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class AtumPathBlock extends DirtPathBlock {
    private final Block baseBlock;

    public AtumPathBlock(Block baseBlock) {
        super(BlockBehaviour.Properties.of(baseBlock.defaultBlockState().getMaterial()).strength(0.65F).sound(baseBlock.defaultBlockState().getSoundType()).isViewBlocking(AtumBlocks::always).isSuffocating(AtumBlocks::always).lootFrom(() -> baseBlock));
        this.baseBlock = baseBlock;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? Block.pushEntitiesUp(this.defaultBlockState(), this.baseBlock.defaultBlockState(), context.getLevel(), context.getClickedPos()) : super.getStateForPlacement(context);
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        level.setBlockAndUpdate(pos, pushEntitiesUp(state, this.baseBlock.defaultBlockState(), level, pos));
    }
}