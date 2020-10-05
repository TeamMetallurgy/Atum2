/*package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.Atum;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class AtumBiomeProviderTypes { //TODO Is this needed still?
    public static final BiomeProviderType<AtumBiomeProviderSettings, AtumBiomeProvider> ATUM = new BiomeProviderType<>(AtumBiomeProvider::new, AtumBiomeProviderSettings::new);

    @SubscribeEvent
    public static void registerBiomeProviderType(RegistryEvent.Register<BiomeProviderType<?, ?>> event) {
        ATUM.setRegistryName(new ResourceLocation(Atum.MOD_ID, "atum"));
        event.getRegistry().register(ATUM);
    }
}*/