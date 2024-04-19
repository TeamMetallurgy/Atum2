package com.teammetallurgy.atum.blocks.linen;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;


public class LinenBlock extends Block {

    public LinenBlock(DyeColor color) {
        super(Block.Properties.of(Material.WOOL, color).strength(0.8F).sound(SoundType.WOOL));
    }
}