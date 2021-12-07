package com.teammetallurgy.atum.blocks.curio.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PalmCurioDisplayTileEntity extends CurioDisplayTileEntity {

    public PalmCurioDisplayTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.PALM_CURIO_DISPLAY.get());
    }
}