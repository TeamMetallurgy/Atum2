package com.teammetallurgy.atum.blocks.stone.limestone;

import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;

public class BlockLimestoneGravel extends BlockGravel {

    public BlockLimestoneGravel() {
        super();
        this.setHardness(0.6F);
        this.setSoundType(SoundType.GROUND);
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull IBlockAccess world, BlockPos pos, @Nonnull Direction facing, IPlantable plantable) {
        EnumPlantType plantType = plantable.getPlantType(world, pos.offset(facing));
        return plantType == EnumPlantType.Desert;
    }

    @Override
    @Nonnull
    public MapColor getMapColor(BlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.SAND;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getDustColor(BlockState state) {
        return -2370656;
    }
}