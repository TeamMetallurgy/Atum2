package com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class LimestoneChestTileEntity extends ChestBaseTileEntity {

    public LimestoneChestTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.LIMESTONE_CHEST.get(), pos, state, true, true, AtumBlocks.LIMESTONE_CHEST.get());
    }
}