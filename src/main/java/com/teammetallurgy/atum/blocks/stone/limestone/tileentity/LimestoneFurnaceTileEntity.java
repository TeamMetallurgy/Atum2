package com.teammetallurgy.atum.blocks.stone.limestone.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.FurnaceBaseTileEntity;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nonnull;

public class LimestoneFurnaceTileEntity extends FurnaceBaseTileEntity {

    public LimestoneFurnaceTileEntity() {
        super(AtumTileEntities.LIMESTONE_FURNACE, RecipeType.SMELTING, 3);
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory) {
        return new FurnaceMenu(windowID, playerInventory, this, this.dataAccess);
    }
}