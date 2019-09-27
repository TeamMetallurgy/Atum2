package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenBonusCrate extends WorldGenerator {

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        for (BlockState state = world.getBlockState(pos); (state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos)) && pos.getY() > 1; state = world.getBlockState(pos)) {
            pos = pos.down();
        }
        if (pos.getY() < 1) {
            return false;
        } else {
            pos = pos.up();
            for (int i = 0; i < 4; ++i) {
                BlockPos posRand = pos.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(3) - rand.nextInt(3), rand.nextInt(4) - rand.nextInt(4));
                if (world.isAirBlock(posRand) && world.getBlockState(posRand.down()).isSideSolid(world, posRand.down(), net.minecraft.util.Direction.UP)) {
                    world.setBlockState(posRand, BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState(), 2);
                    TileEntity tileEntity = world.getTileEntity(posRand);

                    if (tileEntity instanceof CrateTileEntity) {
                        ((CrateTileEntity) tileEntity).setLootTable(AtumLootTables.CRATE_BONUS, rand.nextLong());
                    }
                    BlockPos posEast = posRand.east();
                    BlockPos posWest = posRand.west();
                    BlockPos posNorth = posRand.north();
                    BlockPos posSouth = posRand.south();

                    if (world.isAirBlock(posWest) && world.getBlockState(posWest.down()).isSideSolid(world, posWest.down(), Direction.UP)) {
                        world.setBlockState(posWest, AtumBlocks.DEADWOOD_TORCH.getDefaultState(), 2);
                    }
                    if (world.isAirBlock(posEast) && world.getBlockState(posEast.down()).isSideSolid(world, posEast.down(), Direction.UP)) {
                        world.setBlockState(posEast, AtumBlocks.DEADWOOD_TORCH.getDefaultState(), 2);
                    }
                    if (world.isAirBlock(posNorth) && world.getBlockState(posNorth.down()).isSideSolid(world, posNorth.down(), Direction.UP)) {
                        world.setBlockState(posNorth, AtumBlocks.DEADWOOD_TORCH.getDefaultState(), 2);
                    }
                    if (world.isAirBlock(posSouth) && world.getBlockState(posSouth.down()).isSideSolid(world, posSouth.down(), Direction.UP)) {
                        world.setBlockState(posSouth, AtumBlocks.DEADWOOD_TORCH.getDefaultState(), 2);
                    }
                    return true;
                }
            }
            return false;
        }
    }
}