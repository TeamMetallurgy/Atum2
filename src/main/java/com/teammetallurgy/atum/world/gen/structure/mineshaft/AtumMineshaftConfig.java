/*
package com.teammetallurgy.atum.world.gen.structure.mineshaft;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class AtumMineshaftConfig implements FeatureConfiguration { //TODO
    public static final Codec<AtumMineshaftConfig> CODEC = RecordCodecBuilder.create((b) -> b.group(Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter((c) -> c.probability),
                                                                                               AtumMineshaftStructure.Type.CODEC.fieldOf("type").forGetter((c) -> c.type)).apply(b, AtumMineshaftConfig::new));
    public final float probability;
    public final AtumMineshaftStructure.Type type;

    public AtumMineshaftConfig(float probability, AtumMineshaftStructure.Type type) {
        this.probability = probability;
        this.type = type;
    }
}*/
