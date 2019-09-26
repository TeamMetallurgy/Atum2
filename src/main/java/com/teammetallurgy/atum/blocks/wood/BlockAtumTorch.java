package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockAtumTorch extends TorchBlock {

    public BlockAtumTorch() {
        super();
        this.setHardness(0.0F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(null);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return (int) (15.0F * 0.9375F);
    }
}