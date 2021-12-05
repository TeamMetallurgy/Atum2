package com.teammetallurgy.atum.blocks.stone.khnumite;

import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public class KhnumiteBlock extends RotatedPillarBlock implements IKhnumite {

    public KhnumiteBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.CLAY).hardnessAndResistance(2.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
    }
}