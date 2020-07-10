package com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumTileEntities;

public class LimestoneChestTileEntity extends ChestBaseTileEntity {

    public LimestoneChestTileEntity() {
        super(AtumTileEntities.LIMESTONE_CHEST, true, true, AtumBlocks.LIMESTONE_CHEST);
    }
}