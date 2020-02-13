package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.BurningTrapTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class BurningTrapBlock extends TrapBlock {

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new BurningTrapTileEntity();
    }
}