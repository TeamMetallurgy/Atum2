package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.blocks.machines.tileentity.GlassblowerFurnaceTileEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class GlassblowerFurnace extends AbstractFurnaceBlock {

    public GlassblowerFurnace() {
        super(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(s -> s.getValue(BlockStateProperties.LIT) ? 13 : 0));
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockGetter world) {
        return new GlassblowerFurnaceTileEntity();
    }

    @Override
    protected void openContainer(Level world, @Nonnull BlockPos pos, @Nonnull Player player) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof GlassblowerFurnaceTileEntity) {
            player.openMenu((MenuProvider) tileEntity);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }
}