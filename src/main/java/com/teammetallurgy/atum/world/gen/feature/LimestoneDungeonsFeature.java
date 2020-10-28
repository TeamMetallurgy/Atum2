package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class LimestoneDungeonsFeature extends Feature<NoFeatureConfig> {
    private static final EntityType<?>[] SPAWNERTYPES = new EntityType[]{EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER};
    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();

    public LimestoneDungeonsFeature(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(@Nonnull ISeedReader seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        int j = rand.nextInt(2) + 2;
        int k = -j - 1;
        int l = j + 1;
        int k1 = rand.nextInt(2) + 2;
        int l1 = -k1 - 1;
        int i2 = k1 + 1;
        int j2 = 0;

        for (int x = k; x <= l; ++x) {
            for (int y = -1; y <= 4; ++y) {
                for (int z = l1; z <= i2; ++z) {
                    BlockPos checkPos = pos.add(x, y, z);
                    Material material = seedReader.getBlockState(checkPos).getMaterial();
                    boolean isSolid = material.isSolid();
                    if (y == -1 && !isSolid) {
                        return false;
                    }

                    if (y == 4 && !isSolid) {
                        return false;
                    }

                    if ((x == k || x == l || z == l1 || z == i2) && y == 0 && seedReader.isAirBlock(checkPos) && seedReader.isAirBlock(checkPos.up())) {
                        ++j2;
                    }
                }
            }
        }

        if (j2 >= 1 && j2 <= 5) {
            for (int k3 = k; k3 <= l; ++k3) {
                for (int i4 = 3; i4 >= -1; --i4) {
                    for (int k4 = l1; k4 <= i2; ++k4) {
                        BlockPos genPos = pos.add(k3, i4, k4);
                        if (k3 != k && i4 != -1 && k4 != l1 && k3 != l && i4 != 4 && k4 != i2) {
                            if (seedReader.getBlockState(genPos).getBlock() != AtumBlocks.DEADWOOD_CRATE) {
                                seedReader.setBlockState(genPos, CAVE_AIR, 2);
                            }
                        } else if (genPos.getY() >= 0 && !seedReader.getBlockState(genPos.down()).getMaterial().isSolid()) {
                            seedReader.setBlockState(genPos, CAVE_AIR, 2);
                        } else if (seedReader.getBlockState(genPos).getMaterial().isSolid() && seedReader.getBlockState(genPos).getBlock() != AtumBlocks.DEADWOOD_CRATE) {
                            if (i4 == -1 && rand.nextInt(4) != 0) {
                                seedReader.setBlockState(genPos, AtumBlocks.LIMESTONE_BRICK_CRACKED_BRICK.getDefaultState(), 2);
                            } else {
                                seedReader.setBlockState(genPos, AtumBlocks.LIMESTONE_BRICK_LARGE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            for (int l3 = 0; l3 < 2; ++l3) {
                for (int j4 = 0; j4 < 3; ++j4) {
                    int x = pos.getX() + rand.nextInt(j * 2 + 1) - j;
                    int y = pos.getY();
                    int z = pos.getZ() + rand.nextInt(k1 * 2 + 1) - k1;
                    BlockPos cratePos = new BlockPos(x, y, z);
                    if (seedReader.isAirBlock(cratePos)) {
                        int j3 = 0;

                        for (Direction direction : Direction.Plane.HORIZONTAL) {
                            if (seedReader.getBlockState(cratePos.offset(direction)).getMaterial().isSolid()) {
                                ++j3;
                            }
                        }
                        if (j3 == 1) {
                            seedReader.setBlockState(cratePos, ChestBaseBlock.correctFacing(seedReader, cratePos, AtumBlocks.DEADWOOD_CRATE.getDefaultState(), AtumBlocks.DEADWOOD_CRATE), 2);
                            LockableLootTileEntity.setLootTable(seedReader, rand, cratePos, AtumLootTables.CRATE);
                            break;
                        }
                    }
                }
            }

            seedReader.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 2);
            TileEntity tileEntity = seedReader.getTileEntity(pos);
            if (tileEntity instanceof MobSpawnerTileEntity) {
                ((MobSpawnerTileEntity) tileEntity).getSpawnerBaseLogic().setEntityType(this.getRandomDungeonMob(rand));
            } else {
                Atum.LOG.error("Failed to fetch mob spawner entity at ({}, {}, {})", pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        } else {
            return false;
        }
    }

    private EntityType<?> getRandomDungeonMob(Random rand) {
        int chance = MathHelper.nextInt(rand, 0, 5);
        switch (chance) {
            case 0:
                return AtumEntities.WRAITH;
            case 1:
                return AtumEntities.STONEGUARD;
            case 2:
                return AtumEntities.MUMMY;
            case 3:
                return AtumEntities.BONESTORM;
            case 4:
                return AtumEntities.TARANTULA;
            default:
            case 5:
                return AtumEntities.FORSAKEN;
        }
    }
}