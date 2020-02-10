package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.ArrowTrapTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class BlockArrowTrap extends BlockTrap {

    @Override
    @Nullable
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new ArrowTrapTileEntity();
    }
}