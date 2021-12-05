package com.teammetallurgy.atum.world.gen.feature.pool;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.teammetallurgy.atum.Atum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.data.worldgen.ProcessorLists;

public class GatehousePools {
    public static final StructureTemplatePool POOL = Pools.register(new StructureTemplatePool(new ResourceLocation(Atum.MOD_ID, "ruinedwalls/gatehouses"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(StructurePoolElement.legacy("atum:ruinedwalls/gatehouses/gatehouse", ProcessorLists.EMPTY), 98)), StructureTemplatePool.Projection.TERRAIN_MATCHING));

    public static void init() {
        Pools.register(new StructureTemplatePool(new ResourceLocation(Atum.MOD_ID, "ruinedwalls/walls"), new ResourceLocation(Atum.MOD_ID, "ruinedwalls/terminators"), ImmutableList.of(Pair.of(StructurePoolElement.legacy("atum:ruinedwalls/walls/wall1", ProcessorLists.EMPTY), 2), Pair.of(StructurePoolElement.legacy("atum:ruinedwalls/walls/corner1", ProcessorLists.EMPTY), 2), Pair.of(StructurePoolElement.legacy("atum:ruinedwalls/walls/corner2", ProcessorLists.EMPTY), 2), Pair.of(StructurePoolElement.legacy("atum:ruinedwalls/walls/threeway1", ProcessorLists.EMPTY), 2), Pair.of(StructurePoolElement.legacy("atum:ruinedwalls/walls/tower1", ProcessorLists.EMPTY), 2), Pair.of(StructurePoolElement.legacy("atum:ruinedwalls/walls/tower2", ProcessorLists.EMPTY), 2)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
        Pools.register(new StructureTemplatePool(new ResourceLocation(Atum.MOD_ID, "ruinedwalls/terminators"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(StructurePoolElement.legacy("atum:ruinedwalls/terminators/terminator1"), 1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
        Pools.register(new StructureTemplatePool(new ResourceLocation(Atum.MOD_ID, "ruinedwalls/roads"), new ResourceLocation(Atum.MOD_ID, "ruinedwalls/terminators"), ImmutableList.of(Pair.of(StructurePoolElement.legacy("atum:ruinedwalls/roads/road1"), 3), Pair.of(StructurePoolElement.legacy("atum:ruinedwalls/roads/road2"), 3)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
    }
}