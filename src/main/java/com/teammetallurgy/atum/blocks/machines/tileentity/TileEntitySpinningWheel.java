package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.spinningwheel.ISpinningWheelRecipe;
import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import com.teammetallurgy.atum.blocks.machines.BlockSpinningWheel;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntitySpinningWheel extends TileEntityInventoryBase implements ISidedInventory {
    public CompoundNBT input = new CompoundNBT();
    public boolean wheel;
    public int rotations;

    public TileEntitySpinningWheel() {
        super(2);
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        for (ISpinningWheelRecipe spinningWheelRecipe : RecipeHandlers.spinningWheelRecipes.getValuesCollection()) {
            for (ItemStack input : spinningWheelRecipe.getInput()) {
                if (ItemStack.areItemsEqual(input, stack)) {
                    return spinningWheelRecipe.isValidInput(stack);
                }
            }
        }
        return false;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
        super.onDataPacket(manager, packet);
        this.readFromNBT(packet.getNbtCompound());
        this.markDirty();
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.writeToNBT(new CompoundNBT());
    }

    @Override
    public void markDirty() {
        super.markDirty();
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return new int[]{1};
        } else if (side != EnumFacing.UP) {
            return new int[]{0};
        } else {
            return new int[0];
        }
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing facing) {
        int spool = world.getBlockState(pos).getValue(BlockSpinningWheel.SPOOL);
        if (this.getStackInSlot(0).isEmpty() && this.getStackInSlot(1).isEmpty() && index == 0 && this.isItemValidForSlot(0, stack) && spool < 3
                && (this.input.isEmpty() || StackHelper.areStacksEqualIgnoreSize(new ItemStack(this.input), stack))) {
            if (this.input.isEmpty()) {
                this.input = stack.writeToNBT(new CompoundNBT());
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
        BlockSpinningWheel spinningWheel = (BlockSpinningWheel) world.getBlockState(pos).getBlock();
        if (index == 1 && direction == EnumFacing.DOWN) {
            spinningWheel.output(world, pos, null, this);
            return true;
        } else {
            return false;
        }
    }

    private IItemHandler handlerSide = new SidedInvWrapper(this, EnumFacing.WEST);
    private IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN) {
                return (T) handlerBottom;
            } else {
                return (T) handlerSide;
            }
        }
        return null;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return facing != EnumFacing.UP && super.hasCapability(capability, facing);
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull PlayerEntity player) {
        return null;
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return "";
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        this.wheel = compound.getBoolean("wheel");
        this.rotations = compound.getInteger("rotations");
        this.input = compound.getCompoundTag("input");
    }

    @Nonnull
    @Override
    public CompoundNBT writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        compound.setBoolean("wheel", this.wheel);
        compound.setInteger("rotations", this.rotations);
        if (this.input != null) {
            compound.setTag("input", this.input);
        }
        return compound;
    }
}