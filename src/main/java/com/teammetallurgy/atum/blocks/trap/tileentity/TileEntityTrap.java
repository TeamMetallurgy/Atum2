package com.teammetallurgy.atum.blocks.trap.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class TileEntityTrap extends TileEntity implements ITickable {
    boolean isDisabled = false;

    @Override
    public void update() {
        //System.out.println(this.isDisabled);
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
        this.isDisabled = compound.getBoolean("Disabled");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("Disabled", this.isDisabled);
        return compound;
    }
}