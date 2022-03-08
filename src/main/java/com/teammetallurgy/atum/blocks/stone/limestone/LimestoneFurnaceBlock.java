package com.teammetallurgy.atum.blocks.stone.limestone;

import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.LimestoneFurnaceTileEntity;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LimestoneFurnaceBlock extends FurnaceBlock {

    public LimestoneFurnaceBlock() {
        super(Block.Properties.of(Material.STONE).strength(3.5F).lightLevel(s -> s.getValue(BlockStateProperties.LIT) ? 13 : 0));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> blockEntityType) {
        return createFurnaceTicker(level, blockEntityType, AtumTileEntities.LIMESTONE_FURNACE.get());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return AtumTileEntities.LIMESTONE_FURNACE.get().create(pos, state);
    }

    @Override
    protected void openContainer(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof LimestoneFurnaceTileEntity) {
            player.openMenu((MenuProvider) tileEntity);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }
}