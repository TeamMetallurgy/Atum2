package com.teammetallurgy.atum.inventory.container.block;

import com.teammetallurgy.atum.blocks.machines.tileentity.KilnBaseTileEntity;
import com.teammetallurgy.atum.init.AtumGuis;
import com.teammetallurgy.atum.inventory.container.slot.FuelSlot;
import com.teammetallurgy.atum.inventory.container.slot.KilnOutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;

import javax.annotation.Nonnull;

public class KilnContainer extends AbstractFurnaceContainer {
    public final KilnBaseTileEntity kilnInventory;

    public KilnContainer(int windowID, PlayerInventory playerInventory, KilnBaseTileEntity kilnInventory) {
        super(AtumGuis.KILN, IRecipeType.BLASTING, windowID, playerInventory, kilnInventory, kilnInventory.furnaceData);
        PlayerEntity player = playerInventory.player;
        this.kilnInventory = kilnInventory;
        //Input Slots
        for (int row = 0; row < 2; ++row) {
            for (int slot = 0; slot < 2; ++slot) {
                this.addSlot(new Slot(kilnInventory, slot + row * 2, 71 + slot * 18, 15 + row * 18));
            }
        }
        //Fuel Slot
        this.addSlot(new FuelSlot(kilnInventory, 4, 36, 48));
        //Output Slots
        for (int row = 0; row < 2; ++row) {
            for (int slot = 0; slot < 2; ++slot) {
                this.addSlot(new KilnOutputSlot(player, kilnInventory, 5 + (slot + row * 2), 71 + slot * 18, 63 + row * 18));
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

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index) {
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
                if (!getRecipe(slotStack)) {
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

    protected boolean getRecipe(@Nonnull ItemStack stack) {
        return this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), this.world).isPresent();
    }
}