package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import com.teammetallurgy.atum.inventory.container.block.ContainerKiln;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityKilnBase extends TileEntityInventoryBase implements ISidedInventory {
    private static final int[] SLOTS_TOP = new int[]{0, 1, 2, 3};
    private static final int[] SLOTS_BOTTOM = new int[]{5, 6, 7, 8};
    private static final int[] SLOTS_SIDES = new int[]{4};

    private BlockPos primaryPos;

    TileEntityKilnBase() {
        super(9);
    }

    public boolean isPrimary() {
        return primaryPos != null && primaryPos.equals(this.pos);
    }

    public void setPrimaryPos(BlockPos primaryPos) {
        this.primaryPos = primaryPos;
    }

    public BlockPos getPrimaryPos() {
        return primaryPos;
    }

    TileEntityKilnBase getPrimary() {
        if (this.isPrimary()) {
            return this;
        }
        if (this.hasWorld() && primaryPos != null) {
            TileEntity te = world.getTileEntity(primaryPos);
            if (te instanceof TileEntityKilnBase) {
                return (TileEntityKilnBase) te;
            }
        }
        return null;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
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
            return false;
        } else if (index == 4) {
            return TileEntityFurnace.isItemFuel(stack);
        } else {
            return true;
        }
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull PlayerEntity player) {
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
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        boolean hasPrimary = compound.getBoolean("has_primary");
        if (hasPrimary) {
            int x = compound.getInteger("px");
            int y = compound.getInteger("py");
            int z = compound.getInteger("pz");
            primaryPos = new BlockPos(x, y, z);
        }
    }

    @Override
    @Nonnull
    public CompoundNBT writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        if (primaryPos != null) {
            compound.setBoolean("has_primary", true);
            compound.setInteger("px", primaryPos.getX());
            compound.setInteger("py", primaryPos.getY());
            compound.setInteger("pz", primaryPos.getZ());
        } else {
            compound.setBoolean("has_primary", false);
        }
        return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
        super.onDataPacket(manager, packet);
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.writeToNBT(new CompoundNBT());
    }

    private IItemHandler handlerTop = new SidedInvWrapper(this, EnumFacing.UP);
    private IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);
    private IItemHandler handlerSide = new SidedInvWrapper(this, EnumFacing.WEST);

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN) {
                return (T) handlerBottom;
            } else if (facing == EnumFacing.UP) {
                return (T) handlerTop;
            } else {
                return (T) handlerSide;
            }
        return super.getCapability(capability, facing);
    }
}