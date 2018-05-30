package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.blocks.tileentity.TileEntityBurningTrap;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockBurningTrap extends BlockContainer {

    public BlockBurningTrap() {
        super(Material.ROCK);
        this.setHardness(1.5F);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileEntityBurningTrap();
    }
}