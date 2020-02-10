package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.SmokeTrapTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class BlockSmokeTrap extends BlockTrap {

    @Override
    @Nullable
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new SmokeTrapTileEntity();
    }
}