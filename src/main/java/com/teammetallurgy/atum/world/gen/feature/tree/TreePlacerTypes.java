package com.teammetallurgy.atum.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TreePlacerTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_DEFERRED = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, Atum.MOD_ID);
    public static final RegistryObject<FoliagePlacerType<PalmFoliagePlacer>> PALM_FOLIAGE = registerFoliagePlaceType("palm", PalmFoliagePlacer.CODEC);
    //public static final TrunkPlacerType<PalmTrunkPlacer> PALM_Trunk = registerTrunkPlaceType("palm", PalmTrunkPlacer.CODEC);

    public static <P extends FoliagePlacer> RegistryObject<FoliagePlacerType<P>> registerFoliagePlaceType(String name, Codec<P> codec) {
        return FOLIAGE_PLACER_DEFERRED.register(name, () -> new FoliagePlacerType<>(codec));
    }

    private static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunkPlaceType(String name, Codec<P> codec) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        return Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, id, new TrunkPlacerType<>(codec)); //Registering to vanilla registry, as Forge have not added it to the RegistryEvent - when this is coded
    }
}