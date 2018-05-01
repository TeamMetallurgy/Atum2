package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
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
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        if (world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)) >> 3 == 1) {
            return world.getBlockState(pos.down()).getBlock() == AtumBlocks.FERTILE_SOIL;
        } else {
            Block soil = world.getBlockState(pos.down()).getBlock();
            return (world.getLight(pos) >= 8 || world.canSeeSky(pos)) && soil.canSustainPlant(state, world, pos.down(), net.minecraft.util.EnumFacing.UP, this);
        }
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == Blocks.FARMLAND || state.getBlock() == AtumBlocks.FERTILE_SOIL_TILLED;
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