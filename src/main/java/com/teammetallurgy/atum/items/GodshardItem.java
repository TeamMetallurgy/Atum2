package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.world.item.Item;

public class GodshardItem extends SimpleItem {
    private final God god;

    public GodshardItem(God god) {
        this.god = god;
    }

    public God getGod() {
        return this.god;
    }

    public static Item getGodshardFromGod(God god) {
        return StackHelper.getItemFromName(god.getName() + "_godshard");
    }
}