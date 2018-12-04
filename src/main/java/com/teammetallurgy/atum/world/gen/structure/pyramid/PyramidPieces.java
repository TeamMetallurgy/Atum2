package com.teammetallurgy.atum.world.gen.structure.pyramid;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestoneBricks;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntityLimestoneChest;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockAtumTorchUnlit;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinPieces;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
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
        MapGenStructureIO.registerStructureComponent(Maze.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "maze")));
    }

    static List<StructureComponent> getComponents(TemplateManager manager, BlockPos pos, Rotation rotation, Random random) {
        List<StructureComponent> components = Lists.newArrayList();
        PyramidTemplate template = new PyramidTemplate(manager, pos, rotation, random);
        Maze maze = new Maze(template.rotation, template.getBoundingBox(), template.getCoordBaseMode());

        components.add(template);
        components.add(maze);
        return components;
    }

    public static class PyramidTemplate extends StructureComponentTemplate {
        public static final NonNullList<Block> FLOOR_TRAPS = NonNullList.from(AtumBlocks.BURNING_TRAP, AtumBlocks.POISON_TRAP, AtumBlocks.SMOKE_TRAP, AtumBlocks.TAR_TRAP);
        public static final IBlockState CARVED_BRICK = BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CARVED).getDefaultState().withProperty(BlockLimestoneBricks.UNBREAKABLE, true);
        public static final IBlockState LARGE_BRICK = BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE).getDefaultState().withProperty(BlockLimestoneBricks.UNBREAKABLE, true);
        public boolean isDefeated = false;
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
           /* if (function.equals("Maze")) {
                if (box.isVecInside(pos)) {
                    this.addMaze(world, pos, this.placeSettings.getRotation(), rand); //Might be causing cascading worldgen, look into.
                }
            } else */
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
                    world.setBlockState(pos, CARVED_BRICK, 2);
                }
            } else if (function.startsWith("Floor")) {
                switch (function) {
                    case "FloorTrap":
                        if (rand.nextDouble() <= 0.5D) {
                            Block trap = FLOOR_TRAPS.get(rand.nextInt(FLOOR_TRAPS.size()));
                            world.setBlockState(pos, trap.getDefaultState().withProperty(BlockTrap.FACING, EnumFacing.UP), 2);
                        } else {
                            world.setBlockState(pos, CARVED_BRICK, 2);
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
                        if (tileEntity instanceof TileEntityCrate) {
                            ((TileEntityCrate) tileEntity).setLootTable(AtumLootTables.CRATE, rand.nextLong());
                        }
                    } else {
                        world.setBlockToAir(pos);
                    }
                }
            } else if (function.equals("Chest")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    TileEntity tileentity = world.getTileEntity(posDown);
                    if (tileentity instanceof TileEntityLimestoneChest) {
                        ((TileEntityLimestoneChest) tileentity).setLootTable(AtumLootTables.PYRAMID_CHEST, rand.nextLong());
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
            } else if (function.equals("SarcophagusArtifact")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    TileEntity tileentity = world.getTileEntity(posDown);
                    if (tileentity instanceof TileEntitySarcophagus) {
                        ((TileEntitySarcophagus) tileentity).setLootTable(AtumLootTables.PHARAOH, rand.nextLong());
                        ((TileEntitySarcophagus) tileentity).setLootTable(AtumLootTables.SARCOPHAGUS_ARTIFACT, rand.nextLong());
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
                world.setBlockState(pos, CARVED_BRICK, 2);
            }
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound compound) {
            super.writeStructureToNBT(compound);
            compound.setString("Rot", this.placeSettings.getRotation().name());
            compound.setString("Mi", this.placeSettings.getMirror().name());
            compound.setBoolean("IsDefeated", this.isDefeated);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound compound, TemplateManager manager) {
            super.readStructureFromNBT(compound, manager);
            this.rotation = Rotation.valueOf(compound.getString("Rot"));
            this.mirror = Mirror.valueOf(compound.getString("Mi"));
            this.isDefeated = compound.getBoolean("IsDefeated");
            this.loadTemplate(manager);
        }
    }

    public static class Maze extends StructureComponent {
        private Rotation rotation;

        public Maze() {
        }

        public Maze(Rotation rotation, StructureBoundingBox boundingBox, EnumFacing componentType) {
            this.rotation = rotation;
            this.setCoordBaseMode(componentType);
            this.boundingBox = boundingBox;
        }

        @Override
        public boolean addComponentParts(@Nonnull World world, @Nonnull Random random, @Nonnull StructureBoundingBox box) {
        	BlockPos pos = new BlockPos(box.minX, boundingBox.minY + 60, box.minZ); //TODO

        	this.addMaze(world, pos, this.rotation, random, box);
            return true;
        }

        private void addMaze(World world, BlockPos pos, Rotation rotation, Random random, StructureBoundingBox genBounds) {
            int width = 28;
            int depth = 28;
            boolean[][] size = new boolean[width][depth];
            int zIn = 1 + depth/2;
            size[0][zIn] = true;

            this.generateMaze(size, new Random(world.getSeed() * this.boundingBox.minX * this.boundingBox.minZ), 1, zIn);
            
        	int y = 6;
            for (int x = 0; x < this.boundingBox.getXSize(); x++) {
                for (int z = 0; z < this.boundingBox.getZSize(); z++) {
                    
            		int mazeX = x - 2;
            		int mazeZ = z - 2;
            		if(mazeX < 0 || mazeZ < 0 || mazeX >= width || mazeZ >= depth) {
            			continue;
            		}

                	//Set pathway
            		this.setBlockState(world, Blocks.AIR.getDefaultState(), x, y, z, genBounds);
            		this.setBlockState(world, Blocks.AIR.getDefaultState(), x, y + 1, z, genBounds);
            		
                    if (!size[mazeX][mazeZ]) {
                    	if(this.getBlockStateFromPos(world, x, y - 1, z, genBounds).getBlock() instanceof BlockLadder) {
                        	this.setBlockState(world, Blocks.AIR.getDefaultState(), x, y, z, genBounds);
                        	this.setBlockState(world, Blocks.AIR.getDefaultState(), x, y + 1, z, genBounds);
                    	}
                    	else if(this.getBlockStateFromPos(world, x, y + 2, z, genBounds).getBlock() instanceof BlockLadder) {
                    		/* TODO
                            IBlockState ladder = world.getBlockState(basePos.up(2));
                            EnumFacing facing = ladder.getValue(BlockLadder.FACING);
                            BlockPos wallOffset = basePos.offset(facing.getOpposite());
                            
                            //world.setBlockState(wallOffset, PyramidPieces.PyramidTemplate.LARGE_BRICK, 2);
                            //world.setBlockState(wallOffset.up(), PyramidPieces.PyramidTemplate.LARGE_BRICK, 2);
                            
                            if (world.mayPlace(ladder.getBlock(), basePos, false, facing, null) && !(getBlockState(world, basePos, genBounds).getBlock() instanceof BlockLadder || getBlockState(world, basePos.up(), genBounds).getBlock() instanceof BlockLadder)) {
                                world.setBlockState(basePos.up(), AtumBlocks.DEADWOOD_LADDER.getDefaultState().withProperty(BlockLadder.FACING, facing), 2);
                            }
                            */
                    	}
                    	else {
                    		this.setBlockState(world, PyramidPieces.PyramidTemplate.CARVED_BRICK, x, y, z, genBounds);
                    		this.setBlockState(world, PyramidPieces.PyramidTemplate.CARVED_BRICK, x, y + 1, z, genBounds);
                            if (random.nextDouble() <= 0.10D) {
                                placeTrap(world, x, y, z, random, genBounds);
                            }
                    	}
                    }
                    else {
                        int meta = MathHelper.getInt(random, 0, 1);
                		this.setBlockState(world, AtumBlocks.SAND_LAYERED.getStateFromMeta(meta), x, y - 1, z, genBounds);
                    }
                }
            }    
        }
        
        private void placeTrap(World world, int x, int y, int z, Random random, StructureBoundingBox validBounds) {
            IBlockState trapState = PyramidPieces.PyramidTemplate.FLOOR_TRAPS.get(random.nextInt(PyramidPieces.PyramidTemplate.FLOOR_TRAPS.size())).getDefaultState();

            List<EnumFacing> validDirections = new ArrayList<EnumFacing>();
            for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            	BlockPos pos = new BlockPos(x + facing.getXOffset(), y, z + facing.getZOffset());
            	if(this.getBlockStateFromPos(world, x + facing.getXOffset(), y, z + facing.getZOffset(), validBounds).getBlock() == Blocks.AIR) {
            		validDirections.add(facing);
            	}
            }
            
            if(!validDirections.isEmpty())
            {
	            trapState = trapState.withProperty(BlockTrap.FACING, validDirections.get(random.nextInt(validDirections.size())));
	            this.setBlockState(world, trapState, x, y, z, validBounds);
            }
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

        @Override
        protected void writeStructureToNBT(@Nonnull NBTTagCompound compound) {
            compound.setString("Rot", this.rotation.name());
        }

        @Override
        protected void readStructureFromNBT(@Nonnull NBTTagCompound compound, @Nonnull TemplateManager manager) {
            this.rotation = Rotation.valueOf(compound.getString("Rot"));
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