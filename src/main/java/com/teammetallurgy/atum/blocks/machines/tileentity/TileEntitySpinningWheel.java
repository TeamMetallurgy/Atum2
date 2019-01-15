package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.spinningwheel.ISpinningWheelRecipe;
import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import com.teammetallurgy.atum.blocks.machines.BlockSpinningWheel;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntitySpinningWheel extends TileEntityInventoryBase implements ISidedInventory {
    public NBTTagCompound input;
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
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
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
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
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
                && (this.input == null || StackHelper.areStacksEqualIgnoreSize(new ItemStack(this.input), stack))) {
            if (this.input == null) {
                this.input = stack.writeToNBT(new NBTTagCompound());
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
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer player) {
        return null;
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return "";
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.wheel = compound.getBoolean("wheel");
        this.rotations = compound.getInteger("rotations");
        this.input = compound.getCompoundTag("input");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("wheel", this.wheel);
        compound.setInteger("rotations", this.rotations);
        if (this.input != null) {
            compound.setTag("input", this.input);
        }
        return compound;
    }
}