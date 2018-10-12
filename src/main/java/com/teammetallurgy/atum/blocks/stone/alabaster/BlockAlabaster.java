package com.teammetallurgy.atum.blocks.stone.alabaster;

import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockAlabaster extends Block implements IOreDictEntry {

    public BlockAlabaster() {
        super(Material.ROCK, MapColor.QUARTZ);
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(new ItemStack(this), "stoneAlabaster");
    }
}
