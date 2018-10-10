package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileEntityTrap extends TileEntityInventoryBase implements ITickable {
    int burnTime;
    int currentItemBurnTime;
    boolean isDisabled = false;
    public boolean isInsidePyramid = true;

    public TileEntityTrap() {
        super(1);
    }

    public void setDisabledStatus(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    AxisAlignedBB getFacingBoxWithRange(EnumFacing facing, int range) {
        BlockPos pos = getPos();
        EnumFacing.Axis axis = facing.getAxis();
        EnumFacing.AxisDirection dir = facing.getAxisDirection();
        return new AxisAlignedBB(pos).expand(axis == EnumFacing.Axis.X ? dir == EnumFacing.AxisDirection.POSITIVE ? +range : -range : 0, axis == EnumFacing.Axis.Y ? dir == EnumFacing.AxisDirection.POSITIVE ? +range : -range : 0, axis == EnumFacing.Axis.Z ? dir == EnumFacing.AxisDirection.POSITIVE ? +range : -range : 0);
    }

    @Override
    public void update() {
        boolean isBurningCheck = this.isBurning();
        boolean isBurning = false;
        boolean canDamageEntity = false;

        if (!this.isDisabled && this.isBurning()) {
            EnumFacing facing = world.getBlockState(pos).getValue(BlockTrap.FACING);
            Class<? extends EntityLivingBase> entity;
            if (this.isInsidePyramid) {
                entity = EntityPlayer.class;
            } else {
                entity = EntityLivingBase.class;
            }
            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(entity, getFacingBoxWithRange(facing, 1));
            for (EntityLivingBase livingBase : entities) {
                if (livingBase instanceof EntityPlayer ? !((EntityPlayer) livingBase).capabilities.isCreativeMode : livingBase != null) {
                    canDamageEntity = true;
                    this.spawnParticles(facing, livingBase);
                    this.fire(livingBase);
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

    protected void spawnParticles(EnumFacing facing, EntityLivingBase livingBase) {
    }

    protected void fire(EntityLivingBase livingBase) {
    }

    boolean isBurning() {
        return this.burnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    static boolean isBurning(IInventory inventory) {
        return inventory.getField(0) > 0;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
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
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer player) {
        return new ContainerTrap(playerInventory, this);
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return String.valueOf(new ResourceLocation(Constants.MOD_ID, "trap"));
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
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.burnTime = compound.getInteger("BurnTime");
        this.isDisabled = compound.getBoolean("Disabled");
        this.isInsidePyramid = compound.getBoolean("InPyramid");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short) this.burnTime);
        compound.setBoolean("Disabled", this.isDisabled);
        compound.setBoolean("InPyramid", this.isInsidePyramid);
        return compound;
    }


    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        return !isInsidePyramid ? super.decrStackSize(index, count) : ItemStack.EMPTY;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return !this.isInsidePyramid && super.hasCapability(capability, facing);
    }
}