package com.teammetallurgy.atum.blocks.base.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class ChestBaseTileEntity extends ChestBlockEntity {
    public boolean canBeSingle;
    public boolean canBeDouble;

    public ChestBaseTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean canBeSingle, boolean canBeDouble) {
        super(type, pos, state);
        this.canBeSingle = canBeSingle;
        this.canBeDouble = canBeDouble;
    }

    @Override
    @Nonnull
    protected Component getDefaultName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }
}