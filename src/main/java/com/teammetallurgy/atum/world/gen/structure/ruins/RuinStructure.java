package com.teammetallurgy.atum.world.gen.structure.ruins;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.structure.AtumStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import javax.annotation.Nonnull;
import java.util.Optional;

public class RuinStructure extends AtumStructure {
    public static final Codec<RuinStructure> CODEC = simpleCodec(RuinStructure::new);

    public RuinStructure(Structure.StructureSettings structureSettings) {
        super(structureSettings);
    }

    @Override
    @Nonnull
    public StructureType<?> type() {
        return AtumStructures.RUIN.get();
    }

    @Override
    @Nonnull
    protected Optional<GenerationStub> findGenerationPoint(@Nonnull GenerationContext generationContext) {
        Rotation rotation = Rotation.getRandom(generationContext.random());
        BlockPos pos = this.getLowestYIn5by5BoxOffset7Blocks(generationContext, rotation);
        return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (piecesBuilder) -> {
            generatePieces(piecesBuilder, generationContext, pos, rotation);
        });
    }

    private static void generatePieces(StructurePiecesBuilder piecesBuilder, GenerationContext context, BlockPos pos, Rotation rotation) {
        int ruinType = Mth.nextInt(context.random(), 1, AtumConfig.WORLD_GEN.ruinsAmount.get());
        piecesBuilder.addPiece(new RuinPieces.RuinTemplate(context.structureTemplateManager(), new ResourceLocation(Atum.MOD_ID, "ruins/ruin" + ruinType), pos, rotation));
    }

    @Override
    public void setBelowStructureBlock(WorldGenLevel genLevel, BlockPos.MutableBlockPos mutablePos, RandomSource random) {
        Block brick = AtumBlocks.LIMESTONE_BRICK_LARGE.get();
        if (random.nextDouble() <= 0.20D) {
            brick = AtumBlocks.LIMESTONE_BRICK_CRACKED_BRICK.get();
        } else if (random.nextDouble() >= 0.80D) {
            brick = AtumBlocks.LIMESTONE_BRICK_SMALL.get();
        }
        genLevel.setBlock(mutablePos, brick.defaultBlockState(), 2);
    }
}