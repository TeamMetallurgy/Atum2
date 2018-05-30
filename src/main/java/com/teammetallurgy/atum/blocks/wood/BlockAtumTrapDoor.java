package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockAtumTrapDoor extends BlockTrapDoor {

    public BlockAtumTrapDoor() {
        super(Material.WOOD);
        this.setHardness(3.0F);
        this.setSoundType(SoundType.WOOD);
        this.disableStats();
    }
}