package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockDate extends BushBlock implements IGrowable {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 7);
    private static final AxisAlignedBB STEM = new AxisAlignedBB(0.4125D, 0.625D, 0.4125D, 0.6D, 1.0D, 0.6D);
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.3125D, 0.125D, 0.3125D, 0.6875D, 1.0D, 0.6875D);

    public BlockDate() {
        super(Material.PLANTS);
        this.setHardness(0.35F);
        this.setTickRandomly(true);
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(null);
        this.setDefaultState(this.blockState.getBaseState().with(AGE, 0));
    }

    @Override
    @SuppressWarnings("all")
    public float getBlockHardness(BlockState state, World world, BlockPos pos) {
        if (state.getValue(AGE) != 3) {
            this.blockHardness = 0.25F;
        }
        return super.getBlockHardness(state, world, pos);
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        if (state.getValue(AGE) == 0) {
            return STEM;
        }
        return BOUNDING_BOX;
    }

    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, Random rand) {
        if (!world.isRemote) {
            super.updateTick(world, pos, state, rand);
            if (!world.isAreaLoaded(pos, 1)) return;
            if (state.getValue(AGE) != 7) {
                if (ForgeHooks.onCropsGrowPre(world, pos, state, world.rand.nextDouble() <= 0.12F)) {
                    world.setBlockState(pos, state.cycleProperty(AGE), 2);
                    ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
                }
            }
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() == this) {
            BlockState stateUp = world.getBlockState(pos.up());
            return stateUp.getBlock().isLeaves(stateUp, world, pos.up());
        }
        return this.canSustainBush(world.getBlockState(pos.down()));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        if (state.getValue(AGE) == 7) {
            dropBlockAsItem(world, pos, state, 0);
            return world.setBlockState(pos, this.getDefaultState());
        }
        return false;
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockReader world, BlockPos pos, @Nonnull BlockState state, int fortune) {
        if (state.getValue(AGE) == 7) {
            super.getDrops(drops, world, pos, state, fortune);
        }
    }

    @Override
    @Nonnull
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return AtumItems.DATE;
    }

    @Override
    public int quantityDropped(Random rand) {
        return rand.nextInt(4) + 1;
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull BlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumItems.DATE);
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(AGE, meta);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(AGE);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    public boolean canGrow(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return state.getValue(AGE) != 7;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return true;
    }

    @Override
    public void grow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        int growth = state.getValue(AGE) + MathHelper.getInt(rand, 1, 2);
        int maxAge = 7;

        if (growth > maxAge) {
            growth = maxAge;
        }

        if (state.getValue(AGE) != 7) {
            world.setBlockState(pos, this.getDefaultState().with(AGE, growth), 2);
        }
    }
}