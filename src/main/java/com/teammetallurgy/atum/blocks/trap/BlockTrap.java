package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.TileEntityTrap;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class BlockTrap extends BlockContainer {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    BlockTrap() {
        super(Material.ROCK, MapColor.SAND);
        this.setHardness(1.5F);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        this.setDefaultDirection(world, pos, state);
    }

    private void setDefaultDirection(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            EnumFacing facing = state.getValue(FACING);
            boolean isNorth = world.getBlockState(pos.north()).isFullBlock();
            boolean isSouth = world.getBlockState(pos.south()).isFullBlock();

            if (facing == EnumFacing.NORTH && isNorth && !isSouth) {
                facing = EnumFacing.SOUTH;
            } else if (facing == EnumFacing.SOUTH && isSouth && !isNorth) {
                facing = EnumFacing.NORTH;
            } else {
                boolean isWest = world.getBlockState(pos.west()).isFullBlock();
                boolean isEast = world.getBlockState(pos.east()).isFullBlock();

                if (facing == EnumFacing.WEST && isWest && !isEast) {
                    facing = EnumFacing.EAST;
                } else if (facing == EnumFacing.EAST && isEast && !isWest) {
                    facing = EnumFacing.WEST;
                }
            }
            world.setBlockState(pos, state.getBlock().getDefaultState().withProperty(FACING, facing), 2);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, @Nonnull ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TileEntityTrap) {
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
        return Container.calcRedstone(world.getTileEntity(pos));
    }

    public static IPosition getDispensePosition(IBlockSource coords) {
        EnumFacing facing = coords.getBlockState().getValue(FACING);
        double x = coords.getX() + 0.7D * (double) facing.getFrontOffsetX();
        double y = coords.getY() + 0.7D * (double) facing.getFrontOffsetY();
        double z = coords.getZ() + 0.7D * (double) facing.getFrontOffsetZ();
        return new PositionImpl(x, y, z);
    }

    @Override
    @Nonnull
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rotation) {
        return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public IBlockState withMirror(@Nonnull IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }
}