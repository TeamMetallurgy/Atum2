package com.teammetallurgy.atum.world.dimension;

import com.teammetallurgy.atum.Atum;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumDimensionType {
    public static DimensionType ATUM = DimensionManager.registerOrGetDimension(new ResourceLocation(Atum.MOD_ID, "atum"), AtumModDimension.ATUM, new PacketBuffer(Unpooled.buffer()), true);

    @SubscribeEvent
    public static void registerDimension(RegisterDimensionsEvent event) {
        ATUM = DimensionManager.registerOrGetDimension(new ResourceLocation(Atum.MOD_ID, "atum"), AtumModDimension.ATUM, new PacketBuffer(Unpooled.buffer()), true);
        DimensionManager.keepLoaded(ATUM, false); //Don't keep in memory when all chunks are unloaded
    }

    @Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class AtumModDimension {
        private static final ModDimension ATUM = ModDimension.withFactory(AtumDimension::new).setRegistryName(new ResourceLocation(Atum.MOD_ID, "atum"));

        @SubscribeEvent
        public static void registerModDimension(RegistryEvent.Register<ModDimension> event) {
            event.getRegistry().register(ATUM);
        }
    }
}