package com.teammetallurgy.atum.blocks.stone.limestone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockRaStone extends BlockBreakable {
    private static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

    public BlockRaStone() {
        super(Material.ROCK, false, MapColor.RED);
        this.setTickRandomly(true);
        this.setHardness(0.5F);
        this.setLightOpacity(3);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
    }

    @Override
    @Nonnull
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.NORMAL;
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if ((rand.nextInt(3) == 0 || this.countNeighbors(world, pos) < 4) && world.getLightFromNeighbors(pos) > 11 - state.getValue(AGE) - state.getLightOpacity()) {
            this.startToDisappear(world, pos, state, rand, true);
        } else {
            world.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (block == this) {
            int i = this.countNeighbors(world, pos);
            if (i < 2) {
                this.turnIntoLava(world, pos);
            }
        }
    }

    private int countNeighbors(World worldIn, BlockPos pos) {
        int i = 0;
        for (EnumFacing facing : EnumFacing.values()) {
            if (worldIn.getBlockState(pos.offset(facing)).getBlock() == this) {
                ++i;
                if (i >= 4) {
                    return i;
                }
            }
        }
        return i;
    }

    private void startToDisappear(World world, BlockPos pos, IBlockState state, Random rand, boolean meltNeighbors) {
        int i = state.getValue(AGE);
        if (i < 3) {
            world.setBlockState(pos, state.withProperty(AGE, i + 1), 2);
            world.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
        } else {
            this.turnIntoLava(world, pos);

            if (meltNeighbors) {
                for (EnumFacing facing : EnumFacing.values()) {
                    BlockPos posOffset = pos.offset(facing);
                    IBlockState stateOffset = world.getBlockState(posOffset);

                    if (stateOffset.getBlock() == this) {
                        this.startToDisappear(world, posOffset, stateOffset, rand, false);
                    }
                }
            }
        }
    }

    private void turnIntoLava(World world, BlockPos pos) {
        this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
        world.setBlockState(pos, Blocks.LAVA.getDefaultState());
        world.neighborChanged(pos, Blocks.LAVA, pos);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, PlayerEntity player) {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    protected ItemStack getSilkTouchDrop(@Nonnull IBlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, MathHelper.clamp(meta, 0, 3));
    }
}