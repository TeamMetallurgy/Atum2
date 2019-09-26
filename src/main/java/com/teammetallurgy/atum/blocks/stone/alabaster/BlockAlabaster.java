package com.teammetallurgy.atum.blocks.stone.alabaster;

import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;

public class BlockAlabaster extends Block {

    public BlockAlabaster() {
        super(Material.ROCK, MaterialColor.QUARTZ);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(new ItemStack(this), "stoneAlabaster");
    }
}
