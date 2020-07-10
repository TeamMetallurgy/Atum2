package com.teammetallurgy.atum.blocks.linen;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;

public class LinenBlock extends Block {

    public LinenBlock(DyeColor color) {
        super(Block.Properties.create(Material.WOOL, color).hardnessAndResistance(0.8F).sound(SoundType.CLOTH));
    }
}