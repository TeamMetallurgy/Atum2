package com.teammetallurgy.atum.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public class GodforgedBlock extends Block {

    public GodforgedBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.GOLD).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(3).strength(50.0F, 1200.0F));
    }
}