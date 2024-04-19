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

public class BurningTrapBlock extends TrapBlock {
    public static final MapCodec<BurningTrapBlock> CODEC = simpleCodec(BurningTrapBlock::new);

    public BurningTrapBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    protected MapCodec<? extends BurningTrapBlock> codec() {
        return CODEC;
    }
    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return getTrapBlockEntityType().create(pos, state);
    }

    @Override
    public BlockEntityType<? extends TrapTileEntity> getTrapBlockEntityType() {
        return AtumTileEntities.BURNING_TRAP.get();
    }
}