package com.teammetallurgy.atum.world.gen.structure.girafitomb;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.world.gen.structure.AtumStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import javax.annotation.Nonnull;
import java.util.Optional;

public class GirafiTombStructure extends AtumStructure {
    public static final Codec<GirafiTombStructure> CODEC = simpleCodec(GirafiTombStructure::new);
    public static final ResourceLocation GIRAFI_TOMB = new ResourceLocation(Atum.MOD_ID, "girafi_tomb");

    public GirafiTombStructure(Structure.StructureSettings structureSettings) {
        super(structureSettings);
    }

    @Override
    @Nonnull
    public StructureType<?> type() {
        return AtumStructures.GIRAFI_TOMB.get();
    }

    @Override
    @Nonnull
    protected Optional<GenerationStub> findGenerationPoint(@Nonnull GenerationContext generationContext) {
        Rotation rotation = Rotation.getRandom(generationContext.random());
        BlockPos pos = this.getLowestYIn5by5BoxOffset7Blocks(generationContext, rotation);
        return pos.getY() < 60 ? Optional.empty() : onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (piecesBuilder) -> {
            generatePieces(piecesBuilder, generationContext, pos, rotation);
        });
    }

    private static void generatePieces(StructurePiecesBuilder piecesBuilder, GenerationContext context, BlockPos pos, Rotation rotation) {
        piecesBuilder.addPiece(new GirafiTombPieces.GirafiTombTemplate(context.structureTemplateManager(), GIRAFI_TOMB, pos, rotation));
    }
}