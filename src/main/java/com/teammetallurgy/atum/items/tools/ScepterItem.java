package com.teammetallurgy.atum.items.tools;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.undead.EntityPharaoh;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;

import java.util.Map;

public class ScepterItem extends SwordItem {
    private static final Map<EntityPharaoh.God, ScepterItem> SCEPTERS = Maps.newEnumMap(EntityPharaoh.God.class);

    private ScepterItem() {
        super(ItemTier.GOLD, 3, -2.4F, new Item.Properties());
    }

    public static ScepterItem registerScepters() {
        for (EntityPharaoh.God god : EntityPharaoh.God.values()) {
            ScepterItem scepter = new ScepterItem();
            SCEPTERS.put(god, scepter);
            return (ScepterItem) AtumRegistry.registerItem(scepter, "scepter_" + god.getName());
        }
        return (ScepterItem) Items.AIR;
    }

    public ScepterItem getScepter(EntityPharaoh.God god) {
        return SCEPTERS.get(god);
    }
}
