package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemLimestoneAxe extends ItemAxe {

    public ItemLimestoneAxe() {
        super(ToolMaterial.STONE);
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return Block.getBlockFromItem(repair.getItem()) == AtumBlocks.LIMESTONE_CRACKED;
    }
}