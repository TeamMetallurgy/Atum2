package com.teammetallurgy.atum.blocks.base;

import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class AtumPaneBlock extends IronBarsBlock {

    public AtumPaneBlock() {
        super(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion());
    }
}