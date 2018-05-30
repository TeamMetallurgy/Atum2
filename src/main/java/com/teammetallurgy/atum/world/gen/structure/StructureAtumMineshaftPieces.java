package com.teammetallurgy.atum.world.gen.structure;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.limestone.BlockLimestoneBricks;
import com.teammetallurgy.atum.blocks.limestone.BlockLimestoneWall;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockAtumTorch;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class StructureAtumMineshaftPieces {
    public static void registerMineshaft() {
        MapGenStructureIO.registerStructure(StructureAtumMineshaftStart.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "Mineshaft")));
        MapGenStructureIO.registerStructureComponent(Corridor.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "MSCorridor")));
        MapGenStructureIO.registerStructureComponent(Cross.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "MSCrossing")));
        MapGenStructureIO.registerStructureComponent(Room.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "MSRoom")));
        MapGenStructureIO.registerStructureComponent(Stairs.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "MSStairs")));
    }

    private static MineshaftPiece createRandomShaftPiece(List<StructureComponent> components, Random random, int x, int y, int z, EnumFacing facing, int type, MapGenAtumMineshaft.Type mineshaftType) {
        int i = random.nextInt(100);

        if (i >= 80) {
            StructureBoundingBox box = StructureAtumMineshaftPieces.Cross.findCrossing(components, random, x, y, z, facing);
            if (box != null) {
                return new StructureAtumMineshaftPieces.Cross(type, random, box, facing, mineshaftType);
            }
        } else if (i >= 70) {
            StructureBoundingBox box = StructureAtumMineshaftPieces.Stairs.findStairs(components, random, x, y, z, facing);
            if (box != null) {
                return new StructureAtumMineshaftPieces.Stairs(type, random, box, facing, mineshaftType);
            }
        } else {
            StructureBoundingBox box = StructureAtumMineshaftPieces.Corridor.findCorridorSize(components, random, x, y, z, facing);
            if (box != null) {
                return new StructureAtumMineshaftPieces.Corridor(type, random, box, facing, mineshaftType);
            }
        }
        return null;
    }

    private static MineshaftPiece generateAndAddPiece(StructureComponent component, List<StructureComponent> components, Random random, int x, int y, int z, EnumFacing facing, int type) {
        if (type > 8) {
            return null;
        } else if (Math.abs(x - component.getBoundingBox().minX) <= 80 && Math.abs(z - component.getBoundingBox().minZ) <= 80) {
            MapGenAtumMineshaft.Type mineshaftType = ((MineshaftPiece)component).mineshaftType;
            MineshaftPiece piece = createRandomShaftPiece(components, random, x, y, z, facing, type + 1, mineshaftType);
            if (piece != null) {
                System.out.println("Mineshaft type: " + ((MineshaftPiece) component).mineshaftType.name() + " generated at: " +  new BlockPos(x, y, z));
                components.add(piece);
                piece.buildComponent(component, components, random);
            }
            return piece;
        } else {
            return null;
        }
    }

    public static class Corridor extends StructureAtumMineshaftPieces.MineshaftPiece {
        private boolean hasRails;
        private boolean hasTarantula;
        private boolean spawnerPlaced;
        private int sectionCount;

        public Corridor() {
        }

        @Override
        protected void writeStructureToNBT(@Nonnull NBTTagCompound compound) {
            compound.setBoolean("hr", this.hasRails);
            compound.setBoolean("sc", this.hasTarantula);
            compound.setBoolean("hps", this.spawnerPlaced);
            compound.setInteger("Num", this.sectionCount);
        }

        @Override
        protected void readStructureFromNBT(@Nonnull NBTTagCompound compound, @Nonnull TemplateManager manager) {
            this.hasRails = compound.getBoolean("hr");
            this.hasTarantula = compound.getBoolean("sc");
            this.spawnerPlaced = compound.getBoolean("hps");
            this.sectionCount = compound.getInteger("Num");
        }

        Corridor(int type, Random random, StructureBoundingBox box, EnumFacing facing, MapGenAtumMineshaft.Type mineshaftType) {
            super(type, mineshaftType);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
            this.hasRails = random.nextInt(3) == 0;
            this.hasTarantula = !this.hasRails && random.nextInt(23) == 0;

            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.sectionCount = box.getZSize() / 5;
            } else {
                this.sectionCount = box.getXSize() / 5;
            }
        }

        static StructureBoundingBox findCorridorSize(List<StructureComponent> components, Random random, int x, int y, int z, EnumFacing facing) {
            StructureBoundingBox box = new StructureBoundingBox(x, y, z, x, y + 2, z);
            int i;

            for (i = random.nextInt(3) + 2; i > 0; --i) {
                int j = i * 5;

                switch (facing) {
                    case NORTH:
                    default:
                        box.maxX = x + 2;
                        box.minZ = z - (j - 1);
                        break;
                    case SOUTH:
                        box.maxX = x + 2;
                        box.maxZ = z + (j - 1);
                        break;
                    case WEST:
                        box.minX = x - (j - 1);
                        box.maxZ = z + 2;
                        break;
                    case EAST:
                        box.maxX = x + (j - 1);
                        box.maxZ = z + 2;
                }

                if (StructureComponent.findIntersecting(components, box) == null) {
                    break;
                }
            }
            return i > 0 ? box : null;
        }

        @Override
        public void buildComponent(StructureComponent component, List<StructureComponent> list, Random random) {
            int i = this.getComponentType();
            int j = random.nextInt(4);
            EnumFacing facing = this.getCoordBaseMode();

            if (facing != null) {
                switch (facing) {
                    case NORTH:
                    default:
                        if (j <= 1) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, facing, i);
                        } else if (j == 2) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, i);
                        } else {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, i);
                        }

                        break;
                    case SOUTH:
                        if (j <= 1) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, facing, i);
                        } else if (j == 2) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST, i);
                        } else {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST, i);
                        }

                        break;
                    case WEST:
                        if (j <= 1) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, facing, i);
                        } else if (j == 2) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                        } else {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        }
                        break;
                    case EAST:
                        if (j <= 1) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, facing, i);
                        } else if (j == 2) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                        } else {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        }
                }
            }

            if (i < 8) {
                if (facing != EnumFacing.NORTH && facing != EnumFacing.SOUTH) {
                    for (int count = this.boundingBox.minX + 3; count + 3 <= this.boundingBox.maxX; count += 5) {
                        int j1 = random.nextInt(5);

                        if (j1 == 0) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, count, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i + 1);
                        } else if (j1 == 1) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, count, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i + 1);
                        }
                    }
                } else {
                    for (int k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5) {
                        int l = random.nextInt(5);

                        if (l == 0) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, k, EnumFacing.WEST, i + 1);
                        } else if (l == 1) {
                            StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, k, EnumFacing.EAST, i + 1);
                        }
                    }
                }
            }
        }

        @Override
        protected boolean generateChest(@Nonnull World world, @Nonnull StructureBoundingBox box, @Nonnull Random random, int x, int y, int z, @Nonnull ResourceLocation loot) {
            BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));

            if (box.isVecInside(pos) && world.getBlockState(pos).getMaterial() == Material.AIR && world.getBlockState(pos.down()).getMaterial() != Material.AIR) {
                IBlockState railState = Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, random.nextBoolean() ? BlockRailBase.EnumRailDirection.NORTH_SOUTH : BlockRailBase.EnumRailDirection.EAST_WEST);
                this.setBlockState(world, railState, x, y, z, box);
                EntityMinecartChest cartChest = new EntityMinecartChest(world, (double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F));
                cartChest.setLootTable(loot, random.nextLong());
                world.spawnEntity(cartChest);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean addComponentParts(@Nonnull World world, @Nonnull Random random, @Nonnull StructureBoundingBox box) {
            if (this.isLiquidInStructureBoundingBox(world, box)) {
                return false;
            } else {
                int count = this.sectionCount * 5 - 1;
                IBlockState iblockstate = this.getSupportBlock();
                this.fillWithBlocks(world, box, 0, 0, 0, 2, 1, count, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.generateMaybeBox(world, box, random, 0.8F, 0, 2, 0, 2, 2, count, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false, 0);

                for (int j1 = 0; j1 < this.sectionCount; ++j1) {
                    int k1 = 2 + j1 * 5;
                    this.placeSupport(world, box, 0, 0, k1, 2, 2, random);

                    if (random.nextInt(100) == 0) {
                        this.generateChest(world, box, random, 2, 0, k1 - 1, AtumLootTables.RUINS); //TODO Make new Atum mineshaft loot tables
                    }

                    if (random.nextInt(100) == 0) {
                        this.generateChest(world, box, random, 0, 0, k1 + 1, AtumLootTables.RUINS); //TODO Make new Atum mineshaft loot tables
                    }

                    if (this.hasTarantula && !this.spawnerPlaced) {
                        int l1 = this.getYWithOffset(0);
                        int i2 = k1 - 1 + random.nextInt(3);
                        int j2 = this.getXWithOffset(1, i2);
                        int k2 = this.getZWithOffset(1, i2);
                        BlockPos pos = new BlockPos(j2, l1, k2);

                        if (box.isVecInside(pos) && this.getSkyBrightness(world, 1, 0, i2, box) < 8) {
                            this.spawnerPlaced = true;
                            world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
                            TileEntity tileEntity = world.getTileEntity(pos);

                            if (tileEntity instanceof TileEntityMobSpawner) {
                                MapGenAtumMineshaft.Type type = this.mineshaftType;
                                int chance = random.nextInt(100);
                                if (chance < 40) {
                                    if (type == MapGenAtumMineshaft.Type.DEADWOOD) {
                                        ((TileEntityMobSpawner) tileEntity).getSpawnerBaseLogic().setEntityId(AtumEntities.FORSAKEN.getRegistryName());
                                    } else if (type == MapGenAtumMineshaft.Type.LIMESTONE) {
                                        ((TileEntityMobSpawner) tileEntity).getSpawnerBaseLogic().setEntityId(AtumEntities.STONEGUARD.getRegistryName());
                                    }
                                } else {
                                    ((TileEntityMobSpawner) tileEntity).getSpawnerBaseLogic().setEntityId(AtumEntities.TARANTULA.getRegistryName());
                                }
                            }
                        }
                    }
                }

                for (int l2 = 0; l2 <= 2; ++l2) {
                    for (int i3 = 0; i3 <= count; ++i3) {
                        IBlockState state = this.getBlockStateFromPos(world, l2, -1, i3, box);

                        if (state.getMaterial() == Material.AIR && this.getSkyBrightness(world, l2, -1, i3, box) < 8) {
                            this.setBlockState(world, iblockstate, l2, -1, i3, box);
                        }
                    }
                }

                if (this.hasRails) {
                    IBlockState railState = Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);

                    for (int j3 = 0; j3 <= count; ++j3) {
                        IBlockState state = this.getBlockStateFromPos(world, 1, -1, j3, box);

                        if (state.getMaterial() != Material.AIR && state.isFullBlock()) {
                            float f = this.getSkyBrightness(world, 1, 0, j3, box) > 8 ? 0.9F : 0.7F;
                            this.randomlyPlaceBlock(world, box, random, f, 1, 0, j3, railState);
                        }
                    }
                }
                return true;
            }
        }

        private void placeSupport(World world, StructureBoundingBox box, int x, int y, int z, int yMax, int xMin, Random random) {
            if (this.isSupportingBox(world, box, x, xMin, yMax, z)) {
                IBlockState plankState = this.getSupportBlock();
                IBlockState fenceState = this.getPillarBlock();
                IBlockState airState = Blocks.AIR.getDefaultState();
                this.fillWithBlocks(world, box, x, y, z, x, yMax - 1, z, fenceState, airState, false);
                this.fillWithBlocks(world, box, xMin, y, z, xMin, yMax - 1, z, fenceState, airState, false);

                if (random.nextInt(4) == 0) {
                    this.fillWithBlocks(world, box, x, yMax, z, x, yMax, z, plankState, airState, false);
                    this.fillWithBlocks(world, box, xMin, yMax, z, xMin, yMax, z, plankState, airState, false);
                } else {
                    this.fillWithBlocks(world, box, x, yMax, z, xMin, yMax, z, plankState, airState, false);
                    if (mineshaftType == MapGenAtumMineshaft.Type.DEADWOOD) {
                        this.randomlyPlaceBlock(world, box, random, 0.05F, x + 1, yMax, z - 1, AtumBlocks.DEADWOOD_TORCH.getDefaultState().withProperty(BlockAtumTorch.FACING, EnumFacing.NORTH));
                        this.randomlyPlaceBlock(world, box, random, 0.05F, x + 1, yMax, z + 1, AtumBlocks.DEADWOOD_TORCH.getDefaultState().withProperty(BlockAtumTorch.FACING, EnumFacing.SOUTH));
                    } else {
                        this.randomlyPlaceBlock(world, box, random, 0.05F, x + 1, yMax, z - 1, AtumBlocks.LIMESTONE_TORCH.getDefaultState().withProperty(BlockAtumTorch.FACING, EnumFacing.NORTH));
                        this.randomlyPlaceBlock(world, box, random, 0.05F, x + 1, yMax, z + 1, AtumBlocks.LIMESTONE_TORCH.getDefaultState().withProperty(BlockAtumTorch.FACING, EnumFacing.SOUTH));
                    }
                }
            }
        }
    }

    public static class Cross extends MineshaftPiece {
        private EnumFacing corridorDirection;
        private boolean isMultipleFloors;

        public Cross() {
        }

        @Override
        protected void writeStructureToNBT(@Nonnull NBTTagCompound compound) {
            compound.setBoolean("tf", this.isMultipleFloors);
            compound.setInteger("D", this.corridorDirection.getHorizontalIndex());
        }

        @Override
        protected void readStructureFromNBT(@Nonnull NBTTagCompound compound, @Nonnull TemplateManager manager) {
            this.isMultipleFloors = compound.getBoolean("tf");
            this.corridorDirection = EnumFacing.getHorizontal(compound.getInteger("D"));
        }

        public Cross(int type, Random random, StructureBoundingBox box, @Nullable EnumFacing facing, MapGenAtumMineshaft.Type mineshaftType) {
            super(type, mineshaftType);
            this.corridorDirection = facing;
            this.boundingBox = box;
            this.isMultipleFloors = box.getYSize() > 3;
        }

        public static StructureBoundingBox findCrossing(List<StructureComponent> list, Random random, int x, int y, int z, EnumFacing facing) {
            StructureBoundingBox box = new StructureBoundingBox(x, y, z, x, y + 2, z);

            if (random.nextInt(4) == 0) {
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
            return StructureComponent.findIntersecting(list, box) != null ? null : box;
        }

        @Override
        public void buildComponent(StructureComponent component, List<StructureComponent> list, Random random) {
            int type = this.getComponentType();

            switch (this.corridorDirection) {
                case NORTH:
                default:
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, type);
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, type);
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, type);
                    break;
                case SOUTH:
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, type);
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, type);
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, type);
                    break;
                case WEST:
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, type);
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, type);
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, type);
                    break;
                case EAST:
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, type);
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, type);
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, type);
            }

            if (this.isMultipleFloors) {
                if (random.nextBoolean()) {
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, type);
                }
                if (random.nextBoolean()) {
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, type);
                }
                if (random.nextBoolean()) {
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, type);
                }
                if (random.nextBoolean()) {
                    StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, type);
                }
            }
        }

        @Override
        public boolean addComponentParts(@Nonnull World world, @Nonnull Random random, @Nonnull StructureBoundingBox box) {
            if (this.isLiquidInStructureBoundingBox(world, box)) {
                return false;
            } else {
                IBlockState plankState = this.getSupportBlock();

                if (this.isMultipleFloors) {
                    this.fillWithBlocks(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                    this.fillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                    this.fillWithBlocks(world, box, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                    this.fillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                    this.fillWithBlocks(world, box, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                } else {
                    this.fillWithBlocks(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                    this.fillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }
                this.placeSupportPillar(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, box, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, box, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);

                for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                    for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                        if (this.getBlockStateFromPos(world, i, this.boundingBox.minY - 1, j, box).getMaterial() == Material.AIR && this.getSkyBrightness(world, i, this.boundingBox.minY - 1, j, box) < 8) {
                            this.setBlockState(world, plankState, i, this.boundingBox.minY - 1, j, box);
                        }
                    }
                }
                return true;
            }
        }

        private void placeSupportPillar(World world, StructureBoundingBox box, int x, int y, int z, int yMax) {
            if (this.getBlockStateFromPos(world, x, yMax + 1, z, box).getMaterial() != Material.AIR) {
                this.fillWithBlocks(world, box, x, y, z, x, yMax, z, this.getSupportBlock(), Blocks.AIR.getDefaultState(), false);
            }
        }
    }

    abstract static class MineshaftPiece extends StructureComponent {
        MapGenAtumMineshaft.Type mineshaftType;

        MineshaftPiece() {
        }

        MineshaftPiece(int type, MapGenAtumMineshaft.Type mineshaftType) {
            super(type);
            this.mineshaftType = mineshaftType;
        }

        @Override
        protected void writeStructureToNBT(@Nonnull NBTTagCompound compound) {
            compound.setInteger("MST", this.mineshaftType.ordinal());
        }
        
        @Override
        protected void readStructureFromNBT(@Nonnull NBTTagCompound compound, @Nonnull TemplateManager manager) {
            this.mineshaftType = MapGenAtumMineshaft.Type.byOrdinal(compound.getInteger("MST"));
        }

        IBlockState getSupportBlock() {
            if (this.mineshaftType == null) mineshaftType = MapGenAtumMineshaft.Type.DEADWOOD;
            switch (this.mineshaftType) {
                case DEADWOOD:
                default:
                    return BlockAtumPlank.getPlank(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState();
                case LIMESTONE:
                    return BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE).getDefaultState();
            }
        }

        IBlockState getPillarBlock() {
            switch (this.mineshaftType) {
                case DEADWOOD:
                default:
                    return AtumBlocks.DEADWOOD_FENCE.getDefaultState();
                case LIMESTONE:
                    return BlockLimestoneWall.getWall(BlockLimestoneBricks.BrickType.LARGE).getDefaultState();
            }
        }

        boolean isSupportingBox(World world, StructureBoundingBox boundingBox, int xMin, int x, int y, int z) {
            for (int i = xMin; i <= x; ++i) {
                if (this.getBlockStateFromPos(world, i, y + 1, z, boundingBox).getMaterial() == Material.AIR) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class Room extends MineshaftPiece {
        private final List<StructureBoundingBox> connectedRooms = Lists.newLinkedList();

        public Room() {
        }

        public Room(int type, Random random, int x, int z, MapGenAtumMineshaft.Type mineshaftType) {
            super(type, mineshaftType);
            this.mineshaftType = mineshaftType;
            this.boundingBox = new StructureBoundingBox(x, 50, z, x + 7 + random.nextInt(6), 54 + random.nextInt(6), z + 7 + random.nextInt(6));
        }

        @Override
        public void buildComponent(StructureComponent component, List<StructureComponent> list, Random random) {
            int i = this.getComponentType();
            int j = this.boundingBox.getYSize() - 3 - 1;

            if (j <= 0) {
                j = 1;
            }
            int k;

            for (k = 0; k < this.boundingBox.getXSize(); k = k + 4) {
                k = k + random.nextInt(this.boundingBox.getXSize());

                if (k + 3 > this.boundingBox.getXSize()) {
                    break;
                }
                MineshaftPiece piece = StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX + k, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);

                if (piece != null) {
                    StructureBoundingBox box = piece.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(box.minX, box.minY, this.boundingBox.minZ, box.maxX, box.maxY, this.boundingBox.minZ + 1));
                }
            }

            for (k = 0; k < this.boundingBox.getXSize(); k = k + 4) {
                k = k + random.nextInt(this.boundingBox.getXSize());

                if (k + 3 > this.boundingBox.getXSize()) {
                    break;
                }
                MineshaftPiece piece = StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX + k, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);

                if (piece != null) {
                    StructureBoundingBox box = piece.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(box.minX, box.minY, this.boundingBox.maxZ - 1, box.maxX, box.maxY, this.boundingBox.maxZ));
                }
            }

            for (k = 0; k < this.boundingBox.getZSize(); k = k + 4) {
                k = k + random.nextInt(this.boundingBox.getZSize());

                if (k + 3 > this.boundingBox.getZSize()) {
                    break;
                }
                MineshaftPiece piece = StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.minZ + k, EnumFacing.WEST, i);

                if (piece != null) {
                    StructureBoundingBox box = piece.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(this.boundingBox.minX, box.minY, box.minZ, this.boundingBox.minX + 1, box.maxY, box.maxZ));
                }
            }

            for (k = 0; k < this.boundingBox.getZSize(); k = k + 4) {
                k = k + random.nextInt(this.boundingBox.getZSize());

                if (k + 3 > this.boundingBox.getZSize()) {
                    break;
                }
                StructureComponent structureComponent = StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.minZ + k, EnumFacing.EAST, i);

                if (structureComponent != null) {
                    StructureBoundingBox structureboundingbox3 = structureComponent.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(this.boundingBox.maxX - 1, structureboundingbox3.minY, structureboundingbox3.minZ, this.boundingBox.maxX, structureboundingbox3.maxY, structureboundingbox3.maxZ));
                }
            }
        }

        @Override
        public boolean addComponentParts(@Nonnull World world, @Nonnull Random random, @Nonnull StructureBoundingBox box) {
            if (this.isLiquidInStructureBoundingBox(world, box)) {
                return false;
            } else {
                this.fillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, AtumBlocks.SAND.getDefaultState(), Blocks.AIR.getDefaultState(), true);
                this.fillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

                for (StructureBoundingBox connectedBox : this.connectedRooms) {
                    this.fillWithBlocks(world, box, connectedBox.minX, connectedBox.maxY - 2, connectedBox.minZ, connectedBox.maxX, connectedBox.maxY, connectedBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }
                this.randomlyRareFillWithBlocks(world, box, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), false);
                return true;
            }
        }

        @Override
        public void offset(int x, int y, int z) {
            super.offset(x, y, z);

            for (StructureBoundingBox box : this.connectedRooms) {
                box.offset(x, y, z);
            }
        }

        @Override
        protected void writeStructureToNBT(@Nonnull NBTTagCompound compound) {
            NBTTagList tagList = new NBTTagList();

            for (StructureBoundingBox box : this.connectedRooms) {
                tagList.appendTag(box.toNBTTagIntArray());
            }
            compound.setTag("Entrances", tagList);
        }

        @Override
        protected void readStructureFromNBT(@Nonnull NBTTagCompound compound, @Nonnull TemplateManager manager) {
            NBTTagList tagList = compound.getTagList("Entrances", 11);
            for (int i = 0; i < tagList.tagCount(); ++i) {
                this.connectedRooms.add(new StructureBoundingBox(tagList.getIntArrayAt(i)));
            }
        }
    }

    public static class Stairs extends MineshaftPiece {
        public Stairs() {
        }

        @Override
        protected void writeStructureToNBT(@Nonnull NBTTagCompound compound) {
        }

        @Override
        protected void readStructureFromNBT(@Nonnull NBTTagCompound compound, @Nonnull TemplateManager manager) {
        }

        public Stairs(int type, Random random, StructureBoundingBox box, EnumFacing facing, MapGenAtumMineshaft.Type mineshaftType) {
            super(type, mineshaftType);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
        }

        public static StructureBoundingBox findStairs(List<StructureComponent> list, Random random, int x, int y, int z, EnumFacing facing) {
            StructureBoundingBox box = new StructureBoundingBox(x, y - 5, z, x, y + 2, z);
            switch (facing) {
                case NORTH:
                default:
                    box.maxX = x + 2;
                    box.minZ = z - 8;
                    break;
                case SOUTH:
                    box.maxX = x + 2;
                    box.maxZ = z + 8;
                    break;
                case WEST:
                    box.minX = x - 8;
                    box.maxZ = z + 2;
                    break;
                case EAST:
                    box.maxX = x + 8;
                    box.maxZ = z + 2;
            }
            return StructureComponent.findIntersecting(list, box) != null ? null : box;
        }

        @Override
        public void buildComponent(StructureComponent component, List<StructureComponent> list, Random random) {
            int i = this.getComponentType();
            EnumFacing facing = this.getCoordBaseMode();

            if (facing != null) {
                switch (facing) {
                    case NORTH:
                    default:
                        StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                        break;
                    case SOUTH:
                        StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        break;
                    case WEST:
                        StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, i);
                        break;
                    case EAST:
                        StructureAtumMineshaftPieces.generateAndAddPiece(component, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, i);
                }
            }
        }

        @Override
        public boolean addComponentParts(@Nonnull World world, @Nonnull Random random, @Nonnull StructureBoundingBox box) {
            if (this.isLiquidInStructureBoundingBox(world, box)) {
                return false;
            } else {
                this.fillWithBlocks(world, box, 0, 5, 0, 2, 7, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithBlocks(world, box, 0, 0, 7, 2, 2, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

                for (int i = 0; i < 5; ++i) {
                    this.fillWithBlocks(world, box, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }
                return true;
            }
        }
    }
}