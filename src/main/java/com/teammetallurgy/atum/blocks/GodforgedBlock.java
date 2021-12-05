package com.teammetallurgy.atum.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public class GodforgedBlock extends Block {

    public GodforgedBlock() {
        super(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GOLD).setRequiresTool().harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(50.0F, 1200.0F));
    }
}