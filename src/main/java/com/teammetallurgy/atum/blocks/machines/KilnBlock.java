package com.teammetallurgy.atum.blocks.machines;

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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class KilnBlock extends AbstractFurnaceBlock {
    static final BooleanProperty MULTIBLOCK_PRIMARY = BooleanProperty.create("multiblock_primary");
    private static final BooleanProperty MULTIBLOCK_SECONDARY = BooleanProperty.create("multiblock_secondary");

    public KilnBlock() {
        super(Properties.of(Material.STONE, MaterialColor.SAND).strength(3.5F).lightLevel(s -> s.getValue(BlockStateProperties.LIT) ? 13 : 0).sound(SoundType.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false).setValue(MULTIBLOCK_PRIMARY, false).setValue(MULTIBLOCK_SECONDARY, false));
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new KilnTileEntity(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, AtumTileEntities.KILN.get(), KilnTileEntity::serverTick);
    }

    @Override
    protected void openContainer(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player) {
        BlockPos tepos = getPrimaryKilnBlock(world, pos);
        if (tepos != null) {
            BlockEntity tileEntity = world.getBlockEntity(tepos);
            if (tileEntity instanceof KilnTileEntity kiln && player instanceof ServerPlayer) {
                NetworkHooks.openGui((ServerPlayer) player, kiln, tepos);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (newState.getBlock() != state.getBlock()) {
            if (state.getValue(MULTIBLOCK_PRIMARY)) {
                this.destroyMultiblock(world, pos, state.getValue(FACING));
            } else {
                BlockPos primaryPos = pos.relative(state.getValue(FACING).getCounterClockWise());
                BlockState primaryState = world.getBlockState(primaryPos);
                if (primaryState.getBlock() == AtumBlocks.KILN && primaryState.getValue(MULTIBLOCK_PRIMARY)) {
                    this.destroyMultiblock(world, primaryPos, primaryState.getValue(FACING));
                }
            }
            world.removeBlockEntity(pos);
        }
    }

    @Override
    public void setPlacedBy(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        tryMakeMultiblock(world, pos, world.getBlockState(pos));
    }

    public void tryMakeMultiblock(Level world, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        if (checkMultiblock(world, pos, facing)) {
            world.setBlockAndUpdate(pos, state.setValue(MULTIBLOCK_PRIMARY, true));
            world.setBlockAndUpdate(pos.relative(facing.getClockWise()), state.setValue(MULTIBLOCK_PRIMARY, false));
            createMultiblock(world, pos);
        } else if (checkMultiblock(world, pos.relative(facing.getCounterClockWise()), facing)) {
            world.setBlockAndUpdate(pos, state.setValue(MULTIBLOCK_PRIMARY, false));
            world.setBlockAndUpdate(pos.relative(facing.getCounterClockWise()), state.setValue(MULTIBLOCK_PRIMARY, true));
            createMultiblock(world, pos.relative(facing.getCounterClockWise()));
        }
    }

    public static BlockPos getSecondaryKilnFromPrimary(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof KilnTileEntity) {
            if (state.getBlock() == AtumBlocks.KILN && ((KilnTileEntity) tileEntity).isPrimary()) {
                return pos.relative(state.getValue(FACING).getClockWise());
            }
        }
        return null;
    }

    private BlockPos getPrimaryKilnBlock(Level world, BlockPos pos) {
        if (world != null && !world.isEmptyBlock(pos)) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == AtumBlocks.KILN && state.getValue(MULTIBLOCK_PRIMARY)) {
                return pos;
            } else {
                state = world.getBlockState(pos.relative(state.getValue(FACING).getCounterClockWise()));
                if (state.getBlock() == AtumBlocks.KILN && state.getValue(MULTIBLOCK_PRIMARY)) {
                    return pos.relative(state.getValue(FACING).getCounterClockWise());
                }
            }
        }
        return null;
    }

    private void createMultiblock(Level world, BlockPos primaryPos) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, world.getBlockState(primaryPos).getValue(FACING));
        for (BlockPos brickPos : brickPositions) {
            world.setBlockAndUpdate(brickPos, AtumBlocks.KILN_FAKE.defaultBlockState().setValue(KilnFakeBlock.UP, primaryPos.getY() - 1 == brickPos.getY()));
            BlockEntity tileEntity = world.getBlockEntity(brickPos);
            if (tileEntity != null) {
                ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
            }
        }
        BlockEntity tileEntity = world.getBlockEntity(primaryPos);
        if (tileEntity instanceof KilnBaseTileEntity) {
            ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
        }

        BlockPos secondaryPos = getSecondaryKilnFromPrimary(world, primaryPos);

        if (secondaryPos != null) {
            tileEntity = world.getBlockEntity(secondaryPos);
            if (tileEntity instanceof KilnBaseTileEntity) {
                ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
            }
        }
    }

    void destroyMultiblock(Level world, BlockPos primaryPos, Direction facing) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, facing);
        BlockState primaryState = world.getBlockState(primaryPos);
        BlockPos secondaryPos = primaryPos.relative(facing.getClockWise());
        BlockState secondaryState = world.getBlockState(secondaryPos);
        BlockPos dropPos = primaryPos;

        if (primaryState.getBlock() == AtumBlocks.KILN) {
            world.setBlockAndUpdate(primaryPos, primaryState.setValue(MULTIBLOCK_PRIMARY, false).setValue(LIT, false));
        }
        if (secondaryState.getBlock() == AtumBlocks.KILN) {
            world.setBlockAndUpdate(secondaryPos, secondaryState.setValue(LIT, false));
        } else {
            dropPos = secondaryPos;
        }
        for (BlockPos brickPos : brickPositions) {
            if (world.getBlockState(brickPos).getBlock() == AtumBlocks.KILN_FAKE) {
                world.removeBlockEntity(brickPos);
                world.setBlockAndUpdate(brickPos, AtumBlocks.LIMESTONE_BRICK_SMALL.defaultBlockState());
            } else {
                dropPos = brickPos;
            }
        }

        BlockEntity tileEntity = world.getBlockEntity(primaryPos);
        if (tileEntity instanceof KilnBaseTileEntity) {
            KilnBaseTileEntity kilnBase = (KilnBaseTileEntity) tileEntity;
            kilnBase.setPrimaryPos(null);
            Containers.dropContents(world, dropPos, kilnBase);
            kilnBase.setRemoved();
        }

        tileEntity = world.getBlockEntity(secondaryPos);
        if (tileEntity instanceof KilnBaseTileEntity) {
            KilnBaseTileEntity kilnBase = (KilnBaseTileEntity) tileEntity;
            kilnBase.setPrimaryPos(null);
        }
    }

    private boolean checkMultiblock(Level world, BlockPos primaryPos, Direction facing) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, facing);
        if (world.getBlockState(primaryPos).getBlock() != AtumBlocks.KILN) {
            return false;
        }
        if (world.getBlockState(primaryPos.relative(facing.getClockWise())).getBlock() != AtumBlocks.KILN) {
            return false;
        }
        for (BlockPos brickPos : brickPositions) {
            BlockState brickState = world.getBlockState(brickPos);
            if (brickState.getBlock() != AtumBlocks.LIMESTONE_BRICK_SMALL) {
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
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, LevelAccessor world, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        BlockEntity tileEntity = world.getBlockEntity(currentPos);
        if (tileEntity instanceof KilnTileEntity) {
            KilnTileEntity kiln = (KilnTileEntity) tileEntity;
            if (this.getPrimaryKilnBlock(kiln.getLevel(), currentPos) != null) {
                return state.setValue(MULTIBLOCK_SECONDARY, !kiln.isPrimary());
            }
        }
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(FACING, LIT, MULTIBLOCK_PRIMARY, MULTIBLOCK_SECONDARY);
    }
}