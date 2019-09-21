package com.teammetallurgy.atum.blocks.stone.ceramic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.Map;

public class BlockCeramic extends Block {
    private static final Map<DyeColor, Block> CERAMIC = Maps.newEnumMap(DyeColor.class);

    BlockCeramic() {
        super(Material.ROCK);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 0);
    }

    public static void registerCeramicBlocks() {
        for (DyeColor color : DyeColor.values()) {
            BlockCeramic ceramic = new BlockCeramic();
            CERAMIC.put(color, ceramic);
            AtumRegistry.registerBlock(ceramic, "ceramic_" + color.getName());
        }
    }

    public static Block getCeramicBlocks(DyeColor color) {
        return CERAMIC.get(color);
    }

    @Override
    @Nonnull
    public MapColor getMapColor(BlockState state, IBlockAccess world, BlockPos pos) {
        return MapColor.getBlockColor(DyeColor.valueOf(getColorString().toUpperCase()));
    }

    private String getColorString() {
        Preconditions.checkNotNull(this.getRegistryName(), "registryName");
        return this.getRegistryName().getPath().replace("ceramic_", "").replace("tile_", "");
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return 0;
    }
}