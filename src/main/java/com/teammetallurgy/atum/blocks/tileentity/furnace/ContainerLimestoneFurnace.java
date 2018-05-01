package com.teammetallurgy.atum.blocks.tileentity.furnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ContainerLimestoneFurnace extends Container {
    private final IInventory tileLimestoneFurnace;
    private int cookTime;
    private int totalCookTime;
    private int furnaceBurnTime;
    private int currentItemBurnTime;

    public ContainerLimestoneFurnace(InventoryPlayer playerInventory, IInventory furnaceInventory) {
        this.tileLimestoneFurnace = furnaceInventory;
        this.addSlotToContainer(new Slot(furnaceInventory, 0, 56, 17));
        this.addSlotToContainer(new SlotFurnaceFuel(furnaceInventory, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, furnaceInventory, 2, 116, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileLimestoneFurnace);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener containerListener : this.listeners) {
            if (this.cookTime != this.tileLimestoneFurnace.getField(2)) {
                containerListener.sendWindowProperty(this, 2, this.tileLimestoneFurnace.getField(2));
            }
            if (this.furnaceBurnTime != this.tileLimestoneFurnace.getField(0)) {
                containerListener.sendWindowProperty(this, 0, this.tileLimestoneFurnace.getField(0));
            }
            if (this.currentItemBurnTime != this.tileLimestoneFurnace.getField(1)) {
                containerListener.sendWindowProperty(this, 1, this.tileLimestoneFurnace.getField(1));
            }
            if (this.totalCookTime != this.tileLimestoneFurnace.getField(3)) {
                containerListener.sendWindowProperty(this, 3, this.tileLimestoneFurnace.getField(3));
            }
        }

        this.cookTime = this.tileLimestoneFurnace.getField(2);
        this.furnaceBurnTime = this.tileLimestoneFurnace.getField(0);
        this.currentItemBurnTime = this.tileLimestoneFurnace.getField(1);
        this.totalCookTime = this.tileLimestoneFurnace.getField(3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.tileLimestoneFurnace.setField(id, data);
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return this.tileLimestoneFurnace.isUsableByPlayer(player);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            if (index == 2) {
                if (!this.mergeItemStack(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if (index != 1 && index != 0) {
                if (!FurnaceRecipes.instance().getSmeltingResult(slotStack).isEmpty()) {
                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (TileEntityFurnace.isItemFuel(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(slotStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(slotStack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, slotStack);
        }
        return stack;
    }
}