package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.blocks.machines.tileentity.KilnBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class KilnBlock extends AbstractFurnaceBlock {
    static final BooleanProperty MULTIBLOCK_PRIMARY = BooleanProperty.create("multiblock_primary");
    private static final BooleanProperty MULTIBLOCK_SECONDARY = BooleanProperty.create("multiblock_secondary");

    public KilnBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(3.5F).setLightLevel((state) -> 13).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(LIT, false).with(MULTIBLOCK_PRIMARY, false).with(MULTIBLOCK_SECONDARY, false));
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new KilnTileEntity();
    }

    @Override
    protected void interactWith(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player) {
        BlockPos tepos = getPrimaryKilnBlock(world, pos);
        if (tepos != null) {
            TileEntity tileEntity = world.getTileEntity(tepos);
            if (tileEntity instanceof KilnTileEntity && player instanceof ServerPlayerEntity) {
                KilnTileEntity kiln = (KilnTileEntity) tileEntity;
                NetworkHooks.openGui((ServerPlayerEntity) player, kiln, tepos);
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (newState.getBlock() != state.getBlock()) {
            if (state.get(MULTIBLOCK_PRIMARY)) {
                this.destroyMultiblock(world, pos, state.get(FACING));
            } else {
                BlockPos primaryPos = pos.offset(state.get(FACING).rotateYCCW());
                BlockState primaryState = world.getBlockState(primaryPos);
                if (primaryState.getBlock() == AtumBlocks.KILN && primaryState.get(MULTIBLOCK_PRIMARY)) {
                    this.destroyMultiblock(world, primaryPos, primaryState.get(FACING));
                }
            }
            world.removeTileEntity(pos);
        }
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull LivingEntity placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        tryMakeMultiblock(world, pos, world.getBlockState(pos));
    }

    public void tryMakeMultiblock(World world, BlockPos pos, BlockState state) {
        Direction facing = state.get(FACING);
        if (checkMultiblock(world, pos, facing)) {
            world.setBlockState(pos, state.with(MULTIBLOCK_PRIMARY, true));
            world.setBlockState(pos.offset(facing.rotateY()), state.with(MULTIBLOCK_PRIMARY, false));
            createMultiblock(world, pos);
        } else if (checkMultiblock(world, pos.offset(facing.rotateYCCW()), facing)) {
            world.setBlockState(pos, state.with(MULTIBLOCK_PRIMARY, false));
            world.setBlockState(pos.offset(facing.rotateYCCW()), state.with(MULTIBLOCK_PRIMARY, true));
            createMultiblock(world, pos.offset(facing.rotateYCCW()));
        }
    }

    public static BlockPos getSecondaryKilnFromPrimary(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof KilnTileEntity) {
            if (state.getBlock() == AtumBlocks.KILN && ((KilnTileEntity) tileEntity).isPrimary()) {
                return pos.offset(state.get(FACING).rotateY());
            }
        }
        return null;
    }

    private BlockPos getPrimaryKilnBlock(World world, BlockPos pos) {
        if (world != null && !world.isAirBlock(pos)) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == AtumBlocks.KILN && state.get(MULTIBLOCK_PRIMARY)) {
                return pos;
            } else {
                state = world.getBlockState(pos.offset(state.get(FACING).rotateYCCW()));
                if (state.getBlock() == AtumBlocks.KILN && state.get(MULTIBLOCK_PRIMARY)) {
                    return pos.offset(state.get(FACING).rotateYCCW());
                }
            }
        }
        return null;
    }

    private void createMultiblock(World world, BlockPos primaryPos) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, world.getBlockState(primaryPos).get(FACING));
        for (BlockPos brickPos : brickPositions) {
            world.setBlockState(brickPos, AtumBlocks.KILN_FAKE.getDefaultState().with(KilnFakeBlock.UP, primaryPos.getY() - 1 == brickPos.getY()));
            TileEntity tileEntity = world.getTileEntity(brickPos);
            if (tileEntity != null) {
                ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
            }
        }
        TileEntity tileEntity = world.getTileEntity(primaryPos);
        if (tileEntity instanceof KilnBaseTileEntity) {
            ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
        }

        BlockPos secondaryPos = getSecondaryKilnFromPrimary(world, primaryPos);

        if (secondaryPos != null) {
            tileEntity = world.getTileEntity(secondaryPos);
            if (tileEntity instanceof KilnBaseTileEntity) {
                ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
            }
        }
    }

    void destroyMultiblock(World world, BlockPos primaryPos, Direction facing) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, facing);
        BlockState primaryState = world.getBlockState(primaryPos);
        BlockPos secondaryPos = primaryPos.offset(facing.rotateY());
        BlockState secondaryState = world.getBlockState(secondaryPos);
        BlockPos dropPos = primaryPos;

        if (primaryState.getBlock() == AtumBlocks.KILN) {
            world.setBlockState(primaryPos, primaryState.with(MULTIBLOCK_PRIMARY, false).with(LIT, false));
        }
        if (secondaryState.getBlock() == AtumBlocks.KILN) {
            world.setBlockState(secondaryPos, secondaryState.with(LIT, false));
        } else {
            dropPos = secondaryPos;
        }
        for (BlockPos brickPos : brickPositions) {
            if (world.getBlockState(brickPos).getBlock() == AtumBlocks.KILN_FAKE) {
                world.removeTileEntity(brickPos);
                world.setBlockState(brickPos, AtumBlocks.LIMESTONE_BRICK_SMALL.getDefaultState());
            } else {
                dropPos = brickPos;
            }
        }

        TileEntity tileEntity = world.getTileEntity(primaryPos);
        if (tileEntity instanceof KilnBaseTileEntity) {
            KilnBaseTileEntity kilnBase = (KilnBaseTileEntity) tileEntity;
            kilnBase.setPrimaryPos(null);
            InventoryHelper.dropInventoryItems(world, dropPos, kilnBase);
            kilnBase.remove();
        }

        tileEntity = world.getTileEntity(secondaryPos);
        if (tileEntity instanceof KilnBaseTileEntity) {
            KilnBaseTileEntity kilnBase = (KilnBaseTileEntity) tileEntity;
            kilnBase.setPrimaryPos(null);
        }
    }

    private boolean checkMultiblock(World world, BlockPos primaryPos, Direction facing) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, facing);
        if (world.getBlockState(primaryPos).getBlock() != AtumBlocks.KILN) {
            return false;
        }
        if (world.getBlockState(primaryPos.offset(facing.rotateY())).getBlock() != AtumBlocks.KILN) {
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
        positions.add(pos.offset(Direction.DOWN));
        positions.add(pos.offset(facing.getOpposite()));
        positions.add(pos.offset(facing.getOpposite()).offset(Direction.DOWN));

        BlockPos offset = pos.offset(facing.rotateY());
        positions.add(offset.offset(Direction.DOWN));
        positions.add(offset.offset(facing.getOpposite()));
        positions.add(offset.offset(facing.getOpposite()).offset(Direction.DOWN));

        return positions;
    }

    @Override
    @Nonnull
    public BlockState updatePostPlacement(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, IWorld world, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        TileEntity tileEntity = world.getTileEntity(currentPos);
        if (tileEntity instanceof KilnTileEntity) {
            KilnTileEntity kiln = (KilnTileEntity) tileEntity;
            if (this.getPrimaryKilnBlock(kiln.getWorld(), currentPos) != null) {
                return state.with(MULTIBLOCK_SECONDARY, !kiln.isPrimary());
            }
        }
        return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(FACING, LIT, MULTIBLOCK_PRIMARY, MULTIBLOCK_SECONDARY);
    }
}