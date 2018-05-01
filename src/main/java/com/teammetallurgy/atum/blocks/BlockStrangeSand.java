package com.teammetallurgy.atum.blocks;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockStrangeSand extends BlockFalling {
	public BlockStrangeSand() {
		super(Material.SAND);
        this.setTickRandomly(true);
        this.setSoundType(SoundType.SAND);
        this.setHardness(0.5F);
	}

}
