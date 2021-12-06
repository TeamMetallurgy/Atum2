package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrateBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public CrateBlock(Properties properties) {
        super(properties.noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockGetter reader) {
        return new CrateTileEntity();
    }

    @Override
    @Nonnull
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTrace) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (!ChestBlock.isChestBlockedAt(world, pos)) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof CrateTileEntity) {
                player.openMenu((CrateTileEntity) tileEntity);
                player.awardStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, rayTrace);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public void setPlacedBy(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof CrateTileEntity) {
                ((CrateTileEntity) tileEntity).setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        BlockEntity tileEntity = world.getBlockEntity(pos);

        if (tileEntity instanceof CrateTileEntity) {
            tileEntity.clearCache();
        }
    }

    @Override
    public void onRemove(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        BlockEntity tileEntity = world.getBlockEntity(pos);

        if (tileEntity instanceof CrateTileEntity) {
            world.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public void playerDestroy(@Nonnull Level world, @Nonnull Player player, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable BlockEntity tileEntity, @Nonnull ItemStack stack) {
        if (tileEntity instanceof CrateTileEntity) {
            Containers.dropContents(world, pos, (CrateTileEntity) tileEntity);
            world.updateNeighbourForOutputSignal(pos, this);
        }
        super.playerDestroy(world, player, pos, state, tileEntity, stack);
    }

    @Override
    public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@Nonnull BlockState blockState, Level world, @Nonnull BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromContainer((CrateTileEntity) world.getBlockEntity(pos));
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(FACING);
    }
}