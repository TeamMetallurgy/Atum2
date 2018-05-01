package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDate extends Block {

	public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.3125D, 0.125D, 0.3125D, 0.6875D, 1.0D, 0.6875D);
    public BlockDate() {
        super(Material.PLANTS);
        this.setHardness(0.5F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
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
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
        if (world.getBlockState(pos.up()).getBlock() != BlockLeave.getLeave(BlockAtumPlank.WoodType.PALM) && !world.isRemote) {
            EntityItem entityItem = new EntityItem(world, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), new ItemStack(AtumItems.DATE, 0, this.quantityDropped(new Random())));
            entityItem.dropItem(AtumItems.DATE, this.quantityDropped(new Random()));
            world.setBlockToAir(pos);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return AtumItems.DATE;
    }

    @Override
    public int quantityDropped(Random rand) {
        return rand.nextInt(4) + 1;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(AtumItems.DATE);
    }
}