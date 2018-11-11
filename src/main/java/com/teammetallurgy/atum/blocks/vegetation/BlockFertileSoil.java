package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
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
        super(Material.GRASS);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.GROUND);
        this.setTickRandomly(true);
        this.setLightOpacity(255);
    }

    @Override
    @Nonnull
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.GRASS;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (!world.isRemote) {
            if (!world.isAreaLoaded(pos, 3)) return;

            if (!hasWater(world, pos) && world.getBiome(pos) != AtumBiomes.OASIS) {
                world.setBlockState(pos, AtumBlocks.SAND.getDefaultState(), 2);
            } else {
                if (world.getLightFromNeighbors(pos.up()) >= 9 && world.getBiome(pos) == AtumBiomes.OASIS) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos posGrow = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

                        if (posGrow.getY() >= 0 && posGrow.getY() < 256 && !world.isBlockLoaded(posGrow) || world.getBiome(posGrow) != AtumBiomes.OASIS || !hasWater(world, posGrow)) {
                            return;
                        }
                        IBlockState stateUp = world.getBlockState(posGrow.up());
                        IBlockState stateGrow = world.getBlockState(posGrow);

                        if (stateGrow.getBlock() == AtumBlocks.SAND && world.getLightFromNeighbors(posGrow.up()) >= 4 && stateUp.getLightOpacity(world, pos.up()) <= 2) {
                            world.setBlockState(posGrow, AtumBlocks.FERTILE_SOIL.getDefaultState());
                        }
                    }
                }
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
        IBlockState plant = plantable.getPlant(world, pos.offset(direction));
        EnumPlantType plantType = plantable.getPlantType(world, pos.up());

        boolean hasWater = (world.getBlockState(pos.east()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.west()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.north()).getMaterial() == Material.WATER ||
                world.getBlockState(pos.south()).getMaterial() == Material.WATER);

        switch (plantType) {
            case Plains:
                return true;
            case Beach:
                return hasWater;
            case Crop:
                return plant.getBlock() instanceof BlockStem;
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