package com.teammetallurgy.atum.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TreePlacerTypes {
    private static final List<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = new ArrayList<>();
    public static final FoliagePlacerType<PalmFoliagePlacer> PALM_FOLIAGE = registerFoliagePlaceType("palm", PalmFoliagePlacer.CODEC);

    public static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliagePlaceType(String name, Codec<P> codec) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        FoliagePlacerType foliagePlacerType = new FoliagePlacerType(codec);
        foliagePlacerType.setRegistryName(id);
        FOLIAGE_PLACER_TYPES.add(foliagePlacerType);
        return foliagePlacerType;
    }

    @SubscribeEvent
    public static void registerFoliagePlacerTypes(RegistryEvent.Register<FoliagePlacerType<?>> event) {
        for (FoliagePlacerType<?> foliagePlacerType : FOLIAGE_PLACER_TYPES) {
            event.getRegistry().register(foliagePlacerType);
        }
    }
}