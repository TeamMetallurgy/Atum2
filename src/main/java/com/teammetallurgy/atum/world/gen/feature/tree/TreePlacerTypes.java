package com.teammetallurgy.atum.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TreePlacerTypes {
    private static final List<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = new ArrayList<>();
    public static final FoliagePlacerType<PalmFoliagePlacer> PALM_FOLIAGE = registerFoliagePlaceType("palm", PalmFoliagePlacer.CODEC);
    public static final TrunkPlacerType<PalmTrunkPlacer> PALM_Trunk = registerTrunkPlaceType("palm", PalmTrunkPlacer.CODEC);

    public static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliagePlaceType(String name, Codec<P> codec) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        FoliagePlacerType foliagePlacerType = new FoliagePlacerType<>(codec);
        foliagePlacerType.setRegistryName(id);
        FOLIAGE_PLACER_TYPES.add(foliagePlacerType);
        return foliagePlacerType;
    }

    private static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunkPlaceType(String name, Codec<P> codec) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        return Registry.register(Registry.TRUNK_PLACER_TYPES, id, new TrunkPlacerType<>(codec)); //Registering to vanilla registry, as Forge have not added it to the RegistryEvent - when this is coded
    }

    @SubscribeEvent
    public static void registerFoliagePlacerTypes(RegistryEvent.Register<FoliagePlacerType<?>> event) {
        for (FoliagePlacerType<?> foliagePlacerType : FOLIAGE_PLACER_TYPES) {
            event.getRegistry().register(foliagePlacerType);
        }
    }
}