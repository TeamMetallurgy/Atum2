package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.blocks.machines.tileentity.GlassblowerFurnaceTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class GlassblowerFurnace extends AbstractFurnaceBlock {

    public GlassblowerFurnace() {
        super(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.5F).setLightLevel(s -> s.get(BlockStateProperties.LIT) ? 13 : 0));
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
        return new GlassblowerFurnaceTileEntity();
    }

    @Override
    protected void interactWith(World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof GlassblowerFurnaceTileEntity) {
            player.openContainer((INamedContainerProvider) tileEntity);
            player.addStat(Stats.INTERACT_WITH_FURNACE);
        }
    }
}