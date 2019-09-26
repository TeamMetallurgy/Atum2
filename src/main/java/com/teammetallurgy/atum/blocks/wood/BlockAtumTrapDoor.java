package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.SoundType;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.material.Material;

public class BlockAtumTrapDoor extends TrapDoorBlock {

    public BlockAtumTrapDoor() {
        super(Material.WOOD);
        this.setHardness(3.0F);
        this.setSoundType(SoundType.WOOD);
        this.disableStats();
    }
}