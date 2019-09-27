package com.teammetallurgy.atum.world.gen.structure.girafitomb;

import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.nbt.CompoundNBT;
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

public class GirafiTombPieces {
    public static final ResourceLocation GIRAFI_TOMB = new ResourceLocation(Constants.MOD_ID, "girafi_tomb");

    public static void registerGirafiTomb() {
        MapGenStructureIO.registerStructure(MapGenGirafiTomb.Start.class, String.valueOf(GIRAFI_TOMB));
        MapGenStructureIO.registerStructureComponent(GirafiTombPieces.GirafiTombTemplate.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "girafi_tomb_template")));
    }

    public static class GirafiTombTemplate extends StructureComponentTemplate {
        private Rotation rotation;
        private Mirror mirror;

        public GirafiTombTemplate() { //Needs empty constructor
        }

        GirafiTombTemplate(TemplateManager manager, BlockPos pos, Rotation rotation) {
            this(manager, pos, rotation, Mirror.NONE);
        }

        private GirafiTombTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, Mirror mirror) {
            super(0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.mirror = mirror;
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager manager) {
            Template template = manager.getTemplate(null, GIRAFI_TOMB);
            PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull World world, @Nonnull Random rand, @Nonnull StructureBoundingBox box) {
            if (function.equals("Crate")) {
                if (box.isVecInside(pos)) {
                    if (rand.nextDouble() <= 0.15D) {
                        world.setBlockState(pos, BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD).correctFacing(world, pos, BlockCrate.getCrate(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState()), 2);

                        TileEntity tileEntity = world.getTileEntity(pos);
                        if (tileEntity instanceof CrateTileEntity) {
                            ((CrateTileEntity) tileEntity).setLootTable(AtumLootTables.CRATE, rand.nextLong());
                        }
                    } else {
                        world.setBlockToAir(pos);
                    }
                }
            } else if (function.equals("GirafiSarcophagus")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    TileEntity tileentity = world.getTileEntity(posDown);
                    if (tileentity instanceof SarcophagusTileEntity) {
                        ((SarcophagusTileEntity) tileentity).setLootTable(AtumLootTables.GIRAFI_TOMB, rand.nextLong());
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
}