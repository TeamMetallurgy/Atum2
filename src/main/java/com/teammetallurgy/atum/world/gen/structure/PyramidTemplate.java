package com.teammetallurgy.atum.world.gen.structure;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.nbt.NBTTagCompound;
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

public class PyramidTemplate extends StructureComponentTemplate {
    private static final ResourceLocation PYRAMID = new ResourceLocation(Constants.MOD_ID, "pyramid");
    private Rotation rotation;
    private Mirror mirror;

    public static void registerPyramid() {
        MapGenStructureIO.registerStructure(MapGenPyramid.Start.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "pyramid")));
        MapGenStructureIO.registerStructureComponent(PyramidTemplate.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "pyramid_template")));
    }

    public PyramidTemplate(TemplateManager manager, BlockPos pos, Rotation rotation) {
        this(manager, pos, rotation, Mirror.NONE);
    }

    public PyramidTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, Mirror mirror) {
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
}