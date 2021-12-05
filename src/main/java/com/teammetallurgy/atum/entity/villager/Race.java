package com.teammetallurgy.atum.entity.villager;

import com.google.common.collect.Maps;
import net.minecraft.util.StringRepresentable;
import net.minecraft.Util;

import javax.annotation.Nonnull;
import java.util.Map;

public enum Race implements StringRepresentable {
    HUMAN("human", 1.0D, 18); //Change to 0.5D, when races below is added
    /*EFREET("efreet", 0.10D),
    SHABTI("shabti", 0.10D),
    SYLPH("sylph", 0.10D),
    AVI("avi", 0.10D),
    SPHINX("sphinx", 0.10D);*/

    private static final Map<String, Race> BY_NAME = Util.make(Maps.newHashMap(), (nameToTypeMap) -> {
        for (Race race : values()) {
            nameToTypeMap.put(race.name, race);
        }
    });
    private final String name;
    private final double spawnWeightPercentage;
    private final int variantAmount;

    Race(String name, double spawnWeightPercentage, int variantAmount) {
        this.name = name;
        this.spawnWeightPercentage = spawnWeightPercentage;
        this.variantAmount = variantAmount;
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

    public int getVariantAmount() {
        return this.variantAmount;
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
    public String getSerializedName() {
        return this.name;
    }
}