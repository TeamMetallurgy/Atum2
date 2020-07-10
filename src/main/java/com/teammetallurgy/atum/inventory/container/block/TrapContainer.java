package com.teammetallurgy.atum.inventory.container.block;

import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import com.teammetallurgy.atum.init.AtumGuis;
import com.teammetallurgy.atum.inventory.container.slot.FuelSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class TrapContainer extends Container {
    private final TrapTileEntity trapInventory;
    private final IIntArray trapData;

    public TrapContainer(int windowID, PlayerInventory playerInventory, TrapTileEntity trapInventory) {
        super(AtumGuis.TRAP, windowID);
        this.trapInventory = trapInventory;
        this.trapData = trapInventory.trapData;
        this.addSlot(new FuelSlot(this.trapInventory, 0, 80, 20));

        for (int rows = 0; rows < 3; ++rows) {
            for (int slots = 0; slots < 9; ++slots) {
                this.addSlot(new Slot(playerInventory, slots + rows * 9 + 9, 8 + slots * 18, rows * 18 + 51));
            }
        }

        for (int slot = 0; slot < 9; ++slot) {
            this.addSlot(new Slot(playerInventory, slot, 8 + slot * 18, 109));
        }
        this.trackIntArray(trapInventory.trapData);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index) {
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

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return this.trapInventory.isUsableByPlayer(player);
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int currentItemBurnTime = this.trapData.get(1);
        if (currentItemBurnTime == 0) {
            currentItemBurnTime = 200;
        }
        return this.trapData.get(0) * 13 / currentItemBurnTime;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.trapData.get(0) > 0;
    }
}