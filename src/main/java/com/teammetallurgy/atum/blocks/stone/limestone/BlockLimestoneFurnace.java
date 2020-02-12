package com.teammetallurgy.atum.blocks.stone.limestone;

import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.furnace.TileEntityLimestoneFurnace;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockLimestoneFurnace extends ContainerBlock {
    private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    private final boolean isBurning;
    private static boolean keepInventory;

    public BlockLimestoneFurnace(boolean isBurning) {
        super(Material.ROCK);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
        this.isBurning = isBurning;

        this.setHardness(3.5F);
        this.setSoundType(SoundType.STONE);
        if (isBurning) {
            this.setLightLevel(0.875F);
        }
    }

    @Override
    @Nonnull
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(AtumBlocks.LIMESTONE_FURNACE);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, BlockState state) {
        this.setDefaultFacing(world, pos, state);
    }

    private void setDefaultFacing(World world, BlockPos pos, BlockState state) {
        if (!world.isRemote) {
            BlockState stateNorth = world.getBlockState(pos.north());
            BlockState stateSouth = world.getBlockState(pos.south());
            BlockState stateWest = world.getBlockState(pos.west());
            BlockState stateEast = world.getBlockState(pos.east());
            Direction facing = state.get(FACING);

            if (facing == Direction.NORTH && stateNorth.isFullBlock() && !stateSouth.isFullBlock()) {
                facing = Direction.SOUTH;
            } else if (facing == Direction.SOUTH && stateSouth.isFullBlock() && !stateNorth.isFullBlock()) {
                facing = Direction.NORTH;
            } else if (facing == Direction.WEST && stateWest.isFullBlock() && !stateEast.isFullBlock()) {
                facing = Direction.EAST;
            } else if (facing == Direction.EAST && stateEast.isFullBlock() && !stateWest.isFullBlock()) {
                facing = Direction.WEST;
            }
            world.setBlockState(pos, state.with(FACING, facing), 2);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
        if (this.isBurning) {
            Direction facing = state.get(FACING);
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double) pos.getZ() + 0.5D;
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            if (rand.nextDouble() < 0.1D) {
                world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            switch (facing) {
                case WEST:
                    world.addParticle(ParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    world.addParticle(ParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case EAST:
                    world.addParticle(ParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    world.addParticle(ParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case NORTH:
                    world.addParticle(ParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
                    world.addParticle(ParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    world.addParticle(ParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                    world.addParticle(ParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                    break;
                case DOWN:
                case UP:
                    break;
            }
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTrace) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof TileEntityLimestoneFurnace) {
                //player.openGui(Atum.instance, 0, world, pos.getX(), pos.getY(), pos.getZ()); //TODO
                player.addStat(Stats.INTERACT_WITH_FURNACE);
            }
            return true;
        }
    }

    public static void setState(boolean active, World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        TileEntity te = world.getTileEntity(pos);
        keepInventory = true;

        if (active) {
            world.setBlockState(pos, AtumBlocks.LIMESTONE_FURNACE_LIT.getDefaultState().with(FACING, state.get(FACING)), 3);
            world.setBlockState(pos, AtumBlocks.LIMESTONE_FURNACE_LIT.getDefaultState().with(FACING, state.get(FACING)), 3);
        } else {
            world.setBlockState(pos, AtumBlocks.LIMESTONE_FURNACE.getDefaultState().with(FACING, state.get(FACING)), 3);
            world.setBlockState(pos, AtumBlocks.LIMESTONE_FURNACE.getDefaultState().with(FACING, state.get(FACING)), 3);
        }

        keepInventory = false;

        if (te != null) {
            te.validate();
            world.setTileEntity(pos, te);
        }
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new TileEntityLimestoneFurnace();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        world.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity instanceof TileEntityLimestoneFurnace) {
                ((TileEntityLimestoneFurnace) tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        if (!keepInventory) {
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity instanceof TileEntityLimestoneFurnace) {
                InventoryHelper.dropInventoryItems(world, pos, (TileEntityLimestoneFurnace) tileentity);
                world.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        return Container.calcRedstone(world.getTileEntity(pos));
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumBlocks.LIMESTONE_FURNACE);
    }

    @Override
    @Nonnull
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
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