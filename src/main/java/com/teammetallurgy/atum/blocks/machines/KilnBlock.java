package com.teammetallurgy.atum.blocks.machines;

import com.mojang.serialization.MapCodec;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class KilnBlock extends AbstractFurnaceBlock {
    public static final MapCodec<KilnBlock> CODEC = simpleCodec(KilnBlock::new);
    static final BooleanProperty MULTIBLOCK_PRIMARY = BooleanProperty.create("multiblock_primary");
    private static final BooleanProperty MULTIBLOCK_SECONDARY = BooleanProperty.create("multiblock_secondary");

    public KilnBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false).setValue(MULTIBLOCK_PRIMARY, false).setValue(MULTIBLOCK_SECONDARY, false));
    }

    @Override
    @Nonnull
    protected MapCodec<? extends KilnBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return AtumTileEntities.KILN.get().create(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, AtumTileEntities.KILN.get(), KilnTileEntity::serverTick);
    }

    @Override
    protected void openContainer(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player) {
        BlockPos tepos = getPrimaryKilnBlock(level, pos);
        if (tepos != null) {
            BlockEntity tileEntity = level.getBlockEntity(tepos);
            if (tileEntity instanceof KilnTileEntity kiln && player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(kiln, tepos);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (newState.getBlock() != state.getBlock()) {
            if (state.getValue(MULTIBLOCK_PRIMARY)) {
                this.destroyMultiblock(level, pos, state.getValue(FACING));
            } else {
                BlockPos primaryPos = pos.relative(state.getValue(FACING).getCounterClockWise());
                BlockState primaryState = level.getBlockState(primaryPos);
                if (primaryState.getBlock() == AtumBlocks.KILN.get() && primaryState.getValue(MULTIBLOCK_PRIMARY)) {
                    this.destroyMultiblock(level, primaryPos, primaryState.getValue(FACING));
                }
            }
            level.removeBlockEntity(pos);
        }
    }

    @Override
    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        tryMakeMultiblock(level, pos, level.getBlockState(pos));
    }

    public void tryMakeMultiblock(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        if (checkMultiblock(level, pos, facing)) {
            level.setBlockAndUpdate(pos, state.setValue(MULTIBLOCK_PRIMARY, true));
            level.setBlockAndUpdate(pos.relative(facing.getClockWise()), state.setValue(MULTIBLOCK_PRIMARY, false));
            createMultiblock(level, pos);
        } else if (checkMultiblock(level, pos.relative(facing.getCounterClockWise()), facing)) {
            level.setBlockAndUpdate(pos, state.setValue(MULTIBLOCK_PRIMARY, false));
            level.setBlockAndUpdate(pos.relative(facing.getCounterClockWise()), state.setValue(MULTIBLOCK_PRIMARY, true));
            createMultiblock(level, pos.relative(facing.getCounterClockWise()));
        }
    }

    public static BlockPos getSecondaryKilnFromPrimary(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof KilnTileEntity) {
            if (state.getBlock() == AtumBlocks.KILN.get() && ((KilnTileEntity) tileEntity).isPrimary()) {
                return pos.relative(state.getValue(FACING).getClockWise());
            }
        }
        return null;
    }

    private BlockPos getPrimaryKilnBlock(Level level, BlockPos pos) {
        if (level != null && !level.isEmptyBlock(pos)) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() == AtumBlocks.KILN.get() && state.getValue(MULTIBLOCK_PRIMARY)) {
                return pos;
            } else {
                state = level.getBlockState(pos.relative(state.getValue(FACING).getCounterClockWise()));
                if (state.getBlock() == AtumBlocks.KILN.get() && state.getValue(MULTIBLOCK_PRIMARY)) {
                    return pos.relative(state.getValue(FACING).getCounterClockWise());
                }
            }
        }
        return null;
    }

    private void createMultiblock(Level level, BlockPos primaryPos) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, level.getBlockState(primaryPos).getValue(FACING));
        for (BlockPos brickPos : brickPositions) {
            level.setBlockAndUpdate(brickPos, AtumBlocks.KILN_FAKE.get().defaultBlockState().setValue(KilnFakeBlock.UP, primaryPos.getY() - 1 == brickPos.getY()));
            BlockEntity tileEntity = level.getBlockEntity(brickPos);
            if (tileEntity != null) {
                ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
            }
        }
        BlockEntity tileEntity = level.getBlockEntity(primaryPos);
        if (tileEntity instanceof KilnBaseTileEntity) {
            ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
        }

        BlockPos secondaryPos = getSecondaryKilnFromPrimary(level, primaryPos);

        if (secondaryPos != null) {
            tileEntity = level.getBlockEntity(secondaryPos);
            if (tileEntity instanceof KilnBaseTileEntity) {
                ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
            }
        }
    }

    void destroyMultiblock(Level level, BlockPos primaryPos, Direction facing) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, facing);
        BlockState primaryState = level.getBlockState(primaryPos);
        BlockPos secondaryPos = primaryPos.relative(facing.getClockWise());
        BlockState secondaryState = level.getBlockState(secondaryPos);
        BlockPos dropPos = primaryPos;

        if (primaryState.getBlock() == AtumBlocks.KILN.get()) {
            level.setBlockAndUpdate(primaryPos, primaryState.setValue(MULTIBLOCK_PRIMARY, false).setValue(LIT, false));
        }
        if (secondaryState.getBlock() == AtumBlocks.KILN.get()) {
            level.setBlockAndUpdate(secondaryPos, secondaryState.setValue(LIT, false));
        } else {
            dropPos = secondaryPos;
        }
        for (BlockPos brickPos : brickPositions) {
            if (level.getBlockState(brickPos).getBlock() == AtumBlocks.KILN_FAKE.get()) {
                level.removeBlockEntity(brickPos);
                level.setBlockAndUpdate(brickPos, AtumBlocks.LIMESTONE_BRICK_SMALL.get().defaultBlockState());
            } else {
                dropPos = brickPos;
            }
        }

        BlockEntity tileEntity = level.getBlockEntity(primaryPos);
        if (tileEntity instanceof KilnBaseTileEntity kilnBase) {
            kilnBase.setPrimaryPos(null);
            Containers.dropContents(level, dropPos, kilnBase);
            kilnBase.setRemoved();
        }

        tileEntity = level.getBlockEntity(secondaryPos);
        if (tileEntity instanceof KilnBaseTileEntity kilnBase) {
            kilnBase.setPrimaryPos(null);
        }
    }

    private boolean checkMultiblock(Level level, BlockPos primaryPos, Direction facing) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, facing);
        if (level.getBlockState(primaryPos).getBlock() != AtumBlocks.KILN.get()) {
            return false;
        }
        if (level.getBlockState(primaryPos.relative(facing.getClockWise())).getBlock() != AtumBlocks.KILN.get()) {
            return false;
        }
        for (BlockPos brickPos : brickPositions) {
            BlockState brickState = level.getBlockState(brickPos);
            if (brickState.getBlock() != AtumBlocks.LIMESTONE_BRICK_SMALL.get()) {
                return false;
            }
        }
        return true;
    }

    private List<BlockPos> getKilnBrickPositions(BlockPos pos, Direction facing) {
        List<BlockPos> positions = new LinkedList<>();
        positions.add(pos.relative(Direction.DOWN));
        positions.add(pos.relative(facing.getOpposite()));
        positions.add(pos.relative(facing.getOpposite()).relative(Direction.DOWN));

        BlockPos offset = pos.relative(facing.getClockWise());
        positions.add(offset.relative(Direction.DOWN));
        positions.add(offset.relative(facing.getOpposite()));
        positions.add(offset.relative(facing.getOpposite()).relative(Direction.DOWN));

        return positions;
    }

    @Override
    @Nonnull
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, LevelAccessor level, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        BlockEntity tileEntity = level.getBlockEntity(currentPos);
        if (tileEntity instanceof KilnTileEntity kiln) {
            if (this.getPrimaryKilnBlock(kiln.getLevel(), currentPos) != null) {
                return state.setValue(MULTIBLOCK_SECONDARY, !kiln.isPrimary());
            }
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(FACING, LIT, MULTIBLOCK_PRIMARY, MULTIBLOCK_SECONDARY);
    }
}