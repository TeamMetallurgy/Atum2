package com.teammetallurgy.atum.inventory.container.entity;

import com.teammetallurgy.atum.blocks.wood.CrateBlock;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import com.teammetallurgy.atum.init.AtumGuis;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SaddleItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CamelContainer extends Container {
    public final IInventory camelInventory;
    public final CamelEntity camel;
    private List<Slot> rightCrateSlots = new ArrayList<>();
    private List<Slot> leftCrateSlots = new ArrayList<>();

    public CamelContainer(int windowID, PlayerInventory playerInventory, final int entityID) {
        super(AtumGuis.CAMEL, windowID);
        PlayerEntity player = playerInventory.player;
        this.camel = (CamelEntity) player.world.getEntityByID(entityID);
        this.camelInventory = this.camel.getHorseChest();
        camelInventory.openInventory(player);
        //Saddle slot
        this.addSlot(new Slot(camelInventory, 0, 62, 64) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() instanceof SaddleItem && !this.getHasStack() && camel.canBeSaddled();
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public boolean isEnabled() {
                return camel.canBeSaddled();
            }
        });
        //Armor slot
        this.addSlot(new Slot(camelInventory, 1, 80, 64) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return camel.isArmor(stack);
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public boolean isEnabled() {
                return camel.func_230276_fq_();
            }
        });
        //Carpet slot
        this.addSlot(new Slot(camelInventory, 2, 98, 64) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return camel.isValidCarpet(stack);
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        //Left Crate slot
        this.addSlot(new Slot(camelInventory, 3, 35, 64) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return Block.getBlockFromItem(stack.getItem()) instanceof CrateBlock;
            }

            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                CamelContainer.this.updateLeftChestSlots();
            }

            @Override
            public boolean canTakeStack(PlayerEntity playerIn) {
                for (Slot slot : leftCrateSlots) {
                    if (slot.getHasStack()) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        //Right Crate slot
        this.addSlot(new Slot(camelInventory, 4, 125, 64) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return Block.getBlockFromItem(stack.getItem()) instanceof CrateBlock;
            }

            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                CamelContainer.this.updateRightChestSlots();
            }

            @Override
            public boolean canTakeStack(PlayerEntity playerIn) {
                for (Slot slot : rightCrateSlots) {
                    if (slot.getHasStack()) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public int getSlotStackLimit() {
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
                if (inventorySlots.contains(slot)) {
                    hasLeftSlots = true;
                    break;
                }
            }
            if (!hasLeftSlots) {
                for (int i = 0; i < leftCrateSlots.size(); i++) {
                    inventorySlots.add(camel.getNonCrateSize() + i, leftCrateSlots.get(i));
                    inventoryItemStacks.add(ItemStack.EMPTY);
                }
            }
        } else {
            for (int i = 0; i < inventorySlots.size(); i++) {
                Slot slot = inventorySlots.get(i);
                if (leftCrateSlots.contains(slot)) {
                    inventorySlots.remove(i);
                    inventoryItemStacks.remove(i);
                    i--;
                }
            }
        }
        for (int i = 0; i < inventorySlots.size(); i++) {
            inventorySlots.get(i).slotNumber = i;
        }
    }

    private void updateRightChestSlots() {
        if (camel.hasRightCrate()) {
            boolean hasRightSlots = false;
            for (Slot slot : rightCrateSlots) {
                if (inventorySlots.contains(slot)) {
                    hasRightSlots = true;
                    break;
                }
            }
            if (!hasRightSlots) {
                for (Slot rightCrateSlot : rightCrateSlots) {
                    inventorySlots.add(camel.getNonCrateSize() + leftCrateSlots.size(), rightCrateSlot);
                    inventoryItemStacks.add(ItemStack.EMPTY);
                }
            }
        } else {
            for (int i = 0; i < inventorySlots.size(); i++) {
                Slot slot = inventorySlots.get(i);
                if (rightCrateSlots.contains(slot)) {
                    inventorySlots.remove(i);
                    inventoryItemStacks.remove(i);
                    i--;
                }
            }
        }
        for (int i = 0; i < inventorySlots.size(); i++) {
            inventorySlots.get(i).slotNumber = i;
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return this.camelInventory.isUsableByPlayer(player) && this.camel.isAlive() && this.camel.getDistance(player) < 8.0F;
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            int camelInventorySize = camel.getNonCrateSize() + (camel.hasLeftCrate() ? leftCrateSlots.size() : 0) + (camel.hasRightCrate() ? rightCrateSlots.size() : 0);
            if (index < camelInventorySize) {
                if (!this.mergeItemStack(slotStack, camelInventorySize, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).isItemValid(slotStack) && !this.getSlot(1).getHasStack()) {
                if (!this.mergeItemStack(slotStack, 1, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(2).isItemValid(slotStack) && !this.getSlot(2).getHasStack()) {
                if (!this.mergeItemStack(slotStack, 2, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(3).isItemValid(slotStack) && !this.getSlot(3).getHasStack()) {
                if (!this.mergeItemStack(slotStack, 3, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(4).isItemValid(slotStack) && !this.getSlot(4).getHasStack()) {
                if (!this.mergeItemStack(slotStack, 4, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).isItemValid(slotStack)) {
                if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (camelInventorySize <= 5 || !this.mergeItemStack(slotStack, 5, camelInventorySize, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        this.camelInventory.closeInventory(player);
    }
}