package com.teammetallurgy.atum.world.gen.structure;

import com.teammetallurgy.atum.blocks.limestone.BlockLimestoneBricks;
import com.teammetallurgy.atum.blocks.limestone.chest.tileentity.TileEntityLimestoneChest;
import com.teammetallurgy.atum.blocks.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
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
import net.minecraft.world.gen.structure.StructureComponentTemplate;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.Random;

public class PyramidTemplate extends StructureComponentTemplate {
    private static final NonNullList<Block> FLOOR_TRAPS = NonNullList.from(AtumBlocks.BURNING_TRAP, AtumBlocks.POISON_TRAP, AtumBlocks.SMOKE_TRAP, AtumBlocks.TAR_TRAP);
    static final ResourceLocation PYRAMID = new ResourceLocation(Constants.MOD_ID, "pyramid");
    private Rotation rotation;
    private Mirror mirror;

    public static void registerPyramid() {
        MapGenStructureIO.registerStructure(MapGenPyramid.Start.class, String.valueOf(PYRAMID));
        MapGenStructureIO.registerStructureComponent(PyramidTemplate.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "pyramid_template")));
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
            if (rand.nextDouble() <= 0.2D) {
                if (box.isVecInside(pos) && !(world.getBlockState(pos).getBlock() instanceof BlockCrate)) {
                    world.setBlockState(pos, BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState(), 2);
                    TileEntity tileEntity = world.getTileEntity(pos);
                    if (tileEntity instanceof TileEntityCrate) {
                        ((TileEntityCrate) tileEntity).setLootTable(AtumLootTables.RUINS, rand.nextLong()); //TODO Temporary
                    }
                }
            } else {
                world.setBlockToAir(pos);
            }
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
                for (int i = 0; i <= range; i++) {
                    BlockPos posOffset = pos.offset(horizontal, i);
                    if (box.isVecInside(posOffset)) {
                        IBlockState adjacent = world.getBlockState(pos.offset(horizontal, i));
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

    static class Maze {


   /* private void generateMaze(boolean[][] array, Random random, int x, int y) { //Old maze stuff. Kept as reference
        ArrayList<Pair> choices = new ArrayList<>();
        do {
            choices.clear();
            if (x + 2 < 16 && !array[x + 2][y])
                choices.add(new Pair(2, 0));
            if (x - 2 >= 0 && !array[x - 2][y])
                choices.add(new Pair(-2, 0));
            if (y + 2 < 16 && !array[x][y + 2])
                choices.add(new Pair(0, 2));
            if (y - 2 >= 0 && !array[x][y - 2])
                choices.add(new Pair(0, -2));

            if (choices.size() > 0) {
                int i = random.nextInt(choices.size());
                Pair choice = choices.get(i);
                choices.remove(i);
                array[choice.x + x][choice.y + y] = true;
                array[x + choice.x / 2][y + choice.y / 2] = true;
                generateMaze(array, random, x + choice.x, y + choice.y);
            }

        } while (choices.size() > 0);
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
    }*/
    }
}