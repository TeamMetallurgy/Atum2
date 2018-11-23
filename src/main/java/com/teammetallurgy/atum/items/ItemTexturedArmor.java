package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class ItemTexturedArmor extends ItemArmor {
    private ItemStack repairItem = ItemStack.EMPTY;
    private final String armorPieceName;
    protected boolean isDyeable;

    public ItemTexturedArmor(ArmorMaterial material, String name, EntityEquipmentSlot slot) {
        super(material, 0, slot);
        this.armorPieceName = name;
    }

    public ItemTexturedArmor setRepairItem(Item item) {
        this.repairItem = new ItemStack(item);
        return this;
    }

    public ItemTexturedArmor setDyeable() {
        this.isDyeable = true;
        return this;
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair == this.repairItem;
    }

    @Override
    public String getArmorTexture(@Nonnull ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        String armorModel = Constants.MOD_ID + ":" + "textures/armor/";
        return type == null ? armorModel + armorPieceName + ".png" : armorModel + armorPieceName + "_overlay" + ".png";
    }

    @Override
    public boolean hasOverlay(@ Nonnull ItemStack stack) {
        return this.isDyeable();
    }

    public boolean isDyeable() {
        return this.isDyeable;
    }

    @Override
    public boolean hasColor(@Nonnull ItemStack stack) {
        return this.isDyeable() && (stack.getTagCompound() != null && stack.hasTagCompound() && (stack.getTagCompound().hasKey("display", 10) && stack.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
    }

    @Override
    public int getColor(@Nonnull ItemStack stack) {
        if (this.isDyeable()) {
            NBTTagCompound tag = stack.getTagCompound();

            if (tag != null) {
                NBTTagCompound displayTag = tag.getCompoundTag("display");

                if (displayTag.hasKey("color", 3)) {
                    return displayTag.getInteger("color");
                }
            }
            return -1;
        } else {
            return super.getColor(stack);
        }
    }

    @Override
    public void removeColor(@Nonnull ItemStack stack) {
        if (this.isDyeable()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                NBTTagCompound displayTag = tag.getCompoundTag("display");

                if (displayTag.hasKey("color")) {
                    displayTag.removeTag("color");
                }
            }
        }
    }

    @Override
    public void setColor(@Nonnull ItemStack stack, int color) {
        if (this.isDyeable()) {
            NBTTagCompound tag = stack.getTagCompound();

            if (tag == null) {
                tag = new NBTTagCompound();
                stack.setTagCompound(tag);
            }
            NBTTagCompound displayTag = tag.getCompoundTag("display");

            if (!tag.hasKey("display", 10)) {
                tag.setTag("display", displayTag);
            }
            displayTag.setInteger("color", color);
        }
    }
}