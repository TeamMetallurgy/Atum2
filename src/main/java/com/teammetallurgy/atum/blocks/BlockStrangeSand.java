package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;

public class BlockStrangeSand extends FallingBlock {

    public BlockStrangeSand() {
        super(Block.Properties.create(Material.SAND).hardnessAndResistance(0.5F).sound(SoundType.SAND).tickRandomly().harvestTool(ToolType.SHOVEL).harvestLevel(0));
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull IBlockReader world, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.offset(direction));
        PlantType plantType = plantable.getPlantType(world, pos.up());
        boolean hasWater = (world.getBlockState(pos.east()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.west()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.north()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.south()).getMaterial() == Material.WATER);

        if (plant.getBlock() instanceof CactusBlock || plant.getBlock() == AtumBlocks.ANPUTS_FINGERS) {
            return true;
        }

        switch (plantType) {
            case Desert:
                return true;
            case Beach:
                return hasWater;
            default:
                return super.canSustainPlant(state, world, pos, direction, plantable);
        }
    }
}