package com.teammetallurgy.atum.api;

import com.google.common.collect.Lists;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IFogReductionItem {

    float getFogReduction(float fogDensity, ItemStack armorItem);

    default List<EquipmentSlotType> getSlotTypes() {
        return Lists.newArrayList(EquipmentSlotType.HEAD);
    }
}