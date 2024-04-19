package com.teammetallurgy.atum.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TreePlacerTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_DEFERRED = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, Atum.MOD_ID);
    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<PalmFoliagePlacer>> PALM_FOLIAGE_PLACER = registerFoliagePlaceType("palm_foliage_placer", PalmFoliagePlacer.CODEC);

    public static <P extends FoliagePlacer> DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<P>> registerFoliagePlaceType(String name, Codec<P> codec) {
        return FOLIAGE_PLACER_DEFERRED.register(name, () -> new FoliagePlacerType<>(codec));
    }
}