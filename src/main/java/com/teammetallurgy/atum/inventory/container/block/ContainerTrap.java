package com.teammetallurgy.atum.inventory.container.block;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ContainerTrap extends Container {
    private final IInventory trapInventory;
    private int furnaceBurnTime;
    private int currentItemBurnTime;

    public ContainerTrap(PlayerInventory playerInventory, IInventory trapInventory) {
        this.trapInventory = trapInventory;
        this.addSlot(new SlotFurnaceFuel(trapInventory, 0, 80, 20));

        for (int rows = 0; rows < 3; ++rows) {
            for (int slots = 0; slots < 9; ++slots) {
                this.addSlot(new Slot(playerInventory, slots + rows * 9 + 9, 8 + slots * 18, rows * 18 + 51));
            }
        }

        for (int slot = 0; slot < 9; ++slot) {
            this.addSlot(new Slot(playerInventory, slot, 8 + slot * 18, 109));
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.trapInventory);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener containerListener : this.listeners) {
            if (this.furnaceBurnTime != this.trapInventory.getField(0)) {
                containerListener.sendWindowProperty(this, 0, this.trapInventory.getField(0));
            }
            if (this.currentItemBurnTime != this.trapInventory.getField(1)) {
                containerListener.sendWindowProperty(this, 1, this.trapInventory.getField(1));
            }
        }
        this.furnaceBurnTime = this.trapInventory.getField(0);
        this.currentItemBurnTime = this.trapInventory.getField(1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.trapInventory.setField(id, data);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return trapInventory.isUsableByPlayer(player);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            if (index < this.trapInventory.getSizeInventory()) {
                if (!this.mergeItemStack(slotStack, this.trapInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 0, this.trapInventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }
}