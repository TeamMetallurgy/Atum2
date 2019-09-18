package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public class TexturedArmorItem extends DyeableArmorItem {
    private final String armorPieceName;
    private boolean isDyeable;
    private int damageModifier;

    public TexturedArmorItem(IArmorMaterial material, String name, EquipmentSlotType slot) {
        super(material, slot, new Item.Properties().group(Atum.GROUP));
        this.armorPieceName = name;
    }

    public TexturedArmorItem setDyeable() {
        this.isDyeable = true;
        return this;
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
        String armorModel = Constants.MOD_ID + ":" + "textures/armor/";
        return type == null ? armorModel + armorPieceName + ".png" : armorModel + armorPieceName + "_overlay" + ".png";
    }

    public boolean isDyeable() {
        return this.isDyeable;
    }

    @Override
    public boolean hasColor(@Nonnull ItemStack stack) {
        return this.isDyeable() && super.hasColor(stack);
    }

    @Override
    public int getColor(@Nonnull ItemStack stack) {
        if (this.isDyeable()) {
            CompoundNBT tag = stack.getChildTag("display");
            return tag != null && tag.contains("color", 99) ? tag.getInt("color") : -1;
        }
        return -1;
    }

    @Override
    public void removeColor(@Nonnull ItemStack stack) {
        if (this.isDyeable()) {
            super.removeColor(stack);
        }
    }

    @Override
    public void setColor(@Nonnull ItemStack stack, int color) {
        if (this.isDyeable()) {
            super.setColor(stack, color);
        }
    }
}