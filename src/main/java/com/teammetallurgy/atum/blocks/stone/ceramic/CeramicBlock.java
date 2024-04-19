package com.teammetallurgy.atum.blocks.stone.ceramic;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class CeramicBlock extends Block {

    public CeramicBlock(DyeColor color) {
        this(Block.Properties.of(Material.STONE, color).strength(1.5F, 6.0F).sound(SoundType.STONE));
    }

    public CeramicBlock(Properties properties) {
        super(properties);
    }
}