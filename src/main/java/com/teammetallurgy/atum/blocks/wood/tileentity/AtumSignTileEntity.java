package com.teammetallurgy.atum.blocks.wood.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class AtumSignTileEntity extends SignBlockEntity {

    public AtumSignTileEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    @Nonnull
    public BlockEntityType<?> getType() {
        return AtumTileEntities.SIGN.get();
    }
}