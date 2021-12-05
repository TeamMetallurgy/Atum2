package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.BurningTrapTileEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nonnull;

public class BurningTrapBlock extends TrapBlock {

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockGetter reader) {
        return new BurningTrapTileEntity();
    }
}