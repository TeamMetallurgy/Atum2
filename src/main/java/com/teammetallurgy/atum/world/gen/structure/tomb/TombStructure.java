package com.teammetallurgy.atum.world.gen.structure.tomb;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.world.gen.structure.AtumStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TombStructure extends AtumStructure {
    public static final Codec<TombStructure> CODEC = simpleCodec(TombStructure::new);
    public static final ResourceLocation TOMB = new ResourceLocation(Atum.MOD_ID, "tomb");

    public TombStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    @Nonnull
    public StructureType<?> type() {
        return AtumStructures.TOMB.get();
    }

    @Override
    @Nonnull
    protected Optional<GenerationStub> findGenerationPoint(@Nonnull GenerationContext generationContext) {
        ChunkPos chunkPos = generationContext.chunkPos();
        int y = Mth.nextInt(generationContext.random(), -30, 45);
        BlockPos pos = new BlockPos(chunkPos.getMiddleBlockX(), y, chunkPos.getMinBlockZ());
        Rotation rotation = Rotation.getRandom(generationContext.random());
        return Optional.of(new Structure.GenerationStub(pos, (piecesBuilder) -> generatePieces(piecesBuilder, generationContext, pos, rotation)));
    }

    private static void generatePieces(StructurePiecesBuilder piecesBuilder, Structure.GenerationContext context, BlockPos pos, Rotation rotation) {
        piecesBuilder.addPiece(new TombPieces.TombTemplate(context.structureTemplateManager(), TOMB, pos, rotation));
    }
}