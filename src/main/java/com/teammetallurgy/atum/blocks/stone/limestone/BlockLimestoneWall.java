package com.teammetallurgy.atum.blocks.stone.limestone;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.BlockAtumWall;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;

import java.util.Map;

public class BlockLimestoneWall extends BlockAtumWall {
    private static final Map<BlockLimestoneBricks.BrickType, Block> WALLS = Maps.newEnumMap(BlockLimestoneBricks.BrickType.class);

    public static void registerWalls() {
        for (BlockLimestoneBricks.BrickType type : BlockLimestoneBricks.BrickType.values()) {
            Block wall = new BlockLimestoneWall();
            WALLS.put(type, wall);
            AtumRegistry.registerBlock(wall, type.getName() + "_wall");
        }
    }

    public static Block getWall(BlockLimestoneBricks.BrickType type) {
        return WALLS.get(type);
    }
}