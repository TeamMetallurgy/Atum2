package com.teammetallurgy.atum.entity.villager;

import com.google.common.collect.Maps;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Util;

import javax.annotation.Nonnull;
import java.util.Map;

public enum Race implements IStringSerializable {
    HUMAN("human", 0.50D),
    EFREET("efreet", 0.10D),
    SHABTI("shabti", 0.10D),
    SYLPH("sylph", 0.10D),
    AVI("avi", 0.10D),
    SPHINX("sphinx", 0.10D);

    private static final Map<String, Race> BY_NAME = Util.make(Maps.newHashMap(), (nameToTypeMap) -> {
        for (Race race : values()) {
            nameToTypeMap.put(race.name, race);
        }
    });
    private final String name;
    private final double spawnWeightPercentage;

    Race(String name, double spawnWeightPercentage) {
        this.name = name;
        this.spawnWeightPercentage = spawnWeightPercentage;
    }

    public static Race getTypeFromName(String name) {
        return BY_NAME.get(name);
    }

    public String getName() {
        return this.name;
    }

    public double getSpawnWeightPercentage() {
        return this.spawnWeightPercentage;
    }

    public static Race getRandomRaceWeighted() {
        Race[] races = values();
        int idx = 0;
        for (double r = Math.random(); idx < races.length - 1; ++idx) {
            r -= races[idx].getSpawnWeightPercentage();
            if (r <= 0.0) break;
        }
        return races[idx];
    }

    @Override
    @Nonnull
    public String getString() {
        return this.name;
    }
}