package com.teammetallurgy.atum.blocks.base.tileentity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nonnull;

public abstract class InventoryBaseTileEntity extends RandomizableContainerBlockEntity {
    protected NonNullList<ItemStack> inventory;

    public InventoryBaseTileEntity(BlockEntityType<?> tileEntityType, int slots) {
        super(tileEntityType);
        this.inventory = NonNullList.withSize(slots, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    @Override
    @Nonnull
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(@Nonnull NonNullList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    @Nonnull
    protected Component getDefaultName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
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
    public void load(@Nonnull BlockState state, @Nonnull CompoundTag compound) {
        super.load(state, compound);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compound)) {
            ContainerHelper.loadAllItems(compound, this.getItems());
        }
    }

    @Override
    @Nonnull
    public CompoundTag save(@Nonnull CompoundTag compound) {
        super.save(compound);
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.getItems());
        }
        return compound;
    }
}