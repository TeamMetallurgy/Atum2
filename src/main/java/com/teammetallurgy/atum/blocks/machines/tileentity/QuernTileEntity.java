package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.QuernBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class QuernTileEntity extends InventoryBaseTileEntity implements ITickableTileEntity, ISidedInventory {
    private int currentRotation;
    private int quernRotations;

    public QuernTileEntity() {
        super(AtumBlocks.AtumTileEntities.QUERN, 1);
    }

    @Override
    public void tick() {
        if (this.world != null && !this.world.isRemote) {
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
                            this.outputItems(quernRecipe.getOutput().copy(), this.world, this.getPos());
                            this.quernRotations = 0;
                            this.markDirty();
                        }
                    }
                }
            }
        }
    }

    private void outputItems(@Nonnull ItemStack stack, World world, BlockPos pos) {
        Direction facing = world.getBlockState(pos).get(QuernBlock.FACING).getOpposite();
        TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
        if (tileEntity instanceof ISidedInventory && ((ISidedInventory) tileEntity).getSlotsForFace(facing).length > 0 || tileEntity instanceof IInventory && ((IInventory) tileEntity).getSizeInventory() > 0) {
            IInventory inventory = ((IInventory) tileEntity);
            stack = HopperTileEntity.putStackInInventoryAllSlots(this, inventory, stack, facing);
        } else if (tileEntity != null) {
            LazyOptional<IItemHandler> capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
            if (capability.isPresent()) {
                IItemHandler itemHandler = capability.orElse(null);
                if (itemHandler != null) {
                    stack = ItemHandlerHelper.insertItem(itemHandler, stack, false);
                }
            }
        }

        if (!stack.isEmpty()) {
            StackHelper.spawnItemStack(world, (double) facing.getXOffset() + pos.getX() + 0.5D, (double) pos.getY() + 0.15D, (double) facing.getZOffset() + pos.getZ() + 0.5, stack);
            if (world.isRemote) {
                world.playSound((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1.0F, 0.4F, false);
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        for (IQuernRecipe quernRecipe : RecipeHandlers.quernRecipes.getValues()) {
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
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.currentRotation = compound.getInt("currentRotation");
        this.quernRotations = compound.getInt("quernRotations");
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("currentRotation", this.currentRotation);
        compound.putInt("quernRotations", this.quernRotations);
        return compound;
    }

    @Override
    protected Container createMenu(int windowID, @Nonnull PlayerInventory playerInventory) { //TODO Check if this is okay. Doesn't need a container, since no GUI.
        return null;
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

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.world != null) {
            this.world.notifyBlockUpdate(pos, this.world.getBlockState(pos), world.getBlockState(pos), 3);
        }
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

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        return LazyOptional.empty();
    }
}