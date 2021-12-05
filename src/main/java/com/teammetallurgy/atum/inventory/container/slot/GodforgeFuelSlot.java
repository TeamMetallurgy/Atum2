package com.teammetallurgy.atum.inventory.container.slot;

import com.teammetallurgy.atum.blocks.machines.tileentity.GodforgeTileEntity;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class GodforgeFuelSlot extends FuelSlot {

    public GodforgeFuelSlot(Container inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return GodforgeTileEntity.isFuel(stack) || isBucket(stack);
    }
}