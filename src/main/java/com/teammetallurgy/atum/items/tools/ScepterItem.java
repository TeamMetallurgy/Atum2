package com.teammetallurgy.atum.items.tools;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;

import java.util.Map;

public class ScepterItem extends SwordItem {
    public static final Map<PharaohEntity.God, ScepterItem> SCEPTERS = Maps.newEnumMap(PharaohEntity.God.class);

    public ScepterItem() {
        super(ItemTier.GOLD, 3, -2.4F, new Item.Properties());
    }

    public static ScepterItem getScepter(PharaohEntity.God god) {
        return SCEPTERS.get(god);
    }
}