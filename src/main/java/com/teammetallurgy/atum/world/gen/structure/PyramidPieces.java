package com.teammetallurgy.atum.world.gen.structure;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.limestone.BlockLimestoneBricks;
import com.teammetallurgy.atum.blocks.limestone.chest.tileentity.TileEntityLimestoneChest;
import com.teammetallurgy.atum.blocks.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
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
import java.util.List;
import java.util.Random;

public class PyramidPieces {
    public static final ResourceLocation PYRAMID = new ResourceLocation(Constants.MOD_ID, "pyramid");

    public static void registerPyramid() {
        MapGenStructureIO.registerStructure(MapGenPyramid.Start.class, String.valueOf(PYRAMID));
        MapGenStructureIO.registerStructureComponent(PyramidTemplate.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "pyramid_template")));
        MapGenStructureIO.registerStructureComponent(Maze.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "pyramid_maze")));
    }

    static List<StructureComponent> generatePyramid(TemplateManager manager, BlockPos pos, Rotation rotation, Random rand) {
        List<StructureComponent> list = Lists.newArrayList();
        PyramidTemplate pyramidTemplate = new PyramidTemplate(manager, pos, rotation);
        Maze maze = new Maze(pyramidTemplate.getCoordBaseMode(), pyramidTemplate.getBoundingBox());
        list.add(pyramidTemplate);
        list.add(maze);
        return list;
    }

    public static class PyramidTemplate extends StructureComponentTemplate {
        static final NonNullList<Block> FLOOR_TRAPS = NonNullList.from(AtumBlocks.BURNING_TRAP, AtumBlocks.POISON_TRAP, AtumBlocks.SMOKE_TRAP, AtumBlocks.TAR_TRAP);
        private Rotation rotation;
        private Mirror mirror;

        PyramidTemplate() { //Needs empty constructor
        }

        PyramidTemplate(TemplateManager manager, BlockPos pos, Rotation rotation) {
            this(manager, pos, rotation, Mirror.NONE);
        }

        private PyramidTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, Mirror mirror) {
            super(0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.mirror = mirror;
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager templateManager) {
            Template template = templateManager.getTemplate(null, PYRAMID);
            PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull World world, @Nonnull Random rand, @Nonnull StructureBoundingBox box) {
            if (function.startsWith("Arrow")) {
                Rotation rotation = this.placeSettings.getRotation();
                IBlockState arrowTrap = AtumBlocks.ARROW_TRAP.getDefaultState();

                if (rand.nextDouble() <= 0.3D) {
                    switch (function) {
                        case "ArrowWest":
                            arrowTrap = arrowTrap.withProperty(BlockTrap.FACING, rotation.rotate(EnumFacing.WEST));
                            break;
                        case "ArrowEast":
                            arrowTrap = arrowTrap.withProperty(BlockTrap.FACING, rotation.rotate(EnumFacing.EAST));
                            break;
                        case "ArrowSouth":
                            arrowTrap = arrowTrap.withProperty(BlockTrap.FACING, rotation.rotate(EnumFacing.SOUTH));
                            break;
                        case "ArrowNorth":
                            arrowTrap = arrowTrap.withProperty(BlockTrap.FACING, rotation.rotate(EnumFacing.NORTH));
                            break;
                    }
                    world.setBlockState(pos, arrowTrap, 2);
                } else {
                    world.setBlockState(pos, BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CARVED).getDefaultState(), 2);
                }
            } else if (function.startsWith("Floor")) {
                switch (function) {
                    case "FloorTrap":
                        if (rand.nextDouble() <= 0.5D) {
                            Block trap = FLOOR_TRAPS.get(rand.nextInt(FLOOR_TRAPS.size()));
                            world.setBlockState(pos, trap.getDefaultState().withProperty(BlockTrap.FACING, EnumFacing.UP), 2);
                        } else {
                            world.setBlockState(pos, BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CARVED).getDefaultState(), 2);
                        }
                        break;
                    case "FloorCopy":
                        this.setTrapsCopy(world, pos, rand, box, 2);
                        break;
                    case "FloorBox":
                        this.setTrapsCopy(world, pos, rand, box, 3);
                        break;
                    case "FloorSpace":
                        this.setTrapsCopy(world, pos, rand, box, 3);
                        break;
                }
            } else if (function.equals("CrateChance")) {
                /*if (rand.nextDouble() <= 0.2D) {
                    if (box.isVecInside(pos) && !(world.getBlockState(pos).getBlock() instanceof BlockCrate)) {
                        world.setBlockState(pos, BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState(), 2);
                        TileEntity tileEntity = world.getTileEntity(pos);
                        if (tileEntity instanceof TileEntityCrate) {
                            ((TileEntityCrate) tileEntity).setLootTable(AtumLootTables.RUINS, rand.nextLong()); //TODO Temporary
                        }
                    }
                } else {
                    world.setBlockToAir(pos);
                }*/
            } else if (function.equals("Chest")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    TileEntity tileentity = world.getTileEntity(posDown);
                    if (tileentity instanceof TileEntityLimestoneChest) {
                        ((TileEntityLimestoneChest) tileentity).setLootTable(AtumLootTables.RUINS, rand.nextLong()); //TODO Temporary
                    }
                }
                world.setBlockToAir(pos);
            } else if (function.equals("Sarcophagus")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    TileEntity tileentity = world.getTileEntity(posDown);
                    if (tileentity instanceof TileEntitySarcophagus) {
                        ((TileEntitySarcophagus) tileentity).setLootTable(AtumLootTables.PHARAOH, rand.nextLong());
                    }
                }
                world.setBlockToAir(pos);
            }
        }

        private void setTrapsCopy(World world, BlockPos pos, Random rand, StructureBoundingBox box, int range) {
            if (rand.nextDouble() <= 0.5D) {
                IBlockState copy = null;
                for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
                    for (int xMin = 0; xMin <= range; xMin++) {
                        BlockPos posOffset = pos.offset(horizontal, xMin);
                        if (box.isVecInside(posOffset)) {
                            IBlockState adjacent = world.getBlockState(pos.offset(horizontal, xMin));
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
                    world.setBlockState(pos, trap.getDefaultState().withProperty(BlockTrap.FACING, EnumFacing.UP), 2);
                }
            } else {
                world.setBlockState(pos, BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CARVED).getDefaultState(), 2);
            }
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound compound) {
            super.writeStructureToNBT(compound);
            compound.setString("Rot", this.placeSettings.getRotation().name());
            compound.setString("Mi", this.placeSettings.getMirror().name());
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound compound, TemplateManager manager) {
            super.readStructureFromNBT(compound, manager);
            this.rotation = Rotation.valueOf(compound.getString("Rot"));
            this.mirror = Mirror.valueOf(compound.getString("Mi"));
            this.loadTemplate(manager);
        }
    }

    static class Maze extends StructureComponent {
        private static final IBlockState BRICK = BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CARVED).setUnbreakable().getDefaultState();

        Maze() { //Needs empty constructor
        }

        Maze(EnumFacing facing, StructureBoundingBox box) {
            super(1);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
        }

        @Override
        protected void writeStructureToNBT(@Nonnull NBTTagCompound compound) {
        }

        @Override
        protected void readStructureFromNBT(@Nonnull NBTTagCompound compound, @Nonnull TemplateManager manager) {
        }

        @Override
        public boolean addComponentParts(@Nonnull World world, @Nonnull Random random, @Nonnull StructureBoundingBox box) {
            BlockPos pos = new BlockPos(box.minX, boundingBox.minY + 6, box.minZ);
            System.out.println(pos);

            boolean[][] size = new boolean[17][17];
            int width = 17;
            int depth = 17;
            int zIn = 9;
            size[0][zIn] = true;

            this.generateMaze(size, random, 1, zIn);

            for (int x = -3; x < width + 3; x++) {
                for (int z = -3; z < depth + 3; z++) {
                    if (x >= 0 && x < width && z >= 0 && z < depth) {
                        if (!size[x][z]) {
                            world.setBlockState(pos.add(x, 0, z), BRICK, 2);
                            world.setBlockState(pos.add(x, 1, z), BRICK, 2);

                            if (random.nextDouble() <= 0.10D) {
                                this.placeTrap(world, pos.add(x, 0, z), random);
                            }
                        } else {
                            //int meta = random.nextInt(5);
                            //world.setBlockState(pos.add(x, 0, z), AtumBlocks.SAND_LAYERED.getStateFromMeta(meta), 2); //Readd when done
                        }
                    }
                }
            }
            return false;
        }

        private void generateMaze(boolean[][] array, Random random, int x, int z) { //Originally made by RebelKeithy
            ArrayList<Pair> choices = new ArrayList<>();
            do {
                int innerSize = array.length - 1;
                choices.clear();
                if (x + 2 < innerSize && !array[x + 2][z]) {
                    choices.add(new Pair(2, 0));
                }
                if (x - 2 >= 0 && !array[x - 2][z]) {
                    choices.add(new Pair(-2, 0));
                }
                if (z + 2 < innerSize && !array[x][z + 2]) {
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
                    this.generateMaze(array, random, x + choice.x, z + choice.y);
                }
            } while (choices.size() > 0);
        }

        private void placeTrap(World world, BlockPos pos, Random random) {
            IBlockState trapState = PyramidTemplate.FLOOR_TRAPS.get(random.nextInt(PyramidTemplate.FLOOR_TRAPS.size())).setBlockUnbreakable().getDefaultState();

            if (world.isSideSolid(pos.south(), EnumFacing.NORTH)) {
                trapState.withProperty(BlockTrap.FACING, EnumFacing.SOUTH);
            } else if (world.isSideSolid(pos.north(), EnumFacing.SOUTH)) {
                trapState.withProperty(BlockTrap.FACING, EnumFacing.WEST);
            } else if (world.isSideSolid(pos.east(), EnumFacing.WEST)) {
                trapState.withProperty(BlockTrap.FACING, EnumFacing.EAST);
            } else if (world.isSideSolid(pos.west(), EnumFacing.EAST)) {
                trapState.withProperty(BlockTrap.FACING, EnumFacing.NORTH);
            }
            world.setBlockState(pos, trapState, 2);
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