package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

public class AtumSurfaceBuilders {
    //States
    public static final BlockState SAND = AtumBlocks.SAND.defaultBlockState();
    public static final BlockState LIMESTONE = AtumBlocks.LIMESTONE.defaultBlockState();
    public static final BlockState CRACKED_LIMESTONE = AtumBlocks.LIMESTONE_CRACKED.defaultBlockState();
    public static final BlockState GRAVEL = AtumBlocks.LIMESTONE_GRAVEL.defaultBlockState();
    public static final BlockState FERTILE_SOIL = AtumBlocks.FERTILE_SOIL.defaultBlockState();

    //Configs
    public static final SurfaceBuilderBaseConfiguration SANDY_CONFIG = new SurfaceBuilderBaseConfiguration(SAND, SAND, SAND);
    public static final SurfaceBuilderBaseConfiguration SANDY_LIMESTONE_CONFIG = new SurfaceBuilderBaseConfiguration(SAND, LIMESTONE, SAND);
    public static final SurfaceBuilderBaseConfiguration GRAVEL_CRACKED_CONFIG = new SurfaceBuilderBaseConfiguration(GRAVEL, CRACKED_LIMESTONE, GRAVEL);
    public static final SurfaceBuilderBaseConfiguration OASIS_CONFIG = new SurfaceBuilderBaseConfiguration(FERTILE_SOIL, SAND, FERTILE_SOIL);

    //Configured Surface Builders
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> SANDY = registerConfig("sandy", SurfaceBuilder.DEFAULT.configured(SANDY_CONFIG));
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> SANDY_LIMESTONE = registerConfig("sandy_limestone", SurfaceBuilder.DEFAULT.configured(SANDY_LIMESTONE_CONFIG));
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> GRAVEL_CRACKED = registerConfig("gravel_cracked", SurfaceBuilder.DEFAULT.configured(GRAVEL_CRACKED_CONFIG));
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> OASIS = registerConfig("oasis", SurfaceBuilder.DEFAULT.configured(OASIS_CONFIG));

    private static <SC extends SurfaceBuilderConfiguration> ConfiguredSurfaceBuilder<SC> registerConfig(String name, ConfiguredSurfaceBuilder<SC> csb) {
        return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, name, csb);
    }
}