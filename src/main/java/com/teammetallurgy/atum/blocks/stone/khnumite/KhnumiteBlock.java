package com.teammetallurgy.atum.blocks.stone.khnumite;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ToolType;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class KhnumiteBlock extends RotatedPillarBlock implements IKhnumite {

    public KhnumiteBlock() {
        super(Properties.of(Material.STONE, MaterialColor.CLAY).strength(2.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
    }
}