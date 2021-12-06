package com.teammetallurgy.atum.inventory.container.entity;

import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import com.teammetallurgy.atum.init.AtumGuis;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SaddleItem;

import javax.annotation.Nonnull;

public class AlphaDesertWolfContainer extends AbstractContainerMenu {
    public final Container wolfInventory;
    public final DesertWolfEntity desertWolf;

    public AlphaDesertWolfContainer(int windowID, Inventory playerInventory, final int entityID) {
        super(AtumGuis.ALPHA_DESERT_WOLF, windowID);
        Player player = playerInventory.player;
        this.desertWolf = (DesertWolfEntity) player.level.getEntity(entityID);
        this.wolfInventory = this.desertWolf.getInventory();
        wolfInventory.startOpen(player);
        this.addSlot(new Slot(AlphaDesertWolfContainer.this.wolfInventory, 0, 8, 18) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return stack.getItem() instanceof SaddleItem && !this.hasItem();
            }

            @Override
            public boolean isActive() {
                return AlphaDesertWolfContainer.this.desertWolf.isAlpha();
            }
        });
        this.addSlot(new Slot(AlphaDesertWolfContainer.this.wolfInventory, 1, 8, 36) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return AlphaDesertWolfContainer.this.desertWolf.isArmor(stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < 9; ++slot) {
                this.addSlot(new Slot(playerInventory, slot + row * 9 + 9, 8 + slot * 18, 102 + row * 18 + -18));
            }
        }
        for (int slot = 0; slot < 9; ++slot) {
            this.addSlot(new Slot(playerInventory, slot, 8 + slot * 18, 142));
        }
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return this.wolfInventory.stillValid(player) && this.desertWolf.isAlive() && this.desertWolf.distanceTo(player) < 8.0F;
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index < this.wolfInventory.getContainerSize()) {
                if (!this.moveItemStackTo(slotStack, this.wolfInventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(slotStack) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (AlphaDesertWolfContainer.this.desertWolf.isAlpha() && this.getSlot(0).mayPlace(slotStack)) {
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.wolfInventory.getContainerSize() <= 2 || !this.moveItemStackTo(slotStack, 2, this.wolfInventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }

    @Override
    public void removed(@Nonnull Player player) {
        super.removed(player);
        this.wolfInventory.stopOpen(player);
    }
}