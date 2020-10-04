package com.teammetallurgy.atum.world.gen.carver;

import com.teammetallurgy.atum.Atum;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.carver.ICarverConfig;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumCarvers {
    public static final AtumCarver<ProbabilityConfig> CAVE = new AtumCaveCarver(ProbabilityConfig.field_236576_b_, 256);
    public static final AtumCarver<ProbabilityConfig> CANYON = new AtumCanyonCarver(ProbabilityConfig.field_236576_b_);
    public static final ConfiguredCarver<ProbabilityConfig> CAVE_CONFIGURED = register("cave", CAVE.func_242761_a(new ProbabilityConfig(0.14285715F)));
    public static final ConfiguredCarver<ProbabilityConfig> CANYON_CONFIGURED = register("CANYON", CAVE.func_242761_a(new ProbabilityConfig(0.02F)));

    private static <WC extends ICarverConfig> ConfiguredCarver<WC> register(String name, ConfiguredCarver<WC> carver) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_CARVER, name, carver);
    }

    @SubscribeEvent
    public static void registerCarver(RegistryEvent.Register<WorldCarver<?>> event) {
        CAVE.setRegistryName(new ResourceLocation(Atum.MOD_ID, "cave"));
        CANYON.setRegistryName(new ResourceLocation(Atum.MOD_ID, "canyon"));
        event.getRegistry().register(CAVE);
        event.getRegistry().register(CANYON);
    }
}