package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.feature.pool.AtumJigsaw;
import com.teammetallurgy.atum.world.gen.feature.pool.GatehousePools;
import com.teammetallurgy.atum.world.gen.feature.pool.GenericVillagePools;
import com.teammetallurgy.atum.world.gen.structure.GatehouseStructure;
import com.teammetallurgy.atum.world.gen.structure.girafitomb.GirafiTombStructure;
import com.teammetallurgy.atum.world.gen.structure.mineshaft.AtumMineshaftConfig;
import com.teammetallurgy.atum.world.gen.structure.mineshaft.AtumMineshaftStructure;
import com.teammetallurgy.atum.world.gen.structure.pyramid.PyramidStructure;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinStructure;
import com.teammetallurgy.atum.world.gen.structure.tomb.TombStructure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumStructures {
    private static final List<StructureFeature<?>> STRUCTURES = new ArrayList<>();
    //Structures
    public static final StructureFeature<NoneFeatureConfiguration> GIRAFI_TOMB_STRUCTURE = register("girafi_tomb", new GirafiTombStructure(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
    public static final StructureFeature<NoneFeatureConfiguration> TOMB_STRUCTURE = register("tomb", new TombStructure(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.UNDERGROUND_STRUCTURES);
    public static final StructureFeature<NoneFeatureConfiguration> RUIN_STRUCTURE = register("ruin", new RuinStructure(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
    public static final StructureFeature<NoneFeatureConfiguration> PYRAMID_STRUCTURE = register("pyramid", new PyramidStructure(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
    public static final StructureFeature<AtumMineshaftConfig> MINESHAFT_STRUCTURE = register("mineshaft", new AtumMineshaftStructure(AtumMineshaftConfig.CODEC), GenerationStep.Decoration.UNDERGROUND_STRUCTURES);
    public static final StructureFeature<JigsawConfiguration> GATEHOUSE = register("gatehouse", new GatehouseStructure(JigsawConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);

    //Structure Features
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> GIRAFI_TOMB_FEATURE = register("girafi_tomb", GIRAFI_TOMB_STRUCTURE, NoneFeatureConfiguration.INSTANCE);
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> TOMB_FEATURE = register("tomb", TOMB_STRUCTURE, NoneFeatureConfiguration.INSTANCE);
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> RUIN_FEATURE = register("ruin", RUIN_STRUCTURE, NoneFeatureConfiguration.INSTANCE);
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> PYRAMID_FEATURE = register("pyramid", PYRAMID_STRUCTURE, NoneFeatureConfiguration.INSTANCE);
    public static final ConfiguredStructureFeature<AtumMineshaftConfig, ? extends StructureFeature<AtumMineshaftConfig>> MINESHAFT_DEADWOOD_FEATURE = register("mineshaft_deadwood", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.DEADWOOD));
    public static final ConfiguredStructureFeature<AtumMineshaftConfig, ? extends StructureFeature<AtumMineshaftConfig>> MINESHAFT_LIMESTONE_FEATURE = register("mineshaft_limestone", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.LIMESTONE));
    public static final ConfiguredStructureFeature<AtumMineshaftConfig, ? extends StructureFeature<AtumMineshaftConfig>> MINESHAFT_DEADWOOD_SURFACE_FEATURE = register("mineshaft_deadwood_surface", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.DEADWOOD_SURFACE));
    public static final ConfiguredStructureFeature<AtumMineshaftConfig, ? extends StructureFeature<AtumMineshaftConfig>> MINESHAFT_LIMESTONE_SURFACE_FEATURE = register("mineshaft_limestone_surface", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.LIMESTONE_SURFACE));
    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> GATEHOUSE_FEATURE = register("gatehouse", GATEHOUSE, new JigsawConfiguration(() -> GatehousePools.POOL, 5));
    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> GENERIC_VILLAGE = register("village_generic", StructureFeature.VILLAGE, new JigsawConfiguration(() -> GenericVillagePools.POOL, 6));

    private static <F extends StructureFeature<?>> F register(String name, F structure, GenerationStep.Decoration generationStage) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        structure.setRegistryName(id);
        STRUCTURES.add(structure);
        StructureFeature.STRUCTURES_REGISTRY.put(id.toString().toLowerCase(Locale.ROOT), structure);
        StructureFeature.STEP.put(structure, generationStage);
        return structure;
    }

    private static <FC extends FeatureConfiguration, F extends StructureFeature<FC>> ConfiguredStructureFeature<FC, ?> register(String name, F structure, FC fc) {
        return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Atum.MOD_ID, name), structure.configured(fc));
    }

    @SubscribeEvent
    public static void registerStructure(RegistryEvent.Register<StructureFeature<?>> event) {
        for (StructureFeature<?> feature : STRUCTURES) {
            event.getRegistry().register(feature);
        }
        AtumJigsaw.registerJigsaws();
    }
}