package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.base.IUnbreakable;
import com.teammetallurgy.atum.blocks.lighting.INebuTorch;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class QuandaryBlock extends Block implements IUnbreakable {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    private static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public QuandaryBlock() {
        super(Block.Properties.of(Material.STONE, MaterialColor.SAND).strength(1.5F, 8.0F).requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVATED, false).setValue(UNBREAKABLE, false));
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
        return world.getBlockState(pos).getValue(UNBREAKABLE) ? 6000000.0F : super.getExplosionResistance(state, world, pos, explosion);
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        if (world.getBlockState(pos.relative(state.getValue(FACING))) == world.getBlockState(fromPos)) {
            Block facingBlock = world.getBlockState(fromPos).getBlock();
            boolean activated = facingBlock instanceof INebuTorch && ((INebuTorch) facingBlock).isNebuTorch();
            world.setBlock(pos, state.setValue(ACTIVATED, activated), 2);

            if (activated) {
                Helper.attemptMakeDoor(world, pos, state.getValue(FACING), LimestoneBrickBlock.class, null);
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
    public int getDirectSignal(BlockState blockState, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return blockState.getSignal(world, pos, direction);
    }

    @Override
    public int getSignal(BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return !state.getValue(ACTIVATED) ? 0 : 15;
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
        container.add(FACING, ACTIVATED, UNBREAKABLE);
    }

    public static class Helper {

        /**
         * @param fullBlock Class or block instance for that should be replaced by doors, when successfully activated
         * @param door      Type of door that should be placed. If null, door will attempt to be placed based on fullBlock
         */
        public static void attemptMakeDoor(Level world, BlockPos pos, Direction facing, Class<? extends Block> fullBlock, @Nullable DoorBlock door) {
            if (facing.getAxis() != Direction.Axis.Y) {
                BlockState offsetLeft = world.getBlockState(pos.relative(facing.getClockWise(), 3));
                BlockState offsetRight = world.getBlockState(pos.relative(facing.getCounterClockWise(), 3));

                if ((offsetLeft.getBlock() == AtumBlocks.QUANDARY_BLOCK && offsetLeft.getValue(ACTIVATED)) || (offsetRight.getBlock() == AtumBlocks.QUANDARY_BLOCK && offsetRight.getValue(ACTIVATED))) {
                    boolean isPrimary = false;
                    if (offsetRight.getBlock() == AtumBlocks.QUANDARY_BLOCK) {
                        isPrimary = true;
                    }

                    if (!isPrimary) {
                        pos = pos.relative(facing.getClockWise(), 3); //Change position, if the right Quandary Block is activated last
                    }

                    if (hasEntranceBlocks(world, pos, facing, fullBlock)) {
                        BlockPos rightFromPrimary = pos.relative(facing.getCounterClockWise());
                        if (door == null) {
                            Block readFromBlock = world.getBlockState(rightFromPrimary).getBlock();
                            if (readFromBlock.getRegistryName() != null) {
                                ResourceLocation location = readFromBlock.getRegistryName();
                                Block doorRead = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location.getNamespace(), location.getPath() + "_door"));
                                if (doorRead != null) {
                                    door = (DoorBlock) doorRead;
                                } else {
                                    door = (DoorBlock) AtumBlocks.LIMESTONE_DOOR;
                                }
                            } else {
                                door = (DoorBlock) AtumBlocks.LIMESTONE_DOOR;
                            }
                        }

                        BlockState doorLeft = door.defaultBlockState().setValue(DoorBlock.HINGE, DoorHingeSide.LEFT).setValue(DoorBlock.FACING, facing.getOpposite()).setValue(DoorBlock.OPEN, true);
                        BlockState doorRight = door.defaultBlockState().setValue(DoorBlock.HINGE, DoorHingeSide.RIGHT).setValue(DoorBlock.FACING, facing.getOpposite()).setValue(DoorBlock.OPEN, true);
                        world.setBlock(rightFromPrimary, doorLeft.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 3); //Top Left
                        world.setBlock(rightFromPrimary.below(), doorLeft, 2); //Bottom Left
                        world.setBlock(pos.relative(facing.getCounterClockWise(), 2), doorRight.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 3); //Top Right
                        world.setBlock(pos.relative(facing.getCounterClockWise(), 2).below(), doorRight, 2); //Bottom Right

                        playRewardDing(world, pos);
                    }
                }
            }
        }

        public static boolean hasEntranceBlocks(Level world, BlockPos pos, Direction facing, Class<? extends Block> fullBlock) {
            Block rightFromPrimary = world.getBlockState(pos.relative(facing.getCounterClockWise())).getBlock();
            return fullBlock.isInstance(rightFromPrimary) &&
                    world.getBlockState(pos.relative(facing.getCounterClockWise(), 2)).getBlock() == rightFromPrimary &&
                    world.getBlockState(pos.relative(facing.getCounterClockWise()).below()).getBlock() == rightFromPrimary &&
                    world.getBlockState(pos.relative(facing.getCounterClockWise(), 2).below()).getBlock() == rightFromPrimary;
        }

        public static boolean canSpawnPharaoh(Level world, BlockPos pos, Direction facing, Player player, SarcophagusTileEntity sarcophagus) {
            Block topLeftCorner = world.getBlockState(pos.relative(facing.getClockWise(), 2).relative(facing.getOpposite(), 1)).getBlock();
            Block bottomLeftCorner = world.getBlockState(pos.relative(facing.getClockWise(), 2).relative(facing, 2)).getBlock();
            Block topRightCorner = world.getBlockState(pos.relative(facing.getCounterClockWise(), 3).relative(facing.getOpposite(), 1)).getBlock();
            Block bottomRightCorner = world.getBlockState(pos.relative(facing.getCounterClockWise(), 3).relative(facing, 2)).getBlock();

            if (topLeftCorner instanceof INebuTorch && bottomLeftCorner instanceof INebuTorch && topRightCorner instanceof INebuTorch && bottomRightCorner instanceof INebuTorch) {
                INebuTorch torchTopLeftCorner = (INebuTorch) topLeftCorner;
                INebuTorch torchBottomLeftCorner = (INebuTorch) bottomLeftCorner;
                INebuTorch torchTopRightCorner = (INebuTorch) topRightCorner;
                INebuTorch torchBottomRightCorner = (INebuTorch) bottomRightCorner;

                if (torchTopLeftCorner.isNebuTorch() && torchBottomLeftCorner.isNebuTorch() && torchTopRightCorner.isNebuTorch() && torchBottomRightCorner.isNebuTorch()) {
                    playRewardDing(world, pos);

                    God god = torchTopLeftCorner.getGod();
                    if (god == torchBottomLeftCorner.getGod() && god == torchTopRightCorner.getGod() && god == torchBottomRightCorner.getGod()) {
                        sarcophagus.spawn(player, world.getCurrentDifficultyAt(pos), god);
                    } else {
                        sarcophagus.spawn(player, world.getCurrentDifficultyAt(pos), null);
                    }
                    return true;
                }
                return false;
            }
            return false;
        }

        public static void playRewardDing(Level world, BlockPos pos) {
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.NOTE_BLOCK_CHIME, SoundSource.BLOCKS, 1.3F, 1.0F);
        }
    }
}