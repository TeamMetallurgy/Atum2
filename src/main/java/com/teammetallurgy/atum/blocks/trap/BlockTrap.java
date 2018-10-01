package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.trap.tileentity.TileEntityTrap;
import com.teammetallurgy.atum.world.gen.structure.PyramidTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class BlockTrap extends BlockContainer {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    private static final PropertyBool DISABLED = PropertyBool.create("disabled");

    public BlockTrap() {
        super(Material.ROCK, MapColor.SAND);
        this.setHardness(1.5F);
        this.setHarvestLevel("pickaxe", 0);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DISABLED, Boolean.FALSE));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileEntity = world.getTileEntity(pos);
            boolean isToolEffective = ForgeHooks.isToolEffective(world, pos, player.getHeldItem(EnumHand.MAIN_HAND)) || ForgeHooks.isToolEffective(world, pos, player.getHeldItem(EnumHand.OFF_HAND));
            if (tileEntity instanceof TileEntityTrap) {
                if (!BlockTrap.isInsidePyramid((WorldServer) world, pos)) {
                    player.openGui(Atum.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
                if (BlockTrap.isInsidePyramid((WorldServer) world, pos) && isToolEffective) {
                    ((TileEntityTrap) tileEntity).setDisabledStatus(true);
                    world.setBlockState(pos, state.withProperty(DISABLED, true));
                    return true;
                }
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    public static boolean isInsidePyramid(WorldServer world, BlockPos pos) {
        return world.getChunkProvider().chunkGenerator.isInsideStructure(world, String.valueOf(PyramidTemplate.PYRAMID), pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityTrap && !isInsidePyramid((WorldServer) world, pos)) {
                TileEntityTrap trap = (TileEntityTrap) tileEntity;
                if (world.isBlockPowered(pos)) {
                    trap.setDisabledStatus(true);
                    world.setBlockState(pos, state.withProperty(DISABLED, true));
                } else if (!world.isBlockPowered(pos)) {
                    world.scheduleUpdate(pos, this, 4);
                }
            }
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityTrap && !isInsidePyramid((WorldServer) world, pos)) {
                if (!world.isBlockPowered(pos)) {
                    TileEntityTrap trap = (TileEntityTrap) tileEntity;
                    trap.setDisabledStatus(false);
                    world.setBlockState(pos, state.withProperty(DISABLED, false));
                }
            }
        }
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

        if (stack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity instanceof TileEntityTrap) {
                ((TileEntityTrap) tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TileEntityTrap) {
            InventoryHelper.dropInventoryItems(world, pos, (TileEntityTrap) tileentity);
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

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7)).withProperty(DISABLED, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getIndex();

        if (state.getValue(DISABLED)) {
            i |= 8;
        }
        return i;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, DISABLED);
    }
}