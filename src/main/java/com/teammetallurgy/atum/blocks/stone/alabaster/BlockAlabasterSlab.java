package com.teammetallurgy.atum.blocks.stone.alabaster;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.BlockAtumSlab;
import com.teammetallurgy.atum.items.AtumSlabItem;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

import java.util.Map;

public class BlockAlabasterSlab extends BlockAtumSlab {
    private static final Map<BlockAlabasterBricks.Type, Block> ALABASTER_SLAB = Maps.newEnumMap(BlockAlabasterBricks.Type.class);

    private BlockAlabasterSlab() {
        super(Material.ROCK, MaterialColor.QUARTZ);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.STONE);
        this.useNeighborBrightness = true;
    }

    public static void registerSlabs() {
        for (BlockAlabasterBricks.Type type : BlockAlabasterBricks.Type.values()) {
            BlockAtumSlab slab = new BlockAlabasterSlab();
            ALABASTER_SLAB.put(type, slab);
            AtumRegistry.registerBlock(slab, new AtumSlabItem(slab, slab), "alabaster_" + type.getName() + "_slab");
        }
    }
}