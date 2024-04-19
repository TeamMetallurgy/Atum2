package com.teammetallurgy.atum.blocks.stone.khnumite;

import net.minecraft.world.level.block.RotatedPillarBlock;

public class KhnumiteBlock extends RotatedPillarBlock implements IKhnumite {

    public KhnumiteBlock() {
        super(Properties.of(Material.STONE, MapColor.CLAY).strength(2.0F));
    }
}