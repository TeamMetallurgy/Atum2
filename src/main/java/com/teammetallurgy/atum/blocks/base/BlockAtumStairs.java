package com.teammetallurgy.atum.blocks.base;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.stone.alabaster.BlockAlabasterBricks;
import com.teammetallurgy.atum.blocks.stone.ceramic.BlockCeramic;
import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestoneBricks;
import com.teammetallurgy.atum.blocks.stone.porphyry.BlockPorphyryBricks;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;

import java.util.Map;

public class BlockAtumStairs extends BlockStairs {
    private static final Map<BlockLimestoneBricks.BrickType, Block> BRICK_STAIRS = Maps.newEnumMap(BlockLimestoneBricks.BrickType.class);
    private static final Map<BlockAlabasterBricks.Type, Block> ALABASTER_STAIRS = Maps.newEnumMap(BlockAlabasterBricks.Type.class);
    private static final Map<BlockAlabasterBricks.Type, Block> PORPHYRY_STAIRS = Maps.newEnumMap(BlockAlabasterBricks.Type.class);
    private static final Map<EnumDyeColor, Block> CERAMIC_STAIRS = Maps.newEnumMap(EnumDyeColor.class);
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
            OreDictHelper.add(woodStair, "stairWood");
        }
    }

    public static Block getWoodStairs(BlockAtumPlank.WoodType type) {
        return WOOD_STAIRS.get(type);
    }

    public static void registerAlabasterStairs() {
        for (BlockAlabasterBricks.Type type : BlockAlabasterBricks.Type.values()) {
            Block alabasterStair = new BlockAtumStairs(BlockAlabasterBricks.getBrick(type).getDefaultState());
            ALABASTER_STAIRS.put(type, alabasterStair);
            AtumRegistry.registerBlock(alabasterStair, "alabaster_" + type.getName() + "_stairs");
        }
    }

    public static Block getAlabasterStairs(BlockAlabasterBricks.Type type) {
        return ALABASTER_STAIRS.get(type);
    }

    public static void registerPorphyryStairs() {
        for (BlockAlabasterBricks.Type type : BlockAlabasterBricks.Type.values()) {
            Block alabasterStair = new BlockAtumStairs(BlockPorphyryBricks.getBrick(type).getDefaultState());
            PORPHYRY_STAIRS.put(type, alabasterStair);
            AtumRegistry.registerBlock(alabasterStair, "porphyry_" + type.getName() + "_stairs");
        }
    }

    public static Block getPorphyryStairs(BlockAlabasterBricks.Type type) {
        return PORPHYRY_STAIRS.get(type);
    }

    public static void registerCeramicStairs() {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            Block ceramicStair = new BlockAtumStairs(BlockCeramic.getCeramicBlocks(color).getDefaultState());
            CERAMIC_STAIRS.put(color, ceramicStair);
            AtumRegistry.registerBlock(ceramicStair, "ceramic_stairs_" + color.getName());
        }
    }

    public static Block getCeramicStairs(EnumDyeColor color) {
        return CERAMIC_STAIRS.get(color);
    }
}