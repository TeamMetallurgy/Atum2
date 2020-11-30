package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.entity.undead.PharaohEntity;

import javax.annotation.Nonnull;

public class GodGodforgedBlock extends GodforgedBlock {
    private final PharaohEntity.God god;

    public GodGodforgedBlock(@Nonnull PharaohEntity.God god) {
        this.god = god;
    }

    public PharaohEntity.God getGod() {
        return god;
    }
}