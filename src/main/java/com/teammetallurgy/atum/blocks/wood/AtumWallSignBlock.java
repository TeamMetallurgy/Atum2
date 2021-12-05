package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class AtumWallSignBlock extends WallSignBlock {
    public static final HashMap<Block, Block> WALL_SIGN_BLOCKS = new HashMap<>();

    public AtumWallSignBlock(Properties properties, WoodType type) {
        super(properties, type);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
        return AtumTileEntities.SIGN.create();
    }
}
