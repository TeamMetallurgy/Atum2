package com.teammetallurgy.atum.blocks.stone.limestone.chest;

import com.teammetallurgy.atum.blocks.base.BlockChestBase;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntityLimestoneChest;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLimestoneChest extends BlockChestBase {

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityLimestoneChest();
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "chest");
        OreDictHelper.add(this, "chestStone");
    }
}
