package com.teammetallurgy.atum.world.gen.structure.pyramid;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.QuandaryBlock;
import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchUnlitBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.blocks.trap.TrapBlock;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PyramidPieces {
    public static final ResourceLocation PYRAMID = new ResourceLocation(Atum.MOD_ID, "pyramid");

    // For the maze to generate correctly, it must be an odd width and depth
    private static final int MAZE_SIZE_X = 27;
    private static final int MAZE_SIZE_Z = 25;

    static List<StructurePiece> getComponents(StructureManager manager, BlockPos pos, Rotation rotation) {
        List<StructurePiece> components = Lists.newArrayList();
        PyramidTemplate template = new PyramidTemplate(manager, pos, rotation);
        Maze maze = new Maze(getMazeBounds(template.getBoundingBox(), template.rotation), template.getOrientation());
        components.add(template);
        components.add(maze);
        return components;
    }

    private static BoundingBox getMazeBounds(BoundingBox pyramidBounds, Rotation rotation) {
        // If the pyramid is rotated the bounding box needs to be rotated also. Since it is just a collection
        // of min and max values, we just need to change which direction the width and height go. The maze
        // also needs to be offset from the pyramid corner to align with the maze entrance and to fit in the
        // pyramid. The offset values were chosen by trial and error, and could probably be calculated in a
        // better way.
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

        return BoundingBox.createProper(
                pyramidBounds.x0 + xOffset,
                pyramidBounds.y0 + 6,
                pyramidBounds.z0 + zOffset,
                pyramidBounds.x0 + xOffset + width - 1,
                pyramidBounds.y0 + 7,
                pyramidBounds.z0 + zOffset + depth - 1);
    }

    public static class PyramidTemplate extends TemplateStructurePiece {
        public static final List<Block> FLOOR_TRAPS = Arrays.asList(AtumBlocks.BURNING_TRAP, AtumBlocks.POISON_TRAP, AtumBlocks.SMOKE_TRAP, AtumBlocks.TAR_TRAP);
        static final BlockState CARVED_BRICK = AtumBlocks.LIMESTONE_BRICK_CARVED.defaultBlockState().setValue(LimestoneBrickBlock.UNBREAKABLE, true);
        private static final EntityType<?> UNDEAD_SPAWNER_PAIR = RuinPieces.RuinTemplate.UNDEAD.get(new Random().nextInt(RuinPieces.RuinTemplate.UNDEAD.size()));
        private final Rotation rotation;

        public PyramidTemplate(StructureManager manager, BlockPos pos, Rotation rotation) {
            super(AtumStructurePieces.PYRAMID, 0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.loadTemplate(manager);
        }

        public PyramidTemplate(StructureManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.PYRAMID, nbt);
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.loadTemplate(manager);
        }

        private void loadTemplate(StructureManager templateManager) {
            StructureTemplate template = templateManager.get(PYRAMID);
            StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(Mirror.NONE).setBoundingBox(this.boundingBox).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull ServerLevelAccessor world, @Nonnull Random rand, @Nonnull BoundingBox box) {
            if (function.startsWith("Arrow")) {
                Rotation rotation = this.placeSettings.getRotation();
                BlockState arrowTrap = AtumBlocks.ARROW_TRAP.defaultBlockState();

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
                    world.setBlock(pos, arrowTrap, 2);
                } else {
                    world.setBlock(pos, CARVED_BRICK, 2);
                }
            } else if (function.startsWith("Floor")) {
                switch (function) {
                    case "FloorTrap":
                        if (rand.nextDouble() <= 0.5D) {
                            Block trap = FLOOR_TRAPS.get(rand.nextInt(FLOOR_TRAPS.size()));
                            world.setBlock(pos, trap.defaultBlockState().setValue(TrapBlock.FACING, Direction.UP), 2);
                        } else {
                            world.setBlock(pos, CARVED_BRICK, 2);
                        }
                        break;
                    case "FloorCopy":
                        this.setTrapsCopy(world, pos, rand, box, 2);
                        break;
                    case "FloorBox":
                    case "FloorSpace":
                        this.setTrapsCopy(world, pos, rand, box, 3);
                        break;
                }
            } else if (function.contains("Spawner")) {
                if (function.equals("SpawnerUndead")) {
                    if (box.isInside(pos)) {
                        world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

                        BlockEntity tileEntity = world.getBlockEntity(pos);
                        if (tileEntity instanceof SpawnerBlockEntity) {
                            EntityType<?> entityType = RuinPieces.RuinTemplate.UNDEAD.get(rand.nextInt(RuinPieces.RuinTemplate.UNDEAD.size()));
                            ((SpawnerBlockEntity) tileEntity).getSpawner().setEntityId(entityType);
                        }
                    }
                } else if (function.equals("SpawnerUndeadPair")) {
                    if (box.isInside(pos)) {
                        world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

                        BlockEntity tileEntity = world.getBlockEntity(pos);
                        if (tileEntity instanceof SpawnerBlockEntity) {
                            ((SpawnerBlockEntity) tileEntity).getSpawner().setEntityId(UNDEAD_SPAWNER_PAIR);
                        }
                    }
                }
            } else if (function.equals("CrateChance")) {
                if (box.isInside(pos)) {
                    if (rand.nextDouble() <= 0.2D) {
                        world.setBlock(pos, ChestBaseBlock.correctFacing(world, pos, AtumBlocks.DEADWOOD_CRATE.defaultBlockState(), AtumBlocks.DEADWOOD_CRATE), 2);
                        RandomizableContainerBlockEntity.setLootTable(world, rand, pos, AtumLootTables.CRATE);
                    } else {
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            } else if (function.equals("Chest")) {
                BlockPos posDown = pos.below();
                if (box.isInside(posDown)) {
                    RandomizableContainerBlockEntity.setLootTable(world, rand, posDown, AtumLootTables.PYRAMID_CHEST);
                }
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            } else if (function.equals("Sarcophagus")) {
                BlockPos posDown = pos.below();
                if (box.isInside(posDown)) {
                    RandomizableContainerBlockEntity.setLootTable(world, rand, posDown, AtumLootTables.PHARAOH);
                }
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            } else if (function.equals("NebuTorch")) {
                if (box.isInside(pos)) {
                    if (rand.nextDouble() <= 0.25D) {
                        world.setBlock(pos, AtumTorchUnlitBlock.UNLIT.get(AtumBlocks.NEBU_TORCH).defaultBlockState(), 2);
                    } else {
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            } else if (function.equals("EntrancePuzzle")) {
                if (box.isInside(pos)) {
                    world.setBlock(pos, AtumBlocks.QUANDARY_BLOCK.defaultBlockState().setValue(QuandaryBlock.UNBREAKABLE, true).setValue(QuandaryBlock.FACING, StructureHelper.getDirectionFromRotation(this.getRotation())), 2);
                }
            }
        }

        private void setTrapsCopy(LevelAccessor world, BlockPos pos, Random rand, BoundingBox box, int range) {
            if (rand.nextDouble() <= 0.5D) {
                BlockState copy = null;
                for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                    for (int xMin = 0; xMin <= range; xMin++) {
                        BlockPos posOffset = pos.relative(horizontal, xMin);
                        if (box.isInside(posOffset)) {
                            BlockState adjacent = world.getBlockState(pos.relative(horizontal, xMin));
                            if (adjacent.getBlock() instanceof TrapBlock) {
                                copy = adjacent;
                            }
                        }
                    }
                }
                if (copy != null) {
                    world.setBlock(pos, copy, 2);
                } else {
                    Block trap = FLOOR_TRAPS.get(rand.nextInt(FLOOR_TRAPS.size()));
                    world.setBlock(pos, trap.defaultBlockState().setValue(TrapBlock.FACING, Direction.UP), 2);
                }
            } else {
                world.setBlock(pos, CARVED_BRICK, 2);
            }
        }

        @Override
        protected void addAdditionalSaveData(@Nonnull CompoundTag compound) { //Is actually write, just horrible name
            super.addAdditionalSaveData(compound);
            compound.putString("Rot", this.placeSettings.getRotation().name());
        }
    }

    public static class Maze extends StructurePiece {
        private boolean[][] maze = null;

        public Maze(BoundingBox boundingBox, Direction componentType) {
            super(AtumStructurePieces.PYRAMID_MAZE, 0);
            this.setOrientation(componentType);
            this.boundingBox = boundingBox;
        }

        public Maze(StructureManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.PYRAMID_MAZE, nbt);
        }

        @Override
        public boolean postProcess(@Nonnull WorldGenLevel world, @Nonnull StructureFeatureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random random, @Nonnull BoundingBox box, @Nonnull ChunkPos chunkPos, BlockPos pos) {
            this.addMaze(world, random, box);
            return true;
        }

        private void addMaze(WorldGenLevel world, Random random, BoundingBox validBounds) {
            if (this.maze == null) {
                this.maze = this.generateMaze(new Random(world.getSeed() * this.boundingBox.x0 * this.boundingBox.z0), this.boundingBox.getXSpan(), this.boundingBox.getZSpan());
            }

            for (int x = 0; x < this.boundingBox.getXSpan(); x++) {
                for (int z = 0; z < this.boundingBox.getZSpan(); z++) {
                    //Set pathway
                    this.placeBlock(world, Blocks.AIR.defaultBlockState(), x, 0, z, validBounds);
                    this.placeBlock(world, Blocks.AIR.defaultBlockState(), x, 1, z, validBounds);

                    // Make sure the blocks above the entrance to the lower levels are clear
                    if (this.getBlock(world, x, -1, z, validBounds).getBlock() instanceof LadderBlock) {
                        this.placeBlock(world, Blocks.AIR.defaultBlockState(), x, 0, z, validBounds);
                        this.placeBlock(world, Blocks.AIR.defaultBlockState(), x, 1, z, validBounds);
                    }
                    // Set the blocks for the walls of the maze
                    else if (!maze[x][z]) {
                        this.placeBlock(world, PyramidPieces.PyramidTemplate.CARVED_BRICK, x, 0, z, validBounds);
                        this.placeBlock(world, PyramidPieces.PyramidTemplate.CARVED_BRICK, x, 1, z, validBounds);
                        if (random.nextDouble() <= 0.10D) {
                            placeTrap(world, maze, x, z, random, validBounds);
                        }
                    }
                    // Place sand of the floor of the maze
                    else {
                        int layers = Mth.nextInt(random, 1, 2);
                        this.placeBlock(world, AtumBlocks.SAND_LAYERED.defaultBlockState().setValue(SandLayersBlock.LAYERS, layers), x, 0, z, validBounds);
                    }
                }
            }
        }

        @Override
        protected void placeBlock(@Nonnull WorldGenLevel world, @Nonnull BlockState state, int x, int y, int z, @Nonnull BoundingBox box) {
            BlockPos pos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
            if (box.isInside(pos) && !(world.getBlockState(pos).getBlock() instanceof LadderBlock)) { //Make sure ladder blocks don't get replaced
                super.placeBlock(world, state, x, y, z, box);
            }
        }

        private void placeTrap(WorldGenLevel world, boolean[][] maze, int x, int z, Random random, BoundingBox validBounds) {
            BlockState trapState = PyramidPieces.PyramidTemplate.FLOOR_TRAPS.get(random.nextInt(PyramidPieces.PyramidTemplate.FLOOR_TRAPS.size())).defaultBlockState();

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
                this.placeBlock(world, trapState, x, 0, z, validBounds);
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
        protected void addAdditionalSaveData(@Nonnull CompoundTag compound) {
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