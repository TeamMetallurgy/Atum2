package com.teammetallurgy.atum.entity.villager;

import com.google.common.collect.Maps;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Util;

import javax.annotation.Nonnull;
import java.util.Map;

public enum Race implements IStringSerializable {
    HUMAN("human"),
    EFREET("efreet"),
    SHABTI("shabti"),
    SYLPH("sylph"),
    AVI("avi"),
    SPHINX("sphinx");

    private static final Map<String, Race> BY_NAME = Util.make(Maps.newHashMap(), (nameToTypeMap) -> {
        for(Race race : values()) {
            nameToTypeMap.put(race.name, race);
        }
    });
    private final String name;

    Race(String name) {
        this.name = name;
    }

    public static Race getTypeFromName(String name) {
        return BY_NAME.get(name);
    }

    public String getName() {
        return this.name;
    }

    @Override
    @Nonnull
    public String getString() {
        return this.name;
    }
}