package com.teammetallurgy.atum.blocks.stone.limestone.chest;

import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.LimestoneChestTileEntity;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;

public class LimestoneChestBlock extends ChestBaseBlock {

    public LimestoneChestBlock() {
        super(() -> AtumTileEntities.LIMESTONE_CHEST);
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockGetter reader) {
        return new LimestoneChestTileEntity();
    }
}