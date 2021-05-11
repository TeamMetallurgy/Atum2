package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;

public class EmmerBlock extends CropsBlock {

    public EmmerBlock() {
        super(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.CROP));
    }

    @Override
    @Nonnull
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.CROP;
    }

    @Override
    protected boolean isValidGround(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
        return state.getBlock() instanceof FarmlandBlock;
    }

    @Override
    @Nonnull
    protected IItemProvider getSeedsItem() {
        return AtumItems.EMMER_SEEDS;
    }
}