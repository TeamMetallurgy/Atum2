package com.teammetallurgy.atum.blocks.base;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.limestone.BlockLimestoneBricks;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import java.util.Map;

public class BlockAtumStairs extends BlockStairs {
    private static final Map<BlockLimestoneBricks.BrickType, Block> BRICK_STAIRS = Maps.newEnumMap(BlockLimestoneBricks.BrickType.class);
    private static final Map<BlockAtumPlank.WoodType, Block> WOOD_STAIRS = Maps.newEnumMap(BlockAtumPlank.WoodType.class);

    public BlockAtumStairs(IBlockState modelState) {
        super(modelState);
        this.useNeighborBrightness = true;
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT));
    }

    public static void registerLimestoneStairs() {
        for (BlockLimestoneBricks.BrickType type : BlockLimestoneBricks.BrickType.values()) {
            Block brickStair = new BlockAtumStairs(BlockLimestoneBricks.getBrick(type).getDefaultState());
            BRICK_STAIRS.put(type, brickStair);
            AtumRegistry.registerBlock(brickStair, type.getName() + "_stairs");
        }
    }

    public static Block getBrickStairs(BlockLimestoneBricks.BrickType type) {
        return BRICK_STAIRS.get(type);
    }

    public static void registerWoodStairs() {
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            Block woodStair = new BlockAtumStairs(BlockAtumPlank.getPlank(type).getDefaultState());
            WOOD_STAIRS.put(type, woodStair);
            AtumRegistry.registerBlock(woodStair, type.getName() + "_stairs");
        }
    }

    public static Block getWoodStairs(BlockAtumPlank.WoodType type) {
        return WOOD_STAIRS.get(type);
    }
}