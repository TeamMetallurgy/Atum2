package com.teammetallurgy.atum.blocks.curio.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.network.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class CurioDisplayTileEntity extends InventoryBaseTileEntity {

    public CurioDisplayTileEntity(BlockEntityType<? extends CurioDisplayTileEntity> tileEntityType, BlockPos pos, BlockState state) {
        super(tileEntityType, pos, state, 1);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory player) {
        return null;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(manager, packet);
        if (packet.getTag() != null) {
            this.load(packet.getTag());
            this.setChanged();
        }
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
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
