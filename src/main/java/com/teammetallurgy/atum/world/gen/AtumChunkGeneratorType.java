package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class AtumChunkGeneratorType {
    public static final ChunkGeneratorType<AtumGenSettings, AtumChunkGenerator> ATUM = new ChunkGeneratorType(AtumChunkGenerator::new, true, AtumGenSettings::new);

    @SubscribeEvent
    public static void registerChunkGeneratorType(RegistryEvent.Register<ChunkGeneratorType<?, ?>> event) {
        ATUM.setRegistryName(new ResourceLocation(Constants.MOD_ID, "atum"));
        event.getRegistry().register(ATUM);
    }
}