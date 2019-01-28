package com.teammetallurgy.atum.blocks.stone.ceramic;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.BlockAtumWall;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;

import java.util.Map;

public class BlockCeramicWall extends BlockAtumWall {
    private static final Map<EnumDyeColor, Block> WALLS = Maps.newEnumMap(EnumDyeColor.class);

    public static void registerWalls() {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            Block wall = new BlockCeramicWall();
            WALLS.put(color, wall);
            AtumRegistry.registerBlock(wall, "ceramic_wall_" + color.getName());
        }
    }

    public static Block getWall(EnumDyeColor type) {
        return WALLS.get(type);
    }
}