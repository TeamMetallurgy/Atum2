package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Random;

public class WorldGenPyramid extends WorldGenerator {
    private static final ResourceLocation PYRAMID = new ResourceLocation(Constants.MOD_ID, "pyramid");

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        BlockPos basePos = new BlockPos(chunkPos.x * 16, 32, chunkPos.z * 16);
        final PlacementSettings settings = new PlacementSettings().setRotation(Rotation.NONE);
        final Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), PYRAMID);

        while (basePos.getY() > 1 && world.isAirBlock(basePos)) {
            basePos = basePos.down();
        }

        basePos = basePos.down(MathHelper.getInt(random, 15, 60));

        if (world.isAreaLoaded(basePos, 32)) return false;

        System.out.println("Pyramid spawned at: " + basePos);
        template.addBlocksToWorld(world, basePos, settings);

        /*if (random.nextFloat() > 0.3) {
            j -= 8;
        }

        int width = 17;
        int depth = 17;

        boolean[][] maze = new boolean[17][17];

        int zIn = 9;

        maze[0][zIn] = true;
        generateMaze(maze, random, 1, zIn);

        for (int y = -6; y < 10; y++) {
            for (int x = y; x <= width - y; x++) {
                for (int z = y; z <= depth - y; z++) {
                    Block block = world.getBlockState(x + i, y + j + 3, z + k);
                    if (block == AtumBlocks.SAND) {
                        world.setBlockToAir(x + i, y + j + 3, z + k);
                    }
                    world.setBlock(x + i, y + j + 3, z + k, AtumBlocks.LARGEBRICK);
                }
            }
        }

        for (int x = -3; x < width + 3; x++) {
            for (int z = -3; z < depth + 3; z++) {
                if (x >= 0 && x < width && z >= 0 && z < depth) {
                    world.setBlockToAir(x + i, j, z + k);
                    world.setBlock(x + i, j - 1, z + k, AtumBlocks.LIMESTONE);
                    if (!maze[x][z]) {
                        if (random.nextFloat() > 0.1F) {
                            world.setBlock(x + i, j, z + k, AtumBlocks.LARGEBRICK);
                            Block temp = world.getBlock(x + i, j + 1, z + k);
                            if (temp != null) {
                                temp.setUnbreakable();
                            }
                        } else
                            placeTrap(world, x + i, j, z + k);
                        world.setBlock(x + i, j + 1, z + k, AtumBlocks.LARGEBRICK);
                        Block temp = world.getBlock(x + i, j + 1, z + k);
                        if (temp != null) {
                            temp.setUnbreakable();
                        }

                        world.setBlock(x + i, j + 2, z + k, AtumBlocks.LARGEBRICK);
                        temp = world.getBlock(x + i, j + 2, z + k);
                        if (temp != null) {
                            temp.setUnbreakable();
                        }
                    } else {
                        int meta = random.nextInt(5);
                        world.setBlock(x + i, j, z + k, AtumBlocks.SANDLAYERED, meta, 0);
                        world.setBlockToAir(x + i, j + 1, z + k);
                        world.setBlockToAir(x + i, j + 2, z + k);
                    }
                    world.setBlock(x + i, j + 3, z + k, AtumBlocks.LARGEBRICK);
                    Block temp = world.getBlock(x + i, j + 3, z + k);
                    if (temp != null) {
                        temp.setUnbreakable();
                    }
                }
            }
        }

        world.setBlockToAir(i - 1, j, k + zIn);
        world.setBlockToAir(i - 1, j + 1, k + zIn);
        world.setBlockToAir(i - 2, j, k + zIn);
        world.setBlockToAir(i - 2, j + 1, k + zIn);
        world.setBlockToAir(i - 3, j, k + zIn);
        world.setBlockToAir(i - 3, j + 1, k + zIn);
        world.setBlockToAir(i - 4, j, k + zIn);
        world.setBlockToAir(i - 4, j + 1, k + zIn);

        for (int y = 4; y < 8; y++) {
            for (int x = 6; x < 12; x++) {
                for (int z = 6; z < 12; z++) {
                    world.setBlockToAir(i + x, j + y, k + z);
                }
            }
        }

        world.setBlock(i + 11, j + 6, k + 7, Blocks.torch, 2, 0);
        world.setBlock(i + 11, j + 6, k + 10, Blocks.torch, 2, 0);

        world.setBlock(i + 10, j + 4, k + 8, AtumBlocks.PHARAOHCHEST, 0, 2);
        try {
            TileEntitySarcophagus te = (TileEntitySarcophagus) world.getTileEntity(i + 10, j + 4, k + 8);
            AtumLoot.fillChest(te, 15, 0.9f);
        } catch (ClassCastException e) {
        }
        if (world.isAirBlock(i + 7, j + 1, k + 7)) {
            placeLadders(world, i + 7, j, k + 7, 4);
        } else {
            boolean found = false;
            for (int dx = -1; dx <= 1; dx++) {
                if (found)
                    break;

                for (int dz = -1; dz <= 1; dz++) {
                    if (world.isAirBlock(i + 7 + dx, j + 1, k + 7 + dz)) {
                        placeLadders(world, i + 7 + dx, j, k + 7 + dz, 3);
                        found = true;
                        break;
                    }
                }
            }
        }*/

        return false;
    }

    private void generateMaze(boolean[][] array, Random random, int x, int y) {
        ArrayList<Pair> choices = new ArrayList<>();
        do {
            choices.clear();
            if (x + 2 < 16 && !array[x + 2][y])
                choices.add(new Pair(2, 0));
            if (x - 2 >= 0 && !array[x - 2][y])
                choices.add(new Pair(-2, 0));
            if (y + 2 < 16 && !array[x][y + 2])
                choices.add(new Pair(0, 2));
            if (y - 2 >= 0 && !array[x][y - 2])
                choices.add(new Pair(0, -2));

            if (choices.size() > 0) {
                int i = random.nextInt(choices.size());
                Pair choice = choices.get(i);
                choices.remove(i);
                array[choice.x + x][choice.y + y] = true;
                array[x + choice.x / 2][y + choice.y / 2] = true;
                generateMaze(array, random, x + choice.x, y + choice.y);
            }

        } while (choices.size() > 0);
    }

    class Pair {
        public int x;
        public int y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object p) {
            return p instanceof Pair && ((Pair) p).x == x && ((Pair) p).y == y;
        }
    }
}