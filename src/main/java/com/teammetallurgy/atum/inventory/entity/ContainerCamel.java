package com.teammetallurgy.atum.inventory.entity;

import com.teammetallurgy.atum.entity.animal.EntityCamel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ContainerCamel extends Container {
    private final IInventory camelInventory;
    private final EntityCamel camel;

    public ContainerCamel(IInventory playerInventory, IInventory camelInventory, final EntityCamel camel, EntityPlayer player) {
        this.camelInventory = camelInventory;
        this.camel = camel;
        camelInventory.openInventory(player);
        this.addSlotToContainer(new Slot(camelInventory, 0, 8, 18) {
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
        this.addSlotToContainer(new Slot(camelInventory, 1, 8, 36) {
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
        this.addSlotToContainer(new Slot(camelInventory, 2, 8, 54) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return camel.isValidCarpet(stack);
            }
            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        if (camel != null && camel.hasCrate()) {
            for (int row = 0; row < 3; ++row) {
                for (int slot = 0; slot < camel.getInventoryColumns(); ++slot) {
                    this.addSlotToContainer(new Slot(camelInventory, 3 + slot + row * camel.getInventoryColumns(), 80 + slot * 18, 18 + row * 18));
                }
            }
        }
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < 9; ++slot) {
                this.addSlotToContainer(new Slot(playerInventory, slot + row * 9 + 9, 8 + slot * 18, 102 + row * 18 + -18));
            }
        }
        for (int slot = 0; slot < 9; ++slot) {
            this.addSlotToContainer(new Slot(playerInventory, slot, 8 + slot * 18, 142));
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
                if (!this.mergeItemStack(slotStack, 1, 3, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(2).isItemValid(slotStack) && !this.getSlot(2).getHasStack()) {
                if (!this.mergeItemStack(slotStack, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).isItemValid(slotStack)) {
                if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.camelInventory.getSizeInventory() <= 3 || !this.mergeItemStack(slotStack, 3, this.camelInventory.getSizeInventory(), false)) {
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