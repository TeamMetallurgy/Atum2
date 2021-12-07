package com.teammetallurgy.atum.blocks.stone.limestone.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.FurnaceBaseTileEntity;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class LimestoneFurnaceTileEntity extends FurnaceBaseTileEntity {

    public LimestoneFurnaceTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.LIMESTONE_FURNACE.get(), RecipeType.SMELTING, 3);
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory) {
        return new FurnaceMenu(windowID, playerInventory, this, this.dataAccess);
    }
}