package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class GodforgeTileEntity extends InventoryBaseTileEntity {

    public GodforgeTileEntity() {
        super(AtumTileEntities.GODFORGE, 3);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }
}
