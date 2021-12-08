package com.teammetallurgy.atum.blocks.curio.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AlabasterCurioDisplayTileEntity extends CurioDisplayTileEntity {

    public AlabasterCurioDisplayTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.ALABASTER_CURIO_DISPLAY.get(), pos, state);
    }
}