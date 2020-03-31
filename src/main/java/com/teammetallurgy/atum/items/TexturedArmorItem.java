package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class TexturedArmorItem extends ArmorItem {
    private final String armorPieceName;
    private int damageModifier;

    public TexturedArmorItem(IArmorMaterial material, String name, EquipmentSlotType slot) {
        this(material, name, slot, new Item.Properties().group(Atum.GROUP));
    }

    public TexturedArmorItem(IArmorMaterial material, String name, EquipmentSlotType slot, Item.Properties properties) {
        super(material, slot, properties.group(Atum.GROUP));
        this.armorPieceName = name;
    }

    public TexturedArmorItem setDamageModifier(int percentage) {
        this.damageModifier = percentage;
        return this;
    }

    @Override
    public int getMaxDamage(@Nonnull ItemStack stack) {
        return damageModifier > 0 ? super.getMaxDamage(stack) + (super.getMaxDamage(stack) * this.damageModifier / 100) : super.getMaxDamage(stack);
    }

    @Override
    public String getArmorTexture(@Nonnull ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return String.format("%s:textures/models/armor/%s_%d%s.png", Constants.MOD_ID, armorPieceName, (slot == EquipmentSlotType.LEGS ? 2 : 1), type == null ? "" : "_overlay");
    }
}