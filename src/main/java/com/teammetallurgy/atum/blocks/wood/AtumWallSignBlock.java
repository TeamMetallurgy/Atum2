package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nonnull;
import java.util.HashMap;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class AtumWallSignBlock extends WallSignBlock {
    public static final HashMap<Block, Block> WALL_SIGN_BLOCKS = new HashMap<>();

    public AtumWallSignBlock(Properties properties, WoodType type) {
        super(properties, type);
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockGetter world) {
        return AtumTileEntities.SIGN.create();
    }
}
