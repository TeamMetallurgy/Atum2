package com.teammetallurgy.atum.world.gen.structure.pyramid;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.BlockSandLayers;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.LimestoneChestTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockAtumTorchUnlit;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinPieces;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureComponentTemplate;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PyramidPieces {
    public static final ResourceLocation PYRAMID = new ResourceLocation(Constants.MOD_ID, "pyramid");

    // For the maze to generate correctly, it must be an odd width and depth
    private static final int MAZE_SIZE_X = 27;
    private static final int MAZE_SIZE_Z = 25;

    public static void registerPyramid() {
        MapGenStructureIO.registerStructure(MapGenPyramid.Start.class, String.valueOf(PYRAMID));
        MapGenStructureIO.registerStructureComponent(PyramidTemplate.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "pyramid_template")));
        MapGenStructureIO.registerStructureComponent(Maze.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "maze")));
    }

    static List<StructureComponent> getComponents(TemplateManager manager, BlockPos pos, Rotation rotation, Random random) {
        List<StructureComponent> components = Lists.newArrayList();
        PyramidTemplate template = new PyramidTemplate(manager, pos, rotation, random);
        Maze maze = new Maze(getMazeBounds(template.getBoundingBox(), template.rotation), template.getCoordBaseMode());
        components.add(template);
        components.add(maze);
        return components;
    }

    private static StructureBoundingBox getMazeBounds(StructureBoundingBox pyramidBounds, Rotation rotation) {
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
            xOffset = 3;
            zOffset = 2;
            width = MAZE_SIZE_Z;
            depth = MAZE_SIZE_X;
        } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
            xOffset = 5;
            zOffset = 3;
            width = MAZE_SIZE_Z;
            depth = MAZE_SIZE_X;
        } else if (rotation == Rotation.CLOCKWISE_180) {
            xOffset = 4;
            zOffset = 3;
        }

        return StructureBoundingBox.createProper(
                pyramidBounds.minX + xOffset,
                pyramidBounds.minY + 6,
                pyramidBounds.minZ + zOffset,
                pyramidBounds.minX + xOffset + width - 1,
                pyramidBounds.minY + 7,
                pyramidBounds.minZ + zOffset + depth - 1);
    }

    public static class PyramidTemplate extends StructureComponentTemplate {
        public static final List<Block> FLOOR_TRAPS = Arrays.asList(AtumBlocks.BURNING_TRAP, AtumBlocks.POISON_TRAP, AtumBlocks.SMOKE_TRAP, AtumBlocks.TAR_TRAP);
        static final BlockState CARVED_BRICK = LimestoneBrickBlock.getBrick(LimestoneBrickBlock.BrickType.CARVED).getDefaultState().with(LimestoneBrickBlock.UNBREAKABLE, true);
        private ResourceLocation undeadSpawnerPair;
        private Rotation rotation;
        private Mirror mirror;

        public PyramidTemplate() { //Needs empty constructor
        }

        PyramidTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, Random random) {
            this(manager, pos, rotation, Mirror.NONE, random);
        }

        private PyramidTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, Mirror mirror, Random random) {
            super(0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.mirror = mirror;
            this.undeadSpawnerPair = RuinPieces.RuinTemplate.UNDEAD.get(random.nextInt(RuinPieces.RuinTemplate.UNDEAD.size())).getRegistryName();
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager templateManager) {
            Template template = templateManager.getTemplate(null, PYRAMID);
            PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror).setBoundingBox(this.boundingBox);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull World world, @Nonnull Random rand, @Nonnull StructureBoundingBox box) {
            if (function.startsWith("Arrow")) {
                Rotation rotation = this.placeSettings.getRotation();
                BlockState arrowTrap = AtumBlocks.ARROW_TRAP.getDefaultState();

                if (rand.nextDouble() <= 0.3D) {
                    switch (function) {
                        case "ArrowWest":
                            arrowTrap = arrowTrap.with(BlockTrap.FACING, rotation.rotate(Direction.WEST));
                            break;
                        case "ArrowEast":
                            arrowTrap = arrowTrap.with(BlockTrap.FACING, rotation.rotate(Direction.EAST));
                            break;
                        case "ArrowSouth":
                            arrowTrap = arrowTrap.with(BlockTrap.FACING, rotation.rotate(Direction.SOUTH));
                            break;
                        case "ArrowNorth":
                            arrowTrap = arrowTrap.with(BlockTrap.FACING, rotation.rotate(Direction.NORTH));
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
                            world.setBlockState(pos, trap.getDefaultState().with(BlockTrap.FACING, Direction.UP), 2);
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
                        world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);

                        TileEntity tileEntity = world.getTileEntity(pos);
                        if (tileEntity instanceof TileEntityMobSpawner) {
                            ResourceLocation location = RuinPieces.RuinTemplate.UNDEAD.get(rand.nextInt(RuinPieces.RuinTemplate.UNDEAD.size())).getRegistryName();
                            ((TileEntityMobSpawner) tileEntity).getSpawnerBaseLogic().setEntityId(location);
                        }
                    }
                } else if (function.equals("SpawnerUndeadPair")) {
                    if (box.isVecInside(pos)) {
                        world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);

                        TileEntity tileEntity = world.getTileEntity(pos);
                        if (tileEntity instanceof TileEntityMobSpawner) {
                            ((TileEntityMobSpawner) tileEntity).getSpawnerBaseLogic().setEntityId(this.undeadSpawnerPair);
                        }
                    }
                }
            } else if (function.equals("CrateChance")) {
                if (box.isVecInside(pos)) {
                    if (rand.nextDouble() <= 0.2D) {
                        world.setBlockState(pos, BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD).correctFacing(world, pos, BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState()), 2);

                        TileEntity tileEntity = world.getTileEntity(pos);
                        if (tileEntity instanceof CrateTileEntity) {
                            ((CrateTileEntity) tileEntity).setLootTable(AtumLootTables.CRATE, rand.nextLong());
                        }
                    } else {
                        world.setBlockToAir(pos);
                    }
                }
            } else if (function.equals("Chest")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    TileEntity tileentity = world.getTileEntity(posDown);
                    if (tileentity instanceof LimestoneChestTileEntity) {
                        ((LimestoneChestTileEntity) tileentity).setLootTable(AtumLootTables.PYRAMID_CHEST, rand.nextLong());
                    }
                }
                world.setBlockToAir(pos);
            } else if (function.equals("Sarcophagus")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    TileEntity tileentity = world.getTileEntity(posDown);
                    if (tileentity instanceof SarcophagusTileEntity) {
                        ((SarcophagusTileEntity) tileentity).setLootTable(AtumLootTables.PHARAOH, rand.nextLong());
                    }
                }
                world.setBlockToAir(pos);
            } else if (function.equals("SarcophagusArtifact")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    TileEntity tileentity = world.getTileEntity(posDown);
                    if (tileentity instanceof SarcophagusTileEntity) {
                        ((SarcophagusTileEntity) tileentity).setLootTable(AtumLootTables.SARCOPHAGUS_ARTIFACT, rand.nextLong());
                    }
                }
                world.setBlockToAir(pos);
            } else if (function.equals("PharaohTorch")) {
                if (box.isVecInside(pos)) {
                    if (rand.nextDouble() <= 0.25D) {
                        world.setBlockState(pos, BlockAtumTorchUnlit.getUnlitTorch(AtumBlocks.PHARAOH_TORCH).getDefaultState(), 2);
                    } else {
                        world.setBlockToAir(pos);
                    }
                }
            }
        }

        private void setTrapsCopy(World world, BlockPos pos, Random rand, StructureBoundingBox box, int range) {
            if (rand.nextDouble() <= 0.5D) {
                BlockState copy = null;
                for (Direction horizontal : Direction.HORIZONTALS) {
                    for (int xMin = 0; xMin <= range; xMin++) {
                        BlockPos posOffset = pos.offset(horizontal, xMin);
                        if (box.isVecInside(posOffset)) {
                            BlockState adjacent = world.getBlockState(pos.offset(horizontal, xMin));
                            if (adjacent.getBlock() instanceof BlockTrap) {
                                copy = adjacent;
                            }
                        }
                    }
                }
                if (copy != null) {
                    world.setBlockState(pos, copy, 2);
                } else {
                    Block trap = FLOOR_TRAPS.get(rand.nextInt(FLOOR_TRAPS.size()));
                    world.setBlockState(pos, trap.getDefaultState().with(BlockTrap.FACING, Direction.UP), 2);
                }
            } else {
                world.setBlockState(pos, CARVED_BRICK, 2);
            }
        }

        @Override
        protected void writeStructureToNBT(CompoundNBT compound) {
            super.writeStructureToNBT(compound);
            compound.putString("Rot", this.placeSettings.getRotation().name());
            compound.putString("Mi", this.placeSettings.getMirror().name());
        }

        @Override
        protected void readStructureFromNBT(CompoundNBT compound, TemplateManager manager) {
            super.readStructureFromNBT(compound, manager);
            this.rotation = Rotation.valueOf(compound.getString("Rot"));
            this.mirror = Mirror.valueOf(compound.getString("Mi"));
            this.loadTemplate(manager);
        }
    }

    public static class Maze extends StructureComponent {
        private boolean[][] maze = null;

        public Maze() {
        }

        public Maze(StructureBoundingBox boundingBox, Direction componentType) {
            this.setCoordBaseMode(componentType);
            this.boundingBox = boundingBox;
        }

        @Override
        public boolean addComponentParts(@Nonnull World world, @Nonnull Random random, @Nonnull StructureBoundingBox validBounds) {
            this.addMaze(world, random, validBounds);
            return true;
        }

        private void addMaze(World world, Random random, StructureBoundingBox validBounds) {
            if (maze == null) {
                maze = this.generateMaze(new Random(world.getSeed() * this.boundingBox.minX * this.boundingBox.minZ), this.boundingBox.getXSize(), this.boundingBox.getZSize());
            }

            for (int x = 0; x < this.boundingBox.getXSize(); x++) {
                for (int z = 0; z < this.boundingBox.getZSize(); z++) {
                    //Set pathway
                    this.setBlockState(world, Blocks.AIR.getDefaultState(), x, 0, z, validBounds);
                    this.setBlockState(world, Blocks.AIR.getDefaultState(), x, 1, z, validBounds);

                    // Make sure the blocks above the entrance to the lower levels are clear
                    if (this.getBlockStateFromPos(world, x, -1, z, validBounds).getBlock() instanceof BlockLadder) {
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
                        int layers = MathHelper.getInt(random, 1, 2);
                        this.setBlockState(world, AtumBlocks.SAND_LAYERED.getDefaultState().with(BlockSandLayers.LAYERS, layers), x, 0, z, validBounds);
                    }
                }
            }
        }

        private void placeTrap(World world, boolean[][] maze, int x, int z, Random random, StructureBoundingBox validBounds) {
            BlockState trapState = PyramidPieces.PyramidTemplate.FLOOR_TRAPS.get(random.nextInt(PyramidPieces.PyramidTemplate.FLOOR_TRAPS.size())).getDefaultState();

            List<Direction> validDirections = new ArrayList<>();
            for (Direction facing : Direction.HORIZONTALS) {
                if (x + facing.getXOffset() >= 0 && x + facing.getXOffset() < maze.length && z + facing.getZOffset() >= 0 && z + facing.getZOffset() < maze[0].length) {
                    if (maze[x + facing.getXOffset()][z + facing.getZOffset()]) {
                        validDirections.add(facing.getOpposite());
                    }
                }
            }

            if (!validDirections.isEmpty()) {
                trapState = trapState.with(BlockTrap.FACING, validDirections.get(random.nextInt(validDirections.size())));
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
        protected void writeStructureToNBT(@Nonnull CompoundNBT compound) {
        }

        @Override
        protected void readStructureFromNBT(@Nonnull CompoundNBT compound, @Nonnull TemplateManager manager) {
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
}