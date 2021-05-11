package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassPathBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Random;

public class AtumPathBlock extends GrassPathBlock {
    private final Block baseBlock;

    public AtumPathBlock(Block baseBlock) {
        super(AbstractBlock.Properties.create(baseBlock.getDefaultState().getMaterial()).hardnessAndResistance(0.65F).sound(baseBlock.getDefaultState().getSoundType()).setBlocksVision(AtumBlocks::needsPostProcessing).setSuffocates(AtumBlocks::needsPostProcessing).harvestTool(ToolType.SHOVEL).lootFrom(baseBlock));
        this.baseBlock = baseBlock;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return !this.getDefaultState().isValidPosition(context.getWorld(), context.getPos()) ? Block.nudgeEntitiesWithNewState(this.getDefaultState(), this.baseBlock.getDefaultState(), context.getWorld(), context.getPos()) : super.getStateForPlacement(context);
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        world.setBlockState(pos, nudgeEntitiesWithNewState(state, this.baseBlock.getDefaultState(), world, pos));
    }
}