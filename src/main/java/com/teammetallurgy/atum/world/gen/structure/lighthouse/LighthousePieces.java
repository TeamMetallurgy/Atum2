package com.teammetallurgy.atum.world.gen.structure.lighthouse;

import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.entity.efreet.EntitySunspeaker;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
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

public class LighthousePieces {
    public static final ResourceLocation LIGHTHOUSE = new ResourceLocation(Constants.MOD_ID, "lighthouse");

    public static void registerLighthouse() {
        MapGenStructureIO.registerStructure(MapGenLighthouse.Start.class, String.valueOf(LIGHTHOUSE));
        MapGenStructureIO.registerStructureComponent(LighthousePieces.LighthouseTemplate.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "lighthouse_template")));
    }

    public static class LighthouseTemplate extends StructureComponentTemplate {
        private Rotation rotation;
        private Mirror mirror;
        private int sunspeakerSpawned;

        public LighthouseTemplate() { //Needs empty constructor
        }

        LighthouseTemplate(TemplateManager manager, BlockPos pos, Rotation rotation) {
            this(manager, pos, rotation, Mirror.NONE);
        }

        private LighthouseTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, Mirror mirror) {
            super(0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.mirror = mirror;
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager manager) {
            Template template = manager.getTemplate(null, LIGHTHOUSE);
            PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        public boolean addComponentParts(@Nonnull World world, @Nonnull Random random, @Nonnull StructureBoundingBox box) {
            super.addComponentParts(world, random, box);
            this.spawnSunspeakers(world, box, -8, 1, -8, 5);
            return true;
        }
        
        private void spawnSunspeakers(World world, StructureBoundingBox box, int x, int y, int z, int count) {
            /*System.out.println("Min: " + box.minX + " " + box.minY + " " + box.minZ);
            System.out.println("Max: " + box.maxX + " " + box.maxY + " " + box.maxZ);*/
            if (this.sunspeakerSpawned < count) {
                for (int i = this.sunspeakerSpawned; i < count; ++i) {
                    int j = this.getXWithOffset(x + i, z);
                    int k = this.getYWithOffset(y);
                    int l = this.getZWithOffset(x + i, z);

                    BlockPos pos = new BlockPos(j, k, l);
                    if (!box.isVecInside(pos) || !world.isAirBlock(pos)) {
                        break;
                    }
                    System.out.println(pos);

                    ++this.sunspeakerSpawned;

                    EntitySunspeaker sunspeaker = new EntitySunspeaker(world);
                    sunspeaker.setLocationAndAngles((double) j + 0.5D, (double) k, (double) l + 0.5D, 0.0F, 0.0F);
                    sunspeaker.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(sunspeaker)), null);
                    world.spawnEntity(sunspeaker);
                }
            }
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull World world, @Nonnull Random rand, @Nonnull StructureBoundingBox box) {
            if (function.equals("PalmCrate")) {
                if (box.isVecInside(pos)) {
                    if (rand.nextDouble() <= 0.15D) {
                        BlockCrate palmCrate = BlockCrate.getCrate(BlockAtumPlank.WoodType.PALM);
                        world.setBlockState(pos, palmCrate.correctFacing(world, pos, palmCrate.getDefaultState()), 2);

                        TileEntity tileEntity = world.getTileEntity(pos);
                        if (tileEntity instanceof TileEntityCrate) {
                            ((TileEntityCrate) tileEntity).setLootTable(AtumLootTables.CRATE, rand.nextLong());
                        }
                    } else {
                        world.setBlockToAir(pos);
                    }
                }
            } else if (function.equals("HeartOfRa")) {
                if (box.isVecInside(pos)) {
                    world.setBlockState(pos, AtumBlocks.HEART_OF_RA.getDefaultState(), 2);
                }
            }
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound compound) {
            super.writeStructureToNBT(compound);
            compound.setString("Rot", this.placeSettings.getRotation().name());
            compound.setString("Mi", this.placeSettings.getMirror().name());
            compound.setInteger("SunspeakerCount", this.sunspeakerSpawned);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound compound, TemplateManager manager) {
            super.readStructureFromNBT(compound, manager);
            this.rotation = Rotation.valueOf(compound.getString("Rot"));
            this.mirror = Mirror.valueOf(compound.getString("Mi"));
            this.sunspeakerSpawned = compound.getInteger("SunspeakerCount");
            this.loadTemplate(manager);
        }
    }
}