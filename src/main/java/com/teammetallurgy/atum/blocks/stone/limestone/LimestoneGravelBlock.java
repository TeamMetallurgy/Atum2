package com.teammetallurgy.atum.blocks.stone.limestone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;

public class LimestoneGravelBlock extends GravelBlock {

    public LimestoneGravelBlock() {
        super(Block.Properties.of(Material.SAND).strength(0.6F).sound(SoundType.GRAVEL));
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull BlockGetter level, BlockPos pos, @Nonnull Direction facing, IPlantable plantable) {
        PlantType plantType = plantable.getPlantType(level, pos.relative(facing));
        return plantType == PlantType.DESERT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getDustColor(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return -2370656;
    }
}