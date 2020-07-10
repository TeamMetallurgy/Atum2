package com.teammetallurgy.atum.entity.efreet;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

public class SunspeakerData {
    private final int level;

    public SunspeakerData(int level) {
        this.level = Math.max(1, level);
    }

    public SunspeakerData(Dynamic<?> dynamic) {
        this(dynamic.get("level").asInt(1));
    }

    public int getLevel() {
        return this.level;
    }

    public SunspeakerData withLevel(int level) {
        return new SunspeakerData(level);
    }

    public <T> T serialize(DynamicOps<T> dynamicOps) {
        return dynamicOps.createInt(this.level);
    }
}