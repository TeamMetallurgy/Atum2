package com.teammetallurgy.atum.blocks.stone.khnumite;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class KhnumiteBlock extends RotatedPillarBlock implements IKhnumite {

    public KhnumiteBlock() {
        super(Properties.of().mapColor(MapColor.CLAY).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F));
    }
}