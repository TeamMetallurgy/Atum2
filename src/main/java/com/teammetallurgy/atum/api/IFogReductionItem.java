package com.teammetallurgy.atum.api;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IFogReductionItem {

    float getFogReduction(float fogDensity, ItemStack armorItem);

    default List<EquipmentSlot> getSlotTypes() {
        return Lists.newArrayList(EquipmentSlot.HEAD);
    }
}