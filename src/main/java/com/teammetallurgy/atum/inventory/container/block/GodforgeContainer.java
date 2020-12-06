package com.teammetallurgy.atum.inventory.container.block;

import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.blocks.machines.tileentity.GodforgeTileEntity;
import com.teammetallurgy.atum.init.AtumGuis;
import com.teammetallurgy.atum.inventory.container.slot.GodforgeFuelSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class GodforgeContainer extends Container {
    private final GodforgeTileEntity godforgeInventory;
    private IIntArray godforgeData;
    public Slot fuelSlot;

    public GodforgeContainer(int id, PlayerInventory playerInventory, GodforgeTileEntity godforgeInventory) {
        super(AtumGuis.GODFORGE, id);
        this.godforgeInventory = godforgeInventory;
        this.godforgeData = godforgeInventory.godforgeData;
        this.addSlot(new Slot(godforgeInventory, 0, 56, 17));
        this.fuelSlot = this.addSlot(new GodforgeFuelSlot(godforgeInventory, 1, 56, 53));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, godforgeInventory, 2, 116, 35));
        assertInventorySize(godforgeInventory, 3);
        assertIntArraySize(godforgeInventory.godforgeData, 3);

        for (int rows = 0; rows < 3; ++rows) {
            for (int slots = 0; slots < 9; ++slots) {
                this.addSlot(new Slot(playerInventory, slots + rows * 9 + 9, 8 + slots * 18, rows * 18 + 84));
            }
        }

        for (int slot = 0; slot < 9; ++slot) {
            this.addSlot(new Slot(playerInventory, slot, 8 + slot * 18, 142));
        }
        this.trackIntArray(this.godforgeData);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index) {
        ItemStack transferStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            transferStack = slotStack.copy();
            if (index == 2) {
                if (!this.mergeItemStack(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, transferStack);
            } else if (index != 1 && index != 0) {
                if (slotStack.getItem() instanceof IArtifact) {
                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (GodforgeTileEntity.isFuel(slotStack)) {
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

            if (slotStack.getCount() == transferStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotStack);
        }
        return transferStack;
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return this.godforgeInventory.isUsableByPlayer(player);
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.godforgeData.get(1);
        int j = this.godforgeData.get(2);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int currentItemBurnTime = this.godforgeData.get(2);
        if (currentItemBurnTime == 0) {
            currentItemBurnTime = 200;
        }
        return this.godforgeData.get(0) * 13 / currentItemBurnTime;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.godforgeData.get(0) > 0;
    }
}