package com.teammetallurgy.atum.blocks.base;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class AtumLanternBlock extends LanternBlock {

    public AtumLanternBlock() {
        super(AbstractBlock.Properties.create(Material.IRON).setRequiresTool().hardnessAndResistance(3.5F).sound(SoundType.LANTERN).setLightLevel((state) -> 15).notSolid());
    }
}