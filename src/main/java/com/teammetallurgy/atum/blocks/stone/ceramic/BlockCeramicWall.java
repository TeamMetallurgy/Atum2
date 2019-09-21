package com.teammetallurgy.atum.blocks.stone.ceramic;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.BlockAtumWall;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;

import java.util.Map;

public class BlockCeramicWall extends BlockAtumWall {
    private static final Map<DyeColor, Block> WALLS = Maps.newEnumMap(DyeColor.class);

    public static void registerWalls() {
        for (DyeColor color : DyeColor.values()) {
            Block wall = new BlockCeramicWall();
            WALLS.put(color, wall);
            AtumRegistry.registerBlock(wall, "ceramic_wall_" + color.getName());
        }
    }

    public static Block getWall(DyeColor type) {
        return WALLS.get(type);
    }
}