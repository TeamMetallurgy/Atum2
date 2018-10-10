package com.teammetallurgy.atum.items;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.undead.EntityPharaoh;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.item.ItemSword;

import java.util.Map;

public class ItemScepter extends ItemSword {
    private static final Map<EntityPharaoh.God, ItemScepter> SCEPTERS = Maps.newEnumMap(EntityPharaoh.God.class);

    private ItemScepter() {
        super(ToolMaterial.GOLD);
    }

    public static void registerScepters() {
        for (EntityPharaoh.God god : EntityPharaoh.God.values()) {
            ItemScepter scepter = new ItemScepter();
            SCEPTERS.put(god, scepter);
            AtumRegistry.registerItem(scepter, "scepter_" + god.getName(), null, null);
        }
    }

    public static ItemScepter getScepter(EntityPharaoh.God god) {
        return SCEPTERS.get(god);
    }
}
