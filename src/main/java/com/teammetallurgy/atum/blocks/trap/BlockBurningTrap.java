package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.BurningTrapTileEntity;
import net.minecraft.tileentity.TileEntity;

public class BlockBurningTrap extends BlockTrap {

    @Override
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new BurningTrapTileEntity();
    }
}