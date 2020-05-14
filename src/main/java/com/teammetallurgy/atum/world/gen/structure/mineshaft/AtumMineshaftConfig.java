package com.teammetallurgy.atum.world.gen.structure.mineshaft;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nonnull;

public class AtumMineshaftConfig implements IFeatureConfig {
    public final double probability;
    public final AtumMineshaftStructure.Type type;

    public AtumMineshaftConfig(double probability, AtumMineshaftStructure.Type type) {
        this.probability = probability;
        this.type = type;
    }

    @Override
    @Nonnull
    public <T> Dynamic<T> serialize(@Nonnull DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("probability"), ops.createDouble(this.probability), ops.createString("type"), ops.createString(this.type.getName()))));
    }

    public static <T> AtumMineshaftConfig deserialize(Dynamic<T> data) {
        float f = data.get("probability").asFloat(0.0F);
        AtumMineshaftStructure.Type type = AtumMineshaftStructure.Type.byName(data.get("type").asString(""));
        return new AtumMineshaftConfig(f, type);
    }
}