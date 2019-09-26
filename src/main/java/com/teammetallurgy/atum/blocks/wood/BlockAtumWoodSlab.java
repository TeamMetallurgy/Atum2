package com.teammetallurgy.atum.blocks.wood;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.BlockAtumSlab;
import com.teammetallurgy.atum.items.AtumSlabItem;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import java.util.Map;

public class BlockAtumWoodSlab extends BlockAtumSlab {
    private static final Map<BlockAtumPlank.WoodType, Block> SLABS = Maps.newEnumMap(BlockAtumPlank.WoodType.class);
    private final BlockAtumPlank.WoodType type;

    private BlockAtumWoodSlab(BlockAtumPlank.WoodType type) {
        super(Material.WOOD);
        this.type = type;
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.useNeighborBrightness = true;
    }

    @Override
    @Nonnull
    public MaterialColor getMapColor(BlockState state, IBlockReader blockAccess, BlockPos blockPos) {
        return type.getMapColor();
    }

    public static void registerSlabs() {
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            BlockAtumWoodSlab woodSlab = new BlockAtumWoodSlab(type);
            SLABS.put(type, woodSlab);
            AtumRegistry.registerBlock(woodSlab, new AtumSlabItem(woodSlab, woodSlab), type.getName() + "_slab");
        }
    }

    public static Block getSlab(BlockAtumPlank.WoodType type) {
        return SLABS.get(type);
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "slabWood");
    }
}