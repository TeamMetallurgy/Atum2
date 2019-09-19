package com.teammetallurgy.atum.blocks.beacon;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockFramedRadiantBeacon extends BlockRadiantBeacon {

    public BlockFramedRadiantBeacon() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
    }

    @Override
    @Nonnull
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(AtumBlocks.RADIANT_BEACON);
    }
}