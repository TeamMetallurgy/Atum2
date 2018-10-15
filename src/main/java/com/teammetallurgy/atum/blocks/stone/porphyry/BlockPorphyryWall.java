package com.teammetallurgy.atum.blocks.stone.porphyry;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.BlockAtumWall;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.teammetallurgy.atum.blocks.stone.alabaster.BlockAlabasterBricks.Type;

public class BlockPorphyryWall extends BlockAtumWall {
    private static final Map<Type, Block> WALLS = Maps.newEnumMap(Type.class);

    @Override
    @Nonnull
    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos blockPos) {
        return MapColor.BLACK;
    }

    public static void registerWalls() {
        for (Type type : Type.values()) {
            Block wall = new BlockPorphyryWall();
            WALLS.put(type, wall);
            AtumRegistry.registerBlock(wall, "porphyry_" + type.getName() + "_wall");
        }
    }

    public static Block getWall(Type type) {
        return WALLS.get(type);
    }
}