package com.teammetallurgy.atum.blocks.glass;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.PaneBlock;

public class AtumPaneBlock extends PaneBlock {

    public AtumPaneBlock() {
        super(Block.Properties.from(AtumBlocks.CRYSTAL_GLASS));
    }
}