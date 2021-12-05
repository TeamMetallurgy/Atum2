package com.teammetallurgy.atum.items.tools;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;

import java.util.Map;

public class ScepterItem extends SwordItem {
    public static final Map<God, ScepterItem> SCEPTERS = Maps.newEnumMap(God.class);

    public ScepterItem() {
        super(AtumMats.NEBU, 3, -2.4F, new Item.Properties());
    }

    public static ScepterItem getScepter(God god) {
        return SCEPTERS.get(god);
    }
}