package com.teammetallurgy.atum.init;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.gen.structure.girafitomb.GirafiTombStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AtumStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE_DEFERRED = DeferredRegister.create(Registries.STRUCTURE_TYPE, Atum.MOD_ID);
    public static final RegistryObject<StructureType<GirafiTombStructure>> GIRAFI_TOMB = register("girafi_tomb", GirafiTombStructure.CODEC);

    private static <S extends Structure> RegistryObject<StructureType<S>> register(String name, Codec<S> codec) {
        return STRUCTURE_TYPE_DEFERRED.register(name, () -> structureTypeFromCodec(codec));
    }

    private static <T extends Structure> StructureType<T> structureTypeFromCodec(Codec<T> structureCodec) {
        return () -> structureCodec;
    }

    //Structures /TODO Remove. Kept for reference for now
//    public static final Structure TOMB_STRUCTURE = register("tomb", new TombStructure(NoneFeatureConfiguration.CODEC));
//    public static final Structure RUIN_STRUCTURE = register("ruin", new RuinStructure(NoneFeatureConfiguration.CODEC));
//    public static final Structure PYRAMID_STRUCTURE = register("pyramid", new PyramidStructure(NoneFeatureConfiguration.CODEC));
//    public static final Structure<AtumMineshaftConfig> MINESHAFT_STRUCTURE = register("mineshaft", new AtumMineshaftStructure(AtumMineshaftConfig.CODEC));
//    public static final Structure<JigsawConfiguration> GATEHOUSE = register("gatehouse", new GatehouseStructure(JigsawConfiguration.CODEC));

    //Structure Features //TODO Remove. Kept for reference for now
//    public static final ConfiguredStructureFeature<AtumMineshaftConfig, ? extends StructureFeature<AtumMineshaftConfig>> MINESHAFT_DEADWOOD_FEATURE = register("mineshaft_deadwood", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.DEADWOOD));
//    public static final ConfiguredStructureFeature<AtumMineshaftConfig, ? extends StructureFeature<AtumMineshaftConfig>> MINESHAFT_LIMESTONE_FEATURE = register("mineshaft_limestone", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.LIMESTONE));
//    public static final ConfiguredStructureFeature<AtumMineshaftConfig, ? extends StructureFeature<AtumMineshaftConfig>> MINESHAFT_DEADWOOD_SURFACE_FEATURE = register("mineshaft_deadwood_surface", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.DEADWOOD_SURFACE));
//    public static final ConfiguredStructureFeature<AtumMineshaftConfig, ? extends StructureFeature<AtumMineshaftConfig>> MINESHAFT_LIMESTONE_SURFACE_FEATURE = register("mineshaft_limestone_surface", MINESHAFT_STRUCTURE, new AtumMineshaftConfig(AtumConfig.WORLD_GEN.mineshaftProbability.get().floatValue(), AtumMineshaftStructure.Type.LIMESTONE_SURFACE));
//    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> GATEHOUSE_FEATURE = register("gatehouse", GATEHOUSE, new JigsawConfiguration(() -> GatehousePools.POOL, 5));
//    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> GENERIC_VILLAGE = register("village_generic", StructureFeature.VILLAGE, new JigsawConfiguration(() -> GenericVillagePools.POOL, 6));
}