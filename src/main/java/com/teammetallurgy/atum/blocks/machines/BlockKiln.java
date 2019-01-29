package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityKiln;
import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityKilnBase;
import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestoneBricks;
import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestoneBricks.BrickType;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class BlockKiln extends BlockContainer implements IRenderMapper {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool IS_BURNING = PropertyBool.create("is_burning");
    public static final PropertyBool MULTIBLOCK_PRIMARY = PropertyBool.create("multiblock_primary");

    public BlockKiln() {
        super(Material.ROCK, MapColor.SAND);
        this.setHardness(3.5F);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 0);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(IS_BURNING, false).withProperty(MULTIBLOCK_PRIMARY, false));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileEntityKiln();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }

        BlockPos tepos = getPrimaryKilnBlock(world, pos);
        if (tepos != null) {
            TileEntity tileEntity = world.getTileEntity(tepos);
            if (tileEntity instanceof TileEntityKiln) {
                player.openGui(Atum.instance, 5, world, tepos.getX(), tepos.getY(), tepos.getZ());
                return true;
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(IS_BURNING) ? (int) (15.0F * 0.875F) : 0;
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (state.getValue(MULTIBLOCK_PRIMARY)) {
            this.destroyMultiblock(world, pos, state.getValue(FACING));
        } else {
            this.destroyMultiblock(world, pos.offset(state.getValue(FACING).rotateYCCW()), state.getValue(FACING));
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, @Nonnull ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityKiln) {
                ((TileEntityKiln) tileEntity).setCustomName(stack.getDisplayName());
            }
        }
        state = world.getBlockState(pos);
        tryMakeMultiblock(world, pos, state);
    }

    public void tryMakeMultiblock(World world, BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        if (checkMultiblock(world, pos, facing)) {
            world.setBlockState(pos, state.withProperty(MULTIBLOCK_PRIMARY, true));
            world.setBlockState(pos.offset(facing.rotateY()), state.withProperty(MULTIBLOCK_PRIMARY, false));
            createMultiblock(world, pos);
        } else if (checkMultiblock(world, pos.offset(facing.rotateYCCW()), facing)) {
            world.setBlockState(pos, state.withProperty(MULTIBLOCK_PRIMARY, false));
            world.setBlockState(pos.offset(facing.rotateYCCW()), state.withProperty(MULTIBLOCK_PRIMARY, true));
            createMultiblock(world, pos.offset(facing.rotateYCCW()));
        }
    }

    public static BlockPos getSecondaryKilnFromPrimary(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityKiln) {
            if (state.getBlock() == AtumBlocks.KILN && ((TileEntityKiln) tileEntity).isPrimary()) {
                return pos.offset(state.getValue(FACING).rotateY());
            }
        }
        return null;
    }

    public BlockPos getPrimaryKilnBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == AtumBlocks.KILN && state.getValue(MULTIBLOCK_PRIMARY)) {
            return pos;
        } else {
            state = world.getBlockState(pos.offset(state.getValue(FACING).rotateYCCW()));
            if (state.getBlock() == AtumBlocks.KILN && state.getValue(MULTIBLOCK_PRIMARY))
                return pos.offset(state.getValue(FACING).rotateYCCW());
        }
        return null;
    }

    private void createMultiblock(World world, BlockPos primaryPos) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, world.getBlockState(primaryPos).getValue(FACING));
        for (BlockPos brickPos : brickPositions) {
            world.setBlockState(brickPos, AtumBlocks.KILN_FAKE.getDefaultState()
                    .withProperty(BlockKilnFake.UP, primaryPos.getY() - 1 == brickPos.getY()));
        }
        TileEntity tileEntity = world.getTileEntity(primaryPos);
        if (tileEntity instanceof TileEntityKilnBase) {
            ((TileEntityKilnBase) tileEntity).setPrimary(true);
        }
    }

    void destroyMultiblock(World world, BlockPos primaryPos, EnumFacing facing) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, facing);
        IBlockState primaryState = world.getBlockState(primaryPos);
        BlockPos secondaryPos = primaryPos.offset(facing.rotateY());
        IBlockState secondaryState = world.getBlockState(secondaryPos);
        if (primaryState.getBlock() == AtumBlocks.KILN) {
            world.setBlockState(primaryPos, primaryState.withProperty(MULTIBLOCK_PRIMARY, false).withProperty(IS_BURNING, false));
        }

        TileEntity tileEntity = world.getTileEntity(primaryPos);
        if (tileEntity instanceof TileEntityKilnBase) {
            TileEntityKilnBase kilnBase = (TileEntityKilnBase) tileEntity;
            kilnBase.setPrimary(false);
            InventoryHelper.dropInventoryItems(world, primaryPos, kilnBase);
            kilnBase.invalidate();
        }

        if (secondaryState.getBlock() == AtumBlocks.KILN) {
            world.setBlockState(secondaryPos, secondaryState.withProperty(IS_BURNING, false));
        }
        for (BlockPos brickPos : brickPositions) {
            if (world.getBlockState(brickPos).getBlock() == AtumBlocks.KILN_FAKE) {
                world.setBlockState(brickPos, BlockLimestoneBricks.getBrick(BrickType.SMALL).getDefaultState());
            }
        }
    }

    private boolean checkMultiblock(World world, BlockPos primaryPos, EnumFacing facing) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, facing);
        if (world.getBlockState(primaryPos).getBlock() != AtumBlocks.KILN) {
            return false;
        }
        if (world.getBlockState(primaryPos.offset(facing.rotateY())).getBlock() != AtumBlocks.KILN) {
            return false;
        }
        for (BlockPos brickPos : brickPositions) {
            IBlockState brickState = world.getBlockState(brickPos);
            if (brickState.getBlock() != BlockLimestoneBricks.getBrick(BrickType.SMALL)) {
                return false;
            }
        }
        return true;
    }

    private List<BlockPos> getKilnBrickPositions(BlockPos pos, EnumFacing facing) {
        List<BlockPos> positions = new LinkedList<>();
        positions.add(pos.offset(EnumFacing.DOWN));
        positions.add(pos.offset(facing.getOpposite()));
        positions.add(pos.offset(facing.getOpposite()).offset(EnumFacing.DOWN));

        BlockPos offset = pos.offset(facing.rotateY());
        positions.add(offset.offset(EnumFacing.DOWN));
        positions.add(offset.offset(facing.getOpposite()));
        positions.add(offset.offset(facing.getOpposite()).offset(EnumFacing.DOWN));

        return positions;
    }

    @Override
    @Nonnull
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(MULTIBLOCK_PRIMARY, false);
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(IS_BURNING, (meta & 4) != 0).withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(MULTIBLOCK_PRIMARY, (meta & 8) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        meta = meta | (state.getValue(FACING)).getHorizontalIndex();
        if (state.getValue(IS_BURNING)) {
            meta |= 4;
        }
        if (state.getValue(MULTIBLOCK_PRIMARY)) {
            meta |= 8;
        }
        return meta;
    }

    @Override
    @Nonnull
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rotation) {
        return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public IBlockState withMirror(@Nonnull IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, IS_BURNING, MULTIBLOCK_PRIMARY);
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{MULTIBLOCK_PRIMARY};
    }
}