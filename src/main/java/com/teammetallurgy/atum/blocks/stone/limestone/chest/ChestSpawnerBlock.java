package com.teammetallurgy.atum.blocks.stone.limestone.chest;

import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.ChestSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class ChestSpawnerBlock extends ChestBaseBlock {

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new ChestSpawnerTileEntity();
    }
}