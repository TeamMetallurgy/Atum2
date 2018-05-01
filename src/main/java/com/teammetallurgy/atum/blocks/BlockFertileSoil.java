package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockFertileSoil extends Block {

	protected static final AxisAlignedBB FERTILE_SOIL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);

    public BlockFertileSoil() {
        super(Material.GROUND);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.GROUND);
        this.setTickRandomly(true);
        this.setLightOpacity(255);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FERTILE_SOIL_AABB;
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
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) { //TODO Check if it works (Badly ported)
        if (!world.isRemote) {
            if ((world.getLight(pos.up()) < 4) && (world.getBlockLightOpacity(pos.up()) > 2)) {
                world.setBlockState(pos, state.getBlock().getStateFromMeta(1), 2);
            } else if (world.getLight(pos.up()) >= 9) {
                for (int l = 0; l < 4; l++) {
                    int i1 = pos.getX() + random.nextInt(3) - 1;
                    int j1 = pos.getY() + random.nextInt(5) - 3;
                    int k1 = pos.getZ() + random.nextInt(3) - 1;
                    BlockPos blockPos = new BlockPos(i1, j1, k1);
                    IBlockState blockState = world.getBlockState(blockPos);

                    if ((blockState.getBlock() == this) && (blockState.getBlock().getMetaFromState(blockState) == 1) && (world.getLight(blockPos.up()) >= 4) && (world.getBlockLightOpacity(blockPos.up()) <= 2)) {
                        world.setBlockState(pos, AtumBlocks.FERTILE_SOIL.getDefaultState(), 2);
                    }
                }
            }
        }
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
                return true;
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
            world.setBlockState(pos, AtumBlocks.FERTILE_SOIL.getDefaultState());
        }
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(AtumBlocks.SAND);
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }
}