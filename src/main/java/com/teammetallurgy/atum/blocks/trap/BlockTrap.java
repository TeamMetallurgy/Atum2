package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public abstract class BlockTrap extends ContainerBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing");
    private static final BooleanProperty DISABLED = BooleanProperty.create("disabled");

    protected BlockTrap() {
        super(Material.ROCK, MaterialColor.SAND);
        this.setHardness(1.5F);
        this.setHarvestLevel("pickaxe", 0);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(DISABLED, Boolean.FALSE));
    }

    @Override
    public float getBlockHardness(BlockState state, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof TrapTileEntity && ((TrapTileEntity) tileEntity).isInsidePyramid ? -1.0F : super.getBlockHardness(state, world, pos);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof TrapTileEntity && ((TrapTileEntity) tileEntity).isInsidePyramid ? 6000000.0F : super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTraceResult) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileEntity = world.getTileEntity(pos);
            boolean isToolEffective = ForgeHooks.isToolEffective(world, pos, player.getHeldItem(Hand.MAIN_HAND)) || ForgeHooks.isToolEffective(world, pos, player.getHeldItem(Hand.OFF_HAND));
            if (tileEntity instanceof TrapTileEntity) {
                TrapTileEntity trap = (TrapTileEntity) tileEntity;
                if (!trap.isInsidePyramid) {
                    player.openGui(Atum.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
                if (trap.isInsidePyramid && isToolEffective && !state.get(DISABLED)) {
                    this.setDisabled(world, pos, state, (TrapTileEntity) tileEntity, true);
                    world.playSound(null, pos, SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 1.1F, 1.5F);
                    return true;
                }
            }
        }
        return super.onBlockActivated(state, world, pos, player, handIn, rayTraceResult);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TrapTileEntity && !((TrapTileEntity) tileEntity).isInsidePyramid) {
                if (world.isBlockPowered(pos)) {
                    this.setDisabled(world, pos, state, (TrapTileEntity) tileEntity, true);
                } else if (!world.isBlockPowered(pos)) {
                    world.getPendingBlockTicks().scheduleTick(pos, this, 4);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, World world, BlockPos pos, Random rand) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TrapTileEntity && !((TrapTileEntity) tileEntity).isInsidePyramid) {
                if (!world.isBlockPowered(pos)) {
                    this.setDisabled(world, pos, state, (TrapTileEntity) tileEntity, false);
                }
            }
        }
    }

    private void setDisabled(World world, BlockPos pos, BlockState state, TrapTileEntity trap, boolean disabledStatus) {
        trap.setDisabledStatus(disabledStatus);
        world.setBlockState(pos, state.with(DISABLED, disabledStatus));
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    @Override
    @Nonnull
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, BlockState state) {
        super.onBlockAdded(world, pos, state);
        this.setDefaultDirection(world, pos, state);
    }

    private void setDefaultDirection(World world, BlockPos pos, BlockState state) {
        if (!world.isRemote) {
            Direction facing = state.get(FACING);
            boolean isNorth = world.getBlockState(pos.north()).isFullBlock();
            boolean isSouth = world.getBlockState(pos.south()).isFullBlock();

            if (facing == Direction.NORTH && isNorth && !isSouth) {
                facing = Direction.SOUTH;
            } else if (facing == Direction.SOUTH && isSouth && !isNorth) {
                facing = Direction.NORTH;
            } else {
                boolean isWest = world.getBlockState(pos.west()).isFullBlock();
                boolean isEast = world.getBlockState(pos.east()).isFullBlock();

                if (facing == Direction.WEST && isWest && !isEast) {
                    facing = Direction.EAST;
                } else if (facing == Direction.EAST && isEast && !isWest) {
                    facing = Direction.WEST;
                }
            }
            world.setBlockState(pos, state.getBlock().getDefaultState().with(FACING, facing), 2);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        world.setBlockState(pos, state.with(FACING, Direction.getDirectionFromEntityLiving(pos, placer)), 2);

        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TrapTileEntity) {
            ((TrapTileEntity) tileentity).isInsidePyramid = false;
            if (stack.hasDisplayName()) {
                ((TrapTileEntity) tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TrapTileEntity) {
            InventoryHelper.dropInventoryItems(world, pos, (TrapTileEntity) tileentity);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
        return Container.calcRedstone(world.getTileEntity(pos));
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
        container.add(FACING, DISABLED);
    }
}