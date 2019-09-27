package com.teammetallurgy.atum.blocks.trap;

import com.teammetallurgy.atum.blocks.trap.tileentity.BurningTrapTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockBurningTrap extends BlockTrap {

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new BurningTrapTileEntity();
    }
}