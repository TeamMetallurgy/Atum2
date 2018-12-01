package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.quern.QuernRecipe;
import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityQuern extends TileEntityInventoryBase implements ITickable, ISidedInventory {
    public int rotation;

    public TileEntityQuern() {
        super(1);
    }

    @Override
    public void update() {
        rotation++;
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        return new QuernRecipe("", null, 0).isValidInput(stack);
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing facing) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing facing) {
        return false;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return false;
    }

    // TODO Figure out what to do with these two
    @Override
    public Container createContainer(@Nonnull InventoryPlayer inventoryPlayer, @Nonnull EntityPlayer player) {
        return null;
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return "";
    }
}