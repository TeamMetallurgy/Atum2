package com.teammetallurgy.atum.world.biome;

import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;

public enum BiomeRegion implements StringRepresentable {
    STRANGE_SANDS("strange_sands"),
    LIMESTONE_PEAKS("limestone_peaks"),
    DESSICATED_WOODS("dessicated_woods"),
    SCORCHED_CHASMS("scorched_chasms"),
    SUNSCARRED_CLAIM("sunscarred_claim"),
    ABYSSAL_REACHES("abyssal_reaches"),
    DEPTHS("depths");;

    private final String name;

    BiomeRegion(String name) {
        this.name = name;
    }

    @Override
    @Nonnull
    public String getSerializedName() {
        return this.name;
    }
}