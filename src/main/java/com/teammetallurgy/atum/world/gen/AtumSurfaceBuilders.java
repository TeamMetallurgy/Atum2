package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class AtumSurfaceBuilders {
    public static final BlockState SAND = AtumBlocks.SAND.getDefaultState();
    public static final BlockState LIMESTONE = AtumBlocks.LIMESTONE.getDefaultState();
    public static final BlockState CRACKED_LIMESTONE = AtumBlocks.LIMESTONE_CRACKED.getDefaultState();
    public static final BlockState GRAVEL = AtumBlocks.LIMESTONE_GRAVEL.getDefaultState();
    public static final BlockState FERTILE_SOIL = AtumBlocks.FERTILE_SOIL.getDefaultState();
    public static final SurfaceBuilderConfig SANDY = new SurfaceBuilderConfig(SAND, SAND, SAND);
    public static final SurfaceBuilderConfig SANDY_LIMESTONE = new SurfaceBuilderConfig(SAND, LIMESTONE, SAND);
    public static final SurfaceBuilderConfig GRAVEL_CRACKED = new SurfaceBuilderConfig(GRAVEL, CRACKED_LIMESTONE, GRAVEL);
    public static final SurfaceBuilderConfig OASIS = new SurfaceBuilderConfig(FERTILE_SOIL, LIMESTONE, FERTILE_SOIL);
}