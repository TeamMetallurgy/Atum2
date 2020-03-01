package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.spinningwheel.ISpinningWheelRecipe;
import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.SpinningWheelBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpinningWheelTileEntity extends InventoryBaseTileEntity implements ISidedInventory {
    public CompoundNBT input = new CompoundNBT();
    public int rotations;

    public SpinningWheelTileEntity() {
        super(AtumTileEntities.SPINNING_WHEEL, 2);
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        for (ISpinningWheelRecipe spinningWheelRecipe : RecipeHandlers.spinningWheelRecipes.getValues()) {
            for (ItemStack input : spinningWheelRecipe.getInput()) {
                if (ItemStack.areItemsEqual(input, stack)) {
                    return spinningWheelRecipe.isValidInput(stack);
                }
            }
        }
        return false;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet) {
        super.onDataPacket(manager, packet);
        this.read(packet.getNbtCompound());
        this.markDirty();
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void markDirty() {
        super.markDirty();
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
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
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull Direction facing) {
        int spool = world.getBlockState(pos).get(SpinningWheelBlock.SPOOL);
        if (this.getStackInSlot(0).isEmpty() && this.getStackInSlot(1).isEmpty() && index == 0 && this.isItemValidForSlot(0, stack) && spool < 3
                && (this.input.isEmpty() || StackHelper.areStacksEqualIgnoreSize(ItemStack.read(this.input), stack))) {
            if (this.input.isEmpty()) {
                this.input = stack.write(new CompoundNBT());
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
        SpinningWheelBlock spinningWheel = (SpinningWheelBlock) world.getBlockState(pos).getBlock();
        if (index == 1 && direction == Direction.DOWN) {
            spinningWheel.output(world, pos, null, this);
            return true;
        } else {
            return false;
        }
    }

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.DOWN, Direction.WEST);

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.DOWN) {
                return handlers[0].cast();
            } else if (facing != Direction.UP) {
                return handlers[1].cast();
            }
        }
        return LazyOptional.empty();
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.rotations = compound.getInt("rotations");
        this.input = compound.getCompound("input");
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("rotations", this.rotations);
        if (this.input != null) {
            compound.put("input", this.input);
        }
        return compound;
    }

    @Override
    protected Container createMenu(int windowID, @Nonnull PlayerInventory playerInventory) { //Does not have a Screen
        return null;
    }
}