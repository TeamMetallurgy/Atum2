package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.trap.TrapBlock;
import com.teammetallurgy.atum.inventory.container.block.TrapContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceFuelSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TrapTileEntity extends InventoryBaseTileEntity implements ITickableTileEntity {
    int burnTime;
    int currentItemBurnTime;
    boolean isDisabled = false;
    public boolean isInsidePyramid = true;
    public final IIntArray trapData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return TrapTileEntity.this.burnTime;
                case 1:
                    return TrapTileEntity.this.currentItemBurnTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    TrapTileEntity.this.burnTime = value;
                    break;
                case 1:
                    TrapTileEntity.this.currentItemBurnTime = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public TrapTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType, 1);
    }

    public void setDisabledStatus(boolean isDisabled) {
        this.isDisabled = isDisabled;
        BlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    AxisAlignedBB getFacingBoxWithRange(Direction facing, int range) {
        BlockPos pos = getPos();
        Vec3i dir = facing.getDirectionVec();
        return new AxisAlignedBB(pos).expand(dir.getX() * range, dir.getY() * range, dir.getZ() * range);
    }

    @Override
    public void tick() {
        boolean isBurningCheck = this.isBurning();
        boolean isBurning = false;
        boolean canDamageEntity = false;
        World world = this.world;
        if (world == null) return;

        if (!this.isDisabled && this.isBurning()) {
            Direction facing = world.getBlockState(pos).get(TrapBlock.FACING);
            Class<? extends LivingEntity> entity;
            if (this.isInsidePyramid) {
                entity = PlayerEntity.class;
            } else {
                entity = LivingEntity.class;
            }
            List<LivingEntity> entities = world.getEntitiesWithinAABB(entity, getFacingBoxWithRange(facing, 1));
            for (LivingEntity livingBase : entities) {
                if (livingBase instanceof PlayerEntity ? !((PlayerEntity) livingBase).isCreative() : livingBase != null) {
                    canDamageEntity = true;
                    this.triggerTrap(world, facing, livingBase);
                } else {
                    canDamageEntity = false;
                }
            }
        }

        if (this.isInsidePyramid) {
            this.burnTime = 1;
        }

        if (this.isBurning() && !this.isDisabled && canDamageEntity && !this.isInsidePyramid) {
            --this.burnTime;
        }

        if (!world.isRemote && !this.isDisabled) {
            ItemStack fuel = this.inventory.get(0);
            if (this.isBurning() || !fuel.isEmpty()) {
                if (!this.isBurning()) {
                    this.burnTime = ForgeHooks.getBurnTime(fuel) / 10;
                    this.currentItemBurnTime = this.burnTime;
                    if (this.isBurning()) {
                        isBurning = true;
                        if (!fuel.isEmpty()) {
                            fuel.shrink(1);
                        }
                    }
                }
            }
            if (isBurningCheck != this.isBurning()) {
                isBurning = true;
            }
        }
        if (isBurning) {
            this.markDirty();
        }
    }

    protected void triggerTrap(World world, Direction facing, LivingEntity livingBase) {
    }

    boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        return super.isUsableByPlayer(player) && !isInsidePyramid;
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        ItemStack fuel = this.inventory.get(0);
        return !isInsidePyramid && (FurnaceTileEntity.isFuel(stack) || FurnaceFuelSlot.isBucket(stack) && fuel.getItem() != Items.BUCKET);
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
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.burnTime = compound.getInt("BurnTime");
        this.isDisabled = compound.getBoolean("Disabled");
        this.isInsidePyramid = compound.getBoolean("InPyramid");
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("BurnTime", (short) this.burnTime);
        compound.putBoolean("Disabled", this.isDisabled);
        compound.putBoolean("InPyramid", this.isInsidePyramid);
        return compound;
    }

    @Override
    @Nonnull
    protected Container createMenu(int windowID, @Nonnull PlayerInventory playerInventory) {
        return new TrapContainer(windowID, playerInventory, this);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        return !isInsidePyramid ? super.decrStackSize(index, count) : ItemStack.EMPTY;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        return !this.isInsidePyramid ? super.getCapability(capability, direction) : LazyOptional.empty();
    }
}