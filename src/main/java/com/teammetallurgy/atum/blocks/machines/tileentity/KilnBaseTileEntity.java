package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KilnBaseTileEntity extends InventoryBaseTileEntity implements WorldlyContainer {
    private BlockPos primaryPos;
    private static final int[] SLOTS_TOP = new int[]{0, 1, 2, 3};
    private static final int[] SLOTS_BOTTOM = new int[]{5, 6, 7, 8};
    private static final int[] SLOTS_SIDES = new int[]{4};

    KilnBaseTileEntity(BlockEntityType<?> tileType, BlockPos pos, BlockState state) {
        super(tileType, pos, state, 9);
    }

    @Override
    @Nonnull
    public BlockEntityType<?> getType() {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.getType();
            }
        }
        return super.getType();
    }

    public boolean isPrimary() {
        return primaryPos != null && primaryPos.equals(this.worldPosition);
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
        if (this.level != null && primaryPos != null) {
            BlockEntity te = level.getBlockEntity(primaryPos);
            if (te instanceof KilnBaseTileEntity) {
                return (KilnBaseTileEntity) te;
            }
        }
        return null;
    }

    @Override
    public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.canPlaceItem(index, stack);
            } else {
                return false;
            }
        }
        if (index >= 5 && index <= 9) {
            return false;
        } else if (index == 4) {
            return AbstractFurnaceBlockEntity.isFuel(stack);
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
        if (side == Direction.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return side == Direction.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack stack, Direction side) {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.canPlaceItemThroughFace(index, stack, side);
            } else {
                return false;
            }
        }
        ItemStack slotStack = this.inventory.get(index);
        return (slotStack.isEmpty() || slotStack.getCount() < this.getMaxStackSize()) && this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nonnull Direction side) {
        return this.isPrimary() || this.getPrimary() != null && this.getPrimary().canTakeItemThroughFace(index, stack, side);
    }

    @Override
    public int getContainerSize() {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.getContainerSize();
            }
        }
        return super.getContainerSize();
    }

    @Override
    public int getMaxStackSize() {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.getMaxStackSize();
            }
        }
        return super.getMaxStackSize();
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
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);
        boolean hasPrimary = tag.getBoolean("has_primary");
        if (hasPrimary) {
            int x = tag.getInt("px");
            int y = tag.getInt("py");
            int z = tag.getInt("pz");
            primaryPos = new BlockPos(x, y, z);
        }
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        super.saveAdditional(tag);
        if (primaryPos != null) {
            tag.putBoolean("has_primary", true);
            tag.putInt("px", primaryPos.getX());
            tag.putInt("py", primaryPos.getY());
            tag.putInt("pz", primaryPos.getZ());
        } else {
            tag.putBoolean("has_primary", false);
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory) {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.createMenu(windowID, playerInventory);
            }
        }
        return null;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(manager, packet);
        if (packet.getTag() != null) {
            this.load(packet.getTag());
            this.setChanged();
        }
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.WEST);

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return primary.getCapability(capability, facing);
            }
        }
        if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            if (facing == Direction.UP) {
                return handlers[0].cast();
            } else if (facing == Direction.DOWN) {
                return handlers[1].cast();
            } else {
                return handlers[2].cast();
            }
        }
        return super.getCapability(capability, facing);
    }
}