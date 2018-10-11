package com.teammetallurgy.atum.blocks.base.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public abstract class TileEntityInventoryBase extends TileEntityLockableLoot implements IInventory {
    protected NonNullList<ItemStack> inventory;

    public TileEntityInventoryBase(int slots) {
        this.inventory = NonNullList.withSize(slots, ItemStack.EMPTY);
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    @Nonnull
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    @Nonnull
    public String getName() {
        return this.hasCustomName() ? this.customName : this.getBlockType().getTranslationKey() + ".name";
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.getItems());
        }

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.getItems());
        }

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        return compound;
    }
}