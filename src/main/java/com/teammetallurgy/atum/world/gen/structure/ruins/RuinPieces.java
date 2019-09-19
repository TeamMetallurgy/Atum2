package com.teammetallurgy.atum.world.gen.structure.ruins;

import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RuinPieces {
    public static final ResourceLocation RUIN = new ResourceLocation(Constants.MOD_ID, "ruin");

    public static void registerRuins() {
        MapGenStructureIO.registerStructure(MapGenRuin.Start.class, String.valueOf(RUIN));
        MapGenStructureIO.registerStructureComponent(RuinTemplate.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "ruin_template")));
    }

    public static class RuinTemplate extends StructureComponentTemplate {
        private static final List<EntityEntry> BANDITS = Arrays.asList(AtumEntities.BARBARIAN, AtumEntities.BRIGAND, AtumEntities.NOMAD);
        public static final List<EntityEntry> UNDEAD = Arrays.asList(AtumEntities.BONESTORM, AtumEntities.FORSAKEN, AtumEntities.MUMMY, AtumEntities.WRAITH);
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
            this.ruinType = MathHelper.getInt(random, 1, 19);
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
                            ((TileEntityCrate) tileEntity).setLootTable(AtumLootTables.CRATE, rand.nextLong());
                        }
                    } else {
                        world.setBlockToAir(pos);
                    }
                }
            }
        }

        @Override
        protected void writeStructureToNBT(CompoundNBT compound) {
            super.writeStructureToNBT(compound);
            compound.setString("Rot", this.placeSettings.getRotation().name());
            compound.setString("Mi", this.placeSettings.getMirror().name());
            compound.setInteger("Type", this.ruinType);
        }

        @Override
        protected void readStructureFromNBT(CompoundNBT compound, TemplateManager manager) {
            super.readStructureFromNBT(compound, manager);
            this.rotation = Rotation.valueOf(compound.getString("Rot"));
            this.mirror = Mirror.valueOf(compound.getString("Mi"));
            this.ruinType = compound.getInteger("Type");
            this.loadTemplate(manager);
        }
    }
}