package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.ArrowTrapTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ArrowTrapBlock extends TrapBlock {

    @Override
    @Nullable
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new ArrowTrapTileEntity();
    }
}