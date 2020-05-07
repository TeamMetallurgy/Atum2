package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrateBlock extends ContainerBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public CrateBlock(Properties properties) {
        super(properties.notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new CrateTileEntity();
    }

    @Override
    @Nonnull
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @Nonnull
    public ActionResultType onBlockActivated(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult rayTrace) {
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof CrateTileEntity) {
                player.openContainer((CrateTileEntity) tileEntity);
                player.addStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
            }
            return ActionResultType.SUCCESS;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof CrateTileEntity) {
                ((CrateTileEntity) tileEntity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof CrateTileEntity) {
            tileEntity.updateContainingBlockInfo();
        }
    }

    @Override
    public void onReplaced(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof CrateTileEntity) {
            world.updateComparatorOutputLevel(pos, this);
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public void harvestBlock(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable TileEntity tileEntity, @Nonnull ItemStack stack) {
        if (tileEntity instanceof CrateTileEntity) {
            InventoryHelper.dropInventoryItems(world, pos, (CrateTileEntity) tileEntity);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.harvestBlock(world, player, pos, state, tileEntity, stack);
    }

    @Override
    public boolean hasComparatorInputOverride(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(@Nonnull BlockState blockState, World world, @Nonnull BlockPos pos) {
        return Container.calcRedstoneFromInventory((CrateTileEntity) world.getTileEntity(pos));
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
}