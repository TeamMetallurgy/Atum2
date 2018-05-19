package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ItemTexturedArmor extends ItemArmor {
    private String textureFile;
    private ItemStack repairItem = ItemStack.EMPTY;

    public ItemTexturedArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot) {
        super(material, renderIndex, slot);
    }

    public ItemTexturedArmor setRepairItem(Item item) {
        this.repairItem = new ItemStack(item);
        return this;
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair == this.repairItem;
    }

    public ItemTexturedArmor setTextureFile(String filename) {
        this.textureFile = filename;
        return this;
    }

    @Override
    public String getArmorTexture(@Nonnull ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return String.valueOf(new ResourceLocation(Constants.MOD_ID, "textures/armor/" + this.textureFile + ".png"));
    }
}