package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import com.teammetallurgy.atum.blocks.machines.BlockQuern;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.Direction;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityQuern extends TileEntityInventoryBase implements ITickable, ISidedInventory {
    private int currentRotation;
    private int quernRotations;

    public TileEntityQuern() {
        super(1);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (this.currentRotation >= 360) {
                this.currentRotation = 0;
                this.quernRotations += 1;
            }

            if (this.getStackInSlot(0).isEmpty()) {
                this.quernRotations = 0;
            }

            if (this.quernRotations > 0) {
                for (IQuernRecipe quernRecipe : RecipeHandlers.quernRecipes) {
                    for (ItemStack input : quernRecipe.getInput()) {
                        if (StackHelper.areStacksEqualIgnoreSize(input, this.getStackInSlot(0)) && quernRecipe.getRotations() == this.quernRotations) {
                            this.decrStackSize(0, 1);
                            this.outputItems(quernRecipe.getOutput().copy(), this.getPos());
                            this.quernRotations = 0;
                            this.markDirty();
                        }
                    }
                }
            }
        }
    }

    private void outputItems(@Nonnull ItemStack stack, BlockPos pos) {
        Direction facing = world.getBlockState(pos).getValue(BlockQuern.FACING).getOpposite();
        TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
        if (tileEntity instanceof ISidedInventory && ((ISidedInventory) tileEntity).getSlotsForFace(facing).length > 0 || tileEntity instanceof IInventory && ((IInventory) tileEntity).getSizeInventory() > 0) {
            IInventory inventory = ((IInventory) tileEntity);
            stack = TileEntityHopper.putStackInInventoryAllSlots(this, inventory, stack, facing);
        } else if (tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
            IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
            stack = ItemHandlerHelper.insertItem(itemHandler, stack, false);
        }

        if (!stack.isEmpty()) {
            StackHelper.spawnItemStack(world, (double) facing.getXOffset() + pos.getX() + 0.5D, (double) pos.getY() + 0.15D, (double) facing.getZOffset() + pos.getZ() + 0.5, stack);
            if (world.isRemote) {
                world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1.0F, 0.4F, false);
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        for (IQuernRecipe quernRecipe : RecipeHandlers.quernRecipes.getValuesCollection()) {
            for (ItemStack input : quernRecipe.getInput()) {
                if (ItemStack.areItemsEqual(input, stack)) {
                    return quernRecipe.isValidInput(stack);
                }
            }
        }
        return false;
    }

    public int getRotations() {
        return this.currentRotation;
    }

    public void setRotations(int rotations) {
        this.currentRotation = rotations;
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        this.currentRotation = compound.getInt("currentRotation");
        this.quernRotations = compound.getInt("quernRotations");
    }

    @Nonnull
    @Override
    public CompoundNBT writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        compound.putInt("currentRotation", this.currentRotation);
        compound.putInt("quernRotations", this.quernRotations);
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

    @Override
    public void markDirty() {
        super.markDirty();
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull Direction facing) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction facing) {
        return false;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing) {
        return false;
    }

    @Override
    public Container createContainer(@Nonnull InventoryPlayer inventoryPlayer, @Nonnull PlayerEntity player) {
        return null;
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return "";
    }
}