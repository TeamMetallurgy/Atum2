package com.teammetallurgy.atum.world;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.BiFunction;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumDimensionRegistration {
    @ObjectHolder("atum")
    public static DimensionType ATUM;
    public static final ModDimension ATUM_DIMENSION = new ModDimension() {
        @Override
        public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
            return OverworldDimension::new; //TODO Replace with Atum Dimension
        }
    };

    @SubscribeEvent
    public static void registerDimensionType(RegisterDimensionsEvent event) {
        ResourceLocation registryName = new ResourceLocation(Constants.MOD_ID, "atum");
        ATUM.setRegistryName(registryName);
        ATUM = DimensionManager.registerDimension(registryName, ATUM_DIMENSION, null, true);
    }

    @SubscribeEvent
    public static void registerDimension(RegistryEvent.Register<ModDimension> event) {
        event.getRegistry().register(ATUM_DIMENSION);
    }
}