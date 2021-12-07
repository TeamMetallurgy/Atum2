package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.BurningTrapTileEntity;
import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class BurningTrapBlock extends TrapBlock {

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new BurningTrapTileEntity(pos, state);
    }

    @Override
    public BlockEntityType<? extends TrapTileEntity> getTrapBlockEntityType() {
        return AtumTileEntities.BURNING_TRAP.get();
    }
}