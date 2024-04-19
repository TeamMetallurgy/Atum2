package com.teammetallurgy.atum.blocks.trap;

import com.mojang.serialization.MapCodec;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.blocks.trap.tileentity.ArrowTrapTileEntity;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArrowTrapBlock extends TrapBlock {
    public static final MapCodec<ArrowTrapBlock> CODEC = simpleCodec(ArrowTrapBlock::new);

    public ArrowTrapBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    protected MapCodec<? extends ArrowTrapBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return getTrapBlockEntityType().create(pos, state);
    }

    @Override
    public <E extends BlockEntity> BlockEntityTicker<E> getTrapTickerHelper(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<E> blockEntityType) {
        return createTickerHelper(blockEntityType, getTrapBlockEntityType(), ArrowTrapTileEntity::arrowTrackServerTick);
    }

    @Override
    public BlockEntityType<ArrowTrapTileEntity> getTrapBlockEntityType() {
        return AtumTileEntities.ARROW_TRAP.get();
    }
}