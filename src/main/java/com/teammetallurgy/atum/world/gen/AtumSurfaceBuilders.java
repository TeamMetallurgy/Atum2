package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class AtumSurfaceBuilders {
    //States
    public static final BlockState SAND = AtumBlocks.SAND.getDefaultState();
    public static final BlockState LIMESTONE = AtumBlocks.LIMESTONE.getDefaultState();
    public static final BlockState CRACKED_LIMESTONE = AtumBlocks.LIMESTONE_CRACKED.getDefaultState();
    public static final BlockState GRAVEL = AtumBlocks.LIMESTONE_GRAVEL.getDefaultState();
    public static final BlockState FERTILE_SOIL = AtumBlocks.FERTILE_SOIL.getDefaultState();

    //Configs
    public static final SurfaceBuilderConfig SANDY_CONFIG = new SurfaceBuilderConfig(SAND, SAND, SAND);
    public static final SurfaceBuilderConfig SANDY_LIMESTONE_CONFIG = new SurfaceBuilderConfig(SAND, LIMESTONE, SAND);
    public static final SurfaceBuilderConfig GRAVEL_CRACKED_CONFIG = new SurfaceBuilderConfig(GRAVEL, CRACKED_LIMESTONE, GRAVEL);
    public static final SurfaceBuilderConfig OASIS_CONFIG = new SurfaceBuilderConfig(FERTILE_SOIL, SAND, FERTILE_SOIL);

    //Configured Surface Builders
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> SANDY = registerConfig("sandy", SurfaceBuilder.DEFAULT.func_242929_a(SANDY_CONFIG));
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> SANDY_LIMESTONE = registerConfig("sandy_limestone", SurfaceBuilder.DEFAULT.func_242929_a(SANDY_LIMESTONE_CONFIG));
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> GRAVEL_CRACKED = registerConfig("gravel_cracked", SurfaceBuilder.DEFAULT.func_242929_a(GRAVEL_CRACKED_CONFIG));
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> OASIS = registerConfig("oasis", SurfaceBuilder.DEFAULT.func_242929_a(OASIS_CONFIG));

    private static <SC extends ISurfaceBuilderConfig> ConfiguredSurfaceBuilder<SC> registerConfig(String name, ConfiguredSurfaceBuilder<SC> csb) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, name, csb);
    }
}