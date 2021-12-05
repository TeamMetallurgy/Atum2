package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.GrassPathBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Random;

public class AtumPathBlock extends GrassPathBlock {
    private final Block baseBlock;

    public AtumPathBlock(Block baseBlock) {
        super(BlockBehaviour.Properties.of(baseBlock.defaultBlockState().getMaterial()).strength(0.65F).sound(baseBlock.defaultBlockState().getSoundType()).isViewBlocking(AtumBlocks::needsPostProcessing).isSuffocating(AtumBlocks::needsPostProcessing).harvestTool(ToolType.SHOVEL).dropsLike(baseBlock));
        this.baseBlock = baseBlock;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? Block.pushEntitiesUp(this.defaultBlockState(), this.baseBlock.defaultBlockState(), context.getLevel(), context.getClickedPos()) : super.getStateForPlacement(context);
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        world.setBlockAndUpdate(pos, pushEntitiesUp(state, this.baseBlock.defaultBlockState(), world, pos));
    }
}