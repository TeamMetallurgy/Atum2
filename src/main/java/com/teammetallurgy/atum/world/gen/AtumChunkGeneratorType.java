/*package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.Atum;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class AtumChunkGeneratorType { //TODO Is this needed anymore?
    public static final ChunkGeneratorType<AtumGenSettings, AtumChunkGenerator> ATUM = new ChunkGeneratorType<>(AtumChunkGenerator::new, true, AtumGenSettings::new);

    @SubscribeEvent
    public static void registerChunkGeneratorType(RegistryEvent.Register<ChunkGeneratorType<?, ?>> event) {
        ATUM.setRegistryName(new ResourceLocation(Atum.MOD_ID, "atum"));
        event.getRegistry().register(ATUM);
    }
}*/