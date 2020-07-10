package com.teammetallurgy.atum.blocks.base;

import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ChestBaseBlock extends ChestBlock {

    protected ChestBaseBlock(Supplier<TileEntityType<? extends ChestTileEntity>> tileEntitySupplier) {
        super(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(3.0F, 10.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0), tileEntitySupplier);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumBlocks.LIMESTONE_CHEST);
    }

    @Override
    public void onBlockHarvested(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player) {
        super.onBlockHarvested(world, pos, state, player);

        TileEntity tileEntity = world.getTileEntity(pos);
        if (player.isCreative() && tileEntity instanceof ChestBaseTileEntity) {
            this.harvestBlock(world, player, pos, state, tileEntity, player.getHeldItemMainhand());
        }
    }

    @Override
    public void harvestBlock(@Nonnull World world, PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, TileEntity tileEntity, @Nonnull ItemStack stack) {
        if (!player.isCreative()) {
            super.harvestBlock(world, player, pos, state, tileEntity, stack);
        }
        world.removeBlock(pos, false);

        if (tileEntity instanceof ChestBaseTileEntity) {
            ChestBaseTileEntity chestBase = (ChestBaseTileEntity) tileEntity;
            if (chestBase.canBeDouble && !chestBase.canBeSingle) {
                for (int i = 0; i < 4; i++) {
                    Direction direction = state.get(FACING);
                    ChestType type = state.get(TYPE);
                    if (type == ChestType.LEFT) {
                        direction = direction.rotateY();
                    } else if (type == ChestType.RIGHT) {
                        direction = direction.rotateYCCW();
                    }
                    BlockPos offsetPos = pos.offset(direction);
                    if (world.getBlockState(offsetPos).getBlock() == this) {
                        this.breakDoubleChest(world, offsetPos);
                    }
                }
            }
            chestBase.remove();
        }
    }

    private void breakDoubleChest(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof ChestBaseTileEntity) {
            ChestBaseTileEntity chestBase = (ChestBaseTileEntity) tileEntity;
            if (!chestBase.isEmpty()) {
                InventoryHelper.dropInventoryItems(world, pos, chestBase);
            }
            world.updateComparatorOutputLevel(pos, this);
        }
        world.removeBlock(pos, false);
    }

    @Override
    @Nonnull
    public BlockState updatePostPlacement(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull IWorld world, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        TileEntity tileEntity = world.getTileEntity(currentPos);
        if (tileEntity instanceof ChestBaseTileEntity) {
            ChestBaseTileEntity chest = (ChestBaseTileEntity) tileEntity;
            if (chest.canBeSingle && !chest.canBeDouble) {
                return state;
            } else {
                return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
            }
        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Block block = Block.getBlockFromItem(context.getItem().getItem());
        if (block instanceof ChestBaseBlock && block.hasTileEntity()) {
            TileEntity tileEntity = this.createNewTileEntity(context.getWorld());
            if (tileEntity instanceof ChestBaseTileEntity && !((ChestBaseTileEntity) tileEntity).canBeDouble) {
                return super.getStateForPlacement(context).with(TYPE, ChestType.SINGLE);
            }
        }
        return super.getStateForPlacement(context);
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull LivingEntity placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (placer instanceof PlayerEntity) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof ChestBaseTileEntity) {
                ChestBaseTileEntity chest = (ChestBaseTileEntity) tileEntity;
                if (chest.canBeDouble && !chest.canBeSingle) {
                    Direction direction = Direction.byHorizontalIndex(MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3).getOpposite();
                    BlockPos posRight = pos.offset(direction.rotateY().getOpposite());
                    BlockState rightState = world.getBlockState(posRight);
                    BlockRayTraceResult rayTrace = new BlockRayTraceResult(new Vec3d(posRight.getX(), posRight.getY(), posRight.getZ()), direction, pos, false);
                    BlockItemUseContext context = new BlockItemUseContext(new ItemUseContext((PlayerEntity) placer, Hand.MAIN_HAND, rayTrace));
                    if (rightState.isAir(world, posRight) || rightState.isReplaceable(context)) {
                        placer.world.setBlockState(posRight, state.with(TYPE, ChestType.LEFT)); //Left and right is reversed? o.O
                    }
                }
            }
        }
    }

    public static BlockState correctFacing(IBlockReader world, BlockPos pos, BlockState state, Block checkBlock) {
        Direction direction = null;

        for (Direction horizontal : Direction.Plane.HORIZONTAL) {
            BlockPos horizontalPos = pos.offset(horizontal);
            BlockState horizontalState = world.getBlockState(horizontalPos);
            if (horizontalState.getBlock() == checkBlock) {
                return state;
            }

            if (horizontalState.isOpaqueCube(world, horizontalPos)) {
                if (direction != null) {
                    direction = null;
                    break;
                }
                direction = horizontal;
            }
        }

        if (direction != null) {
            return state.with(HorizontalBlock.HORIZONTAL_FACING, direction.getOpposite());
        } else {
            Direction facing = state.get(HorizontalBlock.HORIZONTAL_FACING);
            BlockPos facingPos = pos.offset(facing);
            if (world.getBlockState(facingPos).isOpaqueCube(world, facingPos)) {
                facing = facing.getOpposite();
                facingPos = pos.offset(facing);
            }

            if (world.getBlockState(facingPos).isOpaqueCube(world, facingPos)) {
                facing = facing.rotateY();
                facingPos = pos.offset(facing);
            }

            if (world.getBlockState(facingPos).isOpaqueCube(world, facingPos)) {
                facing = facing.getOpposite();
                pos.offset(facing);
            }
            return state.with(HorizontalBlock.HORIZONTAL_FACING, facing);
        }
    }
}