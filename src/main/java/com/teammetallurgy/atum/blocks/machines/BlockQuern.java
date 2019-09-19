package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityQuern;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockQuern extends BlockContainer {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.12D, 0.0D, 0.12D, 0.88D, 0.38D, 0.88D);

    public BlockQuern() {
        super(Material.ROCK, MapColor.SAND);
        this.setHardness(1.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, Direction.NORTH));
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileEntityQuern();
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityQuern) {
            TileEntityQuern quern = (TileEntityQuern) tileEntity;
            if (!quern.isEmpty()) {
                if (player.isSneaking()) {
                    StackHelper.dropInventoryItems(world, pos, quern);
                } else {
                    ItemStack slotStack = quern.getStackInSlot(0);
                    ItemStack copyStack = new ItemStack(slotStack.getItem());
                    StackHelper.giveItem(player, Hand.MAIN_HAND, copyStack);
                    quern.decrStackSize(0, 1);
                }
                quern.markDirty();
            }
        }
        super.onBlockClicked(world, pos, player);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        if (player == null || player instanceof FakePlayer) return true;
        ItemStack heldStack = player.getHeldItem(hand);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityQuern) {
            TileEntityQuern quern = (TileEntityQuern) tileEntity;
            ItemStack slotStack = quern.getStackInSlot(0);
            int size = slotStack.getCount();
            if (size < slotStack.getMaxStackSize() && quern.isItemValidForSlot(0, heldStack) && (quern.isEmpty() || StackHelper.areStacksEqualIgnoreSize(heldStack, slotStack))) {
                ItemStack copyStack = new ItemStack(heldStack.getItem(), size + 1, heldStack.getMetadata());
                if (!heldStack.isEmpty()) {
                    quern.setInventorySlotContents(0, copyStack);
                }
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
            } else {
                quern.setRotations(quern.getRotations() + 24);
                if (world.isRemote) {
                    world.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.1F, 0.4F, true);
                }
            }
            quern.markDirty();
            return true;
        }
        return true;
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityQuern) {
            InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Direction facing, float hitX, float hitY, float hitZ, int meta, @Nonnull LivingEntity placer, Hand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        Direction facing = Direction.byIndex(meta);

        if (facing.getAxis() == Direction.Axis.Y) {
            facing = Direction.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    @Nonnull
    public BlockState withRotation(@Nonnull BlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public BlockState withMirror(@Nonnull BlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, BlockState state, BlockPos pos, Direction facing) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean hasCustomBreakingProgress(BlockState state) {
        return true;
    }
}