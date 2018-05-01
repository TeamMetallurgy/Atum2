package com.teammetallurgy.atum.world.decorators;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenRuins extends WorldGenerator { //TODO figure out what this is based on.

    /*private final static int AVG_WIDTH = 9;
    private final static int AVG_DEPTH = 7;
    private final static int VARIATION = 3;

    private final static int BUILDING_CHANCE = 3;    // 1 in 3 ruins will be a full building
    private final static int CHEST_CHANCE = 5;       // 1 in 5 buildings will house a chest*/

    @Override
    public boolean generate(World world, Random random, BlockPos pos) { //TODO Make proper port to 1.9. Talk to Allaryin
        /*int width = random.nextInt(AVG_WIDTH - VARIATION) + VARIATION;
        int depth = random.nextInt(AVG_DEPTH - VARIATION) + VARIATION;
        int x = pos.getX();
        int z = pos.getZ();
        int height = world.getHeight(x, z);
        int x2;
        int z2;
        if (world.getHeight(x + width, z + depth) >= height) {
            x2 = x + width;
            z2 = z + depth;
        } else if (world.getHeight(x - width, z + depth) >= height) {
            x2 = x - width;
            z2 = z + depth;
        } else if (world.getHeight(x + width, z - depth) >= height) {
            x2 = x + width;
            z2 = z - depth;
        } else {
            if (world.getHeight(x - width, z - depth) < height) {
                return false;
            }

            x2 = x - width;
            z2 = z - depth;
        }

        // are we going to make a building?
        final boolean building;
        if (!(building = (random.nextInt(BUILDING_CHANCE) == 0))) {
            // no, we're just making a wall
            if (random.nextBoolean()) {
                x2 = x;
            } else {
                z2 = z;
            }
        }

        int chestX;
        int chestZ;
        int chestY;
        for (chestX = Math.min(x2, x); chestX <= Math.max(x2, x); ++chestX) {
            for (chestZ = Math.min(z2, z); chestZ <= Math.max(z2, z); ++chestZ) {
                int wallHeight = random.nextInt(4);

                for (chestY = -1; chestY < 15; ++chestY) {
                    if (chestX != x2 && chestZ != z2 && chestX != x && chestZ != z && chestY != -1) {
                        world.setBlockToAir(chestX, chestY + height, chestZ);
                    } else if (chestY < wallHeight) {
                        if ((double) random.nextFloat() > 0.1D) {
                            world.setBlockState(chestX, chestY + height, chestZ, AtumBlocks.LIMESTONEBRICK.getDefaultState().withProperty(BlockLimestoneBricks.VARIANT, BlockLimestoneBricks.EnumType.LARGE));
                        } else {
                            world.setBlockState(chestX, chestY + height, chestZ, AtumBlocks.LIMESTONEBRICK.getDefaultState().withProperty(BlockLimestoneBricks.VARIANT, BlockLimestoneBricks.EnumType.SMALL));
                        }
                    } else if (chestY == wallHeight && (double) random.nextFloat() > 0.7D) {
                        if ((double) random.nextFloat() > 0.1D) {
                            world.setBlockState(chestX, chestY + height, chestZ, AtumBlocks.SLABS, 2, 0);
                        } else {
                            world.setBlockState(chestX, chestY + height, chestZ, AtumBlocks.SLABS, 3, 0);
                        }
                    }
                }
            }
        }

        if (building && random.nextInt(CHEST_CHANCE) == 0) {
            chestX = width / 2 + x;
            chestZ = Math.max(z2, z) - 1;
            boolean var16 = false;
            if ((double) random.nextFloat() > 0.5D) {
                chestX = random.nextInt(width - 1) + 1 + Math.min(x, x2);
                if ((double) random.nextFloat() > 0.5D) {
                    chestZ = Math.max(z2, z) - 1;
                    var16 = true;
                } else {
                    chestZ = Math.min(z2, z) + 1;
                    var16 = true;
                }
            } else {
                chestZ = random.nextInt(depth - 1) + 1 + Math.min(z, z2);
                if ((double) random.nextFloat() > 0.5D) {
                    chestX = Math.max(x2, x) - 1;
                    var16 = true;
                } else {
                    chestX = Math.min(x2, x) + 1;
                    var16 = true;
                }
            }

            chestY = world.getHeight(pos.add(chestX, 0, chestZ));
            world.setBlockState(new BlockPos(chestX, chestY, chestZ), AtumBlocks.CURSED_CHEST.getDefaultState(), 2);
            IInventory chest = (IInventory) world.getTileEntity(new BlockPos(chestX, chestY, chestZ));
            AtumLoot.fillChest(chest, 5, 0.5F);
        }*/

        // TODO: pour sand into the building

        return false;
    }
}