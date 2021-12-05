package com.teammetallurgy.atum.blocks.curio.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.network.NetworkHandler;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;

public class CurioDisplayTileEntity extends InventoryBaseTileEntity {

    public CurioDisplayTileEntity(BlockEntityType<? extends CurioDisplayTileEntity> tileEntityType) {
        super(tileEntityType, 1);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory player) {
        return null;
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

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level instanceof ServerLevel) {
            final Packet<?> packet = this.getUpdatePacket();
            NetworkHandler.sendToTracking((ServerLevel) this.level, this.worldPosition, packet, false);
        }
    }
}
