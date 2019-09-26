package com.teammetallurgy.atum.blocks.stone.porphyry;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.BlockAtumSlab;
import com.teammetallurgy.atum.blocks.stone.alabaster.BlockAlabasterBricks;
import com.teammetallurgy.atum.items.AtumSlabItem;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

import java.util.Map;

public class BlockPorphyrySlab extends BlockAtumSlab {
    private static final Map<BlockAlabasterBricks.Type, Block> PORPHYRY_SLAB = Maps.newEnumMap(BlockAlabasterBricks.Type.class);

    private BlockPorphyrySlab() {
        super(Material.ROCK, MaterialColor.BLACK);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.STONE);
        this.useNeighborBrightness = true;
    }

    public static void registerSlabs() {
        for (BlockAlabasterBricks.Type type : BlockAlabasterBricks.Type.values()) {
            BlockAtumSlab slab = new BlockPorphyrySlab();
            PORPHYRY_SLAB.put(type, slab);
            AtumRegistry.registerBlock(slab, new AtumSlabItem(slab, slab), "porphyry_" + type.getName() + "_slab");
        }
    }
}