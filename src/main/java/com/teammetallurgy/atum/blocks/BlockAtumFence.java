package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockAtumFence extends BlockFence {

    public BlockAtumFence(MapColor mapColor) {
        super(Material.WOOD, mapColor);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        Block block = world.getBlockState(pos).getBlock();
        return canBlockConnect(block) || super.canConnectTo(world, pos, facing);
    }

    private boolean canBlockConnect(Block block) {
        return block == AtumBlocks.PALM_FENCE || block == AtumBlocks.PALM_FENCE_GATE ||
                block == AtumBlocks.DEADWOOD_FENCE || block == AtumBlocks.DEADWOOD_FENCE_GATE;
    }
}