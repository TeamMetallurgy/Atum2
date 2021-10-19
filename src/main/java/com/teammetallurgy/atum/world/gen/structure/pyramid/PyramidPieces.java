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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

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

    static List<StructurePiece> getComponents(TemplateManager manager, BlockPos pos, Rotation rotation) {
        List<StructurePiece> components = Lists.newArrayList();
        PyramidTemplate template = new PyramidTemplate(manager, pos, rotation);
        Maze maze = new Maze(getMazeBounds(template.getBoundingBox(), template.rotation), template.getCoordBaseMode());
        components.add(template);
        components.add(maze);
        return components;
    }

    private static MutableBoundingBox getMazeBounds(MutableBoundingBox pyramidBounds, Rotation rotation) {
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

        return MutableBoundingBox.createProper(
                pyramidBounds.minX + xOffset,
                pyramidBounds.minY + 6,
                pyramidBounds.minZ + zOffset,
                pyramidBounds.minX + xOffset + width - 1,
                pyramidBounds.minY + 7,
                pyramidBounds.minZ + zOffset + depth - 1);
    }

    public static class PyramidTemplate extends TemplateStructurePiece {
        public static final List<Block> FLOOR_TRAPS = Arrays.asList(AtumBlocks.BURNING_TRAP, AtumBlocks.POISON_TRAP, AtumBlocks.SMOKE_TRAP, AtumBlocks.TAR_TRAP);
        static final BlockState CARVED_BRICK = AtumBlocks.LIMESTONE_BRICK_CARVED.getDefaultState().with(LimestoneBrickBlock.UNBREAKABLE, true);
        private static final EntityType<?> UNDEAD_SPAWNER_PAIR = RuinPieces.RuinTemplate.UNDEAD.get(new Random().nextInt(RuinPieces.RuinTemplate.UNDEAD.size()));
        private final Rotation rotation;

        public PyramidTemplate(TemplateManager manager, BlockPos pos, Rotation rotation) {
            super(AtumStructurePieces.PYRAMID, 0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.loadTemplate(manager);
        }

        public PyramidTemplate(TemplateManager manager, CompoundNBT nbt) {
            super(AtumStructurePieces.PYRAMID, nbt);
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager templateManager) {
            Template template = templateManager.getTemplate(PYRAMID);
            PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(Mirror.NONE).setBoundingBox(this.boundingBox).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull IServerWorld world, @Nonnull Random rand, @Nonnull MutableBoundingBox box) {
            if (function.startsWith("Arrow")) {
                Rotation rotation = this.placeSettings.getRotation();
                BlockState arrowTrap = AtumBlocks.ARROW_TRAP.getDefaultState();

                if (rand.nextDouble() <= 0.3D) {
                    switch (function) {
                        case "ArrowWest":
                            arrowTrap = arrowTrap.with(TrapBlock.FACING, rotation.rotate(Direction.WEST));
                            break;
                        case "ArrowEast":
                            arrowTrap = arrowTrap.with(TrapBlock.FACING, rotation.rotate(Direction.EAST));
                            break;
                        case "ArrowSouth":
                            arrowTrap = arrowTrap.with(TrapBlock.FACING, rotation.rotate(Direction.SOUTH));
                            break;
                        case "ArrowNorth":
                            arrowTrap = arrowTrap.with(TrapBlock.FACING, rotation.rotate(Direction.NORTH));
                            break;
                    }
                    world.setBlockState(pos, arrowTrap, 2);
                } else {
                    world.setBlockState(pos, CARVED_BRICK, 2);
                }
            } else if (function.startsWith("Floor")) {
                switch (function) {
                    case "FloorTrap":
                        if (rand.nextDouble() <= 0.5D) {
                            Block trap = FLOOR_TRAPS.get(rand.nextInt(FLOOR_TRAPS.size()));
                            world.setBlockState(pos, trap.getDefaultState().with(TrapBlock.FACING, Direction.UP), 2);
                        } else {
                            world.setBlockState(pos, CARVED_BRICK, 2);
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
                    if (box.isVecInside(pos)) {
                        world.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 2);

                        TileEntity tileEntity = world.getTileEntity(pos);
                        if (tileEntity instanceof MobSpawnerTileEntity) {
                            EntityType<?> entityType = RuinPieces.RuinTemplate.UNDEAD.get(rand.nextInt(RuinPieces.RuinTemplate.UNDEAD.size()));
                            ((MobSpawnerTileEntity) tileEntity).getSpawnerBaseLogic().setEntityType(entityType);
                        }
                    }
                } else if (function.equals("SpawnerUndeadPair")) {
                    if (box.isVecInside(pos)) {
                        world.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 2);

                        TileEntity tileEntity = world.getTileEntity(pos);
                        if (tileEntity instanceof MobSpawnerTileEntity) {
                            ((MobSpawnerTileEntity) tileEntity).getSpawnerBaseLogic().setEntityType(UNDEAD_SPAWNER_PAIR);
                        }
                    }
                }
            } else if (function.equals("CrateChance")) {
                if (box.isVecInside(pos)) {
                    if (rand.nextDouble() <= 0.2D) {
                        world.setBlockState(pos, ChestBaseBlock.correctFacing(world, pos, AtumBlocks.DEADWOOD_CRATE.getDefaultState(), AtumBlocks.DEADWOOD_CRATE), 2);
                        LockableLootTileEntity.setLootTable(world, rand, pos, AtumLootTables.CRATE);
                    } else {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                    }
                }
            } else if (function.equals("Chest")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    LockableLootTileEntity.setLootTable(world, rand, posDown, AtumLootTables.PYRAMID_CHEST);
                }
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
            } else if (function.equals("Sarcophagus")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    LockableLootTileEntity.setLootTable(world, rand, posDown, AtumLootTables.PHARAOH);
                }
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
            } else if (function.equals("NebuTorch")) {
                if (box.isVecInside(pos)) {
                    if (rand.nextDouble() <= 0.25D) {
                        world.setBlockState(pos, AtumTorchUnlitBlock.UNLIT.get(AtumBlocks.NEBU_TORCH).getDefaultState(), 2);
                    } else {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                    }
                }
            } else if (function.equals("EntrancePuzzle")) {
                if (box.isVecInside(pos)) {
                    world.setBlockState(pos, AtumBlocks.QUANDARY_BLOCK.getDefaultState().with(QuandaryBlock.UNBREAKABLE, true).with(QuandaryBlock.FACING, StructureHelper.getDirectionFromRotation(this.getRotation())), 2);
                }
            }
        }

        private void setTrapsCopy(IWorld world, BlockPos pos, Random rand, MutableBoundingBox box, int range) {
            if (rand.nextDouble() <= 0.5D) {
                BlockState copy = null;
                for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                    for (int xMin = 0; xMin <= range; xMin++) {
                        BlockPos posOffset = pos.offset(horizontal, xMin);
                        if (box.isVecInside(posOffset)) {
                            BlockState adjacent = world.getBlockState(pos.offset(horizontal, xMin));
                            if (adjacent.getBlock() instanceof TrapBlock) {
                                copy = adjacent;
                            }
                        }
                    }
                }
                if (copy != null) {
                    world.setBlockState(pos, copy, 2);
                } else {
                    Block trap = FLOOR_TRAPS.get(rand.nextInt(FLOOR_TRAPS.size()));
                    world.setBlockState(pos, trap.getDefaultState().with(TrapBlock.FACING, Direction.UP), 2);
                }
            } else {
                world.setBlockState(pos, CARVED_BRICK, 2);
            }
        }

        @Override
        protected void readAdditional(@Nonnull CompoundNBT compound) { //Is actually write, just horrible name
            super.readAdditional(compound);
            compound.putString("Rot", this.placeSettings.getRotation().name());
        }
    }

    public static class Maze extends StructurePiece {
        private boolean[][] maze = null;

        public Maze(MutableBoundingBox boundingBox, Direction componentType) {
            super(AtumStructurePieces.PYRAMID_MAZE, 0);
            this.setCoordBaseMode(componentType);
            this.boundingBox = boundingBox;
        }

        public Maze(TemplateManager manager, CompoundNBT nbt) {
            super(AtumStructurePieces.PYRAMID_MAZE, nbt);
        }

        @Override
        public boolean func_230383_a_(@Nonnull ISeedReader world, @Nonnull StructureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random random, @Nonnull MutableBoundingBox box, @Nonnull ChunkPos chunkPos, BlockPos pos) {
            this.addMaze(world, random, box);
            return true;
        }

        private void addMaze(ISeedReader world, Random random, MutableBoundingBox validBounds) {
            if (this.maze == null) {
                this.maze = this.generateMaze(new Random(world.getSeed() * this.boundingBox.minX * this.boundingBox.minZ), this.boundingBox.getXSize(), this.boundingBox.getZSize());
            }

            for (int x = 0; x < this.boundingBox.getXSize(); x++) {
                for (int z = 0; z < this.boundingBox.getZSize(); z++) {
                    //Set pathway
                    this.setBlockState(world, Blocks.AIR.getDefaultState(), x, 0, z, validBounds);
                    this.setBlockState(world, Blocks.AIR.getDefaultState(), x, 1, z, validBounds);

                    // Make sure the blocks above the entrance to the lower levels are clear
                    if (this.getBlockStateFromPos(world, x, -1, z, validBounds).getBlock() instanceof LadderBlock) {
                        this.setBlockState(world, Blocks.AIR.getDefaultState(), x, 0, z, validBounds);
                        this.setBlockState(world, Blocks.AIR.getDefaultState(), x, 1, z, validBounds);
                    }
                    // Set the blocks for the walls of the maze
                    else if (!maze[x][z]) {
                        this.setBlockState(world, PyramidPieces.PyramidTemplate.CARVED_BRICK, x, 0, z, validBounds);
                        this.setBlockState(world, PyramidPieces.PyramidTemplate.CARVED_BRICK, x, 1, z, validBounds);
                        if (random.nextDouble() <= 0.10D) {
                            placeTrap(world, maze, x, z, random, validBounds);
                        }
                    }
                    // Place sand of the floor of the maze
                    else {
                        int layers = MathHelper.nextInt(random, 1, 2);
                        this.setBlockState(world, AtumBlocks.SAND_LAYERED.getDefaultState().with(SandLayersBlock.LAYERS, layers), x, 0, z, validBounds);
                    }
                }
            }
        }

        @Override
        protected void setBlockState(@Nonnull ISeedReader world, @Nonnull BlockState state, int x, int y, int z, @Nonnull MutableBoundingBox box) {
            BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
            if (box.isVecInside(pos) && !(world.getBlockState(pos).getBlock() instanceof LadderBlock)) { //Make sure ladder blocks don't get replaced
                super.setBlockState(world, state, x, y, z, box);
            }
        }

        private void placeTrap(ISeedReader world, boolean[][] maze, int x, int z, Random random, MutableBoundingBox validBounds) {
            BlockState trapState = PyramidPieces.PyramidTemplate.FLOOR_TRAPS.get(random.nextInt(PyramidPieces.PyramidTemplate.FLOOR_TRAPS.size())).getDefaultState();

            List<Direction> validDirections = new ArrayList<>();
            for (Direction facing : Direction.Plane.HORIZONTAL) {
                if (x + facing.getXOffset() >= 0 && x + facing.getXOffset() < maze.length && z + facing.getZOffset() >= 0 && z + facing.getZOffset() < maze[0].length) {
                    if (maze[x + facing.getXOffset()][z + facing.getZOffset()]) {
                        validDirections.add(facing.getOpposite());
                    }
                }
            }

            if (!validDirections.isEmpty()) {
                trapState = trapState.with(TrapBlock.FACING, validDirections.get(random.nextInt(validDirections.size())));
                this.setBlockState(world, trapState, x, 0, z, validBounds);
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
        protected void readAdditional(@Nonnull CompoundNBT compound) {
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