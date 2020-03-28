package com.teammetallurgy.atum.world.dimension;

import com.teammetallurgy.atum.utils.Constants;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.BiFunction;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class AtumDimensionType {
    public static DimensionType ATUM;

    @SubscribeEvent
    public static void registerDimension(RegisterDimensionsEvent event) {
        ATUM = DimensionManager.registerOrGetDimension(new ResourceLocation(Constants.MOD_ID, "atum"), AtumModDimension.ATUM, new PacketBuffer(Unpooled.buffer()), true);
        DimensionManager.keepLoaded(ATUM, false); //Don't keep in memory when all chunks are unloaded
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class AtumModDimension {
        private static ModDimension ATUM = new ModDimension() {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
                return AtumDimension::new;
            }
        }.setRegistryName(new ResourceLocation(Constants.MOD_ID, "atum"));

        @SubscribeEvent
        public static void registerModDimension(RegistryEvent.Register<ModDimension> event) {
            event.getRegistry().register(ATUM);
        }
    }
}