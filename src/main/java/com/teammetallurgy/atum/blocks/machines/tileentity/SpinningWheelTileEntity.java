package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.SpinningWheelBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpinningWheelTileEntity extends InventoryBaseTileEntity implements WorldlyContainer {
    public CompoundTag input = new CompoundTag();
    public int rotations;

    public SpinningWheelTileEntity() {
        super(AtumTileEntities.SPINNING_WHEEL, 2);
    }

    @Override
    public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
        return RecipeHelper.isItemValidForSlot(this.level, stack, IAtumRecipeType.SPINNING_WHEEL);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(manager, packet);
        this.load(this.getBlockState(), packet.getTag());
        this.setChanged();
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.level.getBlockState(this.worldPosition), this.level.getBlockState(this.worldPosition), 3);
        }
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull Direction side) {
        if (side == Direction.DOWN) {
            return new int[]{1};
        } else if (side != Direction.UP) {
            return new int[]{0};
        } else {
            return new int[0];
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack stack, Direction facing) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
        if (this.level != null) {
            SpinningWheelBlock spinningWheel = (SpinningWheelBlock) level.getBlockState(worldPosition).getBlock();
            if (index == 1 && direction == Direction.DOWN) {
                spinningWheel.output(level, worldPosition, null, this);
                return true;
            }
            return false;
        }
        return false;
    }

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.DOWN, Direction.WEST);

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing != null) {
                if (facing == Direction.DOWN) {
                    return handlers[0].cast();
                } else if (facing != Direction.UP) {
                    return handlers[1].cast();
                }
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundTag compound) {
        super.load(state, compound);
        this.rotations = compound.getInt("rotations");
        this.input = compound.getCompound("input");
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag compound) {
        super.save(compound);
        compound.putInt("rotations", this.rotations);
        if (this.input != null) {
            compound.put("input", this.input);
        }
        return compound;
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory) { //Does not have a Screen
        return null;
    }
}