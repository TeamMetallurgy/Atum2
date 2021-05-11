package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.api.God;

import javax.annotation.Nonnull;

public class GodGodforgedBlock extends GodforgedBlock {
    private final God god;

    public GodGodforgedBlock(@Nonnull God god) {
        this.god = god;
    }

    public God getGod() {
        return god;
    }
}