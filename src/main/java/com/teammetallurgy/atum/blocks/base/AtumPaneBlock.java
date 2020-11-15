package com.teammetallurgy.atum.blocks.base;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class AtumPaneBlock extends PaneBlock {

    public AtumPaneBlock() {
        super(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid());
    }
}