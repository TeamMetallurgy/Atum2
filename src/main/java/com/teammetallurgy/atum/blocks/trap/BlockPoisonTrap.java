package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.PoisonTrapTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class BlockPoisonTrap extends BlockTrap {

    @Override
    @Nullable
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new PoisonTrapTileEntity();
    }
}
