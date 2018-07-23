package com.teammetallurgy.atum.blocks.limestone.chest;

import com.teammetallurgy.atum.blocks.base.BlockChestBase;
import com.teammetallurgy.atum.blocks.limestone.chest.tileentity.TileEntityChestSpawner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChestSpawner extends BlockChestBase {

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityChestSpawner();
    }
}