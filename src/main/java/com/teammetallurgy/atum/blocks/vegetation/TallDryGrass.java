package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.PlantType;

import javax.annotation.Nonnull;

public class TallDryGrass extends DoublePlantBlock {

    public TallDryGrass() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).noCollission().instabreak().sound(SoundType.GRASS));
    }

    @Override
    @Nonnull
    public PlantType getPlantType(@Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return PlantType.DESERT;
    }
}