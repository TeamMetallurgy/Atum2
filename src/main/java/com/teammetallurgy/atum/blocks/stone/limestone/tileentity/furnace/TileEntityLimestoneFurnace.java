package com.teammetallurgy.atum.blocks.stone.limestone.tileentity.furnace;

import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestoneFurnace;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class TileEntityLimestoneFurnace extends FurnaceTileEntity {
    private NonNullList<ItemStack> furnaceItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String limestoneFurnaceName;

    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.furnaceItemStacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int index) {
        return this.furnaceItemStacks.get(index);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        ItemStack furnaceStack = this.furnaceItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(furnaceStack) && ItemStack.areItemStackTagsEqual(stack, furnaceStack);
        this.furnaceItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    @Nonnull
    public String getName() {
        return this.hasCustomName() ? this.limestoneFurnaceName : "container.limestoneFurnace";
    }

    @Override
    public boolean hasCustomName() {
        return this.limestoneFurnaceName != null && this.limestoneFurnaceName.length() > 0;
    }

    @Override
    public void setCustomInventoryName(@Nonnull String name) {
        this.limestoneFurnaceName = name;
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);

        this.furnaceItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
        this.furnaceBurnTime = compound.getInt("BurnTime");
        this.cookTime = compound.getInt("CookTime");
        this.totalCookTime = compound.getInt("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks.get(1));

        if (compound.contains("CustomName", 8)) {
            this.limestoneFurnaceName = compound.getString("CustomName");
        }
    }

    @Override
    @Nonnull
    public CompoundNBT writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);

        compound.putInt("BurnTime", (short) this.furnaceBurnTime);
        compound.putInt("CookTime", (short) this.cookTime);
        compound.putInt("CookTimeTotal", (short) this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);

        if (this.hasCustomName()) {
            compound.putString("CustomName", this.limestoneFurnaceName);
        }
        return compound;
    }

    @Override
    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    @Override
    public void update() {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning()) {
            --this.furnaceBurnTime;
        }

        if (!this.world.isRemote) {
            ItemStack stack = this.furnaceItemStacks.get(1);

            if (this.isBurning() || !stack.isEmpty() && !(this.furnaceItemStacks.get(0)).isEmpty()) {
                if (!this.isBurning() && this.canSmelt()) {
                    this.furnaceBurnTime = getItemBurnTime(stack);
                    this.currentItemBurnTime = this.furnaceBurnTime;

                    if (this.isBurning()) {
                        flag1 = true;

                        if (!stack.isEmpty()) {
                            Item item = stack.getItem();
                            stack.shrink(1);

                            if (stack.isEmpty()) {
                                ItemStack item1 = item.getContainerItem(stack);
                                this.furnaceItemStacks.set(1, item1);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt()) {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.furnaceItemStacks.get(0));
                        this.smeltItem();
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                BlockLimestoneFurnace.setState(this.isBurning(), this.world, this.pos);
            }
        }

        if (flag1) {
            this.markDirty();
        }
    }

    private boolean canSmelt() {
        if (this.furnaceItemStacks.get(0).isEmpty()) {
            return false;
        } else {
            ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks.get(0));

            if (smeltResult.isEmpty()) {
                return false;
            } else {
                ItemStack stack = this.furnaceItemStacks.get(2);
                if (stack.isEmpty()) return true;
                if (!stack.isItemEqual(smeltResult)) return false;
                int result = stack.getCount() + smeltResult.getCount();
                return result <= getInventoryStackLimit() && result <= stack.getMaxStackSize();
            }
        }
    }

    @Override
    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack stack = this.furnaceItemStacks.get(0);
            ItemStack stack1 = FurnaceRecipes.instance().getSmeltingResult(stack);
            ItemStack stack2 = this.furnaceItemStacks.get(2);

            if (stack2.isEmpty()) {
                this.furnaceItemStacks.set(2, stack1.copy());
            } else if (stack2.getItem() == stack1.getItem()) {
                stack2.grow(stack1.getCount());
            }

            if (stack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && stack.getMetadata() == 1 && !this.furnaceItemStacks.get(1).isEmpty() && this.furnaceItemStacks.get(1).getItem() == Items.BUCKET) {
                this.furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            stack.shrink(1);
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        if (index == 2) {
            return false;
        } else if (index != 1) {
            return true;
        } else {
            ItemStack furnaceStack = this.furnaceItemStacks.get(1);
            return isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && furnaceStack.getItem() != Items.BUCKET;
        }
    }

    @Override
    @Nonnull
    public Container createContainer(InventoryPlayer playerInventory, PlayerEntity player) {
        return new ContainerLimestoneFurnace(playerInventory, this);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return this.furnaceBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.furnaceBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    @Override
    public void clear() {
        this.furnaceItemStacks.clear();
    }
}