package com.teammetallurgy.atum.blocks.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

import java.util.function.Supplier;

public class StairsAtumBlock extends StairsBlock {

    public StairsAtumBlock(Supplier<BlockState> supplier, Properties properties) {
        super(supplier, properties);
    }
}