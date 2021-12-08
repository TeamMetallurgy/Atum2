package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nonnull;

public class StrangeSandBlock extends FallingBlock {

    public StrangeSandBlock() {
        super(Block.Properties.of(Material.SAND).strength(0.5F).sound(SoundType.SAND).randomTicks());
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull BlockGetter world, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.relative(direction));
        PlantType plantType = plantable.getPlantType(world, pos.above());
        boolean hasWater = (world.getBlockState(pos.east()).getFluidState().is(FluidTags.WATER) ||
                world.getBlockState(pos.west()).getFluidState().is(FluidTags.WATER)||
                world.getBlockState(pos.north()).getFluidState().is(FluidTags.WATER) ||
                world.getBlockState(pos.south()).getFluidState().is(FluidTags.WATER));

        if (plant.getBlock() instanceof CactusBlock || plant.getBlock() == AtumBlocks.ANPUTS_FINGERS) {
            return true;
        }

        if (plantType.equals(PlantType.DESERT)) {
            return true;
        } else if (plantType.equals(PlantType.BEACH)) {
            return hasWater;
        } else {
            return super.canSustainPlant(state, world, pos, direction, plantable);
        }
    }

    @Override
    public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction) {
        return toolAction == ToolActions.SHOVEL_FLATTEN ? AtumBlocks.STRANGE_SAND_PATH.defaultBlockState() : super.getToolModifiedState(state, world, pos, player, stack, toolAction);
    }
}