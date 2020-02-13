package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;

public class EmmerBlock extends CropsBlock {

    @Override
    @Nonnull
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.Crop;
    }

    @Override
    protected boolean canSustainBush(BlockState state) {
        return state.getBlock() instanceof BlockFarmland;
    }

    @Override
    @Nonnull
    protected Item getSeed() {
        return AtumItems.EMMER_SEEDS;
    }

    @Override
    @Nonnull
    protected Item getCrop() {
        return AtumItems.EMMER;
    }
}