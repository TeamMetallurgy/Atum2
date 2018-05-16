package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.blocks.tileentity.chests.TileEntityChestSpawner;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenRuins extends WorldGenerator {

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        //Debug code
        //System.out.println("Set chest: " + pos);
        world.setBlockState(pos, AtumBlocks.CHEST_SPAWNER.correctFacing(world, pos, AtumBlocks.CHEST_SPAWNER.getDefaultState()), 2);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityChestSpawner) {
            ((TileEntityChestSpawner) tileEntity).setLootTable(AtumLootTables.RUINS, random.nextLong());
        }
        return false;
    }
}