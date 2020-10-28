package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumSurfaceBuilders {
    //Surface Builders
    private static final List<SurfaceBuilder<?>> SURFACE_BUILDERS = new ArrayList<>();
    public static final SurfaceBuilder<SurfaceBuilderConfig> OASIS_SURFACE_BUILDER = registerBuilder("oasis", new OasisSurfaceBuilder(SurfaceBuilderConfig.field_237203_a_));

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
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> OASIS = registerConfig("oasis", OASIS_SURFACE_BUILDER.func_242929_a(OASIS_CONFIG));

    private static <C extends ISurfaceBuilderConfig, F extends SurfaceBuilder<C>> F registerBuilder(String name, F builder) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        builder.setRegistryName(id);
        SURFACE_BUILDERS.add(builder);
        return builder;
    }

    private static <SC extends ISurfaceBuilderConfig> ConfiguredSurfaceBuilder<SC> registerConfig(String name, ConfiguredSurfaceBuilder<SC> csb) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, name, csb);
    }

    @SubscribeEvent
    public static void registerSurfaceBuilders(RegistryEvent.Register<SurfaceBuilder<?>> event) {
        for (SurfaceBuilder<?> surfaceBuilder : SURFACE_BUILDERS) {
            event.getRegistry().register(surfaceBuilder);
        }
    }
}