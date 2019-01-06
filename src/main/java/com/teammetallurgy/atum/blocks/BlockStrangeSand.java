package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;

public class BlockStrangeSand extends BlockFalling implements IOreDictEntry {
    public BlockStrangeSand() {
        super(Material.SAND);
        this.setTickRandomly(true);
        this.setSoundType(SoundType.SAND);
        this.setHardness(0.5F);
    }

    @Override
    public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, BlockPos pos, @Nonnull EnumFacing direction, IPlantable plantable) {
        IBlockState plant = plantable.getPlant(world, pos.offset(direction));
        EnumPlantType plantType = plantable.getPlantType(world, pos.up());
        boolean hasWater = (world.getBlockState(pos.east()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.west()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.north()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.south()).getMaterial() == Material.WATER);

        if (plant.getBlock() instanceof BlockCactus || plant.getBlock() == AtumBlocks.ANPUTS_FINGERS) {
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

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "sand");
    }
}