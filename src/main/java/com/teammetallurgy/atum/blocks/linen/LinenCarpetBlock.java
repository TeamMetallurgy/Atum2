package com.teammetallurgy.atum.blocks.linen;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.DyeColor;

public class LinenCarpetBlock extends WoolCarpetBlock {

    public LinenCarpetBlock(DyeColor color) {
        super(color, Block.Properties.of(Material.WOOL, color).strength(0.1F).sound(SoundType.WOOL));
    }
}