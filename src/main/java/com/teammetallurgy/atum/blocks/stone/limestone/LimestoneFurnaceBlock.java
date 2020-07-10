package com.teammetallurgy.atum.blocks.stone.limestone;

import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.LimestoneFurnaceTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LimestoneFurnaceBlock extends FurnaceBlock {

    public LimestoneFurnaceBlock() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new LimestoneFurnaceTileEntity();
    }

    @Override
    protected void interactWith(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof LimestoneFurnaceTileEntity) {
            player.openContainer((INamedContainerProvider) tileEntity);
            player.addStat(Stats.INTERACT_WITH_FURNACE);
        }
    }
}