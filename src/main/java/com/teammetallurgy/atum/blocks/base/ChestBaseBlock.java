package com.teammetallurgy.atum.blocks.base;

import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ChestBaseBlock extends ChestBlock {

    protected ChestBaseBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> tileEntitySupplier) {
        this(tileEntitySupplier, BlockBehaviour.Properties.of(Material.STONE, MaterialColor.SAND));
    }

    protected ChestBaseBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> tileEntitySupplier, BlockBehaviour.Properties properties) {
        super(properties.strength(3.0F, 10.0F).sound(SoundType.STONE), tileEntitySupplier);
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new ItemStack(AtumBlocks.LIMESTONE_CHEST);
    }

    @Override
    public void playerWillDestroy(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        super.playerWillDestroy(world, pos, state, player);

        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (player.isCreative() && tileEntity instanceof ChestBaseTileEntity) {
            this.playerDestroy(world, player, pos, state, tileEntity, player.getMainHandItem());
        }
    }

    @Override
    public void playerDestroy(@Nonnull Level world, Player player, @Nonnull BlockPos pos, @Nonnull BlockState state, BlockEntity tileEntity, @Nonnull ItemStack stack) {
        if (!player.isCreative()) {
            super.playerDestroy(world, player, pos, state, tileEntity, stack);
        }
        world.removeBlock(pos, false);

        if (tileEntity instanceof ChestBaseTileEntity chestBase) {
            if (chestBase.canBeDouble && !chestBase.canBeSingle) {
                for (int i = 0; i < 4; i++) {
                    Direction direction = state.getValue(FACING);
                    ChestType type = state.getValue(TYPE);
                    if (type == ChestType.LEFT) {
                        direction = direction.getClockWise();
                    } else if (type == ChestType.RIGHT) {
                        direction = direction.getCounterClockWise();
                    }
                    BlockPos offsetPos = pos.relative(direction);
                    if (world.getBlockState(offsetPos).getBlock() == this) {
                        this.breakDoubleChest(world, offsetPos);
                    }
                }
            }
            chestBase.setRemoved();
        }
    }

    private void breakDoubleChest(Level world, BlockPos pos) {
        BlockEntity tileEntity = world.getBlockEntity(pos);

        if (tileEntity instanceof ChestBaseTileEntity chestBase) {
            if (!chestBase.isEmpty()) {
                Containers.dropContents(world, pos, chestBase);
            }
            world.updateNeighbourForOutputSignal(pos, this);
        }
        world.removeBlock(pos, false);
    }

    @Override
    @Nonnull
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor world, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        BlockEntity tileEntity = world.getBlockEntity(currentPos);
        if (tileEntity instanceof ChestBaseTileEntity chest) {
            if (chest.canBeSingle && !chest.canBeDouble) {
                return state;
            } else {
                return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
            }
        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Block block = Block.byItem(context.getItemInHand().getItem());
        if (block instanceof ChestBaseBlock) {
            BlockEntity tileEntity = this.newBlockEntity(context.getClickedPos(), context.getLevel().getBlockState(context.getClickedPos()));
            if (tileEntity instanceof ChestBaseTileEntity && !((ChestBaseTileEntity) tileEntity).canBeDouble) {
                return super.getStateForPlacement(context).setValue(TYPE, ChestType.SINGLE);
            }
        }
        return super.getStateForPlacement(context);
    }

    @Override
    public void setPlacedBy(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if (placer instanceof Player) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof ChestBaseTileEntity chest) {
                if (chest.canBeDouble && !chest.canBeSingle) {
                    Direction direction = Direction.from2DDataValue(Mth.floor(placer.getYRot() * 4.0F / 360.0F + 0.5D) & 3).getOpposite();
                    BlockPos posRight = pos.relative(direction.getClockWise().getOpposite());
                    BlockState rightState = world.getBlockState(posRight);
                    BlockHitResult rayTrace = new BlockHitResult(new Vec3(posRight.getX(), posRight.getY(), posRight.getZ()), direction, pos, false);
                    BlockPlaceContext context = new BlockPlaceContext(new UseOnContext((Player) placer, InteractionHand.MAIN_HAND, rayTrace));
                    if (rightState.isAir() || rightState.canBeReplaced(context)) {
                        placer.level.setBlockAndUpdate(posRight, state.setValue(TYPE, ChestType.LEFT)); //Left and right is reversed? o.O
                    }
                }
            }
        }
    }

    public static BlockState correctFacing(BlockGetter world, BlockPos pos, BlockState state, Block checkBlock) {
        Direction direction = null;

        for (Direction horizontal : Direction.Plane.HORIZONTAL) {
            BlockPos horizontalPos = pos.relative(horizontal);
            BlockState horizontalState = world.getBlockState(horizontalPos);
            if (horizontalState.getBlock() == checkBlock) {
                return state;
            }

            if (horizontalState.isSolidRender(world, horizontalPos)) {
                if (direction != null) {
                    direction = null;
                    break;
                }
                direction = horizontal;
            }
        }

        if (direction != null) {
            return state.setValue(HorizontalDirectionalBlock.FACING, direction.getOpposite());
        } else {
            Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
            BlockPos facingPos = pos.relative(facing);
            if (world.getBlockState(facingPos).isSolidRender(world, facingPos)) {
                facing = facing.getOpposite();
                facingPos = pos.relative(facing);
            }

            if (world.getBlockState(facingPos).isSolidRender(world, facingPos)) {
                facing = facing.getClockWise();
                facingPos = pos.relative(facing);
            }

            if (world.getBlockState(facingPos).isSolidRender(world, facingPos)) {
                facing = facing.getOpposite();
                pos.relative(facing);
            }
            return state.setValue(HorizontalDirectionalBlock.FACING, facing);
        }
    }
}