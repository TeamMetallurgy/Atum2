package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemLimestonePickaxe extends ItemPickaxe {

    public ItemLimestonePickaxe(ToolMaterial material) {
        super(material);
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return Block.getBlockFromItem(repair.getItem()) == AtumBlocks.LIMESTONE_CRACKED;
    }
}