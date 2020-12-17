package com.teammetallurgy.atum.world.gen.structure.mineshaft;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.lighting.AtumWallTorchUnlitBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RailBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class AtumMineshaftPieces {

    private static Piece createRandomShaftPiece(List<StructurePiece> list, Random rand, int x, int y, int z, @Nullable Direction direction, int componentType, AtumMineshaftStructure.Type type) {
        int i = rand.nextInt(100);
        if (i >= 80) {
            MutableBoundingBox box = Cross.findCrossing(list, rand, x, y, z, direction);
            if (box != null) {
                return new Cross(componentType, box, direction, type);
            }
        } else if (i >= 70) {
            MutableBoundingBox box = Stairs.findStairs(list, rand, x, y, z, direction);
            if (box != null) {
                return new Stairs(componentType, box, direction, type);
            }
        } else {
            MutableBoundingBox box = Corridor.findCorridorSize(list, rand, x, y, z, direction);
            if (box != null) {
                return new Corridor(componentType, rand, box, direction, type);
            }
        }

        return null;
    }

    private static Piece generateAndAddPiece(StructurePiece structurePiece, List<StructurePiece> list, Random p_189938_2_, int x, int y, int z, Direction direction, int componentType) {
        if (componentType > 8) {
            return null;
        } else if (Math.abs(x - structurePiece.getBoundingBox().minX) <= 80 && Math.abs(z - structurePiece.getBoundingBox().minZ) <= 80) {
            AtumMineshaftStructure.Type type = ((Piece) structurePiece).mineShaftType;
            Piece piece = createRandomShaftPiece(list, p_189938_2_, x, y, z, direction, componentType + 1, type);
            if (piece != null) {
                list.add(piece);
                piece.buildComponent(structurePiece, list, p_189938_2_);
            }
            return piece;
        } else {
            return null;
        }
    }

    public static class Corridor extends Piece {
        private final boolean hasRails;
        private final boolean hasTarantula;
        private boolean spawnerPlaced;
        private final int sectionCount;

        public Corridor(TemplateManager manager, CompoundNBT nbt) {
            super(AtumStructurePieces.MINESHAFT_CORRIDOR, nbt);
            this.hasRails = nbt.getBoolean("hr");
            this.hasTarantula = nbt.getBoolean("sc");
            this.spawnerPlaced = nbt.getBoolean("hps");
            this.sectionCount = nbt.getInt("Num");
        }

        @Override
        protected void readAdditional(CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putBoolean("hr", this.hasRails);
            tagCompound.putBoolean("sc", this.hasTarantula);
            tagCompound.putBoolean("hps", this.spawnerPlaced);
            tagCompound.putInt("Num", this.sectionCount);
        }

        public Corridor(int componentType, Random rand, MutableBoundingBox box, Direction direction, AtumMineshaftStructure.Type type) {
            super(AtumStructurePieces.MINESHAFT_CORRIDOR, componentType, type);
            this.setCoordBaseMode(direction);
            this.boundingBox = box;
            this.hasRails = rand.nextInt(3) == 0;
            this.hasTarantula = !this.hasRails && rand.nextInt(23) == 0;
            if (this.getCoordBaseMode().getAxis() == Direction.Axis.Z) {
                this.sectionCount = box.getZSize() / 5;
            } else {
                this.sectionCount = box.getXSize() / 5;
            }
        }

        public static MutableBoundingBox findCorridorSize(List<StructurePiece> list, Random rand, int x, int y, int z, Direction facing) {
            MutableBoundingBox box = new MutableBoundingBox(x, y, z, x, y + 3 - 1, z);

            int i;
            for (i = rand.nextInt(3) + 2; i > 0; --i) {
                int j = i * 5;
                switch (facing) {
                    case NORTH:
                    default:
                        box.maxX = x + 3 - 1;
                        box.minZ = z - (j - 1);
                        break;
                    case SOUTH:
                        box.maxX = x + 3 - 1;
                        box.maxZ = z + j - 1;
                        break;
                    case WEST:
                        box.minX = x - (j - 1);
                        box.maxZ = z + 3 - 1;
                        break;
                    case EAST:
                        box.maxX = x + j - 1;
                        box.maxZ = z + 3 - 1;
                }

                if (StructurePiece.findIntersecting(list, box) == null) {
                    break;
                }
            }
            return i > 0 ? box : null;
        }

        @Override
        public void buildComponent(@Nonnull StructurePiece component, @Nonnull List<StructurePiece> list, Random rand) {
            int i = this.getComponentType();
            int j = rand.nextInt(4);
            Direction direction = this.getCoordBaseMode();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        if (j <= 1) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, direction, i);
                        } else if (j == 2) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, Direction.WEST, i);
                        } else {
                            generateAndAddPiece(component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, Direction.EAST, i);
                        }
                        break;
                    case SOUTH:
                        if (j <= 1) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, direction, i);
                        } else if (j == 2) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ - 3, Direction.WEST, i);
                        } else {
                            generateAndAddPiece(component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ - 3, Direction.EAST, i);
                        }
                        break;
                    case WEST:
                        if (j <= 1) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, direction, i);
                        } else if (j == 2) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, Direction.NORTH, i);
                        } else {
                            generateAndAddPiece(component, list, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                        }
                        break;
                    case EAST:
                        if (j <= 1) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, direction, i);
                        } else if (j == 2) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, Direction.NORTH, i);
                        } else {
                            generateAndAddPiece(component, list, rand, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                        }
                }
            }

            if (i < 8) {
                if (direction != Direction.NORTH && direction != Direction.SOUTH) {
                    for (int i1 = this.boundingBox.minX + 3; i1 + 3 <= this.boundingBox.maxX; i1 += 5) {
                        int j1 = rand.nextInt(5);
                        if (j1 == 0) {
                            generateAndAddPiece(component, list, rand, i1, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i + 1);
                        } else if (j1 == 1) {
                            generateAndAddPiece(component, list, rand, i1, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i + 1);
                        }
                    }
                } else {
                    for (int k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5) {
                        int l = rand.nextInt(5);
                        if (l == 0) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY, k, Direction.WEST, i + 1);
                        } else if (l == 1) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, k, Direction.EAST, i + 1);
                        }
                    }
                }
            }

        }

        @Override
        protected boolean generateChest(@Nonnull ISeedReader world, MutableBoundingBox box, @Nonnull Random rand, int x, int y, int z, @Nonnull ResourceLocation loot) {
            BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
            if (box.isVecInside(blockpos) && world.getBlockState(blockpos).isAir(world, blockpos) && !world.getBlockState(blockpos.down()).isAir(world, blockpos.down())) {
                BlockState plankState = Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, rand.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
                this.setBlockState(world, plankState, x, y, z, box);
                ChestMinecartEntity chestMinecraftEntity = new ChestMinecartEntity(world.getWorld(), (float) blockpos.getX() + 0.5F, (float) blockpos.getY() + 0.5F, (float) blockpos.getZ() + 0.5F);
                chestMinecraftEntity.setLootTable(loot, rand.nextLong());
                world.addEntity(chestMinecraftEntity);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean func_230383_a_(@Nonnull ISeedReader world, @Nonnull StructureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull MutableBoundingBox box, @Nonnull ChunkPos chunkPos, BlockPos pos) {
            if (this.isLiquidInStructureBoundingBox(world, box)) {
                return false;
            } else {
                int i1 = this.sectionCount * 5 - 1;
                BlockState plankState = this.getPlanksBlock();
                this.fillWithBlocks(world, box, 0, 0, 0, 2, 1, i1, CAVE_AIR, CAVE_AIR, false);
                this.generateMaybeBox(world, box, rand, 0.8F, 0, 2, 0, 2, 2, i1, CAVE_AIR, CAVE_AIR, false, false);
                if (this.hasTarantula) {
                    this.generateMaybeBox(world, box, rand, 0.6F, 0, 0, 0, 2, 1, i1, Blocks.COBWEB.getDefaultState(), CAVE_AIR, false, true);
                }

                for (int j1 = 0; j1 < this.sectionCount; ++j1) {
                    int k1 = 2 + j1 * 5;
                    this.placeSupport(world, box, 0, 0, k1, 2, 2, rand);
                    this.placeCobWeb(world, box, rand, 0.1F, 0, 2, k1 - 1);
                    this.placeCobWeb(world, box, rand, 0.1F, 2, 2, k1 - 1);
                    this.placeCobWeb(world, box, rand, 0.1F, 0, 2, k1 + 1);
                    this.placeCobWeb(world, box, rand, 0.1F, 2, 2, k1 + 1);
                    this.placeCobWeb(world, box, rand, 0.05F, 0, 2, k1 - 2);
                    this.placeCobWeb(world, box, rand, 0.05F, 2, 2, k1 - 2);
                    this.placeCobWeb(world, box, rand, 0.05F, 0, 2, k1 + 2);
                    this.placeCobWeb(world, box, rand, 0.05F, 2, 2, k1 + 2);
                    if (rand.nextInt(100) == 0) {
                        this.generateChest(world, box, rand, 2, 0, k1 - 1, AtumLootTables.CRATE);
                    }

                    if (rand.nextInt(100) == 0) {
                        this.generateChest(world, box, rand, 0, 0, k1 + 1, AtumLootTables.CRATE);
                    }

                    if (this.hasTarantula && !this.spawnerPlaced) {
                        int l1 = this.getYWithOffset(0);
                        int i2 = k1 - 1 + rand.nextInt(3);
                        int j2 = this.getXWithOffset(1, i2);
                        int k2 = this.getZWithOffset(1, i2);
                        BlockPos blockpos = new BlockPos(j2, l1, k2);
                        if (box.isVecInside(blockpos) && this.getSkyBrightness(world, 1, 0, i2, box)) {
                            this.spawnerPlaced = true;
                            world.setBlockState(blockpos, Blocks.SPAWNER.getDefaultState(), 2);
                            TileEntity tileEntity = world.getTileEntity(blockpos);
                            if (tileEntity instanceof MobSpawnerTileEntity) {
                                AtumMineshaftStructure.Type type = this.mineShaftType;
                                int chance = rand.nextInt(100);
                                if (chance < 40) {
                                    if (type == AtumMineshaftStructure.Type.DEADWOOD || type == AtumMineshaftStructure.Type.DEADWOOD_SURFACE) {
                                        ((MobSpawnerTileEntity) tileEntity).getSpawnerBaseLogic().setEntityType(AtumEntities.FORSAKEN);
                                    } else if (type == AtumMineshaftStructure.Type.LIMESTONE || type == AtumMineshaftStructure.Type.LIMESTONE_SURFACE) {
                                        ((MobSpawnerTileEntity) tileEntity).getSpawnerBaseLogic().setEntityType(AtumEntities.STONEGUARD);
                                    }
                                } else {
                                    ((MobSpawnerTileEntity) tileEntity).getSpawnerBaseLogic().setEntityType(AtumEntities.TARANTULA);
                                }
                            }
                        }
                    }
                }

                for (int l2 = 0; l2 <= 2; ++l2) {
                    for (int i3 = 0; i3 <= i1; ++i3) {
                        BlockState blockstate3 = this.getBlockStateFromPos(world, l2, -1, i3, box);
                        if (blockstate3.isAir() && this.getSkyBrightness(world, l2, -1, i3, box)) {
                            this.setBlockState(world, plankState, l2, -1, i3, box);
                        }
                    }
                }

                if (this.hasRails) {
                    BlockState railState = Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.NORTH_SOUTH);
                    for (int j3 = 0; j3 <= i1; ++j3) {
                        BlockState blockstate2 = this.getBlockStateFromPos(world, 1, -1, j3, box);
                        if (!blockstate2.isAir() && blockstate2.isOpaqueCube(world, new BlockPos(this.getXWithOffset(1, j3), this.getYWithOffset(-1), this.getZWithOffset(1, j3)))) {
                            float f = this.getSkyBrightness(world, 1, 0, j3, box) ? 0.7F : 0.9F;
                            this.randomlyPlaceBlock(world, box, rand, f, 1, 0, j3, railState);
                        }
                    }
                }
                return true;
            }
        }

        private void placeSupport(ISeedReader world, MutableBoundingBox box, int x, int yMin, int zMin, int yMax, int zMax, Random rand) {
            if (this.isSupportingBox(world, box, x, zMax, yMax, zMin)) {
                BlockState plankState = this.getPlanksBlock();
                BlockState fenceState = this.getFenceBlock();
                BlockState torchState = this.getTorchBlock();
                this.fillWithBlocks(world, box, x, yMin, zMin, x, yMax - 1, zMin, fenceState, CAVE_AIR, false);
                this.fillWithBlocks(world, box, zMax, yMin, zMin, zMax, yMax - 1, zMin, fenceState, CAVE_AIR, false);
                if (rand.nextInt(4) == 0) {
                    this.fillWithBlocks(world, box, x, yMax, zMin, x, yMax, zMin, plankState, CAVE_AIR, false);
                    this.fillWithBlocks(world, box, zMax, yMax, zMin, zMax, yMax, zMin, plankState, CAVE_AIR, false);
                } else {
                    this.fillWithBlocks(world, box, x, yMax, zMin, zMax, yMax, zMin, plankState, CAVE_AIR, false);
                    this.randomlyPlaceBlock(world, box, rand, 0.05F, x + 1, yMax, zMin - 1, torchState.with(WallTorchBlock.HORIZONTAL_FACING, Direction.NORTH));
                    this.randomlyPlaceBlock(world, box, rand, 0.05F, x + 1, yMax, zMin + 1, torchState.with(WallTorchBlock.HORIZONTAL_FACING, Direction.SOUTH));
                }
            }
        }

        private void placeCobWeb(ISeedReader world, MutableBoundingBox box, Random rand, float chance, int x, int y, int z) {
            if (this.getSkyBrightness(world, x, y, z, box)) {
                this.randomlyPlaceBlock(world, box, rand, chance, x, y, z, Blocks.COBWEB.getDefaultState());
            }
        }
    }

    public static class Cross extends Piece {
        private final Direction corridorDirection;
        private final boolean isMultipleFloors;

        public Cross(TemplateManager manager, CompoundNBT nbt) {
            super(AtumStructurePieces.MINESHAFT_CROSSING, nbt);
            this.isMultipleFloors = nbt.getBoolean("tf");
            this.corridorDirection = Direction.byHorizontalIndex(nbt.getInt("D"));
        }

        @Override
        protected void readAdditional(CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putBoolean("tf", this.isMultipleFloors);
            tagCompound.putInt("D", this.corridorDirection.getHorizontalIndex());
        }

        public Cross(int componentType, MutableBoundingBox box, @Nullable Direction direction, AtumMineshaftStructure.Type type) {
            super(AtumStructurePieces.MINESHAFT_CROSSING, componentType, type);
            this.corridorDirection = direction;
            this.boundingBox = box;
            this.isMultipleFloors = box.getYSize() > 3;
        }

        public static MutableBoundingBox findCrossing(List<StructurePiece> list, Random rand, int x, int y, int z, Direction facing) {
            MutableBoundingBox box = new MutableBoundingBox(x, y, z, x, y + 3 - 1, z);
            if (rand.nextInt(4) == 0) {
                box.maxY += 4;
            }

            switch (facing) {
                case NORTH:
                default:
                    box.minX = x - 1;
                    box.maxX = x + 3;
                    box.minZ = z - 4;
                    break;
                case SOUTH:
                    box.minX = x - 1;
                    box.maxX = x + 3;
                    box.maxZ = z + 3 + 1;
                    break;
                case WEST:
                    box.minX = x - 4;
                    box.minZ = z - 1;
                    box.maxZ = z + 3;
                    break;
                case EAST:
                    box.maxX = x + 3 + 1;
                    box.minZ = z - 1;
                    box.maxZ = z + 3;
            }

            return StructurePiece.findIntersecting(list, box) != null ? null : box;
        }

        @Override
        public void buildComponent(@Nonnull StructurePiece component, @Nonnull List<StructurePiece> list, @Nonnull Random rand) {
            int i = this.getComponentType();
            switch (this.corridorDirection) {
                case NORTH:
                default:
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.WEST, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.EAST, i);
                    break;
                case SOUTH:
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.WEST, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.EAST, i);
                    break;
                case WEST:
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.WEST, i);
                    break;
                case EAST:
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.EAST, i);
            }

            if (this.isMultipleFloors) {
                if (rand.nextBoolean()) {
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, Direction.NORTH, i);
                }

                if (rand.nextBoolean()) {
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, Direction.WEST, i);
                }

                if (rand.nextBoolean()) {
                    generateAndAddPiece(component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, Direction.EAST, i);
                }

                if (rand.nextBoolean()) {
                    generateAndAddPiece(component, list, rand, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                }
            }
        }

        @Override
        public boolean func_230383_a_(@Nonnull ISeedReader world, StructureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull MutableBoundingBox box, @Nonnull ChunkPos chunkPos, BlockPos pos) {
            if (this.isLiquidInStructureBoundingBox(world, box)) {
                return false;
            } else {
                BlockState plankState = this.getPlanksBlock();
                if (this.isMultipleFloors) {
                    this.fillWithBlocks(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, CAVE_AIR, CAVE_AIR, false);
                    this.fillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, CAVE_AIR, CAVE_AIR, false);
                    this.fillWithBlocks(world, box, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, CAVE_AIR, CAVE_AIR, false);
                    this.fillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, CAVE_AIR, CAVE_AIR, false);
                    this.fillWithBlocks(world, box, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, CAVE_AIR, CAVE_AIR, false);
                } else {
                    this.fillWithBlocks(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, CAVE_AIR, CAVE_AIR, false);
                    this.fillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, CAVE_AIR, CAVE_AIR, false);
                }

                this.placeSupportPillar(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, box, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, box, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);

                for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                    for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                        if (this.getBlockStateFromPos(world, i, this.boundingBox.minY - 1, j, box).isAir() && this.getSkyBrightness(world, i, this.boundingBox.minY - 1, j, box)) {
                            this.setBlockState(world, plankState, i, this.boundingBox.minY - 1, j, box);
                        }
                    }
                }
                return true;
            }
        }

        private void placeSupportPillar(ISeedReader world, MutableBoundingBox box, int x, int y, int z, int yMax) {
            if (!this.getBlockStateFromPos(world, x, yMax + 1, z, box).isAir()) {
                this.fillWithBlocks(world, box, x, y, z, x, yMax, z, this.getPlanksBlock(), CAVE_AIR, false);
            }
        }
    }

    abstract static class Piece extends StructurePiece {
        protected AtumMineshaftStructure.Type mineShaftType;

        public Piece(IStructurePieceType structurePieceType, int componentType, AtumMineshaftStructure.Type type) {
            super(structurePieceType, componentType);
            this.mineShaftType = type;
        }

        public Piece(IStructurePieceType type, CompoundNBT nbt) {
            super(type, nbt);
            this.mineShaftType = AtumMineshaftStructure.Type.byId(nbt.getInt("MST"));
        }

        @Override
        protected void readAdditional(CompoundNBT tagCompound) {
            tagCompound.putInt("MST", this.mineShaftType.ordinal());
        }

        protected BlockState getPlanksBlock() {
            switch (this.mineShaftType) {
                case DEADWOOD:
                case DEADWOOD_SURFACE:
                default:
                    return AtumBlocks.DEADWOOD_PLANKS.getDefaultState();
                case LIMESTONE:
                case LIMESTONE_SURFACE:
                    return AtumBlocks.LIMESTONE_BRICK_LARGE.getDefaultState();
            }
        }

        protected BlockState getFenceBlock() {
            switch (this.mineShaftType) {
                case DEADWOOD:
                case DEADWOOD_SURFACE:
                default:
                    return AtumBlocks.DEADWOOD_FENCE.getDefaultState();
                case LIMESTONE:
                case LIMESTONE_SURFACE:
                    return AtumBlocks.LARGE_WALL.getDefaultState();
            }
        }

        protected BlockState getTorchBlock() {
            switch (this.mineShaftType) {
                case DEADWOOD:
                case DEADWOOD_SURFACE:
                default:
                    return AtumWallTorchUnlitBlock.UNLIT.get(AtumBlocks.DEADWOOD_TORCH).getDefaultState();
                case LIMESTONE:
                case LIMESTONE_SURFACE:
                    return AtumWallTorchUnlitBlock.UNLIT.get(AtumBlocks.LIMESTONE_TORCH).getDefaultState();
            }
        }

        protected boolean isSupportingBox(IBlockReader blockReader, MutableBoundingBox box, int xStart, int xEnd, int x, int z) {
            for (int i = xStart; i <= xEnd; ++i) {
                if (this.getBlockStateFromPos(blockReader, i, x + 1, z, box).isAir()) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class Room extends Piece {
        private final List<MutableBoundingBox> connectedRooms = Lists.newLinkedList();

        public Room(int componentType, Random rand, int x, int z, AtumMineshaftStructure.Type type) {
            super(AtumStructurePieces.MINESHAFT_ROOM, componentType, type);
            this.mineShaftType = type;
            this.boundingBox = new MutableBoundingBox(x, 50, z, x + 7 + rand.nextInt(6), 54 + rand.nextInt(6), z + 7 + rand.nextInt(6));
        }

        public Room(TemplateManager manager, CompoundNBT nbt) {
            super(AtumStructurePieces.MINESHAFT_ROOM, nbt);
            ListNBT listnbt = nbt.getList("Entrances", 11);

            for (int i = 0; i < listnbt.size(); ++i) {
                this.connectedRooms.add(new MutableBoundingBox(listnbt.getIntArray(i)));
            }
        }

        @Override
        public void buildComponent(@Nonnull StructurePiece component, @Nonnull List<StructurePiece> list, @Nonnull Random rand) {
            int i = this.getComponentType();
            int j = this.boundingBox.getYSize() - 3 - 1;
            if (j <= 0) {
                j = 1;
            }

            int k;
            for (k = 0; k < this.boundingBox.getXSize(); k = k + 4) {
                k = k + rand.nextInt(this.boundingBox.getXSize());
                if (k + 3 > this.boundingBox.getXSize()) {
                    break;
                }

                Piece piece = generateAndAddPiece(component, list, rand, this.boundingBox.minX + k, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ - 1, Direction.NORTH, i);
                if (piece != null) {
                    MutableBoundingBox box = piece.getBoundingBox();
                    this.connectedRooms.add(new MutableBoundingBox(box.minX, box.minY, this.boundingBox.minZ, box.maxX, box.maxY, this.boundingBox.minZ + 1));
                }
            }

            for (k = 0; k < this.boundingBox.getXSize(); k = k + 4) {
                k = k + rand.nextInt(this.boundingBox.getXSize());
                if (k + 3 > this.boundingBox.getXSize()) {
                    break;
                }

                Piece piece = generateAndAddPiece(component, list, rand, this.boundingBox.minX + k, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                if (piece != null) {
                    MutableBoundingBox box = piece.getBoundingBox();
                    this.connectedRooms.add(new MutableBoundingBox(box.minX, box.minY, this.boundingBox.maxZ - 1, box.maxX, box.maxY, this.boundingBox.maxZ));
                }
            }

            for (k = 0; k < this.boundingBox.getZSize(); k = k + 4) {
                k = k + rand.nextInt(this.boundingBox.getZSize());
                if (k + 3 > this.boundingBox.getZSize()) {
                    break;
                }

                Piece piece = generateAndAddPiece(component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ + k, Direction.WEST, i);
                if (piece != null) {
                    MutableBoundingBox box = piece.getBoundingBox();
                    this.connectedRooms.add(new MutableBoundingBox(this.boundingBox.minX, box.minY, box.minZ, this.boundingBox.minX + 1, box.maxY, box.maxZ));
                }
            }

            for (k = 0; k < this.boundingBox.getZSize(); k = k + 4) {
                k = k + rand.nextInt(this.boundingBox.getZSize());
                if (k + 3 > this.boundingBox.getZSize()) {
                    break;
                }

                StructurePiece piece = generateAndAddPiece(component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ + k, Direction.EAST, i);
                if (piece != null) {
                    MutableBoundingBox box = piece.getBoundingBox();
                    this.connectedRooms.add(new MutableBoundingBox(this.boundingBox.maxX - 1, box.minY, box.minZ, this.boundingBox.maxX, box.maxY, box.maxZ));
                }
            }

        }

        @Override
        public boolean func_230383_a_(@Nonnull ISeedReader world, @Nonnull StructureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull MutableBoundingBox box, @Nonnull ChunkPos chunkPos, BlockPos pos) {
            if (this.isLiquidInStructureBoundingBox(world, box)) {
                return false;
            } else {
                this.fillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, AtumBlocks.SAND.getDefaultState(), CAVE_AIR, true);
                this.fillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, CAVE_AIR, CAVE_AIR, false);

                for (MutableBoundingBox connectedRooms : this.connectedRooms) {
                    this.fillWithBlocks(world, box, connectedRooms.minX, connectedRooms.maxY - 2, connectedRooms.minZ, connectedRooms.maxX, connectedRooms.maxY, connectedRooms.maxZ, CAVE_AIR, CAVE_AIR, false);
                }

                this.randomlyRareFillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, CAVE_AIR, false);
                return true;
            }
        }

        @Override
        public void offset(int x, int y, int z) {
            super.offset(x, y, z);

            for (MutableBoundingBox connectedRooms : this.connectedRooms) {
                connectedRooms.offset(x, y, z);
            }

        }

        @Override
        protected void readAdditional(CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            ListNBT listnbt = new ListNBT();

            for (MutableBoundingBox connectedRooms : this.connectedRooms) {
                listnbt.add(connectedRooms.toNBTTagIntArray());
            }
            tagCompound.put("Entrances", listnbt);
        }
    }

    public static class Stairs extends Piece {

        public Stairs(int componentType, MutableBoundingBox box, Direction direction, AtumMineshaftStructure.Type type) {
            super(AtumStructurePieces.MINESHAFT_STAIRS, componentType, type);
            this.setCoordBaseMode(direction);
            this.boundingBox = box;
        }

        public Stairs(TemplateManager manager, CompoundNBT nbt) {
            super(AtumStructurePieces.MINESHAFT_STAIRS, nbt);
        }

        public static MutableBoundingBox findStairs(List<StructurePiece> list, Random rand, int x, int y, int z, Direction facing) {
            MutableBoundingBox box = new MutableBoundingBox(x, y - 5, z, x, y + 3 - 1, z);
            switch (facing) {
                case NORTH:
                default:
                    box.maxX = x + 3 - 1;
                    box.minZ = z - 8;
                    break;
                case SOUTH:
                    box.maxX = x + 3 - 1;
                    box.maxZ = z + 8;
                    break;
                case WEST:
                    box.minX = x - 8;
                    box.maxZ = z + 3 - 1;
                    break;
                case EAST:
                    box.maxX = x + 8;
                    box.maxZ = z + 3 - 1;
            }
            return StructurePiece.findIntersecting(list, box) != null ? null : box;
        }

        @Override
        public void buildComponent(@Nonnull StructurePiece component, @Nonnull List<StructurePiece> list, @Nonnull Random rand) {
            int i = this.getComponentType();
            Direction direction = this.getCoordBaseMode();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        generateAndAddPiece(component, list, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                        break;
                    case SOUTH:
                        generateAndAddPiece(component, list, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                        break;
                    case WEST:
                        generateAndAddPiece(component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, Direction.WEST, i);
                        break;
                    case EAST:
                        generateAndAddPiece(component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, Direction.EAST, i);
                }
            }

        }

        @Override
        public boolean func_230383_a_(@Nonnull ISeedReader world, @Nonnull StructureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull MutableBoundingBox box, @Nonnull ChunkPos chunkPos, BlockPos pos) {
            if (this.isLiquidInStructureBoundingBox(world, box)) {
                return false;
            } else {
                this.fillWithBlocks(world, box, 0, 5, 0, 2, 7, 1, CAVE_AIR, CAVE_AIR, false);
                this.fillWithBlocks(world, box, 0, 0, 7, 2, 2, 8, CAVE_AIR, CAVE_AIR, false);

                for (int i = 0; i < 5; ++i) {
                    this.fillWithBlocks(world, box, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, CAVE_AIR, CAVE_AIR, false);
                }
                return true;
            }
        }
    }
}