package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

import javax.annotation.Nonnull;

public class TileEntityKilnBase extends TileEntityInventoryBase {

    public TileEntityKilnBase() {
        super(9);
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer player) {
        return null; //TODO
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return "atum:kiln";
    }
}