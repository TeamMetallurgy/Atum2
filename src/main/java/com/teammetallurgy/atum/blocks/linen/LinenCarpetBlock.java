package com.teammetallurgy.atum.blocks.linen;

import net.minecraft.block.Block;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;

public class LinenCarpetBlock extends CarpetBlock {

    public LinenCarpetBlock(DyeColor color) {
        super(color, Block.Properties.create(Material.WOOL, color).hardnessAndResistance(0.1F).sound(SoundType.CLOTH));
    }
}