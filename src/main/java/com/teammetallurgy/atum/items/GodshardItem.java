package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.entity.undead.PharaohEntity;

public class GodshardItem extends SimpleItem {
    private final PharaohEntity.God god;

    public GodshardItem(PharaohEntity.God god) {
        this.god = god;
    }

    public PharaohEntity.God getGod() {
        return this.god;
    }
}