package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.ArrowTrapTileEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArrowTrapBlock extends TrapBlock {

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockGetter reader) {
        return new ArrowTrapTileEntity();
    }
}