package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.FurnaceBaseTileEntity;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.inventory.container.block.KilnContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class KilnBaseTileEntity extends FurnaceBaseTileEntity {
    private BlockPos primaryPos;

    KilnBaseTileEntity() {
        super(AtumTileEntities.KILN, IRecipeType.SMELTING, 9);
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

    KilnBaseTileEntity getPrimary() {
        if (this.isPrimary()) {
            return this;
        }
        if (this.world != null && primaryPos != null) {
            TileEntity te = world.getTileEntity(primaryPos);
            if (te instanceof KilnBaseTileEntity) {
                return (KilnBaseTileEntity) te;
            }
        }
        return null;
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.isItemValidForSlot(index, stack);
            } else {
                return false;
            }
        }
        if (index >= 5 && index <= 9) {
            return false;
        } else if (index == 4) {
            return isFuel(stack);
        } else {
            return true;
        }
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull Direction side) {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.getSlotsForFace(side);
            }
        }
        return super.getSlotsForFace(side);
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, Direction side) {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.canInsertItem(index, stack, side);
            } else {
                return false;
            }
        }
        return this.isItemValidForSlot(index, stack);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction side) {
        return this.isPrimary() || this.getPrimary() != null && this.getPrimary().canExtractItem(index, stack, side);
    }

    @Override
    public int getSizeInventory() {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.getSizeInventory();
            }
        }
        return super.getSizeInventory();
    }

    @Override
    public int getInventoryStackLimit() {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.getInventoryStackLimit();
            }
        }
        return super.getInventoryStackLimit();
    }

    @Override
    public boolean isEmpty() {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.isEmpty();
            }
        }
        return super.isEmpty();
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        boolean hasPrimary = compound.getBoolean("has_primary");
        if (hasPrimary) {
            int x = compound.getInt("px");
            int y = compound.getInt("py");
            int z = compound.getInt("pz");
            primaryPos = new BlockPos(x, y, z);
        }
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (primaryPos != null) {
            compound.putBoolean("has_primary", true);
            compound.putInt("px", primaryPos.getX());
            compound.putInt("py", primaryPos.getY());
            compound.putInt("pz", primaryPos.getZ());
        } else {
            compound.putBoolean("has_primary", false);
        }
        return compound;
    }

    @Override
    @Nonnull
    protected Container createMenu(int windowID, @Nonnull PlayerInventory playerInventory) {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.createMenu(windowID, playerInventory);
            }
        }
        return new KilnContainer(windowID, playerInventory, this);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet) {
        super.onDataPacket(manager, packet);
        this.read(packet.getNbtCompound());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }
}