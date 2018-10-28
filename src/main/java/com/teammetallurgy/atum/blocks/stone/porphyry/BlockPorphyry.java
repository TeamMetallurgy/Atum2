package com.teammetallurgy.atum.blocks.stone.porphyry;

import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockPorphyry extends Block implements IOreDictEntry {

    public BlockPorphyry() {
        super(Material.ROCK, MapColor.BLACK);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(new ItemStack(this), "stonePorphyry");
    }
}
