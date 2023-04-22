package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class TexturedArmorItem extends ArmorItem {
    private final String armorPieceName;
    private boolean hasRender = false;
    private int damageModifier;

    public TexturedArmorItem(ArmorMaterial material, String name, Type type) {
        this(material, name, type, new Item.Properties());
    }

    public TexturedArmorItem(ArmorMaterial material, String name, Type type, Item.Properties properties) {
        super(material, type, properties);
        this.armorPieceName = name;
    }

    public TexturedArmorItem setDamageModifier(int percentage) {
        this.damageModifier = percentage;
        return this;
    }

    public TexturedArmorItem setHasRender() {
        this.hasRender = true;
        return this;
    }

    @Override
    public int getMaxDamage(@Nonnull ItemStack stack) {
        return damageModifier > 0 ? super.getMaxDamage(stack) + (super.getMaxDamage(stack) * this.damageModifier / 100) : super.getMaxDamage(stack);
    }

    @Override
    public String getArmorTexture(@Nonnull ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return this.hasRender ? String.format("%s:textures/models/armor/%s.png", Atum.MOD_ID, armorPieceName) : String.format("%s:textures/models/armor/%s_%d%s.png", Atum.MOD_ID, armorPieceName, (slot == EquipmentSlot.LEGS ? 2 : 1), type == null ? "" : "_overlay");
    }
}