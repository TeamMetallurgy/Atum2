package com.teammetallurgy.atum.inventory.container.block;

import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import com.teammetallurgy.atum.init.AtumGuis;
import com.teammetallurgy.atum.inventory.container.slot.FuelSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ContainerData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class TrapContainer extends AbstractContainerMenu {
    private final TrapTileEntity trapInventory;
    private final ContainerData trapData;

    public TrapContainer(int windowID, Inventory playerInventory, TrapTileEntity trapInventory) {
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
        this.addDataSlots(trapInventory.trapData);
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();

            if (index < this.trapInventory.getContainerSize()) {
                if (!this.moveItemStackTo(slotStack, this.trapInventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, this.trapInventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return this.trapInventory.stillValid(player);
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