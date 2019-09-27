package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import com.teammetallurgy.atum.inventory.container.block.ContainerTrap;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TrapTileEntity extends InventoryBaseTileEntity implements ITickableTileEntity {
    int burnTime;
    int currentItemBurnTime;
    boolean isDisabled = false;
    public boolean isInsidePyramid = true;

    public TrapTileEntity() {
        super(1);
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
    public void update() {
        boolean isBurningCheck = this.isBurning();
        boolean isBurning = false;
        boolean canDamageEntity = false;

        if (!this.isDisabled && this.isBurning()) {
            Direction facing = world.getBlockState(pos).getValue(BlockTrap.FACING);
            Class<? extends LivingEntity> entity;
            if (this.isInsidePyramid) {
                entity = PlayerEntity.class;
            } else {
                entity = LivingEntity.class;
            }
            List<LivingEntity> entities = world.getEntitiesWithinAABB(entity, getFacingBoxWithRange(facing, 1));
            for (LivingEntity livingBase : entities) {
                if (livingBase instanceof PlayerEntity ? !((PlayerEntity) livingBase).capabilities.isCreativeMode : livingBase != null) {
                    canDamageEntity = true;
                    this.triggerTrap(facing, livingBase);
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

        if (!this.world.isRemote && !this.isDisabled) {
            ItemStack fuel = this.inventory.get(0);
            if (this.isBurning() || !fuel.isEmpty()) {
                if (!this.isBurning()) {
                    this.burnTime = TileEntityFurnace.getItemBurnTime(fuel) / 10;
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

    protected void triggerTrap(Direction facing, LivingEntity livingBase) {
    }

    boolean isBurning() {
        return this.burnTime > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isBurning(IInventory inventory) {
        return inventory.getField(0) > 0;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        return super.isUsableByPlayer(player) && !isInsidePyramid;
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        ItemStack fuel = this.inventory.get(0);
        return !isInsidePyramid && (TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && fuel.getItem() != Items.BUCKET);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return this.burnTime;
            case 1:
                return this.currentItemBurnTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.burnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player) {
        return new ContainerTrap(playerInventory, this);
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return String.valueOf(new ResourceLocation(Constants.MOD_ID, "trap"));
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
        return this.write(new CompoundNBT());
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
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
    public ItemStack decrStackSize(int index, int count) {
        return !isInsidePyramid ? super.decrStackSize(index, count) : ItemStack.EMPTY;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing) {
        return !this.isInsidePyramid && super.hasCapability(capability, facing);
    }
}