package com.teammetallurgy.atum.blocks.limestone;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockLimestone extends Block implements IOreDictEntry {

    public BlockLimestone() {
        super(Material.ROCK, MapColor.SAND);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(AtumBlocks.LIMESTONE_CRACKED);
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add   (new ItemStack(this), "stoneLimestone", "stone");

    }
}