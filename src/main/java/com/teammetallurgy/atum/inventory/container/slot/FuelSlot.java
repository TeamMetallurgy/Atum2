package com.teammetallurgy.atum.inventory.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;

import javax.annotation.Nonnull;

public class FuelSlot extends Slot {

    public FuelSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return AbstractFurnaceTileEntity.isFuel(stack) || isBucket(stack);
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }

    public static boolean isBucket(@Nonnull ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }
}