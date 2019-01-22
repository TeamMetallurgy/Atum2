package com.teammetallurgy.atum.inventory.container.block;

import com.teammetallurgy.atum.inventory.slot.SlotKilnOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ContainerKiln extends Container {
    private final IInventory kiln;
    private int cookTime;
    private int totalCookTime;
    private int burnTime;
    private int currentItemBurnTime;

    public ContainerKiln(InventoryPlayer playerInventory, IInventory kiln) {
        this.kiln = kiln;
        //Input Slots
        for (int row = 0; row < 2; ++row) {
            for (int slot = 0; slot < 2; ++slot) {
                this.addSlotToContainer(new Slot(kiln, slot + row * 2, 71 + slot * 18, 15 + row * 18));
            }
        }
        //Fuel Slot
        this.addSlotToContainer(new SlotFurnaceFuel(kiln, 4, 36, 48));
        //Output Slots
        for (int row = 0; row < 2; ++row) {
            for (int slot = 0; slot < 2; ++slot) {
                this.addSlotToContainer(new SlotKilnOutput(playerInventory.player, kiln, 5 + (slot + row * 2), 71 + slot * 18, 63 + row * 18));
            }
        }
        //Player Inventory
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < 9; ++slot) {
                this.addSlotToContainer(new Slot(playerInventory, slot + row * 9 + 9, 8 + slot * 18, 84 + row * 18 + 26));
            }
        }
        //Player Hotbar
        for (int slot = 0; slot < 9; ++slot) {
            this.addSlotToContainer(new Slot(playerInventory, slot, 8 + slot * 18, 142 + 26));
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.kiln);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener icontainerlistener : this.listeners) {
            if (this.cookTime != this.kiln.getField(2)) {
                icontainerlistener.sendWindowProperty(this, 2, this.kiln.getField(2));
            }
            if (this.burnTime != this.kiln.getField(0)) {
                icontainerlistener.sendWindowProperty(this, 0, this.kiln.getField(0));
            }
            if (this.currentItemBurnTime != this.kiln.getField(1)) {
                icontainerlistener.sendWindowProperty(this, 1, this.kiln.getField(1));
            }
            if (this.totalCookTime != this.kiln.getField(3)) {
                icontainerlistener.sendWindowProperty(this, 3, this.kiln.getField(3));
            }
        }
        this.cookTime = this.kiln.getField(2);
        this.burnTime = this.kiln.getField(0);
        this.currentItemBurnTime = this.kiln.getField(1);
        this.totalCookTime = this.kiln.getField(3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.kiln.setField(id, data);
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return this.kiln.isUsableByPlayer(player);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer player, int index) { //TODO
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
                if (!FurnaceRecipes.instance().getSmeltingResult(slotStack).isEmpty()) { //TODO
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
            slot.onTake(player, slotStack);
        }
        return stack;
    }
}