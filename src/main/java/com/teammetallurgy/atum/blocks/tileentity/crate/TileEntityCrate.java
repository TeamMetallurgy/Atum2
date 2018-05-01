package com.teammetallurgy.atum.blocks.tileentity.crate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants.NBT;

import javax.annotation.Nonnull;

public class TileEntityCrate extends TileEntityLockable {

    private static int inventorySize = 27;
    private String customName;
    private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(inventorySize, ItemStack.EMPTY);

    public TileEntityCrate() {
    }

    @Override
    @Nonnull
    public String getName() {
        return this.hasCustomName() ? this.customName : getDefaultName();
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String name) {
        customName = name;
    }

    private String getDefaultName() {
        int meta = getBlockMetadata();
        String name = "container.crate.";
        switch (meta) {
            case 0:
                name += "palm";
                break;
            case 1:
                name += "deadwood";
                break;
            default:
                name += "invaild";
        }
        return name;
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer player) {
        return new ContainerCrate(playerInventory, this);
    }

    @Override
    public int getSizeInventory() {
        return inventorySize;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory){
            if(!stack.isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(inventory, index, count);

        if (!stack.isEmpty()){
            markDirty();
        }

        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.set(index, stack);

        if (stack.getCount() > getInventoryStackLimit()){
            stack.setCount(getInventoryStackLimit());
        }

        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        boolean isUsable = world.getTileEntity(pos) == this;
        if (isUsable) {
            double distanceSqFromPlayer = player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
            isUsable = distanceSqFromPlayer <= 64.0D;
        }
        return isUsable ;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public String getGuiID() {
        return "atum:crate";
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        inventory = NonNullList.<ItemStack>withSize(inventorySize, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, inventory);

        if (compound.hasKey("CustomName", NBT.TAG_STRING))
        {
            customName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        ItemStackHelper.saveAllItems(compound, inventory);

        if (hasCustomName()){
            compound.setString("CustomName", customName);
        }
        return compound;
    }
}