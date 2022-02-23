package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.SpinningWheelBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import net.minecraft.core.BlockPos;
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

    public SpinningWheelTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.SPINNING_WHEEL.get(), pos, state, 2);
    }

    @Override
    public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
        return RecipeHelper.isItemValidForSlot(this.level, stack, AtumRecipeTypes.SPINNING_WHEEL);
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
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);
        this.rotations = tag.getInt("rotations");
        this.input = tag.getCompound("input");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("rotations", this.rotations);
        if (this.input != null) {
            tag.put("input", this.input);
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory) { //Does not have a Screen
        return null;
    }
}