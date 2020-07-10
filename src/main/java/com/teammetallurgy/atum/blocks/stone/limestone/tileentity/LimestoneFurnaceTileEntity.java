package com.teammetallurgy.atum.blocks.stone.limestone.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.FurnaceBaseTileEntity;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.item.crafting.IRecipeType;

import javax.annotation.Nonnull;

public class LimestoneFurnaceTileEntity extends FurnaceBaseTileEntity {

    public LimestoneFurnaceTileEntity() {
        super(AtumTileEntities.LIMESTONE_FURNACE, IRecipeType.SMELTING, 3);
    }

    @Override
    @Nonnull
    protected Container createMenu(int windowID, @Nonnull PlayerInventory playerInventory) {
        return new FurnaceContainer(windowID, playerInventory, this, this.furnaceData);
    }
}