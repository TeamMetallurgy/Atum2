package com.teammetallurgy.atum.blocks.trap.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class TileEntityTrap extends TileEntity {
    boolean isDisabled = false;

    AxisAlignedBB getFacingBoxWithRange(EnumFacing facing, int range) {
        BlockPos pos = getPos();
        BlockPos posMax = pos.add(range, range, range);
        pos = pos.add(facing.getFrontOffsetX() * range, facing.getFrontOffsetY() * range, facing.getFrontOffsetZ() * range);
        posMax = posMax.add(facing.getFrontOffsetX() * range, facing.getFrontOffsetY() * range, facing.getFrontOffsetZ() * range);
        return new AxisAlignedBB(pos, posMax);
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

    public void setDisabled() {
        this.isDisabled = true;
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