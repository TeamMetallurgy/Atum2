package com.teammetallurgy.atum.world.gen.structure.pyramid;

import com.teammetallurgy.atum.blocks.QuandaryBlock;
import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchUnlitBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.LimestoneChestTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.blocks.trap.TrapBlock;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinPieces;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PyramidPieces {
    // For the maze to generate correctly, it must be an odd width and depth
    private static final int MAZE_SIZE_X = 27;
    private static final int MAZE_SIZE_Z = 25;

    static StructurePiecesBuilder addComponents(StructurePiecesBuilder piecesBuilder, StructureTemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation) {
        PyramidTemplate template = new PyramidTemplate(manager, location, pos, rotation);
        Maze maze = new Maze(getMazeBounds(template.getBoundingBox(), template.getRotation()), template.getOrientation());
        piecesBuilder.addPiece(template);
        piecesBuilder.addPiece(maze);
        return piecesBuilder;
    }

    private static BoundingBox getMazeBounds(BoundingBox pyramidBounds, Rotation rotation) {
        // If the pyramid is rotated the bounding box needs to be rotated also. Since it is just a collection
        // of min and max values, we just need to change which direction the width and height go. The maze
        // also needs to be offset from the pyramid corner to align with the maze entrance and to fit in the
        // pyramid. The offset values were chosen by trial and error, and could probably be calculated in a better way.
        int width = MAZE_SIZE_X;
        int depth = MAZE_SIZE_Z;
        int xOffset = 2;
        int zOffset = 5;

        if (rotation == Rotation.CLOCKWISE_90) {
            zOffset = 2;
            width = MAZE_SIZE_Z;
            depth = MAZE_SIZE_X;
        } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
            xOffset = 5;
            zOffset = 3;
            width = MAZE_SIZE_Z;
            depth = MAZE_SIZE_X;
        } else if (rotation == Rotation.CLOCKWISE_180) {
            xOffset = 3;
            zOffset = 6;
        }

        return new BoundingBox(
                pyramidBounds.minX() + xOffset,
                pyramidBounds.minY() + 6,
                pyramidBounds.minZ() + zOffset,
                pyramidBounds.minX() + xOffset + width - 1,
                pyramidBounds.minY() + 7,
                pyramidBounds.minZ() + zOffset + depth - 1);
    }

    public static class PyramidTemplate extends TemplateStructurePiece {
        private static final EntityType<?> UNDEAD_SPAWNER_PAIR = RuinPieces.getUndead().get(new Random().nextInt(RuinPieces.getUndead().size()));

        public PyramidTemplate(StructureTemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation) {
            super(AtumStructurePieces.PYRAMID.get(), 0, manager, location, location.toString(), makeSettings(rotation), pos);
        }

        public PyramidTemplate(StructureTemplateManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.PYRAMID.get(), nbt, manager, (t) -> makeSettings(Rotation.valueOf(nbt.getString("Rot"))));
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            return (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        public static BlockState getCarvedBrick() {
            return AtumBlocks.LIMESTONE_BRICK_CARVED.get().defaultBlockState().setValue(LimestoneBrickBlock.UNBREAKABLE, true);
        }

        public static List<Block> getFloorTraps() {
            return Arrays.asList(AtumBlocks.BURNING_TRAP.get(), AtumBlocks.POISON_TRAP.get(), AtumBlocks.SMOKE_TRAP.get(), AtumBlocks.TAR_TRAP.get());
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull ServerLevelAccessor level, @Nonnull RandomSource rand, @Nonnull BoundingBox box) {
            if (function.startsWith("Arrow")) {
                Rotation rotation = this.placeSettings.getRotation();
                BlockState arrowTrap = AtumBlocks.ARROW_TRAP.get().defaultBlockState();

                if (rand.nextDouble() <= 0.3D) {
                    switch (function) {
                        case "ArrowWest":
                            arrowTrap = arrowTrap.setValue(TrapBlock.FACING, rotation.rotate(Direction.WEST));
                            break;
                        case "ArrowEast":
                            arrowTrap = arrowTrap.setValue(TrapBlock.FACING, rotation.rotate(Direction.EAST));
                            break;
                        case "ArrowSouth":
                            arrowTrap = arrowTrap.setValue(TrapBlock.FACING, rotation.rotate(Direction.SOUTH));
                            break;
                        case "ArrowNorth":
                            arrowTrap = arrowTrap.setValue(TrapBlock.FACING, rotation.rotate(Direction.NORTH));
                            break;
                    }
                    level.setBlock(pos, arrowTrap, 2);
                } else {
                    level.setBlock(pos, getCarvedBrick(), 2);
                }
            } else if (function.startsWith("Floor")) {
                switch (function) {
                    case "FloorTrap":
                        if (rand.nextDouble() <= 0.5D) {
                            Block trap = getFloorTraps().get(rand.nextInt(getFloorTraps().size()));
                            level.setBlock(pos, trap.defaultBlockState().setValue(TrapBlock.FACING, Direction.UP), 2);
                        } else {
                            level.setBlock(pos, getCarvedBrick(), 2);
                        }
                        break;
                    case "FloorCopy":
                        this.setTrapsCopy(level, pos, rand, box, 2);
                        break;
                    case "FloorBox":
                    case "FloorSpace":
                        this.setTrapsCopy(level, pos, rand, box, 3);
                        break;
                }
            } else if (function.contains("Spawner")) {
                if (function.equals("SpawnerUndead")) {
                    if (box.isInside(pos)) {
                        level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

                        BlockEntity tileEntity = level.getBlockEntity(pos);
                        if (tileEntity instanceof SpawnerBlockEntity) {
                            EntityType<?> entityType = RuinPieces.getUndead().get(rand.nextInt(RuinPieces.getUndead().size()));
                            ((SpawnerBlockEntity) tileEntity).setEntityId(entityType, rand);
                        }
                    }
                } else if (function.equals("SpawnerUndeadPair")) {
                    if (box.isInside(pos)) {
                        level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

                        BlockEntity tileEntity = level.getBlockEntity(pos);
                        if (tileEntity instanceof SpawnerBlockEntity) {
                            ((SpawnerBlockEntity) tileEntity).setEntityId(UNDEAD_SPAWNER_PAIR, rand);
                        }
                    }
                }
            } else if (function.equals("CrateChance")) {
                if (box.isInside(pos)) {
                    if (rand.nextDouble() <= 0.2D) {
                        level.setBlock(pos, ChestBaseBlock.correctFacing(level, pos, AtumBlocks.DEADWOOD_CRATE.get().defaultBlockState(), AtumBlocks.DEADWOOD_CRATE.get()), 2);
                        if (level.getBlockEntity(pos) instanceof CrateTileEntity crate) {
                            crate.setLootTable(AtumLootTables.CRATE, rand.nextLong());
                        }
                    } else {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            } else if (function.equals("Chest")) {
                BlockPos posDown = pos.below();
                if (box.isInside(posDown)) {
                    if (level.getBlockEntity(posDown) instanceof LimestoneChestTileEntity limestoneChest) {
                        limestoneChest.setLootTable(AtumLootTables.PYRAMID_CHEST, rand.nextLong());
                    }
                }
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            } else if (function.equals("Sarcophagus")) {
                BlockPos posDown = pos.below();
                if (box.isInside(posDown)) {
                    if (level.getBlockEntity(posDown) instanceof SarcophagusTileEntity sarcophagus) {
                        sarcophagus.setLootTable(AtumLootTables.PHARAOH, rand.nextLong());
                    }
                }
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            } else if (function.equals("NebuTorch")) {
                if (box.isInside(pos)) {
                    if (rand.nextDouble() <= 0.25D) {
                        level.setBlock(pos, AtumTorchUnlitBlock.UNLIT.get(AtumBlocks.NEBU_TORCH).get().defaultBlockState(), 2);
                    } else {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            } else if (function.equals("EntrancePuzzle")) {
                if (box.isInside(pos)) {
                    level.setBlock(pos, AtumBlocks.QUANDARY_BLOCK.get().defaultBlockState().setValue(QuandaryBlock.UNBREAKABLE, true).setValue(QuandaryBlock.FACING, StructureHelper.getDirectionFromRotation(this.getRotation())), 2);
                }
            }
        }

        private void setTrapsCopy(LevelAccessor level, BlockPos pos, RandomSource rand, BoundingBox box, int range) {
            if (rand.nextDouble() <= 0.5D) {
                BlockState copy = null;
                for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                    for (int xMin = 0; xMin <= range; xMin++) {
                        BlockPos posOffset = pos.relative(horizontal, xMin);
                        if (box.isInside(posOffset)) {
                            BlockState adjacent = level.getBlockState(pos.relative(horizontal, xMin));
                            if (adjacent.getBlock() instanceof TrapBlock) {
                                copy = adjacent;
                            }
                        }
                    }
                }
                if (copy != null) {
                    level.setBlock(pos, copy, 2);
                } else {
                    Block trap = getFloorTraps().get(rand.nextInt(getFloorTraps().size()));
                    level.setBlock(pos, trap.defaultBlockState().setValue(TrapBlock.FACING, Direction.UP), 2);
                }
            } else {
                level.setBlock(pos, getCarvedBrick(), 2);
            }
        }

        @Override
        protected void addAdditionalSaveData(@Nonnull StructurePieceSerializationContext context, @Nonnull CompoundTag compound) { //Is actually write, just horrible name
            super.addAdditionalSaveData(context, compound);
            compound.putString("Rot", this.placeSettings.getRotation().name());
        }
    }

    public static class Maze extends StructurePiece {
        private boolean[][] maze = null;

        public Maze(BoundingBox boundingBox, Direction componentType) {
            super(AtumStructurePieces.PYRAMID_MAZE.get(), 0, boundingBox);
            this.setOrientation(componentType);
            this.boundingBox = boundingBox;
        }

        public Maze(CompoundTag nbt) {
            super(AtumStructurePieces.PYRAMID_MAZE.get(), nbt);
        }

        @Override
        public void postProcess(@Nonnull WorldGenLevel level, @Nonnull StructureManager manager, @Nonnull ChunkGenerator generator, @Nonnull RandomSource random, @Nonnull BoundingBox box, @Nonnull ChunkPos chunkPos, @Nonnull BlockPos pos) {
            this.addMaze(level, random, box);
        }

        private void addMaze(WorldGenLevel level, RandomSource random, BoundingBox validBounds) {
            if (this.maze == null) {
                this.maze = this.generateMaze(new Random(level.getSeed() * this.boundingBox.minX() * this.boundingBox.minZ()), this.boundingBox.getXSpan(), this.boundingBox.getZSpan());
            }

            for (int x = 0; x < this.boundingBox.getXSpan(); x++) {
                for (int z = 0; z < this.boundingBox.getZSpan(); z++) {
                    //Set pathway
                    this.placeBlock(level, Blocks.AIR.defaultBlockState(), x, 0, z, validBounds);
                    this.placeBlock(level, Blocks.AIR.defaultBlockState(), x, 1, z, validBounds);

                    // Make sure the blocks above the entrance to the lower levels are clear
                    if (this.getBlock(level, x, -1, z, validBounds).getBlock() instanceof LadderBlock) {
                        this.placeBlock(level, Blocks.AIR.defaultBlockState(), x, 0, z, validBounds);
                        this.placeBlock(level, Blocks.AIR.defaultBlockState(), x, 1, z, validBounds);
                    }
                    // Set the blocks for the walls of the maze
                    else if (!maze[x][z]) {
                        this.placeBlock(level, PyramidPieces.PyramidTemplate.getCarvedBrick(), x, 0, z, validBounds);
                        this.placeBlock(level, PyramidPieces.PyramidTemplate.getCarvedBrick(), x, 1, z, validBounds);
                        if (random.nextDouble() <= 0.10D) {
                            placeTrap(level, maze, x, z, random, validBounds);
                        }
                    }
                    // Place sand of the floor of the maze
                    else {
                        int layers = Mth.nextInt(random, 1, 2);
                        this.placeBlock(level, AtumBlocks.STRANGE_SAND_LAYERED.get().defaultBlockState().setValue(SandLayersBlock.LAYERS, layers), x, 0, z, validBounds);
                    }
                }
            }
        }

        @Override
        protected void placeBlock(@Nonnull WorldGenLevel level, @Nonnull BlockState state, int x, int y, int z, @Nonnull BoundingBox box) {
            BlockPos pos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
            if (box.isInside(pos) && !(level.getBlockState(pos).getBlock() instanceof LadderBlock)) { //Make sure ladder blocks don't get replaced
                super.placeBlock(level, state, x, y, z, box);
            }
        }

        private void placeTrap(WorldGenLevel level, boolean[][] maze, int x, int z, RandomSource random, BoundingBox validBounds) {
            BlockState trapState = PyramidPieces.PyramidTemplate.getFloorTraps().get(random.nextInt(PyramidPieces.PyramidTemplate.getFloorTraps().size())).defaultBlockState();

            List<Direction> validDirections = new ArrayList<>();
            for (Direction facing : Direction.Plane.HORIZONTAL) {
                if (x + facing.getStepX() >= 0 && x + facing.getStepX() < maze.length && z + facing.getStepZ() >= 0 && z + facing.getStepZ() < maze[0].length) {
                    if (maze[x + facing.getStepX()][z + facing.getStepZ()]) {
                        validDirections.add(facing.getOpposite());
                    }
                }
            }

            if (!validDirections.isEmpty()) {
                trapState = trapState.setValue(TrapBlock.FACING, validDirections.get(random.nextInt(validDirections.size())));
                this.placeBlock(level, trapState, x, 0, z, validBounds);
            }
        }

        // Generate an maze using prim's algorithm.
        private boolean[][] generateMaze(Random random, int sizeX, int sizeZ) {
            boolean[][] array = new boolean[sizeX][sizeZ];
            return generateMazeRecursive(array, random, 1, 1);
        }

        // Generate a random walk going two blocks each time. Whenever a
        // wall is hit, retrace your steps until you can try another path
        // and continue. Repeat this until the maze if filled.
        private boolean[][] generateMazeRecursive(boolean[][] array, Random random, int x, int z) { //Originally made by RebelKeithy
            ArrayList<Pair> choices = new ArrayList<>();
            do {
                int innerSizeX = array.length - 1;
                int innerSizeZ = array[0].length - 1;
                choices.clear();
                if (x + 2 < innerSizeX && !array[x + 2][z]) {
                    choices.add(new Pair(2, 0));
                }
                if (x - 2 >= 0 && !array[x - 2][z]) {
                    choices.add(new Pair(-2, 0));
                }
                if (z + 2 < innerSizeZ && !array[x][z + 2]) {
                    choices.add(new Pair(0, 2));
                }
                if (z - 2 >= 0 && !array[x][z - 2]) {
                    choices.add(new Pair(0, -2));
                }
                if (choices.size() > 0) {
                    int xMin = random.nextInt(choices.size());
                    Pair choice = choices.get(xMin);
                    choices.remove(xMin);
                    array[choice.x + x][choice.y + z] = true;
                    array[x + choice.x / 2][z + choice.y / 2] = true;
                    this.generateMazeRecursive(array, random, x + choice.x, z + choice.y);
                }
            } while (choices.size() > 0);
            return array;
        }

        @Override
        protected void addAdditionalSaveData(@Nonnull StructurePieceSerializationContext context, @Nonnull CompoundTag compound) {
        }

        static class Pair {
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
}