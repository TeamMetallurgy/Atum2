package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.DeadBushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockShrub extends DeadBushBlock {

    public BlockShrub() {
        super();
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    public boolean canSustainBush(BlockState state) {
        return state.getBlock() == AtumBlocks.SAND;
    }

    @Override
    @Nonnull
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return new ItemStack(this).getItem();
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }


    @Override
    @Nonnull
    public List<ItemStack> onSheared(@Nonnull ItemStack stack, IBlockReader world, BlockPos pos, int fortune) {
        return Collections.singletonList(new ItemStack(this));
    }
}