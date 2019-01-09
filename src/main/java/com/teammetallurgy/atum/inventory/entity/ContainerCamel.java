package com.teammetallurgy.atum.inventory.entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.entity.animal.EntityCamel;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ContainerCamel extends Container {
    private final IInventory camelInventory;
    private final EntityCamel camel;
    
    private List<Slot> rightCrateSlots = new ArrayList<>();
    private List<Slot> leftCrateSlots = new ArrayList<>();

    public ContainerCamel(IInventory playerInventory, IInventory camelInventory, final EntityCamel camel, EntityPlayer player) {
        this.camelInventory = camelInventory;
        this.camel = camel;
        camelInventory.openInventory(player);
        //Saddle slot
        this.addSlotToContainer(new Slot(camelInventory, 0, 62, 64) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Items.SADDLE && !this.getHasStack() && camel.canBeSaddled();
            }

            @Override
            @SideOnly(Side.CLIENT)
            public boolean isEnabled() {
                return camel.canBeSaddled();
            }
        });
        //Armor slot
        this.addSlotToContainer(new Slot(camelInventory, 1, 80, 64) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return camel.isArmor(stack);
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }

            @Override
            @SideOnly(Side.CLIENT)
            public boolean isEnabled() {
                return camel.wearsArmor();
            }
        });
        //Carpet slot
        this.addSlotToContainer(new Slot(camelInventory, 2, 98, 64) {
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
        this.addSlotToContainer(new Slot(camelInventory, 3, 35, 64) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return Block.getBlockFromItem(stack.getItem()) instanceof BlockCrate;
            }

            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                ContainerCamel.this.updateLeftChestSlots();
            }

            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
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
        this.addSlotToContainer(new Slot(camelInventory, 4, 125, 64) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return Block.getBlockFromItem(stack.getItem()) instanceof BlockCrate;
            }

            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                ContainerCamel.this.updateRightChestSlots();
            }

            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
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
                    this.addSlotToContainer(leftCrateSlots.get(leftCrateSlots.size() - 1));
                }
            }
        }
        //Right Crate Inventory
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < camel.getInventoryColumns(); ++slot) {
                rightCrateSlots.add(new Slot(camelInventory, camel.getNonCrateSize() + 3 * camel.getInventoryColumns() + slot + row * camel.getInventoryColumns(), 98 + slot * 18, 86 + row * 18));
                if (camel != null && camel.hasRightCrate()) {
                    this.addSlotToContainer(rightCrateSlots.get(rightCrateSlots.size() - 1));
                }
            }
        }
        //Player Inventory
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < 9; ++slot) {
                this.addSlotToContainer(new Slot(playerInventory, slot + row * 9 + 9, 8 + slot * 18, 102 + row * 18 + 52));
            }
        }
        //Player Hotbar
        for (int slot = 0; slot < 9; ++slot) {
            this.addSlotToContainer(new Slot(playerInventory, slot, 8 + slot * 18, 212));
        }
    }

    private void updateLeftChestSlots() {
        if (camel.hasLeftCrate())
        {
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
        if (camel.hasRightCrate())
        {
            boolean hasRightSlots = false;
            for (Slot slot : rightCrateSlots) {
                if (inventorySlots.contains(slot)) {
                    hasRightSlots = true;
                    break;
                }
            }
            if (!hasRightSlots) {
                for (int i = 0; i < rightCrateSlots.size(); i++) {
                    inventorySlots.add(camel.getNonCrateSize() + leftCrateSlots.size(), rightCrateSlots.get(i));
                    inventoryItemStacks.add(ItemStack.EMPTY);
                }
            }
        } else {
            for (int i = 0; i < inventorySlots.size(); i++) {
                Slot slot = inventorySlots.get(i);
                if(rightCrateSlots.contains(slot)) {    
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
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return this.camelInventory.isUsableByPlayer(player) && this.camel.isEntityAlive() && this.camel.getDistance(player) < 8.0F;
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index < this.camelInventory.getSizeInventory()) {
                if (!this.mergeItemStack(slotStack, this.camelInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
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
            } else if (this.camelInventory.getSizeInventory() <= 5 || !this.mergeItemStack(slotStack, 5, this.camelInventory.getSizeInventory(), false)) {
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
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.camelInventory.closeInventory(player);
    }
}