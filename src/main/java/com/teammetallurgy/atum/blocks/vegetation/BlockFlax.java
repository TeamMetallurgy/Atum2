package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

import javax.annotation.Nonnull;

public class BlockFlax extends BlockCrops {

    public BlockFlax() {
        super();
    }

    @Override
    @Nonnull
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return super.canSustainBush(state) || state.getBlock() == AtumBlocks.FERTILE_SOIL_TILLED;
    }

    @Override
    @Nonnull
    protected Item getSeed() {
        return AtumItems.FLAX_SEED;
    }

    @Override
    @Nonnull
    protected Item getCrop() {
        return AtumItems.FLAX;
    }
}