package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.SmokeTrapTileEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SmokeTrapBlock extends TrapBlock {

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockGetter reader) {
        return new SmokeTrapTileEntity();
    }
}