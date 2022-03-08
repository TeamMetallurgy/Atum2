package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class PalmLog extends RotatedPillarBlock {

    public PalmLog() {
        super(BlockBehaviour.Properties.of(Material.WOOD, (state) -> MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD));
    }

    @Override
    public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction) {
        return toolAction == ToolActions.AXE_STRIP ? AtumBlocks.STRIPPED_PALM_LOG.get().defaultBlockState() : super.getToolModifiedState(state, world, pos, player, stack, toolAction);
    }
}