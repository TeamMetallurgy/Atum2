package com.teammetallurgy.atum.blocks.curio.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AcaciaCurioDisplayTileEntity extends CurioDisplayTileEntity {

    public AcaciaCurioDisplayTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.ACACIA_CURIO_DISPLAY.get());
    }
}