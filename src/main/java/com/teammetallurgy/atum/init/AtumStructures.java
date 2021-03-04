package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.feature.pool.AtumJigsaw;
import com.teammetallurgy.atum.world.gen.feature.pool.GatehousePools;
import com.teammetallurgy.atum.world.gen.feature.pool.GenericVillagePools;
import com.teammetallurgy.atum.world.gen.structure.GatehouseStructure;
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
import net.minecraft.world.gen.feature.structure.VillageConfig;
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
    public static final Structure<NoFeatureConfig> TOMB_STRUCTURE = register("tomb", new TombStructure(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.UNDERGROUND_STRUCTURES);
    public static final Structure<NoFeatureConfig> RUIN_STRUCTURE = register("ruin", new RuinStructure(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.SURFACE_STRUCTURES);
    public static final Structure<NoFeatureConfig> PYRAMID_STRUCTURE = register("pyramid", new PyramidStructure(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.SURFACE_STRUCTURES);
    public static final Structure<AtumMineshaftConfig> MINESHAFT_STRUCTURE = register("mineshaft", new AtumMineshaftStructure(AtumMineshaftConfig.CODEC), GenerationStage.Decoration.UNDERGROUND_STRUCTURES);
    public static final Structure<VillageConfig> GATEHOUSE = register("gatehouse", new GatehouseStructure(VillageConfig.field_236533_a_), GenerationStage.Decoration.SURFACE_STRUCTURES);

    //Structure Features
    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> GIRAFI_TOMB_FEATURE = register("girafi_tomb", GIRAFI_TOMB_STRUCTURE, NoFeatureConfig.field_236559_b_);
    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> TOMB_FEATURE = register("tomb", TOMB_STRUCTURE, NoFeatureConfig.field_236559_b_);
    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> RUIN_FEATURE = register("ruin", RUIN_STRUCTURE, NoFeatureConfig.field_236559_b_);
    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> PYRAMID_FEATURE = register("pyramid", PYRAMID_STRUCTURE, NoFeatureConfig.field_236559_b_);
    public static final StructureFeature<AtumMineshaftConfig, ? extends Structure<AtumMineshaftConfig>> MINESHAFT_DEADWOOD_FEATURE = register("mineshaft_deadwood", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.DEADWOOD));
    public static final StructureFeature<AtumMineshaftConfig, ? extends Structure<AtumMineshaftConfig>> MINESHAFT_LIMESTONE_FEATURE = register("mineshaft_limestone", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.LIMESTONE));
    public static final StructureFeature<AtumMineshaftConfig, ? extends Structure<AtumMineshaftConfig>> MINESHAFT_DEADWOOD_SURFACE_FEATURE = register("mineshaft_deadwood_surface", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.DEADWOOD_SURFACE));
    public static final StructureFeature<AtumMineshaftConfig, ? extends Structure<AtumMineshaftConfig>> MINESHAFT_LIMESTONE_SURFACE_FEATURE = register("mineshaft_limestone_surface", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.LIMESTONE_SURFACE));
    public static final StructureFeature<VillageConfig, ? extends Structure<VillageConfig>> GATEHOUSE_FEATURE = register("gatehouse", GATEHOUSE, new VillageConfig(() -> GatehousePools.POOL, 5));
    public static final StructureFeature<VillageConfig, ? extends Structure<VillageConfig>> GENERIC_VILLAGE = register("village_generic", Structure.VILLAGE, new VillageConfig(() -> GenericVillagePools.POOL, 6));

    private static <F extends Structure<?>> F register(String name, F structure, GenerationStage.Decoration generationStage) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        structure.setRegistryName(id);
        STRUCTURES.add(structure);
        Structure.NAME_STRUCTURE_BIMAP.put(id.toString().toLowerCase(Locale.ROOT), structure);
        Structure.STRUCTURE_DECORATION_STAGE_MAP.put(structure, generationStage);
        return structure;
    }

    private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, ?> register(String name, F structure, FC fc) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Atum.MOD_ID, name), structure.withConfiguration(fc));
    }

    @SubscribeEvent
    public static void registerStructure(RegistryEvent.Register<Structure<?>> event) {
        for (Structure<?> feature : STRUCTURES) {
            event.getRegistry().register(feature);
        }
        AtumJigsaw.registerJigsaws();
    }
}