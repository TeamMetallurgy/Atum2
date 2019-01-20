package com.teammetallurgy.atum.blocks;

import net.minecraft.block.BlockClay;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockMarl extends BlockClay {

    public BlockMarl() {
        super();
        this.setHardness(0.6F);
        this.setSoundType(SoundType.GROUND);
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
}