package com.teammetallurgy.atum.api;

import com.google.common.collect.Lists;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

import java.util.List;

public interface IFogReductionItem {

    float getFogReduction(float fogDensity, ArmorItem armorItem);

    default List<EquipmentSlotType> getSlotTypes() {
        return Lists.newArrayList(EquipmentSlotType.HEAD);
    }
}