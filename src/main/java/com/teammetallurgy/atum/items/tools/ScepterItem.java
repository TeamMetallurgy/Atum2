package com.teammetallurgy.atum.items.tools;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.material.AtumMaterialTiers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class ScepterItem extends SwordItem {
    public static final Map<God, RegistryObject<Item>> SCEPTERS = Maps.newEnumMap(God.class);

    public ScepterItem() {
        super(AtumMaterialTiers.NEBU, 3, -2.4F, new Item.Properties());
    }

    public static Item getScepter(God god) {
        return SCEPTERS.get(god).get();
    }
}