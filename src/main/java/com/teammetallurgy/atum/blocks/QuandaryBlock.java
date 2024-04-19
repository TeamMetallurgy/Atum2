package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.base.IUnbreakable;
import com.teammetallurgy.atum.blocks.lighting.INebuTorch;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class QuandaryBlock extends Block implements IUnbreakable {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    private static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public QuandaryBlock() {
        super(Block.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 8.0F).isRedstoneConductor(AtumBlocks::never).requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVATED, false).setValue(UNBREAKABLE, false));
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return level.getBlockState(pos).getValue(UNBREAKABLE) ? 6000000.0F : super.getExplosionResistance(state, level, pos, explosion);
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (level.getBlockState(pos.relative(state.getValue(FACING))) == level.getBlockState(fromPos)) {
            Block facingBlock = level.getBlockState(fromPos).getBlock();
            boolean activated = facingBlock instanceof INebuTorch && ((INebuTorch) facingBlock).isNebuTorch();
            level.setBlock(pos, state.setValue(ACTIVATED, activated), 2);
            level.updateNeighborsAt(pos, this);

            if (activated) {
                Helper.attemptMakeDoor(level, pos, state.getValue(FACING), LimestoneBrickBlock.class, null);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public boolean isSignalSource(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState blockState, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return blockState.getSignal(level, pos, direction);
    }

    @Override
    public int getSignal(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return !state.getValue(ACTIVATED) ? 0 : 15;
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(FACING, ACTIVATED, UNBREAKABLE);
    }

    public static class Helper {

        /**
         * @param fullBlock Class or block instance for that should be replaced by doors, when successfully activated
         * @param door      Type of door that should be placed. If null, door will attempt to be placed based on fullBlock
         */
        public static void attemptMakeDoor(Level level, BlockPos pos, Direction facing, Class<? extends Block> fullBlock, @Nullable DoorBlock door) {
            if (facing.getAxis() != Direction.Axis.Y) {
                BlockState offsetLeft = level.getBlockState(pos.relative(facing.getClockWise(), 3));
                BlockState offsetRight = level.getBlockState(pos.relative(facing.getCounterClockWise(), 3));

                if ((offsetLeft.getBlock() == AtumBlocks.QUANDARY_BLOCK.get() && offsetLeft.getValue(ACTIVATED)) || (offsetRight.getBlock() == AtumBlocks.QUANDARY_BLOCK.get() && offsetRight.getValue(ACTIVATED))) {
                    boolean isPrimary = false;
                    if (offsetRight.getBlock() == AtumBlocks.QUANDARY_BLOCK.get()) {
                        isPrimary = true;
                    }

                    if (!isPrimary) {
                        pos = pos.relative(facing.getClockWise(), 3); //Change position, if the right Quandary Block is activated last
                    }

                    if (hasEntranceBlocks(level, pos, facing, fullBlock)) {
                        BlockPos rightFromPrimary = pos.relative(facing.getCounterClockWise());
                        if (door == null) {
                            Block readFromBlock = level.getBlockState(rightFromPrimary).getBlock();
                            ResourceLocation readFromBlockID = BuiltInRegistries.BLOCK.getKey(readFromBlock);
                            if (readFromBlockID != null) {
                                Block doorRead = BuiltInRegistries.BLOCK.get(new ResourceLocation(readFromBlockID.getNamespace(), readFromBlockID.getPath() + "_door"));
                                if (doorRead != null) {
                                    door = (DoorBlock) doorRead;
                                } else {
                                    door = (DoorBlock) AtumBlocks.LIMESTONE_DOOR.get();
                                }
                            } else {
                                door = (DoorBlock) AtumBlocks.LIMESTONE_DOOR.get();
                            }
                        }

                        BlockState doorLeft = door.defaultBlockState().setValue(DoorBlock.HINGE, DoorHingeSide.LEFT).setValue(DoorBlock.FACING, facing.getOpposite()).setValue(DoorBlock.OPEN, true);
                        BlockState doorRight = door.defaultBlockState().setValue(DoorBlock.HINGE, DoorHingeSide.RIGHT).setValue(DoorBlock.FACING, facing.getOpposite()).setValue(DoorBlock.OPEN, true);
                        level.setBlock(rightFromPrimary, doorLeft.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 3); //Top Left
                        level.setBlock(rightFromPrimary.below(), doorLeft, 2); //Bottom Left
                        level.setBlock(pos.relative(facing.getCounterClockWise(), 2), doorRight.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 3); //Top Right
                        level.setBlock(pos.relative(facing.getCounterClockWise(), 2).below(), doorRight, 2); //Bottom Right

                        playRewardDing(level, pos);
                    }
                }
            }
        }

        public static boolean hasEntranceBlocks(Level level, BlockPos pos, Direction facing, Class<? extends Block> fullBlock) {
            Block rightFromPrimary = level.getBlockState(pos.relative(facing.getCounterClockWise())).getBlock();
            return fullBlock.isInstance(rightFromPrimary) &&
                    level.getBlockState(pos.relative(facing.getCounterClockWise(), 2)).getBlock() == rightFromPrimary &&
                    level.getBlockState(pos.relative(facing.getCounterClockWise()).below()).getBlock() == rightFromPrimary &&
                    level.getBlockState(pos.relative(facing.getCounterClockWise(), 2).below()).getBlock() == rightFromPrimary;
        }

        public static boolean canSpawnPharaoh(Level level, BlockPos pos, Direction facing, Player player, RandomSource randomSource, SarcophagusTileEntity sarcophagus) {
            Block topLeftCorner = level.getBlockState(pos.relative(facing.getClockWise(), 2).relative(facing.getOpposite(), 1)).getBlock();
            Block bottomLeftCorner = level.getBlockState(pos.relative(facing.getClockWise(), 2).relative(facing, 2)).getBlock();
            Block topRightCorner = level.getBlockState(pos.relative(facing.getCounterClockWise(), 3).relative(facing.getOpposite(), 1)).getBlock();
            Block bottomRightCorner = level.getBlockState(pos.relative(facing.getCounterClockWise(), 3).relative(facing, 2)).getBlock();

            if (topLeftCorner instanceof INebuTorch torchTopLeftCorner && bottomLeftCorner instanceof INebuTorch torchBottomLeftCorner && topRightCorner instanceof INebuTorch torchTopRightCorner && bottomRightCorner instanceof INebuTorch torchBottomRightCorner) {

                if (torchTopLeftCorner.isNebuTorch() && torchBottomLeftCorner.isNebuTorch() && torchTopRightCorner.isNebuTorch() && torchBottomRightCorner.isNebuTorch()) {
                    playRewardDing(level, pos);

                    God god = torchTopLeftCorner.getGod();
                    if (god == torchBottomLeftCorner.getGod() && god == torchTopRightCorner.getGod() && god == torchBottomRightCorner.getGod()) {
                        sarcophagus.spawn(player, level.getCurrentDifficultyAt(pos), randomSource, god);
                    } else {
                        sarcophagus.spawn(player, level.getCurrentDifficultyAt(pos), randomSource, null);
                    }
                    return true;
                }
                return false;
            }
            return false;
        }

        public static void playRewardDing(Level level, BlockPos pos) {
            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.BLOCKS, 1.3F, 1.0F);
        }
    }
}