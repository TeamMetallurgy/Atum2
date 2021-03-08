package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.blocks.wood.tileentity.CurioDisplayTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class CurioDisplayBlock extends ContainerBlock {

    public CurioDisplayBlock() {
        super(AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(1.5F, 1.0F).sound(SoundType.GLASS));
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
        return new CurioDisplayTileEntity();
    }
}