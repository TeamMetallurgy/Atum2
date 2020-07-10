package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DeadBushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class ShrubBlock extends DeadBushBlock {

    public ShrubBlock() {
        super(Block.Properties.create(Material.TALL_PLANTS, MaterialColor.WOOD).doesNotBlockMovement().hardnessAndResistance(0.0F).sound(SoundType.PLANT));
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
        return state.getBlock() == AtumBlocks.SAND;
    }

    @Override
    @Nonnull
    public List<ItemStack> onSheared(@Nonnull ItemStack stack, IWorld world, BlockPos pos, int fortune) {
        return Collections.singletonList(new ItemStack(this));
    }
}