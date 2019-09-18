package com.teammetallurgy.atum.items.tools;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.undead.EntityPharaoh;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.item.ItemSword;

import java.util.Map;

public class ScepterItem extends ItemSword {
    private static final Map<EntityPharaoh.God, ScepterItem> SCEPTERS = Maps.newEnumMap(EntityPharaoh.God.class);

    private ScepterItem() {
        super(ToolMaterial.GOLD);
        this.setCreativeTab(null);
    }

    public static void registerScepters() {
        for (EntityPharaoh.God god : EntityPharaoh.God.values()) {
            ScepterItem scepter = new ScepterItem();
            SCEPTERS.put(god, scepter);
            AtumRegistry.registerItem(scepter, "scepter_" + god.getName(), null, null);
        }
    }

    public static ScepterItem getScepter(EntityPharaoh.God god) {
        return SCEPTERS.get(god);
    }
}
