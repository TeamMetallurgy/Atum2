package com.teammetallurgy.atum.world.gen.feature.pool;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.teammetallurgy.atum.Atum;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.ProcessorLists;

public class AtumVillagePools {

    public static void init() {
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(Atum.MOD_ID, "village/generic/houses"), new ResourceLocation("village/desert/terminators"), ImmutableList.of(Pair.of(JigsawPiece.func_242851_a("atum:village/generic/houses/generic_small_house_1", ProcessorLists.field_244101_a), 2), Pair.of(JigsawPiece.func_242851_a("atum:village/generic/houses/generic_small_house_2", ProcessorLists.field_244101_a), 2), Pair.of(JigsawPiece.func_242851_a("atum:village/generic/houses/generic_small_house_3", ProcessorLists.field_244101_a), 2), Pair.of(JigsawPiece.func_242851_a("atum:village/generic/houses/generic_small_house_4", ProcessorLists.field_244101_a), 2), Pair.of(JigsawPiece.func_242851_a("atum:village/generic/houses/generic_small_house_5", ProcessorLists.field_244101_a), 2), Pair.of(JigsawPiece.func_242851_a("atum:village/generic/houses/generic_small_house_6", ProcessorLists.field_244101_a), 1), Pair.of(JigsawPiece.func_242851_a("atum:village/generic/houses/generic_small_house_7", ProcessorLists.field_244101_a), 2), Pair.of(JigsawPiece.func_242851_a("atum:village/generic/houses/generic_small_house_8", ProcessorLists.field_244101_a), 2)), JigsawPattern.PlacementBehaviour.RIGID));
    }
}