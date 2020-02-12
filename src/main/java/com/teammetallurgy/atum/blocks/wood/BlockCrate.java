package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCrate extends ContainerBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public BlockCrate() {
        super(Material.WOOD);
        this.setHardness(3.0F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new CrateTileEntity();
    }

    @Override
    @Nonnull
    public MaterialColor getMapColor(BlockState state, IBlockReader blockAccess, BlockPos blockPos) {
        return BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length).getMapColor();
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
    @OnlyIn(Dist.CLIENT)
    public boolean hasCustomBreakingProgress(BlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTrace) {
        if (world.isRemote) {
            return true;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof CrateTileEntity) {
            //player.openGui(Atum.instance, 1, world, pos.getX(), pos.getY(), pos.getZ()); //TODO
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        Direction facing = Direction.byHorizontalIndex(MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        state = state.with(FACING, facing);
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
        } else if (facing.getAxis() != Direction.Axis.X || !isNorth && !isSouth) {
            if (facing.getAxis() == Direction.Axis.Z && (isWest || isEast)) {
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
            if (tileEntity instanceof CrateTileEntity) {
                ((CrateTileEntity) tileEntity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof CrateTileEntity) {
            tileEntity.updateContainingBlockInfo();
        }
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof CrateTileEntity) {
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void harvestBlock(@Nonnull World world, PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable TileEntity tileEntity, @Nonnull ItemStack stack) {
        if (tileEntity instanceof CrateTileEntity) {
            InventoryHelper.dropInventoryItems(world, pos, (CrateTileEntity) tileEntity);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.harvestBlock(world, player, pos, state, tileEntity, stack);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
        return Container.calcRedstoneFromInventory((CrateTileEntity) world.getTileEntity(pos));
    }

    public BlockState correctFacing(World world, BlockPos pos, BlockState state) {
        Direction facingCheck = null;
        for (Direction horizontal : Direction.Plane.HORIZONTAL) {
            BlockState stateHorizontal = world.getBlockState(pos.offset(horizontal));
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
            return state.with(FACING, facingCheck.getOpposite());
        } else {
            Direction facing = state.get(FACING);

            if (world.getBlockState(pos.offset(facing)).isFullBlock()) {
                facing = facing.getOpposite();
            }
            if (world.getBlockState(pos.offset(facing)).isFullBlock()) {
                facing = facing.rotateY();
            }
            if (world.getBlockState(pos.offset(facing)).isFullBlock()) {
                facing = facing.getOpposite();
            }
            return state.with(FACING, facing);
        }
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
    
    @Override
    @Nonnull
    public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
        return state.rotate(mirror.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(FACING);
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "crate");
        OreDictHelper.add(this, "chest");
        OreDictHelper.add(this, "chestWood");
    }
}