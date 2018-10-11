package com.teammetallurgy.atum.world.gen.structure;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.limestone.BlockLimestoneBricks;
import com.teammetallurgy.atum.blocks.limestone.chest.tileentity.TileEntityLimestoneChest;
import com.teammetallurgy.atum.blocks.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import com.teammetallurgy.atum.blocks.wood.BlockAtumTorchUnlit;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
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
import java.util.List;
import java.util.Random;

public class PyramidPieces {
    public static final ResourceLocation PYRAMID = new ResourceLocation(Constants.MOD_ID, "pyramid");

    public static void registerPyramid() {
        MapGenStructureIO.registerStructure(MapGenPyramid.Start.class, String.valueOf(PYRAMID));
        MapGenStructureIO.registerStructureComponent(PyramidTemplate.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "pyramid_template")));
    }

    static List<StructureComponent> generatePyramid(TemplateManager manager, BlockPos pos, Rotation rotation) {
        List<StructureComponent> list = Lists.newArrayList();
        PyramidTemplate pyramidTemplate = new PyramidTemplate(manager, pos, Rotation.COUNTERCLOCKWISE_90);
        list.add(pyramidTemplate);
        return list;
    }

    public static class PyramidTemplate extends StructureComponentTemplate {
        private final NonNullList<Block> FLOOR_TRAPS = NonNullList.from(AtumBlocks.BURNING_TRAP, AtumBlocks.POISON_TRAP, AtumBlocks.SMOKE_TRAP, AtumBlocks.TAR_TRAP);
        private static final IBlockState CARVED_BRICK = BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CARVED).setUnbreakable().getDefaultState();
        private static final IBlockState LARGE_BRICK = BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE).setUnbreakable().getDefaultState();
        private Rotation rotation;
        private Mirror mirror;

        public PyramidTemplate() { //Needs empty constructor
        }

        PyramidTemplate(TemplateManager manager, BlockPos pos, Rotation rotation) {
            this(manager, pos, rotation, Mirror.NONE);
        }

        private PyramidTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, Mirror mirror) {
            super(0);
            this.templatePosition = pos;
            this.rotation = rotation;
            System.out.println("Rotation: " + rotation);
            this.mirror = mirror;
            this.boundingBox = StructureBoundingBox.createProper(0, 0, 0, templatePosition.getX(), templatePosition.getY(), templatePosition.getZ());
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
            } else if (function.equals("PharaohTorch")) {
                if (box.isVecInside(pos)) {
                    if (rand.nextDouble() <= 0.25D) {
                        world.setBlockState(pos, BlockAtumTorchUnlit.getUnlitTorch(AtumBlocks.PHARAOH_TORCH).getDefaultState(), 2);
                    } else {
                        world.setBlockToAir(pos);
                    }
                }
            } else if (function.equals("Maze")) {
                if (box.isVecInside(pos)) {
                    this.addMaze(world, pos, this.placeSettings.getRotation(), rand);
                }
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

        private void addMaze(World world, BlockPos pos, Rotation rotation, Random random) {
            int width = 28;
            int depth = 28;
            boolean[][] size = new boolean[width][depth];
            int zIn = 9;
            size[0][zIn] = true;

            this.generateMaze(size, random, 1, zIn);

            for (int x = -3; x < width + 3; x++) {
                for (int z = -3; z < depth + 3; z++) {
                    if (x >= 0 && x < width && z >= 0 && z < depth) {
                        BlockPos localPos = new BlockPos(x, 0, z);
                        localPos = localPos.rotate(rotation);
                        BlockPos basePos = pos.add(localPos.getX(), 0, localPos.getZ());

                        //Set pathway
                        if (canPlace(world, basePos.up())) {
                            world.setBlockToAir(basePos);
                        }
                        if (this.canPlace(world, basePos.up())) {
                            world.setBlockToAir(basePos.up());
                        }

                        if (!size[x][z]) {
                            if (world.getBlockState(basePos.down()).getBlock() instanceof BlockLadder) {
                                world.setBlockToAir(basePos);
                                world.setBlockToAir(basePos.up());
                            } else if (world.getBlockState(basePos.up(2)).getBlock() instanceof BlockLadder) {
                                IBlockState ladder = world.getBlockState(basePos.up(2));
                                EnumFacing facing = ladder.getValue(BlockLadder.FACING);
                                BlockPos wallOffset = basePos.offset(facing.getOpposite());
                                world.setBlockState(wallOffset, BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE).getDefaultState(), 2);
                                world.setBlockState(wallOffset.up(), BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE).getDefaultState(), 2);
                                if (world.mayPlace(ladder.getBlock(), basePos, false, facing, null) && !(world.getBlockState(basePos).getBlock() instanceof BlockLadder || world.getBlockState(basePos.up()).getBlock() instanceof BlockLadder)) {
                                    world.setBlockState(basePos, AtumBlocks.DEADWOOD_LADDER.getDefaultState().withProperty(BlockLadder.FACING, facing), 2);
                                    world.setBlockState(basePos.up(), AtumBlocks.DEADWOOD_LADDER.getDefaultState().withProperty(BlockLadder.FACING, facing), 2);
                                }
                            } else {
                                world.setBlockState(basePos, CARVED_BRICK, 2);
                                world.setBlockState(basePos.up(), CARVED_BRICK, 2);
                                if (random.nextDouble() <= 0.10D) {
                                    this.placeTrap(world, basePos, random);
                                }
                            }
                        } else {
                            int meta = MathHelper.getInt(random, 0, 1);
                            if (this.canPlace(world, basePos)) {
                                world.setBlockState(basePos, AtumBlocks.SAND_LAYERED.getStateFromMeta(meta), 2);
                            }
                        }
                    }
                }
            }
        }

        private boolean canPlace(World world, BlockPos pos) {
            IBlockState state = world.getBlockState(pos);
            return state != LARGE_BRICK && !(state.getBlock() instanceof BlockLadder);
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
            IBlockState trapState = FLOOR_TRAPS.get(random.nextInt(FLOOR_TRAPS.size())).setBlockUnbreakable().getDefaultState();

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