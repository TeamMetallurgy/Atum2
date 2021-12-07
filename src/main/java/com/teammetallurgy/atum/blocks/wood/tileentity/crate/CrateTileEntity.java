package com.teammetallurgy.atum.blocks.wood.tileentity.crate;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class CrateTileEntity extends ChestBlockEntity {

    public CrateTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.CRATE.get(), pos, state);
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory) {
        return ChestMenu.threeRows(windowID, playerInventory, this);
    }

    @Override
    @Nonnull
    public Component getDefaultName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }
}