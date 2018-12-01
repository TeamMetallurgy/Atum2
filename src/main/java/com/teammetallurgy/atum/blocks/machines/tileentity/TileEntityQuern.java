package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityQuern extends TileEntityInventoryBase implements ITickable {
    public float rotation;

    public TileEntityQuern() {
        super(1);
    }

    @Override
    public void update() {
        rotation++;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return false;
    }

    // TODO Figure out what to do with these two
    @Override
    public Container createContainer(@Nonnull InventoryPlayer inventoryPlayer, @Nonnull EntityPlayer player) {
        return null;
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return "";
    }
}