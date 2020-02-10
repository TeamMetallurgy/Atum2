package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.TarTrapTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class BlockTarTrap extends BlockTrap {

    @Override
    @Nullable
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new TarTrapTileEntity();
    }
}