package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;

public class EmmerBlock extends CropBlock {

    public EmmerBlock() {
        super(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0.0F).sound(SoundType.CROP));
    }

    @Override
    @Nonnull
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.CROP;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return state.getBlock() instanceof FarmBlock;
    }

    @Override
    @Nonnull
    protected ItemLike getBaseSeedId() {
        return AtumItems.EMMER_SEEDS.get();
    }
}