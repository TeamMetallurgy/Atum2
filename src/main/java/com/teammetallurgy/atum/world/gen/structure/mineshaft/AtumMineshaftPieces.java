/*
package com.teammetallurgy.atum.world.gen.structure.mineshaft;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.lighting.AtumWallTorchUnlitBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class AtumMineshaftPieces { // TODO

    private static Piece createRandomShaftPiece(List<StructurePiece> list, Random rand, int x, int y, int z, @Nullable Direction direction, int componentType, AtumMineshaftStructure.Type type) {
        int i = rand.nextInt(100);
        if (i >= 80) {
            BoundingBox box = Cross.findCrossing(list, rand, x, y, z, direction);
            if (box != null) {
                return new Cross(componentType, box, direction, type);
            }
        } else if (i >= 70) {
            BoundingBox box = Stairs.findStairs(list, rand, x, y, z, direction);
            if (box != null) {
                return new Stairs(componentType, box, direction, type);
            }
        } else {
            BoundingBox box = Corridor.findCorridorSize(list, rand, x, y, z, direction);
            if (box != null) {
                return new Corridor(componentType, rand, box, direction, type);
            }
        }

        return null;
    }

    private static Piece generateAndAddPiece(StructurePiece structurePiece, List<StructurePiece> list, Random p_189938_2_, int x, int y, int z, Direction direction, int componentType) {
        if (componentType > 8) {
            return null;
        } else if (Math.abs(x - structurePiece.getBoundingBox().x0) <= 80 && Math.abs(z - structurePiece.getBoundingBox().z0) <= 80) {
            AtumMineshaftStructure.Type type = ((Piece) structurePiece).mineShaftType;
            Piece piece = createRandomShaftPiece(list, p_189938_2_, x, y, z, direction, componentType + 1, type);
            if (piece != null) {
                list.add(piece);
                piece.addChildren(structurePiece, list, p_189938_2_);
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

        public Corridor(StructureManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.MINESHAFT_CORRIDOR, nbt);
            this.hasRails = nbt.getBoolean("hr");
            this.hasTarantula = nbt.getBoolean("sc");
            this.spawnerPlaced = nbt.getBoolean("hps");
            this.sectionCount = nbt.getInt("Num");
        }

        @Override
        protected void addAdditionalSaveData(CompoundTag tagCompound) {
            super.addAdditionalSaveData(tagCompound);
            tagCompound.putBoolean("hr", this.hasRails);
            tagCompound.putBoolean("sc", this.hasTarantula);
            tagCompound.putBoolean("hps", this.spawnerPlaced);
            tagCompound.putInt("Num", this.sectionCount);
        }

        public Corridor(int componentType, Random rand, BoundingBox box, Direction direction, AtumMineshaftStructure.Type type) {
            super(AtumStructurePieces.MINESHAFT_CORRIDOR, componentType, type);
            this.setOrientation(direction);
            this.boundingBox = box;
            this.hasRails = rand.nextInt(3) == 0;
            this.hasTarantula = !this.hasRails && rand.nextInt(23) == 0;
            if (this.getOrientation().getAxis() == Direction.Axis.Z) {
                this.sectionCount = box.getZSpan() / 5;
            } else {
                this.sectionCount = box.getXSpan() / 5;
            }
        }

        public static BoundingBox findCorridorSize(List<StructurePiece> list, Random rand, int x, int y, int z, Direction facing) {
            BoundingBox box = new BoundingBox(x, y, z, x, y + 3 - 1, z);

            int i;
            for (i = rand.nextInt(3) + 2; i > 0; --i) {
                int j = i * 5;
                switch (facing) {
                    case NORTH:
                    default:
                        box.x1 = x + 3 - 1;
                        box.z0 = z - (j - 1);
                        break;
                    case SOUTH:
                        box.x1 = x + 3 - 1;
                        box.z1 = z + j - 1;
                        break;
                    case WEST:
                        box.x0 = x - (j - 1);
                        box.z1 = z + 3 - 1;
                        break;
                    case EAST:
                        box.x1 = x + j - 1;
                        box.z1 = z + 3 - 1;
                }

                if (StructurePiece.findCollisionPiece(list, box) == null) {
                    break;
                }
            }
            return i > 0 ? box : null;
        }

        @Override
        public void addChildren(@Nonnull StructurePiece component, @Nonnull List<StructurePiece> list, Random rand) {
            int i = this.getGenDepth();
            int j = rand.nextInt(4);
            Direction direction = this.getOrientation();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        if (j <= 1) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x0, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z0 - 1, direction, i);
                        } else if (j == 2) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x0 - 1, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z0, Direction.WEST, i);
                        } else {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x1 + 1, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z0, Direction.EAST, i);
                        }
                        break;
                    case SOUTH:
                        if (j <= 1) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x0, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z1 + 1, direction, i);
                        } else if (j == 2) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x0 - 1, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z1 - 3, Direction.WEST, i);
                        } else {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x1 + 1, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z1 - 3, Direction.EAST, i);
                        }
                        break;
                    case WEST:
                        if (j <= 1) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x0 - 1, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z0, direction, i);
                        } else if (j == 2) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x0, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z0 - 1, Direction.NORTH, i);
                        } else {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x0, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z1 + 1, Direction.SOUTH, i);
                        }
                        break;
                    case EAST:
                        if (j <= 1) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x1 + 1, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z0, direction, i);
                        } else if (j == 2) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x1 - 3, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z0 - 1, Direction.NORTH, i);
                        } else {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x1 - 3, this.boundingBox.y0 - 1 + rand.nextInt(3), this.boundingBox.z1 + 1, Direction.SOUTH, i);
                        }
                }
            }

            if (i < 8) {
                if (direction != Direction.NORTH && direction != Direction.SOUTH) {
                    for (int i1 = this.boundingBox.x0 + 3; i1 + 3 <= this.boundingBox.x1; i1 += 5) {
                        int j1 = rand.nextInt(5);
                        if (j1 == 0) {
                            generateAndAddPiece(component, list, rand, i1, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, i + 1);
                        } else if (j1 == 1) {
                            generateAndAddPiece(component, list, rand, i1, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, i + 1);
                        }
                    }
                } else {
                    for (int k = this.boundingBox.z0 + 3; k + 3 <= this.boundingBox.z1; k += 5) {
                        int l = rand.nextInt(5);
                        if (l == 0) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x0 - 1, this.boundingBox.y0, k, Direction.WEST, i + 1);
                        } else if (l == 1) {
                            generateAndAddPiece(component, list, rand, this.boundingBox.x1 + 1, this.boundingBox.y0, k, Direction.EAST, i + 1);
                        }
                    }
                }
            }

        }

        @Override
        protected boolean createChest(@Nonnull WorldGenLevel level, BoundingBox box, @Nonnull Random rand, int x, int y, int z, @Nonnull ResourceLocation loot) {
            BlockPos blockpos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
            if (box.isInside(blockpos) && level.getBlockState(blockpos).isAir(level, blockpos) && !level.getBlockState(blockpos.below()).isAir(level, blockpos.below())) {
                BlockState plankState = Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, rand.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
                this.placeBlock(level, plankState, x, y, z, box);
                MinecartChest chestMinecraftEntity = new MinecartChest(level.getLevel(), (float) blockpos.getX() + 0.5F, (float) blockpos.getY() + 0.5F, (float) blockpos.getZ() + 0.5F);
                chestMinecraftEntity.setLootTable(loot, rand.nextLong());
                level.addFreshEntity(chestMinecraftEntity);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean postProcess(@Nonnull WorldGenLevel level, @Nonnull StructureFeatureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BoundingBox box, @Nonnull ChunkPos chunkPos, BlockPos pos) {
            if (this.edgesLiquid(level, box)) {
                return false;
            } else {
                int i1 = this.sectionCount * 5 - 1;
                BlockState plankState = this.getPlanksBlock();
                this.generateBox(level, box, 0, 0, 0, 2, 1, i1, CAVE_AIR, CAVE_AIR, false);
                this.generateMaybeBox(level, box, rand, 0.8F, 0, 2, 0, 2, 2, i1, CAVE_AIR, CAVE_AIR, false, false);
                if (this.hasTarantula) {
                    this.generateMaybeBox(level, box, rand, 0.6F, 0, 0, 0, 2, 1, i1, Blocks.COBWEB.defaultBlockState(), CAVE_AIR, false, true);
                }

                for (int j1 = 0; j1 < this.sectionCount; ++j1) {
                    int k1 = 2 + j1 * 5;
                    this.placeSupport(level, box, 0, 0, k1, 2, 2, rand);
                    this.placeCobWeb(level, box, rand, 0.1F, 0, 2, k1 - 1);
                    this.placeCobWeb(level, box, rand, 0.1F, 2, 2, k1 - 1);
                    this.placeCobWeb(level, box, rand, 0.1F, 0, 2, k1 + 1);
                    this.placeCobWeb(level, box, rand, 0.1F, 2, 2, k1 + 1);
                    this.placeCobWeb(level, box, rand, 0.05F, 0, 2, k1 - 2);
                    this.placeCobWeb(level, box, rand, 0.05F, 2, 2, k1 - 2);
                    this.placeCobWeb(level, box, rand, 0.05F, 0, 2, k1 + 2);
                    this.placeCobWeb(level, box, rand, 0.05F, 2, 2, k1 + 2);
                    if (rand.nextInt(100) == 0) {
                        this.createChest(level, box, rand, 2, 0, k1 - 1, AtumLootTables.CRATE);
                    }

                    if (rand.nextInt(100) == 0) {
                        this.createChest(level, box, rand, 0, 0, k1 + 1, AtumLootTables.CRATE);
                    }

                    if (this.hasTarantula && !this.spawnerPlaced) {
                        int l1 = this.getWorldY(0);
                        int i2 = k1 - 1 + rand.nextInt(3);
                        int j2 = this.getWorldX(1, i2);
                        int k2 = this.getWorldZ(1, i2);
                        BlockPos blockpos = new BlockPos(j2, l1, k2);
                        if (box.isInside(blockpos) && this.isInterior(level, 1, 0, i2, box)) {
                            this.spawnerPlaced = true;
                            level.setBlock(blockpos, Blocks.SPAWNER.defaultBlockState(), 2);
                            BlockEntity tileEntity = level.getBlockEntity(blockpos);
                            if (tileEntity instanceof SpawnerBlockEntity) {
                                AtumMineshaftStructure.Type type = this.mineShaftType;
                                int chance = rand.nextInt(100);
                                if (chance < 40) {
                                    if (type == AtumMineshaftStructure.Type.DEADWOOD || type == AtumMineshaftStructure.Type.DEADWOOD_SURFACE) {
                                        ((SpawnerBlockEntity) tileEntity).getSpawner().setEntityId(AtumEntities.FORSAKEN);
                                    } else if (type == AtumMineshaftStructure.Type.LIMESTONE || type == AtumMineshaftStructure.Type.LIMESTONE_SURFACE) {
                                        ((SpawnerBlockEntity) tileEntity).getSpawner().setEntityId(AtumEntities.STONEGUARD);
                                    }
                                } else {
                                    ((SpawnerBlockEntity) tileEntity).getSpawner().setEntityId(AtumEntities.TARANTULA);
                                }
                            }
                        }
                    }
                }

                for (int l2 = 0; l2 <= 2; ++l2) {
                    for (int i3 = 0; i3 <= i1; ++i3) {
                        BlockState blockstate3 = this.getBlock(level, l2, -1, i3, box);
                        if (blockstate3.isAir() && this.isInterior(level, l2, -1, i3, box)) {
                            this.placeBlock(level, plankState, l2, -1, i3, box);
                        }
                    }
                }

                if (this.hasRails) {
                    BlockState railState = Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH);
                    for (int j3 = 0; j3 <= i1; ++j3) {
                        BlockState blockstate2 = this.getBlock(level, 1, -1, j3, box);
                        if (!blockstate2.isAir() && blockstate2.isSolidRender(level, new BlockPos(this.getWorldX(1, j3), this.getWorldY(-1), this.getWorldZ(1, j3)))) {
                            float f = this.isInterior(level, 1, 0, j3, box) ? 0.7F : 0.9F;
                            this.maybeGenerateBlock(level, box, rand, f, 1, 0, j3, railState);
                        }
                    }
                }
                return true;
            }
        }

        private void placeSupport(WorldGenLevel level, BoundingBox box, int x, int yMin, int zMin, int yMax, int zMax, Random rand) {
            if (this.isSupportingBox(level, box, x, zMax, yMax, zMin)) {
                BlockState plankState = this.getPlanksBlock();
                BlockState fenceState = this.getFenceBlock();
                BlockState torchState = this.getTorchBlock();
                this.generateBox(level, box, x, yMin, zMin, x, yMax - 1, zMin, fenceState, CAVE_AIR, false);
                this.generateBox(level, box, zMax, yMin, zMin, zMax, yMax - 1, zMin, fenceState, CAVE_AIR, false);
                if (rand.nextInt(4) == 0) {
                    this.generateBox(level, box, x, yMax, zMin, x, yMax, zMin, plankState, CAVE_AIR, false);
                    this.generateBox(level, box, zMax, yMax, zMin, zMax, yMax, zMin, plankState, CAVE_AIR, false);
                } else {
                    this.generateBox(level, box, x, yMax, zMin, zMax, yMax, zMin, plankState, CAVE_AIR, false);
                    this.maybeGenerateBlock(level, box, rand, 0.05F, x + 1, yMax, zMin - 1, torchState.setValue(WallTorchBlock.FACING, Direction.NORTH));
                    this.maybeGenerateBlock(level, box, rand, 0.05F, x + 1, yMax, zMin + 1, torchState.setValue(WallTorchBlock.FACING, Direction.SOUTH));
                }
            }
        }

        private void placeCobWeb(WorldGenLevel level, BoundingBox box, Random rand, float chance, int x, int y, int z) {
            if (this.isInterior(level, x, y, z, box)) {
                this.maybeGenerateBlock(level, box, rand, chance, x, y, z, Blocks.COBWEB.defaultBlockState());
            }
        }
    }

    public static class Cross extends Piece {
        private final Direction corridorDirection;
        private final boolean isMultipleFloors;

        public Cross(StructureManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.MINESHAFT_CROSSING, nbt);
            this.isMultipleFloors = nbt.getBoolean("tf");
            this.corridorDirection = Direction.from2DDataValue(nbt.getInt("D"));
        }

        @Override
        protected void addAdditionalSaveData(CompoundTag tagCompound) {
            super.addAdditionalSaveData(tagCompound);
            tagCompound.putBoolean("tf", this.isMultipleFloors);
            tagCompound.putInt("D", this.corridorDirection.get2DDataValue());
        }

        public Cross(int componentType, BoundingBox box, @Nullable Direction direction, AtumMineshaftStructure.Type type) {
            super(AtumStructurePieces.MINESHAFT_CROSSING, componentType, type);
            this.corridorDirection = direction;
            this.boundingBox = box;
            this.isMultipleFloors = box.getYSpan() > 3;
        }

        public static BoundingBox findCrossing(List<StructurePiece> list, Random rand, int x, int y, int z, Direction facing) {
            BoundingBox box = new BoundingBox(x, y, z, x, y + 3 - 1, z);
            if (rand.nextInt(4) == 0) {
                box.y1 += 4;
            }

            switch (facing) {
                case NORTH:
                default:
                    box.x0 = x - 1;
                    box.x1 = x + 3;
                    box.z0 = z - 4;
                    break;
                case SOUTH:
                    box.x0 = x - 1;
                    box.x1 = x + 3;
                    box.z1 = z + 3 + 1;
                    break;
                case WEST:
                    box.x0 = x - 4;
                    box.z0 = z - 1;
                    box.z1 = z + 3;
                    break;
                case EAST:
                    box.x1 = x + 3 + 1;
                    box.z0 = z - 1;
                    box.z1 = z + 3;
            }

            return StructurePiece.findCollisionPiece(list, box) != null ? null : box;
        }

        @Override
        public void addChildren(@Nonnull StructurePiece component, @Nonnull List<StructurePiece> list, @Nonnull Random rand) {
            int i = this.getGenDepth();
            switch (this.corridorDirection) {
                case NORTH:
                default:
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.WEST, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.EAST, i);
                    break;
                case SOUTH:
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.WEST, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.EAST, i);
                    break;
                case WEST:
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.WEST, i);
                    break;
                case EAST:
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, i);
                    generateAndAddPiece(component, list, rand, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.EAST, i);
            }

            if (this.isMultipleFloors) {
                if (rand.nextBoolean()) {
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 + 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z0 - 1, Direction.NORTH, i);
                }

                if (rand.nextBoolean()) {
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 - 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z0 + 1, Direction.WEST, i);
                }

                if (rand.nextBoolean()) {
                    generateAndAddPiece(component, list, rand, this.boundingBox.x1 + 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z0 + 1, Direction.EAST, i);
                }

                if (rand.nextBoolean()) {
                    generateAndAddPiece(component, list, rand, this.boundingBox.x0 + 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z1 + 1, Direction.SOUTH, i);
                }
            }
        }

        @Override
        public boolean postProcess(@Nonnull WorldGenLevel level, StructureFeatureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BoundingBox box, @Nonnull ChunkPos chunkPos, BlockPos pos) {
            if (this.edgesLiquid(level, box)) {
                return false;
            } else {
                BlockState plankState = this.getPlanksBlock();
                if (this.isMultipleFloors) {
                    this.generateBox(level, box, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0, this.boundingBox.x1 - 1, this.boundingBox.y0 + 3 - 1, this.boundingBox.z1, CAVE_AIR, CAVE_AIR, false);
                    this.generateBox(level, box, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.x1, this.boundingBox.y0 + 3 - 1, this.boundingBox.z1 - 1, CAVE_AIR, CAVE_AIR, false);
                    this.generateBox(level, box, this.boundingBox.x0 + 1, this.boundingBox.y1 - 2, this.boundingBox.z0, this.boundingBox.x1 - 1, this.boundingBox.y1, this.boundingBox.z1, CAVE_AIR, CAVE_AIR, false);
                    this.generateBox(level, box, this.boundingBox.x0, this.boundingBox.y1 - 2, this.boundingBox.z0 + 1, this.boundingBox.x1, this.boundingBox.y1, this.boundingBox.z1 - 1, CAVE_AIR, CAVE_AIR, false);
                    this.generateBox(level, box, this.boundingBox.x0 + 1, this.boundingBox.y0 + 3, this.boundingBox.z0 + 1, this.boundingBox.x1 - 1, this.boundingBox.y0 + 3, this.boundingBox.z1 - 1, CAVE_AIR, CAVE_AIR, false);
                } else {
                    this.generateBox(level, box, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0, this.boundingBox.x1 - 1, this.boundingBox.y1, this.boundingBox.z1, CAVE_AIR, CAVE_AIR, false);
                    this.generateBox(level, box, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.x1, this.boundingBox.y1, this.boundingBox.z1 - 1, CAVE_AIR, CAVE_AIR, false);
                }

                this.placeSupportPillar(level, box, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.y1);
                this.placeSupportPillar(level, box, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 - 1, this.boundingBox.y1);
                this.placeSupportPillar(level, box, this.boundingBox.x1 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.y1);
                this.placeSupportPillar(level, box, this.boundingBox.x1 - 1, this.boundingBox.y0, this.boundingBox.z1 - 1, this.boundingBox.y1);

                for (int i = this.boundingBox.x0; i <= this.boundingBox.x1; ++i) {
                    for (int j = this.boundingBox.z0; j <= this.boundingBox.z1; ++j) {
                        if (this.getBlock(level, i, this.boundingBox.y0 - 1, j, box).isAir() && this.isInterior(level, i, this.boundingBox.y0 - 1, j, box)) {
                            this.placeBlock(level, plankState, i, this.boundingBox.y0 - 1, j, box);
                        }
                    }
                }
                return true;
            }
        }

        private void placeSupportPillar(WorldGenLevel level, BoundingBox box, int x, int y, int z, int yMax) {
            if (!this.getBlock(level, x, yMax + 1, z, box).isAir()) {
                this.generateBox(level, box, x, y, z, x, yMax, z, this.getPlanksBlock(), CAVE_AIR, false);
            }
        }
    }

    abstract static class Piece extends StructurePiece {
        protected AtumMineshaftStructure.Type mineShaftType;

        public Piece(StructurePieceType structurePieceType, int componentType, AtumMineshaftStructure.Type type) {
            super(structurePieceType, componentType);
            this.mineShaftType = type;
        }

        public Piece(StructurePieceType type, CompoundTag nbt) {
            super(type, nbt);
            this.mineShaftType = AtumMineshaftStructure.Type.byId(nbt.getInt("MST"));
        }

        @Override
        protected void addAdditionalSaveData(CompoundTag tagCompound) {
            tagCompound.putInt("MST", this.mineShaftType.ordinal());
        }

        protected BlockState getPlanksBlock() {
            switch (this.mineShaftType) {
                case DEADWOOD:
                case DEADWOOD_SURFACE:
                default:
                    return AtumBlocks.DEADWOOD_PLANKS.defaultBlockState();
                case LIMESTONE:
                case LIMESTONE_SURFACE:
                    return AtumBlocks.LIMESTONE_BRICK_LARGE.defaultBlockState();
            }
        }

        protected BlockState getFenceBlock() {
            switch (this.mineShaftType) {
                case DEADWOOD:
                case DEADWOOD_SURFACE:
                default:
                    return AtumBlocks.DEADWOOD_FENCE.defaultBlockState();
                case LIMESTONE:
                case LIMESTONE_SURFACE:
                    return AtumBlocks.LARGE_WALL.defaultBlockState();
            }
        }

        protected BlockState getTorchBlock() {
            switch (this.mineShaftType) {
                case DEADWOOD:
                case DEADWOOD_SURFACE:
                default:
                    return AtumWallTorchUnlitBlock.UNLIT.get(AtumBlocks.DEADWOOD_TORCH).defaultBlockState();
                case LIMESTONE:
                case LIMESTONE_SURFACE:
                    return AtumWallTorchUnlitBlock.UNLIT.get(AtumBlocks.LIMESTONE_TORCH).defaultBlockState();
            }
        }

        protected boolean isSupportingBox(BlockGetter blockReader, BoundingBox box, int xStart, int xEnd, int x, int z) {
            for (int i = xStart; i <= xEnd; ++i) {
                if (this.getBlock(blockReader, i, x + 1, z, box).isAir()) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class Room extends Piece {
        private final List<BoundingBox> connectedRooms = Lists.newLinkedList();

        public Room(int componentType, Random rand, int x, int z, AtumMineshaftStructure.Type type) {
            super(AtumStructurePieces.MINESHAFT_ROOM, componentType, type);
            this.mineShaftType = type;
            this.boundingBox = new BoundingBox(x, 50, z, x + 7 + rand.nextInt(6), 54 + rand.nextInt(6), z + 7 + rand.nextInt(6));
        }

        public Room(StructureManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.MINESHAFT_ROOM, nbt);
            ListTag listnbt = nbt.getList("Entrances", 11);

            for (int i = 0; i < listnbt.size(); ++i) {
                this.connectedRooms.add(new BoundingBox(listnbt.getIntArray(i)));
            }
        }

        @Override
        public void addChildren(@Nonnull StructurePiece component, @Nonnull List<StructurePiece> list, @Nonnull Random rand) {
            int i = this.getGenDepth();
            int j = this.boundingBox.getYSpan() - 3 - 1;
            if (j <= 0) {
                j = 1;
            }

            int k;
            for (k = 0; k < this.boundingBox.getXSpan(); k = k + 4) {
                k = k + rand.nextInt(this.boundingBox.getXSpan());
                if (k + 3 > this.boundingBox.getXSpan()) {
                    break;
                }

                Piece piece = generateAndAddPiece(component, list, rand, this.boundingBox.x0 + k, this.boundingBox.y0 + rand.nextInt(j) + 1, this.boundingBox.z0 - 1, Direction.NORTH, i);
                if (piece != null) {
                    BoundingBox box = piece.getBoundingBox();
                    this.connectedRooms.add(new BoundingBox(box.x0, box.y0, this.boundingBox.z0, box.x1, box.y1, this.boundingBox.z0 + 1));
                }
            }

            for (k = 0; k < this.boundingBox.getXSpan(); k = k + 4) {
                k = k + rand.nextInt(this.boundingBox.getXSpan());
                if (k + 3 > this.boundingBox.getXSpan()) {
                    break;
                }

                Piece piece = generateAndAddPiece(component, list, rand, this.boundingBox.x0 + k, this.boundingBox.y0 + rand.nextInt(j) + 1, this.boundingBox.z1 + 1, Direction.SOUTH, i);
                if (piece != null) {
                    BoundingBox box = piece.getBoundingBox();
                    this.connectedRooms.add(new BoundingBox(box.x0, box.y0, this.boundingBox.z1 - 1, box.x1, box.y1, this.boundingBox.z1));
                }
            }

            for (k = 0; k < this.boundingBox.getZSpan(); k = k + 4) {
                k = k + rand.nextInt(this.boundingBox.getZSpan());
                if (k + 3 > this.boundingBox.getZSpan()) {
                    break;
                }

                Piece piece = generateAndAddPiece(component, list, rand, this.boundingBox.x0 - 1, this.boundingBox.y0 + rand.nextInt(j) + 1, this.boundingBox.z0 + k, Direction.WEST, i);
                if (piece != null) {
                    BoundingBox box = piece.getBoundingBox();
                    this.connectedRooms.add(new BoundingBox(this.boundingBox.x0, box.y0, box.z0, this.boundingBox.x0 + 1, box.y1, box.z1));
                }
            }

            for (k = 0; k < this.boundingBox.getZSpan(); k = k + 4) {
                k = k + rand.nextInt(this.boundingBox.getZSpan());
                if (k + 3 > this.boundingBox.getZSpan()) {
                    break;
                }

                StructurePiece piece = generateAndAddPiece(component, list, rand, this.boundingBox.x1 + 1, this.boundingBox.y0 + rand.nextInt(j) + 1, this.boundingBox.z0 + k, Direction.EAST, i);
                if (piece != null) {
                    BoundingBox box = piece.getBoundingBox();
                    this.connectedRooms.add(new BoundingBox(this.boundingBox.x1 - 1, box.y0, box.z0, this.boundingBox.x1, box.y1, box.z1));
                }
            }

        }

        @Override
        public boolean postProcess(@Nonnull WorldGenLevel level, @Nonnull StructureFeatureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BoundingBox box, @Nonnull ChunkPos chunkPos, BlockPos pos) {
            if (this.edgesLiquid(level, box)) {
                return false;
            } else {
                this.generateBox(level, box, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0, this.boundingBox.x1, this.boundingBox.y0, this.boundingBox.z1, AtumBlocks.SAND.defaultBlockState(), CAVE_AIR, true);
                this.generateBox(level, box, this.boundingBox.x0, this.boundingBox.y0 + 1, this.boundingBox.z0, this.boundingBox.x1, Math.min(this.boundingBox.y0 + 3, this.boundingBox.y1), this.boundingBox.z1, CAVE_AIR, CAVE_AIR, false);

                for (BoundingBox connectedRooms : this.connectedRooms) {
                    this.generateBox(level, box, connectedRooms.x0, connectedRooms.y1 - 2, connectedRooms.z0, connectedRooms.x1, connectedRooms.y1, connectedRooms.z1, CAVE_AIR, CAVE_AIR, false);
                }

                this.generateUpperHalfSphere(level, box, this.boundingBox.x0, this.boundingBox.y0 + 4, this.boundingBox.z0, this.boundingBox.x1, this.boundingBox.y1, this.boundingBox.z1, CAVE_AIR, false);
                return true;
            }
        }

        @Override
        public void move(int x, int y, int z) {
            super.move(x, y, z);

            for (BoundingBox connectedRooms : this.connectedRooms) {
                connectedRooms.move(x, y, z);
            }

        }

        @Override
        protected void addAdditionalSaveData(CompoundTag tagCompound) {
            super.addAdditionalSaveData(tagCompound);
            ListTag listnbt = new ListTag();

            for (BoundingBox connectedRooms : this.connectedRooms) {
                listnbt.add(connectedRooms.createTag());
            }
            tagCompound.put("Entrances", listnbt);
        }
    }

    public static class Stairs extends Piece {

        public Stairs(int componentType, BoundingBox box, Direction direction, AtumMineshaftStructure.Type type) {
            super(AtumStructurePieces.MINESHAFT_STAIRS, componentType, type);
            this.setOrientation(direction);
            this.boundingBox = box;
        }

        public Stairs(StructureManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.MINESHAFT_STAIRS, nbt);
        }

        public static BoundingBox findStairs(List<StructurePiece> list, Random rand, int x, int y, int z, Direction facing) {
            BoundingBox box = new BoundingBox(x, y - 5, z, x, y + 3 - 1, z);
            switch (facing) {
                case NORTH:
                default:
                    box.x1 = x + 3 - 1;
                    box.z0 = z - 8;
                    break;
                case SOUTH:
                    box.x1 = x + 3 - 1;
                    box.z1 = z + 8;
                    break;
                case WEST:
                    box.x0 = x - 8;
                    box.z1 = z + 3 - 1;
                    break;
                case EAST:
                    box.x1 = x + 8;
                    box.z1 = z + 3 - 1;
            }
            return StructurePiece.findCollisionPiece(list, box) != null ? null : box;
        }

        @Override
        public void addChildren(@Nonnull StructurePiece component, @Nonnull List<StructurePiece> list, @Nonnull Random rand) {
            int i = this.getGenDepth();
            Direction direction = this.getOrientation();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        generateAndAddPiece(component, list, rand, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, i);
                        break;
                    case SOUTH:
                        generateAndAddPiece(component, list, rand, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, i);
                        break;
                    case WEST:
                        generateAndAddPiece(component, list, rand, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0, Direction.WEST, i);
                        break;
                    case EAST:
                        generateAndAddPiece(component, list, rand, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0, Direction.EAST, i);
                }
            }

        }

        @Override
        public boolean postProcess(@Nonnull WorldGenLevel level, @Nonnull StructureFeatureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BoundingBox box, @Nonnull ChunkPos chunkPos, BlockPos pos) {
            if (this.edgesLiquid(level, box)) {
                return false;
            } else {
                this.generateBox(level, box, 0, 5, 0, 2, 7, 1, CAVE_AIR, CAVE_AIR, false);
                this.generateBox(level, box, 0, 0, 7, 2, 2, 8, CAVE_AIR, CAVE_AIR, false);

                for (int i = 0; i < 5; ++i) {
                    this.generateBox(level, box, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, CAVE_AIR, CAVE_AIR, false);
                }
                return true;
            }
        }
    }
}*/
