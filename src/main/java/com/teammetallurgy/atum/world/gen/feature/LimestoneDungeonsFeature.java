package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class LimestoneDungeonsFeature extends Feature<NoneFeatureConfiguration> {
    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();

    public LimestoneDungeonsFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> placeContext) {
        Predicate<BlockState> predicate = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
        WorldGenLevel genLevel = placeContext.level();
        RandomSource random = placeContext.random();
        BlockPos pos = placeContext.origin();

        int j = random.nextInt(2) + 2;
        int k = -j - 1;
        int l = j + 1;
        int k1 = random.nextInt(2) + 2;
        int l1 = -k1 - 1;
        int i2 = k1 + 1;
        int j2 = 0;

        for (int x = k; x <= l; ++x) {
            for (int y = -1; y <= 4; ++y) {
                for (int z = l1; z <= i2; ++z) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    boolean isSolid = genLevel.getBlockState(checkPos).isSolid();
                    if (y == -1 && !isSolid) {
                        return false;
                    }

                    if (y == 4 && !isSolid) {
                        return false;
                    }

                    if ((x == k || x == l || z == l1 || z == i2) && y == 0 && genLevel.isEmptyBlock(checkPos) && genLevel.isEmptyBlock(checkPos.above())) {
                        ++j2;
                    }
                }
            }
        }

        if (j2 >= 1 && j2 <= 5) {
            for (int k3 = k; k3 <= l; ++k3) {
                for (int i4 = 3; i4 >= -1; --i4) {
                    for (int k4 = l1; k4 <= i2; ++k4) {
                        BlockPos genPos = pos.offset(k3, i4, k4);
                        BlockState blockstate = genLevel.getBlockState(genPos);
                        if (k3 != k && i4 != -1 && k4 != l1 && k3 != l && i4 != 4 && k4 != i2) {
                            if (blockstate.getBlock() != AtumBlocks.DEADWOOD_CRATE.get() && !blockstate.is(Blocks.SPAWNER)) {
                                genLevel.setBlock(genPos, CAVE_AIR, 2);
                            }
                        } else if (genPos.getY() >= genLevel.getMinBuildHeight() && !genLevel.getBlockState(genPos.below()).isSolid()) {
                            genLevel.setBlock(genPos, CAVE_AIR, 2);
                        } else if (blockstate.isSolid() && genLevel.getBlockState(genPos).getBlock() != AtumBlocks.DEADWOOD_CRATE.get()) {
                            if (i4 == -1 && random.nextInt(4) != 0) {
                                genLevel.setBlock(genPos, AtumBlocks.LIMESTONE_BRICK_CRACKED_BRICK.get().defaultBlockState(), 2);
                            } else {
                                genLevel.setBlock(genPos, AtumBlocks.LIMESTONE_BRICK_LARGE.get().defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }

            for (int l3 = 0; l3 < 2; ++l3) {
                for (int j4 = 0; j4 < 3; ++j4) {
                    int x = pos.getX() + random.nextInt(j * 2 + 1) - j;
                    int y = pos.getY();
                    int z = pos.getZ() + random.nextInt(k1 * 2 + 1) - k1;
                    BlockPos cratePos = new BlockPos(x, y, z);
                    if (genLevel.isEmptyBlock(cratePos)) {
                        int j3 = 0;

                        for (Direction direction : Direction.Plane.HORIZONTAL) {
                            if (genLevel.getBlockState(cratePos.relative(direction)).isSolid()) {
                                ++j3;
                            }
                        }
                        if (j3 == 1) {
                            this.safeSetBlock(genLevel, cratePos, ChestBaseBlock.correctFacing(genLevel, cratePos, AtumBlocks.DEADWOOD_CRATE.get().defaultBlockState(), AtumBlocks.DEADWOOD_CRATE.get()), predicate);
                            RandomizableContainerBlockEntity.setBlockEntityLootTable(genLevel, random, cratePos, AtumLootTables.CRATE);
                            break;
                        }
                    }
                }
            }

            this.safeSetBlock(genLevel, pos, Blocks.SPAWNER.defaultBlockState(), predicate);
            BlockEntity tileEntity = genLevel.getBlockEntity(pos);
            if (tileEntity instanceof SpawnerBlockEntity) {
                ((SpawnerBlockEntity) tileEntity).setEntityId(this.getRandomDungeonMob(random), random);
            } else {
                Atum.LOG.error("Failed to fetch mob spawner entity at ({}, {}, {})", pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        } else {
            return false;
        }
    }

    private EntityType<?> getRandomDungeonMob(RandomSource random) {
        int chance = Mth.nextInt(random, 0, 5);
        switch (chance) {
            case 0:
                return AtumEntities.WRAITH.get();
            case 1:
                return AtumEntities.STONEGUARD.get();
            case 2:
                return AtumEntities.MUMMY.get();
            case 3:
                return AtumEntities.BONESTORM.get();
            case 4:
                return AtumEntities.TARANTULA.get();
            default:
            case 5:
                return AtumEntities.FORSAKEN.get();
        }
    }
}