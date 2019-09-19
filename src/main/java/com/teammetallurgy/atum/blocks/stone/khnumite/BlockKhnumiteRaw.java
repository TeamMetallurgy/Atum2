package com.teammetallurgy.atum.blocks.stone.khnumite;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockKhnumiteRaw extends Block {

    public BlockKhnumiteRaw() {
        super(Material.CLAY);
        this.setHardness(0.6F);
        this.setSoundType(SoundType.GROUND);
    }

    @Override
    @Nonnull
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return AtumItems.KHNUMITE;
    }

    @Override
    public int quantityDropped(Random random) {
        return MathHelper.getInt(random, 2, 3);
    }
}