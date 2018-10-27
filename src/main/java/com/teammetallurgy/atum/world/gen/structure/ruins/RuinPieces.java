package com.teammetallurgy.atum.world.gen.structure.ruins;

import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockDoor;
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
import net.minecraft.world.gen.structure.StructureComponentTemplate;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.registry.EntityEntry;

import javax.annotation.Nonnull;
import java.util.Random;

public class RuinPieces {
    public static final ResourceLocation RUIN = new ResourceLocation(Constants.MOD_ID, "ruin");

    public static void registerRuins() {
        MapGenStructureIO.registerStructure(MapGenRuin.Start.class, String.valueOf(RUIN));
        MapGenStructureIO.registerStructureComponent(RuinTemplate.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "ruin_template")));
    }

    public static class RuinTemplate extends StructureComponentTemplate {
        private static final NonNullList<EntityEntry> BANDITS = NonNullList.from(AtumEntities.BARBARIAN, AtumEntities.BRIGAND, AtumEntities.NOMAD);
        private static final NonNullList<EntityEntry> UNDEAD = NonNullList.from(AtumEntities.BONESTORM, AtumEntities.FORSAKEN, AtumEntities.MUMMY, AtumEntities.WRAITH);
        private int ruinType;
        private Rotation rotation;
        private Mirror mirror;

        public RuinTemplate() { //Needs empty constructor
        }

        RuinTemplate(TemplateManager manager, BlockPos pos, Random random, Rotation rotation) {
            this(manager, pos, random, rotation, Mirror.NONE);
        }

        private RuinTemplate(TemplateManager manager, BlockPos pos, Random random, Rotation rotation, Mirror mirror) {
            super(0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.mirror = mirror;
            this.ruinType = MathHelper.getInt(random, 1, 15);
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager manager) {
            Template template = manager.getTemplate(null, new ResourceLocation(Constants.MOD_ID, "ruins/ruin" + this.ruinType));
            PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull World world, @Nonnull Random rand, @Nonnull StructureBoundingBox box) {
            if (function.equals("Spawner")) {
                if (box.isVecInside(pos)) {
                    world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);

                    TileEntity tileEntity = world.getTileEntity(pos);
                    if (tileEntity instanceof TileEntityMobSpawner) {
                        ResourceLocation location;
                        if (rand.nextDouble() < 0.5D) {
                            location = BANDITS.get(rand.nextInt(BANDITS.size())).getRegistryName();
                        } else {
                            location = UNDEAD.get(rand.nextInt(UNDEAD.size())).getRegistryName();
                        }
                        ((TileEntityMobSpawner) tileEntity).getSpawnerBaseLogic().setEntityId(location);
                    }
                }
            } else if (function.equals("Crate")) {
                if (box.isVecInside(pos)) {
                    if (rand.nextDouble() <= 0.15D) {
                        world.setBlockState(pos, BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD).correctFacing(world, pos, BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState()), 2);

                        TileEntity tileEntity = world.getTileEntity(pos);
                        if (tileEntity instanceof TileEntityCrate) {
                            ((TileEntityCrate) tileEntity).setLootTable(AtumLootTables.RUINS, rand.nextLong());
                        }
                    } else {
                        world.setBlockToAir(pos);
                    }
                }
            } /*else if (function.equals("Door")) {
                if (box.isVecInside(pos)) {
                    BlockDoor deadwoodDoor = AtumBlocks.DEADWOOD_DOOR;
                    if (world.mayPlace(deadwoodDoor, pos, false, EnumFacing.WEST, null) && world.getBlockState(pos).getBlock() != deadwoodDoor && world.getBlockState(pos.up()).getBlock() != deadwoodDoor) {
                        this.setDoorDate(world, deadwoodDoor.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.WEST), pos, box);
                    }
                }
            }*/
        }

        private void placeDoor(@Nonnull World world, StructureBoundingBox box, BlockPos pos, @Nonnull EnumFacing facing, BlockDoor door) {
            this.setDoorDate(world, door.getDefaultState().withProperty(BlockDoor.FACING, facing), pos, box);
            this.setDoorDate(world, door.getDefaultState().withProperty(BlockDoor.FACING, facing).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), pos.up(), box);
        }

        private void setDoorDate(World world, IBlockState state, BlockPos pos, StructureBoundingBox box) {
            if (box.isVecInside(pos)) {
                if (this.mirror != Mirror.NONE) {
                    state = state.withMirror(this.mirror);
                }
                if (this.rotation != Rotation.NONE) {
                    state = state.withRotation(this.rotation);
                }
                world.setBlockState(pos, state, 2);
            }
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound compound) {
            super.writeStructureToNBT(compound);
            compound.setString("Rot", this.placeSettings.getRotation().name());
            compound.setString("Mi", this.placeSettings.getMirror().name());
            compound.setInteger("Type", this.ruinType);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound compound, TemplateManager manager) {
            super.readStructureFromNBT(compound, manager);
            this.rotation = Rotation.valueOf(compound.getString("Rot"));
            this.mirror = Mirror.valueOf(compound.getString("Mi"));
            this.ruinType = compound.getInteger("Type");
            this.loadTemplate(manager);
        }
    }
}