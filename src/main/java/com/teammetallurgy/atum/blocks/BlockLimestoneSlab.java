package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.items.ItemAtumSlab;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockLimestoneSlab extends BlockAtumSlab {

    public BlockLimestoneSlab() {
        super(Material.ROCK, MapColor.SAND);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.STONE);
    }

    public static void registerSlabs() {
        for (BlockLimestoneBricks.BrickType type : BlockLimestoneBricks.BrickType.values()) {
            Block LIMESTONE_SLAB = new BlockLimestoneSlab();
            AtumRegistry.registerBlock(LIMESTONE_SLAB, new ItemAtumSlab(LIMESTONE_SLAB, (BlockAtumSlab) LIMESTONE_SLAB), "limestone_" + type.getName() + "_slab");
        }
    }
}