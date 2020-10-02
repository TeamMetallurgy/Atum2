package com.teammetallurgy.atum.entity.efreet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SunspeakerData {
    public static final Codec<SunspeakerData> CODEC = RecordCodecBuilder.create((dataInstance) -> dataInstance.group(Codec.INT.fieldOf("level").orElse(1).forGetter((data) -> data.level))
            .apply(dataInstance, SunspeakerData::new));
    private final int level;

    public SunspeakerData(int level) {
        this.level = Math.max(1, level);
    }

    public int getLevel() {
        return this.level;
    }

    public SunspeakerData withLevel(int level) {
        return new SunspeakerData(level);
    }
}