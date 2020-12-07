package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.GodforgeBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.inventory.container.block.GodforgeContainer;
import com.teammetallurgy.atum.items.GodshardItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GodforgeTileEntity extends InventoryBaseTileEntity implements ISidedInventory, ITickableTileEntity {
    private static final int[] SLOTS_UP = new int[]{0};
    private static final int[] SLOTS_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_HORIZONTAL = new int[]{1};
    private int burnTime;
    private int cookTime;
    private int cookTimeTotal;
    public final IIntArray godforgeData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return GodforgeTileEntity.this.burnTime;
                case 1:
                    return GodforgeTileEntity.this.cookTime;
                case 2:
                    return GodforgeTileEntity.this.cookTimeTotal;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    GodforgeTileEntity.this.burnTime = value;
                    break;
                case 1:
                    GodforgeTileEntity.this.cookTime = value;
                    break;
                case 2:
                    GodforgeTileEntity.this.cookTimeTotal = value;
            }

        }

        @Override
        public int size() {
            return 3;
        }
    };

    public GodforgeTileEntity() {
        super(AtumTileEntities.GODFORGE, 3);
    }

    @Override
    @Nonnull
    protected Container createMenu(int id, @Nonnull PlayerInventory playerInventory) {
        return new GodforgeContainer(id, playerInventory, this);
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);
        this.burnTime = nbt.getInt("BurnTime");
        this.cookTime = nbt.getInt("CookTime");
        this.cookTimeTotal = nbt.getInt("CookTimeTotal");
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        return compound;
    }

    @Override
    public void tick() {
        boolean isBurningCache = this.isBurning();
        boolean markDirty = false;
        if (this.isBurning()) {
            --this.burnTime;
        }

        if (!this.world.isRemote) {
            God godCache = this.world.getBlockState(this.pos).get(GodforgeBlock.GOD);

            ItemStack fuel = this.inventory.get(1);
            ItemStack input = this.inventory.get(0);
            if (this.isBurning() || !fuel.isEmpty() && !input.isEmpty()) {
                if (this.canSmelt(input)) {
                    if (godCache != ((IArtifact) input.getItem()).getGod()) {
                        markDirty = true;
                        this.setGodforgeState(input);
                    }
                    if (!this.isBurning()) {
                        this.burnTime = this.getBurnTime(fuel);
                        if (this.isBurning()) {
                            markDirty = true;
                            if (fuel.hasContainerItem()) {
                                this.inventory.set(1, fuel.getContainerItem());
                            } else if (!fuel.isEmpty()) {
                                if (fuel.isEmpty()) {
                                    this.inventory.set(1, fuel.getContainerItem());
                                }
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt(input)) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        this.smelt(input);
                        markDirty = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (isBurningCache != this.isBurning()) {
                markDirty = true;
                this.setGodforgeState(input);
            }
        }

        if (markDirty) {
            this.markDirty();
        }
    }

    public void setGodforgeState(@Nonnull ItemStack input) {
        if (this.world != null) {
            GodforgeBlock.setLitGod(this.world, this.pos, this.world.getBlockState(this.pos), this.isBurning(), input.isEmpty() ? God.ANPUT : ((IArtifact) input.getItem()).getGod());
        }
    }

    protected boolean canSmelt(@Nonnull ItemStack input) {
        if (!this.inventory.get(0).isEmpty()) {
            if (input.isEmpty() || !(input.getItem() instanceof IArtifact)) {
                return false;
            } else {
                ItemStack output = this.inventory.get(2);
                IArtifact artifact = (IArtifact) input.getItem();
                if (output.isEmpty()) {
                    return true;
                } else if (!((GodshardItem) output.getItem()).getGod().equals(artifact.getGod())) {
                    return false;
                } else if (output.getCount() + input.getCount() <= this.getInventoryStackLimit() && output.getCount() + input.getCount() <= output.getMaxStackSize()) {
                    return true;
                } else {
                    return output.getCount() + input.getCount() <= input.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private void smelt(@Nonnull ItemStack input) {
        if (!input.isEmpty() && this.canSmelt(input)) {
            ItemStack fuel = this.inventory.get(0);
            ItemStack outputStack = new ItemStack(GodshardItem.getGodshardFromGod(((IArtifact) input.getItem()).getGod()), 4);
            ItemStack output = this.inventory.get(2);
            if (output.isEmpty()) {
                this.inventory.set(2, outputStack.copy());
            } else if (output.getItem() == outputStack.getItem()) {
                output.grow(outputStack.getCount());
            }
            fuel.shrink(1);
        }
    }

    protected int getBurnTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            return ForgeHooks.getBurnTime(fuel) / 32;
        }
    }

    protected int getCookTime() {
        return 600;
    }

    public static boolean isFuel(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof BucketItem && ((BucketItem) stack.getItem()).getFluid().isIn(FluidTags.LAVA);
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        } else {
            return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
        }
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, stack);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
        return direction != Direction.DOWN || index != 1;
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        ItemStack inventoryStack = this.inventory.get(index);
        boolean haveInputChanged = !stack.isEmpty() && stack.isItemEqual(inventoryStack) && ItemStack.areItemStackTagsEqual(stack, inventoryStack);
        this.inventory.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !haveInputChanged) {
            this.cookTimeTotal = this.getCookTime();
            this.cookTime = 0;
            this.markDirty();
        }
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
        if (index == 0) {
            return stack.getItem() instanceof IArtifact;
        } else if (index == 1) {
            return isFuel(stack);
        } else {
            return false;
        }
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet) {
        super.onDataPacket(manager, packet);
        this.read(this.getBlockState(), packet.getNbtCompound());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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

    @Override
    public void remove() {
        super.remove();
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }
}