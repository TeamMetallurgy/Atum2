package com.teammetallurgy.atum.blocks.trap;

import com.mojang.serialization.MapCodec;
import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PoisonTrapBlock extends TrapBlock {
    public static final MapCodec<PoisonTrapBlock> CODEC = simpleCodec(PoisonTrapBlock::new);

    public PoisonTrapBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    protected MapCodec<? extends PoisonTrapBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return getTrapBlockEntityType().create(pos, state);
    }

    @Override
    public BlockEntityType<? extends TrapTileEntity> getTrapBlockEntityType() {
        return AtumTileEntities.POISON_TRAP.get();
    }
}