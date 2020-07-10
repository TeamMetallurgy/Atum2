package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.Material;

public class AtumTorchBlock extends TorchBlock {

    public AtumTorchBlock(int lightValue) {
        super(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0F).lightValue(lightValue).sound(SoundType.WOOD));
    }
}