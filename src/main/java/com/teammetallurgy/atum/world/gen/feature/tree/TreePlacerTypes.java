package com.teammetallurgy.atum.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TreePlacerTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_DEFERRED = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, Atum.MOD_ID);
    public static final RegistryObject<FoliagePlacerType<PalmFoliagePlacer>> PALM_FOLIAGE_PLACER = registerFoliagePlaceType("palm_foliage_placer", PalmFoliagePlacer.CODEC);

    public static <P extends FoliagePlacer> RegistryObject<FoliagePlacerType<P>> registerFoliagePlaceType(String name, Codec<P> codec) {
        return FOLIAGE_PLACER_DEFERRED.register(name, () -> new FoliagePlacerType<>(codec));
    }
}