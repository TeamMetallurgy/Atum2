package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.gen.structure.girafitomb.GirafiTombStructure;
import com.teammetallurgy.atum.world.gen.structure.lighthouse.LighthouseStructure;
import com.teammetallurgy.atum.world.gen.structure.mineshaft.AtumMineshaftConfig;
import com.teammetallurgy.atum.world.gen.structure.mineshaft.AtumMineshaftStructure;
import com.teammetallurgy.atum.world.gen.structure.pyramid.PyramidStructure;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinStructure;
import com.teammetallurgy.atum.world.gen.structure.tomb.TombStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumStructures {
    private static final List<Structure<?>> STRUCTURES = new ArrayList<>();
    //Structures
    public static final Structure<NoFeatureConfig> GIRAFI_TOMB_STRUCTURE = register("girafi_tomb", new GirafiTombStructure(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.SURFACE_STRUCTURES);
    public static final Structure<NoFeatureConfig> LIGHTHOUSE_STRUCTURE = register("lighthouse", new LighthouseStructure(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.SURFACE_STRUCTURES);
    public static final Structure<NoFeatureConfig> TOMB_STRUCTURE = register("tomb", new TombStructure(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.UNDERGROUND_STRUCTURES);
    public static final Structure<NoFeatureConfig> RUIN_STRUCTURE = register("ruin", new RuinStructure(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.SURFACE_STRUCTURES);
    public static final Structure<NoFeatureConfig> PYRAMID_STRUCTURE = register("pyramid", new PyramidStructure(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.SURFACE_STRUCTURES);
    public static final Structure<AtumMineshaftConfig> MINESHAFT_STRUCTURE = register("mineshaft", new AtumMineshaftStructure(AtumMineshaftConfig.CODEC), GenerationStage.Decoration.UNDERGROUND_STRUCTURES);

    //Structure Features
    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> GIRAFI_TOMB_FEATURE = register(GIRAFI_TOMB_STRUCTURE, NoFeatureConfig.field_236559_b_);
    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> LIGHTHOUSE_FEATURE = register(LIGHTHOUSE_STRUCTURE, NoFeatureConfig.field_236559_b_);
    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> TOMB_FEATURE = register(TOMB_STRUCTURE, NoFeatureConfig.field_236559_b_);
    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> RUIN_FEATURE = register(RUIN_STRUCTURE, NoFeatureConfig.field_236559_b_);
    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> PYRAMID_FEATURE = register(PYRAMID_STRUCTURE, NoFeatureConfig.field_236559_b_);
    public static final StructureFeature<AtumMineshaftConfig, ? extends Structure<AtumMineshaftConfig>> MINESHAFT_FEATURE = register(MINESHAFT_STRUCTURE, new AtumMineshaftConfig(0.1F, AtumMineshaftStructure.Type.LIMESTONE)); //TODO Doing the config like this, is probably a problem

    private static <F extends Structure<?>> F register(String name, F structure, GenerationStage.Decoration generationStage) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        structure.setRegistryName(id);
        STRUCTURES.add(structure);
        Structure.field_236365_a_.put(id.toString().toLowerCase(Locale.ROOT), structure);
        Structure.field_236385_u_.put(structure, generationStage);
        return structure;
    }

    private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, ?> register(F structure, FC fc) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, structure.getStructureName(), structure.func_236391_a_(fc));
    }

    @SubscribeEvent
    public static void registerStructure(RegistryEvent.Register<Structure<?>> event) {
        for (Structure<?> feature : STRUCTURES) {
            event.getRegistry().register(feature);
        }
    }
}