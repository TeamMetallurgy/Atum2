package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.BlockCrate;
import com.teammetallurgy.atum.blocks.BlockLimestoneBricks;
import com.teammetallurgy.atum.blocks.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenAtumDungeons extends WorldGenerator {

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        int j = rand.nextInt(2) + 2;
        int k = -j - 1;
        int l = j + 1;
        int k1 = rand.nextInt(2) + 2;
        int l1 = -k1 - 1;
        int i2 = k1 + 1;
        int j2 = 0;

        for (int k2 = k; k2 <= l; ++k2) {
            for (int l2 = -1; l2 <= 4; ++l2) {
                for (int i3 = l1; i3 <= i2; ++i3) {
                    BlockPos airCheckPos = pos.add(k2, l2, i3);
                    Material material = world.getBlockState(airCheckPos).getMaterial();
                    boolean flag = material.isSolid();

                    if (l2 == -1 && !flag) {
                        return false;
                    }
                    if (l2 == 4 && !flag) {
                        return false;
                    }

                    if ((k2 == k || k2 == l || i3 == l1 || i3 == i2) && l2 == 0 && world.isAirBlock(airCheckPos) && world.isAirBlock(airCheckPos.up())) {
                        ++j2;
                    }
                }
            }
        }

        if (j2 >= 1 && j2 <= 5) {
            for (int k3 = k; k3 <= l; ++k3) {
                for (int i4 = 3; i4 >= -1; --i4) {
                    for (int k4 = l1; k4 <= i2; ++k4) {
                        BlockPos boxPos = pos.add(k3, i4, k4);

                        if (k3 != k && i4 != -1 && k4 != l1 && k3 != l && i4 != 4 && k4 != i2) {
                            if (world.getBlockState(boxPos).getBlock() != BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD)) {
                                world.setBlockToAir(boxPos);
                            }
                        } else if (boxPos.getY() >= 0 && !world.getBlockState(boxPos.down()).getMaterial().isSolid()) {
                            world.setBlockToAir(boxPos);
                        } else if (world.getBlockState(boxPos).getMaterial().isSolid() && world.getBlockState(boxPos).getBlock() != BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD)) {
                            if (i4 == -1 && rand.nextInt(4) != 0) {
                                world.setBlockState(boxPos, BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CRACKED).getDefaultState(), 2);
                            } else {
                                world.setBlockState(boxPos, BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE).getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            for (int l3 = 0; l3 < 2; ++l3) {
                for (int j4 = 0; j4 < 3; ++j4) {
                    int l4 = pos.getX() + rand.nextInt(j * 2 + 1) - j;
                    int i5 = pos.getY();
                    int j5 = pos.getZ() + rand.nextInt(k1 * 2 + 1) - k1;
                    BlockPos chestPos = new BlockPos(l4, i5, j5);

                    if (world.isAirBlock(chestPos)) {
                        int j3 = 0;

                        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                            if (world.getBlockState(chestPos.offset(enumfacing)).getMaterial().isSolid()) {
                                ++j3;
                            }
                        }

                        if (j3 == 1) {
                            world.setBlockState(chestPos, BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState(), 2);
                            TileEntity tileEntity = world.getTileEntity(chestPos);

                            if (tileEntity instanceof TileEntityCrate) {
                                ((TileEntityCrate) tileEntity).setLootTable(AtumLootTables.RUINS, rand.nextLong()); //TODO
                            }

                            break;
                        }
                    }
                }
            }

            world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof TileEntityMobSpawner) {
                ((TileEntityMobSpawner) tileEntity).getSpawnerBaseLogic().setEntityId(this.pickMobSpawner(rand));
                System.out.println("Dungeon pos: " + pos);
            } else {
                Atum.LOG.error("Failed to fetch mob spawner entity at ({}, {}, {})", pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        } else {
            return false;
        }
    }

    private ResourceLocation pickMobSpawner(Random rand) {
        int chance = MathHelper.getInt(rand, 0, 5);
        switch (chance) {
            case 0:
                return AtumEntities.WRAITH.getRegistryName();
            case 1:
                return AtumEntities.STONEGUARD.getRegistryName();
            case 2:
                return AtumEntities.MUMMY.getRegistryName();
            case 3:
                return AtumEntities.BONESTORM.getRegistryName();
            case 4:
                return AtumEntities.TARANTULA.getRegistryName();
            default:
            case 5:
                return AtumEntities.FORSAKEN.getRegistryName();
        }
    }
}