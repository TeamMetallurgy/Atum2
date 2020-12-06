package com.teammetallurgy.atum.inventory.container.slot;

import com.teammetallurgy.atum.blocks.machines.tileentity.GodforgeTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class GodforgeFuelSlot extends FuelSlot {

    public GodforgeFuelSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return GodforgeTileEntity.isFuel(stack) || isBucket(stack);
    }
}