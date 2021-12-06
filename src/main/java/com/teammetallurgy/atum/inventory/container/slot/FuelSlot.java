package com.teammetallurgy.atum.inventory.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import javax.annotation.Nonnull;

public class FuelSlot extends Slot {

    public FuelSlot(Container inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return AbstractFurnaceBlockEntity.isFuel(stack) || isBucket(stack);
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isBucket(@Nonnull ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }
}