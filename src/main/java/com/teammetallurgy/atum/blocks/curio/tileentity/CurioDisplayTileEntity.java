package com.teammetallurgy.atum.blocks.curio.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.network.NetworkHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class CurioDisplayTileEntity extends InventoryBaseTileEntity {

    public CurioDisplayTileEntity(TileEntityType<? extends CurioDisplayTileEntity> tileEntityType) {
        super(tileEntityType, 1);
    }

    @Override
    protected Container createMenu(int id, @Nonnull PlayerInventory player) {
        return null;
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

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.world instanceof ServerWorld) {
            final IPacket<?> packet = this.getUpdatePacket();
            NetworkHandler.sendToTracking((ServerWorld) this.world, this.pos, packet, false);
        }
    }
}
