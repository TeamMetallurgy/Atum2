package com.teammetallurgy.atum.inventory.container.entity;

import com.teammetallurgy.atum.blocks.wood.CrateBlock;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import com.teammetallurgy.atum.init.AtumGuis;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SaddleItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CamelContainer extends AbstractContainerMenu {
    public final Container camelInventory;
    public final CamelEntity camel;
    private List<Slot> rightCrateSlots = new ArrayList<>();
    private List<Slot> leftCrateSlots = new ArrayList<>();

    public CamelContainer(int windowID, Inventory playerInventory, final int entityID) {
        super(AtumGuis.CAMEL, windowID);
        Player player = playerInventory.player;
        this.camel = (CamelEntity) player.level.getEntity(entityID);
        this.camelInventory = this.camel.getHorseChest();
        camelInventory.startOpen(player);
        //Saddle slot
        this.addSlot(new Slot(camelInventory, 0, 62, 64) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof SaddleItem && !this.hasItem();
            }
        });
        //Armor slot
        this.addSlot(new Slot(camelInventory, 1, 80, 64) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return camel.isArmor(stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public boolean isActive() {
                return camel.canWearArmor();
            }
        });
        //Carpet slot
        this.addSlot(new Slot(camelInventory, 2, 98, 64) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return camel.isValidCarpet(stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        //Left Crate slot
        this.addSlot(new Slot(camelInventory, 3, 35, 64) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return Block.byItem(stack.getItem()) instanceof CrateBlock;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                CamelContainer.this.updateLeftChestSlots();
            }

            @Override
            public boolean mayPickup(Player playerIn) {
                for (Slot slot : leftCrateSlots) {
                    if (slot.hasItem()) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        //Right Crate slot
        this.addSlot(new Slot(camelInventory, 4, 125, 64) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return Block.byItem(stack.getItem()) instanceof CrateBlock;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                CamelContainer.this.updateRightChestSlots();
            }

            @Override
            public boolean mayPickup(Player playerIn) {
                for (Slot slot : rightCrateSlots) {
                    if (slot.hasItem()) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        //Left Crate Inventory
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < camel.getInventoryColumns(); ++slot) {
                leftCrateSlots.add(new Slot(camelInventory, camel.getNonCrateSize() + slot + row * camel.getInventoryColumns(), 8 + slot * 18, 86 + row * 18));
                if (camel != null && camel.hasLeftCrate()) {
                    this.addSlot(leftCrateSlots.get(leftCrateSlots.size() - 1));
                }
            }
        }
        //Right Crate Inventory
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < camel.getInventoryColumns(); ++slot) {
                rightCrateSlots.add(new Slot(camelInventory, camel.getNonCrateSize() + 3 * camel.getInventoryColumns() + slot + row * camel.getInventoryColumns(), 98 + slot * 18, 86 + row * 18));
                if (camel != null && camel.hasRightCrate()) {
                    this.addSlot(rightCrateSlots.get(rightCrateSlots.size() - 1));
                }
            }
        }
        //Player Inventory
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < 9; ++slot) {
                this.addSlot(new Slot(playerInventory, slot + row * 9 + 9, 8 + slot * 18, 102 + row * 18 + 52));
            }
        }
        //Player Hotbar
        for (int slot = 0; slot < 9; ++slot) {
            this.addSlot(new Slot(playerInventory, slot, 8 + slot * 18, 212));
        }
    }

    private void updateLeftChestSlots() {
        if (camel.hasLeftCrate()) {
            boolean hasLeftSlots = false;
            for (Slot slot : leftCrateSlots) {
                if (slots.contains(slot)) {
                    hasLeftSlots = true;
                    break;
                }
            }
            if (!hasLeftSlots) {
                for (int i = 0; i < leftCrateSlots.size(); i++) {
                    slots.add(camel.getNonCrateSize() + i, leftCrateSlots.get(i));
                    lastSlots.add(ItemStack.EMPTY);
                }
            }
        } else {
            for (int i = 0; i < slots.size(); i++) {
                Slot slot = slots.get(i);
                if (leftCrateSlots.contains(slot)) {
                    slots.remove(i);
                    lastSlots.remove(i);
                    i--;
                }
            }
        }
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).index = i;
        }
    }

    private void updateRightChestSlots() {
        if (camel.hasRightCrate()) {
            boolean hasRightSlots = false;
            for (Slot slot : rightCrateSlots) {
                if (slots.contains(slot)) {
                    hasRightSlots = true;
                    break;
                }
            }
            if (!hasRightSlots) {
                for (Slot rightCrateSlot : rightCrateSlots) {
                    slots.add(camel.getNonCrateSize() + leftCrateSlots.size(), rightCrateSlot);
                    lastSlots.add(ItemStack.EMPTY);
                }
            }
        } else {
            for (int i = 0; i < slots.size(); i++) {
                Slot slot = slots.get(i);
                if (rightCrateSlots.contains(slot)) {
                    slots.remove(i);
                    lastSlots.remove(i);
                    i--;
                }
            }
        }
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).index = i;
        }
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return this.camelInventory.stillValid(player) && this.camel.isAlive() && this.camel.distanceTo(player) < 8.0F;
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            int camelInventorySize = camel.getNonCrateSize() + (camel.hasLeftCrate() ? leftCrateSlots.size() : 0) + (camel.hasRightCrate() ? rightCrateSlots.size() : 0);
            if (index < camelInventorySize) {
                if (!this.moveItemStackTo(slotStack, camelInventorySize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(slotStack) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(slotStack, 1, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(2).mayPlace(slotStack) && !this.getSlot(2).hasItem()) {
                if (!this.moveItemStackTo(slotStack, 2, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(3).mayPlace(slotStack) && !this.getSlot(3).hasItem()) {
                if (!this.moveItemStackTo(slotStack, 3, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(4).mayPlace(slotStack) && !this.getSlot(4).hasItem()) {
                if (!this.moveItemStackTo(slotStack, 4, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(slotStack)) {
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (camelInventorySize <= 5 || !this.moveItemStackTo(slotStack, 5, camelInventorySize, false)) {
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
    public void removed(Player player) {
        super.removed(player);
        this.camelInventory.stopOpen(player);
    }
}