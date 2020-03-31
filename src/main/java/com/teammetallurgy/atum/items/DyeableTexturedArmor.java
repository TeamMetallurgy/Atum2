package com.teammetallurgy.atum.items;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;

public class DyeableTexturedArmor extends TexturedArmorItem implements IDyeableArmorItem {

    public DyeableTexturedArmor(IArmorMaterial material, String name, EquipmentSlotType slot) {
        super(material, name, slot);
    }

    public DyeableTexturedArmor(IArmorMaterial material, String name, EquipmentSlotType slot, Item.Properties properties) {
        super(material, name, slot, properties);
    }
}