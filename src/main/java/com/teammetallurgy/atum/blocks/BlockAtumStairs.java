package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class BlockAtumStairs extends BlockStairs {

    public BlockAtumStairs(IBlockState modelState) {
        super(modelState);
        this.useNeighborBrightness = true;
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT));
    }

    public static void registerLimestoneStairs() {
        for (BlockLimestoneBricks.BrickType type : BlockLimestoneBricks.BrickType.values()) {
            AtumRegistry.registerBlock(new BlockAtumStairs(BlockLimestoneBricks.getBrick(type).getDefaultState()), type.getName() + "_stairs");
        }
    }

    public static void registerWoodStairs() {
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            AtumRegistry.registerBlock(new BlockAtumStairs(BlockAtumPlank.getPlank(type).getDefaultState()), type.getName() + "_stairs");
        }
    }
}