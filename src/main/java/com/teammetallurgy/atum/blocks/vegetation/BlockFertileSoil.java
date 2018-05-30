package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockFertileSoil extends Block {

    public BlockFertileSoil() {
        super(Material.GROUND);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.GROUND);
        this.setTickRandomly(true);
        this.setLightOpacity(255);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (!world.isRemote) {
            if (!hasWater(world, pos)) {
                world.setBlockState(pos, AtumBlocks.SAND.getDefaultState(), 2);
            }
        }
    }

    private boolean hasWater(World world, BlockPos pos) {
        for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 4, 4))) {
            if (world.getBlockState(mutableBlockPos).getMaterial() == Material.WATER) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, BlockPos pos, @Nonnull EnumFacing direction, IPlantable plantable) {
        EnumPlantType plantType = plantable.getPlantType(world, pos.up());

        boolean hasWater = (world.getBlockState(pos.east()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.west()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.north()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.south()).getMaterial() == Material.WATER);

        switch (plantType) {
            case Plains:
                return hasWater;
            case Beach:
                return hasWater;
            default:
                return super.canSustainPlant(state, world, pos, direction, plantable);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
        super.neighborChanged(state, world, pos, neighborBlock, neighborPos);
        if (world.getBlockState(pos.up()).getMaterial().isSolid()) {
            world.setBlockState(pos, AtumBlocks.SAND.getDefaultState());
        }
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    @Nonnull
    protected ItemStack getSilkTouchDrop(@Nonnull IBlockState state) {
        return new ItemStack(AtumBlocks.FERTILE_SOIL);
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return AtumItems.FERTILE_SOIL_PILE;
    }

    @Override
    public int quantityDropped(Random random) {
        return MathHelper.getInt(random, 3, 5);
    }
}