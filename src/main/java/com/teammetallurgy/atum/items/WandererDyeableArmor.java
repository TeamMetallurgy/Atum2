package com.teammetallurgy.atum.items;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public class WandererDyeableArmor extends TexturedArmorItem implements IDyeableArmorItem {

    public WandererDyeableArmor(IArmorMaterial material, String name, EquipmentSlotType slot) {
        super(material, name, slot);
    }

    public WandererDyeableArmor(IArmorMaterial material, String name, EquipmentSlotType slot, Item.Properties properties) {
        super(material, name, slot, properties);
    }

    @Override
    public int getColor(@Nonnull ItemStack stack) {
        CompoundNBT nbt = stack.getChildTag("display");
        return nbt != null && nbt.contains("color", 99) ? nbt.getInt("color") : 14869989;
    }
}