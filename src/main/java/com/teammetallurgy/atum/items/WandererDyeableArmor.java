package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.api.IFogReductionItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class WandererDyeableArmor extends TexturedArmorItem implements DyeableLeatherItem, IFogReductionItem {

    public WandererDyeableArmor(ArmorMaterial material, String name, EquipmentSlot slot) {
        super(material, name, slot);
    }

    public WandererDyeableArmor(ArmorMaterial material, String name, EquipmentSlot slot, Item.Properties properties) {
        super(material, name, slot, properties);
    }

    @Override
    public int getColor(@Nonnull ItemStack stack) {
        CompoundTag nbt = stack.getTagElement("display");
        return nbt != null && nbt.contains("color", 99) ? nbt.getInt("color") : 14869989;
    }

    @Override
    public float getFogReduction(float fogDensity, ItemStack armorItem) {
        return fogDensity * 2.0F;
    }
}