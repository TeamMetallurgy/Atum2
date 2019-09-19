package com.teammetallurgy.atum.blocks.base.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class TileEntityInventoryBase extends LockableLootTileEntity {
    protected NonNullList<ItemStack> inventory;

    public TileEntityInventoryBase(int slots) {
        this.inventory = NonNullList.withSize(slots, ItemStack.EMPTY);
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull BlockState oldState, @Nonnull BlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    @Nonnull
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    @Nonnull
    public String getName() {
        return this.hasCustomName() ? this.customName : this.getBlockType().getTranslationKey() + ".name";
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.getItems());
        }

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    @Nonnull
    public CompoundNBT writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.getItems());
        }

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        return compound;
    }
}