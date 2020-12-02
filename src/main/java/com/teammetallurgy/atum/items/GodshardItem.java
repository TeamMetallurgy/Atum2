package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.api.God;

public class GodshardItem extends SimpleItem {
    private final God god;

    public GodshardItem(God god) {
        this.god = god;
    }

    public God getGod() {
        return this.god;
    }
}