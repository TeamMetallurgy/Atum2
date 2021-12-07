/*
package com.teammetallurgy.atum.world.gen.carver;

import com.teammetallurgy.atum.Atum;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumCarvers { //TODO. Is this still needed, or can vanilla carver be used?
    public static final AtumCarver<ProbabilityFeatureConfiguration> CAVE = new AtumCaveCarver(ProbabilityFeatureConfiguration.CODEC, 256);
    public static final AtumCarver<ProbabilityFeatureConfiguration> CANYON = new AtumCanyonCarver(ProbabilityFeatureConfiguration.CODEC);
    public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> CAVE_CONFIGURED = register("cave", CAVE.configured(new ProbabilityFeatureConfiguration(0.14285715F)));
    public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> CANYON_CONFIGURED = register("canyon", CAVE.configured(new ProbabilityFeatureConfiguration(0.02F)));

    private static <WC extends CarverConfiguration> ConfiguredWorldCarver<WC> register(String name, ConfiguredWorldCarver<WC> carver) {
        return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_CARVER, new ResourceLocation(Atum.MOD_ID, name), carver);
    }

    @SubscribeEvent
    public static void registerCarver(RegistryEvent.Register<WorldCarver<?>> event) {
        CAVE.setRegistryName(new ResourceLocation(Atum.MOD_ID, "cave"));
        CANYON.setRegistryName(new ResourceLocation(Atum.MOD_ID, "canyon"));
        event.getRegistry().register(CAVE);
        event.getRegistry().register(CANYON);
    }
}*/
