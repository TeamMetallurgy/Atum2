package com.teammetallurgy.atum.blocks.stone.ceramic;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ToolType;

public class CeramicBlock extends Block {

    public CeramicBlock(DyeColor color) {
        this(Block.Properties.create(Material.ROCK, color).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0));
    }

    public CeramicBlock(Properties properties) {
        super(properties);
    }
}