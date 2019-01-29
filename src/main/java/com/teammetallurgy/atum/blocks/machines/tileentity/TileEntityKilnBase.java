package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import com.teammetallurgy.atum.blocks.machines.BlockKiln;
import com.teammetallurgy.atum.blocks.machines.BlockKilnFake;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.inventory.container.block.ContainerKiln;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class TileEntityKilnBase extends TileEntityInventoryBase implements ISidedInventory {
    protected static final int[] SLOTS_TOP = new int[]{0, 1, 2, 3};
    protected static final int[] SLOTS_BOTTOM = new int[]{5, 6, 7, 8};
    protected static final int[] SLOTS_SIDES = new int[]{4};

    private TileEntityKilnBase primary;
    private boolean isPrimary;

    public TileEntityKilnBase() {
        super(9);
        isPrimary = false;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean value) {
        isPrimary = value;
    }

    public TileEntityKilnBase getPrimary() {
        if (this.hasWorld() && primary == null) {
            IBlockState state = world.getBlockState(this.pos);

            if (state.getBlock() == AtumBlocks.KILN) {
                if (state.getValue(BlockKiln.MULTIBLOCK_PRIMARY)) {
                    primary = this;
                    primary.setPrimary(true);
                } else {
                    BlockPos primaryPos = ((BlockKiln) state.getBlock()).getPrimaryKilnBlock(world, pos);
                    if(primaryPos == null)
                    	return null;
                    IBlockState primaryState = world.getBlockState(primaryPos);
                    if (primaryState.getBlock() == AtumBlocks.KILN && primaryState.getValue(BlockKiln.MULTIBLOCK_PRIMARY)) {
                        primary = (TileEntityKilnBase) world.getTileEntity(primaryPos);
                        if (primary != null) {
                            primary.setPrimary(true);
                        }
                    } else {
                        return null;
                    }
                }
            } else if (state.getBlock() == AtumBlocks.KILN_FAKE) {
                primary = (TileEntityKilnBase) world.getTileEntity(((BlockKilnFake) state.getBlock()).getPrimaryKilnBlock(world, pos, state));
                if (primary != null) {
                    primary.setPrimary(true);
                }
            }
        }
        return primary;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        if (!isPrimary()) {
            TileEntityKilnBase primary = getPrimary();
            if (primary != null) {
                return primary.isItemValidForSlot(index, stack);
            } else {
                return false;
            }
        }
        if (index >= 5 && index <= 9) {
            System.out.println("output");
            return false;
        } else if (index == 4) {
            System.out.println("Input");
            return TileEntityFurnace.isItemFuel(stack);
        } else {
            System.out.println("Else");
            return true;
        }
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer player) {
        if (!isPrimary()) {
            TileEntityKilnBase primary = getPrimary();
            if (primary != null) {
                return primary.createContainer(playerInventory, player);
            }
        }
        return new ContainerKiln(playerInventory, this);
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return "atum:kiln";
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        if (!isPrimary()) {
            TileEntityKilnBase primary = getPrimary();
            if (primary != null) {
                return primary.getSlotsForFace(side);
            }
        }
        if (side == EnumFacing.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
        if (!isPrimary()) {
            TileEntityKilnBase primary = getPrimary();
            if (primary != null) {
                return primary.canInsertItem(index, stack, side);
            } else {
                return false;
            }
        }
        return this.isItemValidForSlot(index, stack);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
        return this.isPrimary() || this.getPrimary() != null && this.getPrimary().canExtractItem(index, stack, side);
    }

    @Override
    @Nonnull
    protected NonNullList<ItemStack> getItems() {
        if (!isPrimary()) {
            TileEntityKilnBase primary = getPrimary();
            if (primary != null) {
                return primary.getItems();
            }
        }
        return super.getItems();
    }

    @Override
    public int getSizeInventory() {
        if (!isPrimary()) {
            TileEntityKilnBase primary = getPrimary();
            if (primary != null) {
                return primary.getSizeInventory();
            }
        }
        return super.getSizeInventory();
    }

    @Override
    public int getInventoryStackLimit() {
        if (!isPrimary()) {
            TileEntityKilnBase primary = getPrimary();
            if (primary != null) {
                return primary.getInventoryStackLimit();
            }
        }
        return super.getInventoryStackLimit();
    }

    @Override
    public boolean isEmpty() {
        if (!isPrimary()) {
            TileEntityKilnBase primary = getPrimary();
            if (primary != null) {
                return primary.isEmpty();
            }
        }
        return super.isEmpty();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.isPrimary = compound.getBoolean("is_primary");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("is_primary", this.isPrimary);
        return compound;
    }
}