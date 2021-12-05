package com.teammetallurgy.atum.blocks.stone.ceramic;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.common.ToolType;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CeramicBlock extends Block {

    public CeramicBlock(DyeColor color) {
        this(Block.Properties.of(Material.STONE, color).strength(1.5F, 6.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0));
    }

    public CeramicBlock(Properties properties) {
        super(properties);
    }
}