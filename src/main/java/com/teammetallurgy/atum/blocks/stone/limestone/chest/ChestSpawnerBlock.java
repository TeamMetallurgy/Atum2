package com.teammetallurgy.atum.blocks.stone.limestone.chest;

import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.ChestSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ChestSpawnerBlock extends ChestBaseBlock {

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ChestSpawnerTileEntity();
    }
}