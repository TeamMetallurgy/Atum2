package com.teammetallurgy.atum.world.gen.structure.tomb;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;

public class TombStructure extends Structure<NoFeatureConfig> {

    public TombStructure(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    protected boolean func_230363_a_(ChunkGenerator generator, BiomeProvider provider, long seed, @Nonnull SharedSeedRandom seedRandom, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull ChunkPos chunkPos, @Nonnull NoFeatureConfig config) {
        for (Biome b : provider.getBiomes(chunkX * 16 + 9, generator.func_230356_f_(), chunkZ * 16 + 9, 32)) {
            if (!b.getGenerationSettings().hasStructure(this)) {
                return false;
            } else {
                return StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, null) <= 55;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox box, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, box, references, seed);
        }

        @Override
        public void func_230364_a_(@Nonnull DynamicRegistries registries, @Nonnull ChunkGenerator generator, @Nonnull TemplateManager manager, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull NoFeatureConfig config) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            int y = MathHelper.nextInt(this.rand, 6, 55);
            BlockPos pos = new BlockPos(chunkX * 16 + 8, y, chunkZ * 16 + 8);

            if (y < 60 /* || !(world.getBlockState(pos).getBlock() instanceof BlockLimestone)*/) {
                TombPieces.TombTemplate tomb = new TombPieces.TombTemplate(manager, pos, rotation);
                this.components.add(tomb);
                this.recalculateStructureSize();
            }
        }
    }
}