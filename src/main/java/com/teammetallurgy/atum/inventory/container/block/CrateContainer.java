package com.teammetallurgy.atum.inventory.container.block;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;

import javax.annotation.Nonnull;

public class CrateContainer extends AbstractContainerMenu {
    private final Container crateInventory;
    private final int numRows;

    public CrateContainer(int windowID, BlockPos pos, Inventory playerInventory) {
        super(MenuType.GENERIC_9x3, windowID);
        Player player = playerInventory.player;
        this.crateInventory = (Container) player.level.getBlockEntity(pos);
        this.numRows = this.crateInventory.getContainerSize() / 9;
        crateInventory.startOpen(player);
        int i = (this.numRows - 4) * 18;

        for (int j = 0; j < this.numRows; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(this.crateInventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return crateInventory.stillValid(player);
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack slotStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            slotStack = stack.copy();

            if (index < this.numRows * 9) {
                if (!this.moveItemStackTo(stack, this.numRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, this.numRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return slotStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.crateInventory.stopOpen(player);
    }

    public Container getCrateInventory() {
        return crateInventory;
    }
}