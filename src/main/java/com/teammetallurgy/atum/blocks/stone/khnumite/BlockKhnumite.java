package com.teammetallurgy.atum.blocks.stone.khnumite;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockKhnumite extends BlockRotatedPillar implements IKhnumite {

    public BlockKhnumite() {
        super(Material.ROCK, MapColor.CLAY);
        this.setHardness(2.0F);
    }
}
