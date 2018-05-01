package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.items.ItemAtumSlab;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public class BlockAtumWoodSlab extends BlockAtumSlab {
    private final BlockAtumPlank.WoodType type;

    public BlockAtumWoodSlab(BlockAtumPlank.WoodType type) {
        super(Material.WOOD);
        this.type = type;
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.useNeighborBrightness = true;
    }

    @Override
    @Nonnull
    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos blockPos) {
        return type.getMapColor();
    }

    public static void registerSlabs() {
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            Block WOOD_SLAB = new BlockAtumWoodSlab(type);
            AtumRegistry.registerBlock(WOOD_SLAB, new ItemAtumSlab(WOOD_SLAB, (BlockAtumSlab) WOOD_SLAB), type.getName() + "_slab");
        }
    }
}