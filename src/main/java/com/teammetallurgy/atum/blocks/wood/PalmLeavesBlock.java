package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class PalmLeavesBlock extends LeavesAtumBlock implements IGrowable {

    public PalmLeavesBlock() {
        super(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT));
    }

    @Override
    public void randomTick(BlockState state, @Nonnull World world, @Nonnull BlockPos pos, Random rand) {
        super.randomTick(state, world, pos, rand);
        if (!world.isRemote) {
            if (world.rand.nextDouble() <= 0.05F) {
                if (canGrow(world, pos, state, false)) {
                    world.setBlockState(pos.down(), AtumBlocks.DATE_BLOCK.getDefaultState());
                }
            }
        }
    }

    @Override
    public boolean canGrow(@Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return !state.get(PERSISTENT) && isValidLocation(reader, pos.down()) && reader.getBlockState(pos.down()).isAir(reader, pos.down());
    }

    private boolean isValidLocation(@Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        for (int i = 0; i < 4; i++) {
            Direction horizontal = Direction.byHorizontalIndex((5 - i) % 4); //[W, S, E, N]
            BlockPos check = pos.offset(horizontal);
            if (reader.getBlockState(check).getBlock() == AtumBlocks.PALM_LOG) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return true;
    }

    @Override
    public void grow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        if (canGrow(world, pos, state, false) && rand.nextDouble() <= 0.5D) {
            world.setBlockState(pos.down(), AtumBlocks.DATE_BLOCK.getDefaultState());
        }
    }
}