package com.teammetallurgy.atum.blocks.stone.limestone.chest;

import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.LimestoneChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LimestoneChestBlock extends ChestBaseBlock {

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new LimestoneChestTileEntity();
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "chest");
        OreDictHelper.add(this, "chestStone");
    }
}
