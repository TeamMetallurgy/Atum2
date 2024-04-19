package com.teammetallurgy.atum.inventory.container.block;

import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.blocks.machines.tileentity.GodforgeTileEntity;
import com.teammetallurgy.atum.init.AtumMenuType;
import com.teammetallurgy.atum.inventory.container.slot.GodforgeFuelSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class GodforgeContainer extends AbstractContainerMenu {
    private final GodforgeTileEntity godforgeInventory;
    private ContainerData godforgeData;
    public Slot fuelSlot;

    public GodforgeContainer(int id, Inventory playerInventory, GodforgeTileEntity godforgeInventory) {
        super(AtumMenuType.GODFORGE.get(), id);
        this.godforgeInventory = godforgeInventory;
        this.godforgeData = godforgeInventory.godforgeData;
        this.addSlot(new Slot(godforgeInventory, 0, 56, 17) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return stack.getItem() instanceof IArtifact;
            }
        });
        this.fuelSlot = this.addSlot(new GodforgeFuelSlot(godforgeInventory, 1, 56, 53));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, godforgeInventory, 2, 116, 35));
        checkContainerSize(godforgeInventory, 3);
        checkContainerDataCount(godforgeInventory.godforgeData, 3);

        for (int rows = 0; rows < 3; ++rows) {
            for (int slots = 0; slots < 9; ++slots) {
                this.addSlot(new Slot(playerInventory, slots + rows * 9 + 9, 8 + slots * 18, rows * 18 + 84));
            }
        }

        for (int slot = 0; slot < 9; ++slot) {
            this.addSlot(new Slot(playerInventory, slot, 8 + slot * 18, 142));
        }
        this.addDataSlots(this.godforgeData);
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack transferStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            transferStack = slotStack.copy();
            if (index == 2) {
                if (!this.moveItemStackTo(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, transferStack);
            } else if (index != 1 && index != 0) {
                if (slotStack.getItem() instanceof IArtifact) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (GodforgeTileEntity.isFuel(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.moveItemStackTo(slotStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.moveItemStackTo(slotStack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == transferStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotStack);
        }
        return transferStack;
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return this.godforgeInventory.stillValid(player);
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