package com.teammetallurgy.atum.blocks.stone.limestone;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;

public class LimestoneGravelBlock extends GravelBlock {

    public LimestoneGravelBlock() {
        super(Block.Properties.of(Material.SAND).strength(0.6F).sound(SoundType.GRAVEL).harvestTool(ToolType.SHOVEL).harvestLevel(0));
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull BlockGetter world, BlockPos pos, @Nonnull Direction facing, IPlantable plantable) {
        PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
        return plantType == PlantType.DESERT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getDustColor(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return -2370656;
    }
}