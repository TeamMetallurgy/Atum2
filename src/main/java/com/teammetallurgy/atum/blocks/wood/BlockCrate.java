package com.teammetallurgy.atum.blocks.wood;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class BlockCrate extends BlockContainer implements IOreDictEntry {
    private static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final Map<BlockAtumPlank.WoodType, BlockCrate> CRATES = Maps.newEnumMap(BlockAtumPlank.WoodType.class);

    private BlockCrate() {
        super(Material.WOOD);
        this.setHardness(3.0F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public static void registerCrates() {
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            BlockCrate crate = new BlockCrate();
            CRATES.put(type, crate);
            AtumRegistry.registerBlock(crate, type.getName() + "_crate");
        }
    }

    public static BlockCrate getCrate(BlockAtumPlank.WoodType type) {
        return CRATES.get(type);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileEntityCrate();
    }

    @Override
    @Nonnull
    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos blockPos) {
        return BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length).getMapColor();
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
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCrate) {
            player.openGui(Atum.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Override
    @Nonnull
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        state = state.withProperty(FACING, facing);
        BlockPos posNorth = pos.north();
        BlockPos posSouth = pos.south();
        BlockPos posWest = pos.west();
        BlockPos posEast = pos.east();
        boolean isNorth = this == world.getBlockState(posNorth).getBlock();
        boolean isSouth = this == world.getBlockState(posSouth).getBlock();
        boolean isWest = this == world.getBlockState(posWest).getBlock();
        boolean isEast = this == world.getBlockState(posEast).getBlock();

        if (!isNorth && !isSouth && !isWest && !isEast) {
            world.setBlockState(pos, state, 3);
        } else if (facing.getAxis() != EnumFacing.Axis.X || !isNorth && !isSouth) {
            if (facing.getAxis() == EnumFacing.Axis.Z && (isWest || isEast)) {
                if (isWest) {
                    world.setBlockState(posWest, state, 3);
                } else {
                    world.setBlockState(posEast, state, 3);
                }
                world.setBlockState(pos, state, 3);
            }
        } else {
            if (isNorth) {
                world.setBlockState(posNorth, state, 3);
            } else {
                world.setBlockState(posSouth, state, 3);
            }
            world.setBlockState(pos, state, 3);
        }

        if (stack.hasDisplayName()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityCrate) {
                ((TileEntityCrate) tileEntity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityCrate) {
            tileEntity.updateContainingBlockInfo();
        }
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityCrate) {
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity tileEntity, @Nonnull ItemStack stack) {
        if (tileEntity instanceof TileEntityCrate) {
            InventoryHelper.dropInventoryItems(world, pos, (TileEntityCrate) tileEntity);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.harvestBlock(world, player, pos, state, tileEntity, stack);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
        return Container.calcRedstoneFromInventory((TileEntityCrate) world.getTileEntity(pos));
    }

    public IBlockState correctFacing(World world, BlockPos pos, IBlockState state) {
        EnumFacing facingCheck = null;
        for (EnumFacing horizontal : EnumFacing.Plane.HORIZONTAL) {
            IBlockState stateHorizontal = world.getBlockState(pos.offset(horizontal));
            if (stateHorizontal.getBlock() == this) {
                return state;
            }
            if (stateHorizontal.isFullBlock()) {
                if (facingCheck != null) {
                    facingCheck = null;
                    break;
                }
                facingCheck = horizontal;
            }
        }

        if (facingCheck != null) {
            return state.withProperty(FACING, facingCheck.getOpposite());
        } else {
            EnumFacing facing = state.getValue(FACING);

            if (world.getBlockState(pos.offset(facing)).isFullBlock()) {
                facing = facing.getOpposite();
            }
            if (world.getBlockState(pos.offset(facing)).isFullBlock()) {
                facing = facing.rotateY();
            }
            if (world.getBlockState(pos.offset(facing)).isFullBlock()) {
                facing = facing.getOpposite();
            }
            return state.withProperty(FACING, facing);
        }
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byIndex(meta);

        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    @Nonnull
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    @Nonnull
    public IBlockState withMirror(@Nonnull IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "crate");
        OreDictHelper.add(this, "chest");
        OreDictHelper.add(this, "chestWood");
    }
}