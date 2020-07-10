package com.teammetallurgy.atum.world.gen.carver;

import com.teammetallurgy.atum.Atum;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumCarvers {
    public static final AtumCarver<ProbabilityConfig> CAVE = new AtumCaveCarver(ProbabilityConfig::deserialize, 256);
    public static final AtumCarver<ProbabilityConfig> CANYON = new AtumCanyonCarver(ProbabilityConfig::deserialize);

    @SubscribeEvent
    public static void registerCarver(RegistryEvent.Register<WorldCarver<?>> event) {
        CAVE.setRegistryName(new ResourceLocation(Atum.MOD_ID, "cave"));
        CANYON.setRegistryName(new ResourceLocation(Atum.MOD_ID, "canyon"));
        event.getRegistry().register(CAVE);
        event.getRegistry().register(CANYON);
    }
}