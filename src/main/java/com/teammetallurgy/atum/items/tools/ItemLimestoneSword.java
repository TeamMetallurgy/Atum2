package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import javax.annotation.Nonnull;

public class ItemLimestoneSword extends ItemSword {

    public ItemLimestoneSword(ToolMaterial material) {
        super(material);
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return Block.getBlockFromItem(repair.getItem()) == AtumBlocks.LIMESTONE_CRACKED;
    }
}