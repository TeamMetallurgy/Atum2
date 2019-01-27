package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import com.teammetallurgy.atum.blocks.machines.BlockKiln;
import com.teammetallurgy.atum.blocks.machines.BlockKilnFake;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.inventory.container.block.ContainerKiln;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class TileEntityKilnBase extends TileEntityInventoryBase implements ISidedInventory {
    protected static final int[] SLOTS_TOP = new int[]{0, 1, 2, 3};
    protected static final int[] SLOTS_BOTTOM = new int[]{5, 6, 7, 8};
    protected static final int[] SLOTS_SIDES = new int[]{4};

    public TileEntityKilnBase() {
        super(9);
    }

    public boolean isPrimary() {
        IBlockState state = world.getBlockState(this.pos);
        return state.getBlock() == AtumBlocks.KILN && state.getValue(BlockKiln.MULTIBLOCK_PRIMARY);
    }

    public TileEntityKilnBase getPrimary() {
        IBlockState state = world.getBlockState(this.pos);

        if (state.getBlock() == AtumBlocks.KILN) {
            if (state.getValue(BlockKiln.MULTIBLOCK_PRIMARY)) {
                return this;
            } else {
            	BlockPos primaryPos = ((BlockKiln) state.getBlock()).getPrimaryKilnBlock(world, pos);
            	IBlockState primaryState = world.getBlockState(primaryPos);
            	if(primaryState.getBlock() == AtumBlocks.KILN && primaryState.getValue(BlockKiln.MULTIBLOCK_PRIMARY))
            		return (TileEntityKilnBase) world.getTileEntity(primaryPos);
            	else
            		return null;
            }
        } else if (state.getBlock() == AtumBlocks.KILN_FAKE) {
            return (TileEntityKilnBase) world.getTileEntity(((BlockKilnFake) state.getBlock()).getPrimaryKilnBlock(world, pos, state));
        }
        return null;
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
        	if(primary != null) {
        		return primary.isItemValidForSlot(index, stack);
        	} else {
        		return false;
        	}
        }
        if (index >= 5 && index <= 9) {
            return false;
        } else if (index != 4) {
            return true;
        } else {
            ItemStack fuelStack = this.inventory.get(4);
            return TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && fuelStack.getItem() != Items.BUCKET;
        }
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer player) {
        if (!isPrimary()) {
        	TileEntityKilnBase primary = getPrimary();
        	if(primary != null) {
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
        	if(primary != null) {
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
        	if(primary != null) {
        		return primary.canInsertItem(index, stack, side);
        	} else {
        		return false;
        	}
        }
        return this.isItemValidForSlot(index, stack);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
        if (!isPrimary()) {
        	TileEntityKilnBase primary = getPrimary();
        	if(primary != null) {
        		return primary.canExtractItem(index, stack, side);
        	} else {
        		return false;
        	}
        }
        if (side == EnumFacing.DOWN && index == 4) {
            Item item = stack.getItem();
            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        }
        return true;
    }


    @Override
    @Nonnull
    protected NonNullList<ItemStack> getItems() {
        if (!isPrimary()) {
        	TileEntityKilnBase primary = getPrimary();
        	if(primary != null) {
        		return primary.getItems();
        	}
        }
        return super.getItems();
    }

    @Override
    public int getSizeInventory() {
        if (!isPrimary()) {
        	TileEntityKilnBase primary = getPrimary();
        	if(primary != null) {
        		return primary.getSizeInventory();
        	} else {
        		return 0;
        	}
        }
        return super.getSizeInventory();
    }

    @Override
    public int getInventoryStackLimit() {
        if (!isPrimary()) {
        	TileEntityKilnBase primary = getPrimary();
        	if(primary != null) {
        		return primary.getInventoryStackLimit();
        	} else {
        		return 0;
        	}
        }
        return super.getInventoryStackLimit();
    }

    @Override
    public boolean isEmpty() {
        if (!isPrimary()) {
        	TileEntityKilnBase primary = getPrimary();
        	if(primary != null) {
        		return primary.isEmpty();
        	} else {
        		return false;
        	}
        }
        return super.isEmpty();
    }
}