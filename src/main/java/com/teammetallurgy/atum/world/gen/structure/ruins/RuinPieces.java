package com.teammetallurgy.atum.world.gen.structure.ruins;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.nbt.NBTTagCompound;
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

import javax.annotation.Nonnull;
import java.util.Random;

public class RuinPieces {
    public static final ResourceLocation RUIN = new ResourceLocation(Constants.MOD_ID, "ruin");

    public static void registerRuins() {
        MapGenStructureIO.registerStructure(MapGenRuin.Start.class, String.valueOf(RUIN));
        MapGenStructureIO.registerStructureComponent(RuinTemplate.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "ruin_template")));
    }

    public static class RuinTemplate extends StructureComponentTemplate {
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
            this.ruinType = MathHelper.getInt(random, 1, 3);
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager manager) {
            Template template = manager.getTemplate(null, new ResourceLocation(Constants.MOD_ID, "ruins/ruin_" + this.ruinType));
            PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull World world, @Nonnull Random rand, @Nonnull StructureBoundingBox box) {
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