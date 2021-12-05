package com.teammetallurgy.atum.world.gen.feature.pool;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.teammetallurgy.atum.Atum;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.ProcessorLists;

public class GatehousePools {
    public static final JigsawPattern POOL = JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(Atum.MOD_ID, "ruinedwalls/gatehouses"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242851_a("atum:ruinedwalls/gatehouses/gatehouse", ProcessorLists.field_244101_a), 98)), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING));

    public static void init() {
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(Atum.MOD_ID, "ruinedwalls/walls"), new ResourceLocation(Atum.MOD_ID, "ruinedwalls/terminators"), ImmutableList.of(Pair.of(JigsawPiece.func_242851_a("atum:ruinedwalls/walls/wall1", ProcessorLists.field_244101_a), 2), Pair.of(JigsawPiece.func_242851_a("atum:ruinedwalls/walls/corner1", ProcessorLists.field_244101_a), 2), Pair.of(JigsawPiece.func_242851_a("atum:ruinedwalls/walls/corner2", ProcessorLists.field_244101_a), 2), Pair.of(JigsawPiece.func_242851_a("atum:ruinedwalls/walls/threeway1", ProcessorLists.field_244101_a), 2), Pair.of(JigsawPiece.func_242851_a("atum:ruinedwalls/walls/tower1", ProcessorLists.field_244101_a), 2), Pair.of(JigsawPiece.func_242851_a("atum:ruinedwalls/walls/tower2", ProcessorLists.field_244101_a), 2)), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(Atum.MOD_ID, "ruinedwalls/terminators"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242849_a("atum:ruinedwalls/terminators/terminator1"), 1)), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(Atum.MOD_ID, "ruinedwalls/roads"), new ResourceLocation(Atum.MOD_ID, "ruinedwalls/terminators"), ImmutableList.of(Pair.of(JigsawPiece.func_242849_a("atum:ruinedwalls/roads/road1"), 3), Pair.of(JigsawPiece.func_242849_a("atum:ruinedwalls/roads/road2"), 3)), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING));
    }
}