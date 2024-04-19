package com.teammetallurgy.atum.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;


public class GodforgedBlock extends Block {

    public GodforgedBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE, MapColor.GOLD).requiresCorrectToolForDrops().strength(50.0F, 1200.0F));
    }
}