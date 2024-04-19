package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredBlock;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class AtumWallSignBlock extends WallSignBlock {
    public static final HashMap<DeferredBlock<Block>, DeferredBlock<Block>> WALL_SIGN_BLOCKS = new HashMap<>();

    public AtumWallSignBlock(Properties properties, WoodType type) {
        super(type, properties);
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return AtumTileEntities.SIGN.get().create(pos, state);
    }
}