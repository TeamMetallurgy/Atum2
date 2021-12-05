package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.GodforgeBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.inventory.container.block.GodforgeContainer;
import com.teammetallurgy.atum.items.GodshardItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.util.Mth;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GodforgeTileEntity extends InventoryBaseTileEntity implements WorldlyContainer, TickableBlockEntity {
    private static final int[] SLOTS_UP = new int[]{0};
    private static final int[] SLOTS_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_HORIZONTAL = new int[]{1};
    private int burnTime;
    private int cookTime;
    private int cookTimeTotal;
    public final ContainerData godforgeData = new ContainerData() {
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
        public int getCount() {
            return 3;
        }
    };

    public GodforgeTileEntity() {
        super(AtumTileEntities.GODFORGE, 3);
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory) {
        return new GodforgeContainer(id, playerInventory, this);
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundTag nbt) {
        super.load(state, nbt);
        this.burnTime = nbt.getInt("BurnTime");
        this.cookTime = nbt.getInt("CookTime");
        this.cookTimeTotal = nbt.getInt("CookTimeTotal");
    }

    @Override
    @Nonnull
    public CompoundTag save(@Nonnull CompoundTag compound) {
        super.save(compound);
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

        if (!this.level.isClientSide) {
            God godCache = this.level.getBlockState(this.worldPosition).getValue(GodforgeBlock.GOD);

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
                this.cookTime = Mth.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (isBurningCache != this.isBurning()) {
                markDirty = true;
                this.setGodforgeState(input);
            }
        }

        if (markDirty) {
            this.setChanged();
        }
    }

    public void setGodforgeState(@Nonnull ItemStack input) {
        if (this.level != null) {
            GodforgeBlock.setLitGod(this.level, this.worldPosition, this.level.getBlockState(this.worldPosition), this.isBurning(), input.isEmpty() ? God.ANPUT : ((IArtifact) input.getItem()).getGod());
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
                } else if (output.getCount() + input.getCount() <= this.getMaxStackSize() && output.getCount() + input.getCount() <= output.getMaxStackSize()) {
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
        return stack.getItem() instanceof BucketItem && ((BucketItem) stack.getItem()).getFluid().is(FluidTags.LAVA);
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
    public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
        return direction != Direction.DOWN || index != 1;
    }

    @Override
    public void setItem(int index, @Nonnull ItemStack stack) {
        ItemStack inventoryStack = this.inventory.get(index);
        boolean haveInputChanged = !stack.isEmpty() && stack.sameItem(inventoryStack) && ItemStack.tagMatches(stack, inventoryStack);
        this.inventory.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (index == 0 && !haveInputChanged) {
            this.cookTimeTotal = this.getCookTime();
            this.cookTime = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
        if (index == 0) {
            return stack.getItem() instanceof IArtifact;
        } else if (index == 1) {
            return isFuel(stack);
        } else {
            return false;
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(manager, packet);
        this.load(this.getBlockState(), packet.getTag());
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
    protected void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }
}