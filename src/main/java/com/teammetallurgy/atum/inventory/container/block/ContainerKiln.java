package com.teammetallurgy.atum.inventory.container.block;

import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.init.AtumGuis;
import com.teammetallurgy.atum.inventory.container.slot.FuelSlot;
import com.teammetallurgy.atum.inventory.container.slot.KilnOutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ContainerKiln extends Container {
    private final KilnTileEntity kiln;
    private int cookTime;
    private int totalCookTime;
    private int burnTime;
    private int currentItemBurnTime;

    public ContainerKiln(int windowID, BlockPos pos, PlayerInventory playerInventory) {
        super(AtumGuis.KILN, windowID);
        this.kiln = (KilnTileEntity) playerInventory.player.world.getTileEntity(pos);
        if (this.kiln != null) {
            //Input Slots
            for (int row = 0; row < 2; ++row) {
                for (int slot = 0; slot < 2; ++slot) {
                    this.addSlot(new Slot(kiln, slot + row * 2, 71 + slot * 18, 15 + row * 18));
                }
            }
            //Fuel Slot
            this.addSlot(new FuelSlot(kiln, 4, 36, 48));
            //Output Slots
            for (int row = 0; row < 2; ++row) {
                for (int slot = 0; slot < 2; ++slot) {
                    this.addSlot(new KilnOutputSlot(playerInventory.player, kiln, 5 + (slot + row * 2), 71 + slot * 18, 63 + row * 18));
                }
            }
            //Player Inventory
            for (int row = 0; row < 3; ++row) {
                for (int slot = 0; slot < 9; ++slot) {
                    this.addSlot(new Slot(playerInventory, slot + row * 9 + 9, 8 + slot * 18, 84 + row * 18 + 26));
                }
            }
            //Player Hotbar
            for (int slot = 0; slot < 9; ++slot) {
                this.addSlot(new Slot(playerInventory, slot, 8 + slot * 18, 142 + 26));
            }
        }
    }

    @Override
    public void addListener(@Nonnull IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.kiln);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener containerListener : this.listeners) {
            if (this.cookTime != this.kiln.getField(2)) {
                containerListener.sendWindowProperty(this, 2, this.kiln.getField(2));
            }
            if (this.burnTime != this.kiln.getField(0)) {
                containerListener.sendWindowProperty(this, 0, this.kiln.getField(0));
            }
            if (this.currentItemBurnTime != this.kiln.getField(1)) {
                containerListener.sendWindowProperty(this, 1, this.kiln.getField(1));
            }
            if (this.totalCookTime != this.kiln.getField(3)) {
                containerListener.sendWindowProperty(this, 3, this.kiln.getField(3));
            }
        }
        this.cookTime = this.kiln.getField(2);
        this.burnTime = this.kiln.getField(0);
        this.currentItemBurnTime = this.kiln.getField(1);
        this.totalCookTime = this.kiln.getField(3);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.kiln.setField(id, data);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return this.kiln.isUsableByPlayer(player);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity player, int index) { //TODO
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        int slotFuel = 4;
        int internalStart = 0;
        int internalEnd = 3;
        int outputStart = 5;
        int outputEnd = 8;
        int playerStart = 9;
        int playerEnd = 35;
        int hotbarStart = 36;
        int hotbarEnd = 44;

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index == slotFuel) {
                if (!this.mergeItemStack(slotStack, playerStart, hotbarEnd + 1, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if ((index < internalStart || index > internalEnd) && (index < outputStart || index > outputEnd)) {
                if (!FurnaceRecipes.instance().getSmeltingResult(slotStack).isEmpty()) { //TODO
                    if (!this.mergeItemStack(slotStack, internalStart, internalEnd + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (AbstractFurnaceTileEntity.isFuel(slotStack)) {
                    if (!this.mergeItemStack(slotStack, slotFuel, slotFuel + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= playerStart && index <= playerEnd) {
                    if (!this.mergeItemStack(slotStack, hotbarStart, hotbarEnd + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= hotbarStart && index <= hotbarEnd && !this.mergeItemStack(slotStack, playerStart, playerEnd + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, playerStart, hotbarEnd + 1, false)) {
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