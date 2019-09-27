package com.teammetallurgy.atum.world.gen.structure.tomb;

import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.LimestoneChestTileEntity;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinPieces;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
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

public class TombPieces {
    public static final ResourceLocation TOMB = new ResourceLocation(Constants.MOD_ID, "tomb");

    public static void registerTomb() {
        MapGenStructureIO.registerStructure(MapGenTomb.Start.class, String.valueOf(TOMB));
        MapGenStructureIO.registerStructureComponent(TombPieces.TombTemplate.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "tomb_template")));
    }

    public static class TombTemplate extends StructureComponentTemplate {
        private Rotation rotation;
        private Mirror mirror;

        public TombTemplate() { //Needs empty constructor
        }

        TombTemplate(TemplateManager manager, BlockPos pos, Random random, Rotation rotation) {
            this(manager, pos, random, rotation, Mirror.NONE);
        }

        private TombTemplate(TemplateManager manager, BlockPos pos, Random random, Rotation rotation, Mirror mirror) {
            super(0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.mirror = mirror;
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager manager) {
            Template template = manager.getTemplate(null, new ResourceLocation(Constants.MOD_ID, "tomb"));
            PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull World world, @Nonnull Random rand, @Nonnull StructureBoundingBox box) {
            if (function.equals("SpawnerUndead")) {
                if (box.isVecInside(pos)) {
                    world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);

                    TileEntity tileEntity = world.getTileEntity(pos);
                    if (tileEntity instanceof TileEntityMobSpawner) {
                        ((TileEntityMobSpawner) tileEntity).getSpawnerBaseLogic().setEntityId(RuinPieces.RuinTemplate.UNDEAD.get(rand.nextInt(RuinPieces.RuinTemplate.UNDEAD.size())).getRegistryName());
                    }
                }
            } else if (function.equals("Crate")) {
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
            } else if (function.equals("Chest")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    TileEntity tileentity = world.getTileEntity(posDown);
                    if (tileentity instanceof LimestoneChestTileEntity) {
                        ((LimestoneChestTileEntity) tileentity).setLootTable(AtumLootTables.TOMB_CHEST, rand.nextLong());
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