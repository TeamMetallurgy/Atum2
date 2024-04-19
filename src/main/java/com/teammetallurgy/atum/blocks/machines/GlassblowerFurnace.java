package com.teammetallurgy.atum.blocks.machines;

import com.mojang.serialization.MapCodec;
import com.teammetallurgy.atum.blocks.machines.tileentity.GlassblowerFurnaceTileEntity;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nonnull;

public class GlassblowerFurnace extends AbstractFurnaceBlock {
    public static final MapCodec<GlassblowerFurnace> CODEC = simpleCodec(GlassblowerFurnace::new);

    public GlassblowerFurnace(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    protected MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return AtumTileEntities.GLASSBLOWER_FURNACE.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, AtumTileEntities.GLASSBLOWER_FURNACE.get(), GlassblowerFurnaceTileEntity::serverTick);
    }

    @Override
    protected void openContainer(Level level, @Nonnull BlockPos pos, @Nonnull Player player) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof GlassblowerFurnaceTileEntity) {
            player.openMenu((MenuProvider) tileEntity);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }
}