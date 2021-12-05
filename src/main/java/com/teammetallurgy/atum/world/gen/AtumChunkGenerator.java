package com.teammetallurgy.atum.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.world.DimensionHelper;
import com.teammetallurgy.atum.world.WorldSeedHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.Util;
import net.minecraft.util.math.*;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.gen.*;
import net.minecraft.world.level.levelgen.feature.structures.JigsawJunction;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.world.StructureSpawnManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import net.minecraft.world.level.levelgen.synth.SurfaceNoise;

public class AtumChunkGenerator extends ChunkGenerator { //Copied from NoiseChunkGenerator, to make noise seed random.
    public static final Codec<AtumChunkGenerator> CODEC = RecordCodecBuilder.create((c) -> {
        return c.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter((chunkGenerator) -> {
            return chunkGenerator.biomeSource;
        }), NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((chunkGenerator) -> {
            return chunkGenerator.dimensionSettings;
        }), Codec.LONG.fieldOf("seed").orElseGet(WorldSeedHolder::getSeed).forGetter((chunkGenerator) -> {
            return chunkGenerator.seed;
        })).apply(c, c.stable(AtumChunkGenerator::new));
    });
    private static final float[] BEARD_KERNEL = Util.make(new float[13824], (p_236094_0_) -> {
        for (int i = 0; i < 24; ++i) {
            for (int j = 0; j < 24; ++j) {
                for (int k = 0; k < 24; ++k) {
                    p_236094_0_[i * 24 * 24 + j * 24 + k] = (float) computeContribution(j - 12, k - 12, i - 12);
                }
            }
        }

    });
    private static final float[] BIOME_WEIGHTS = Util.make(new float[25], (p_236092_0_) -> {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                float f = 10.0F / Mth.sqrt((float) (i * i + j * j) + 0.2F);
                p_236092_0_[i + 2 + (j + 2) * 5] = f;
            }
        }

    });
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private final int verticalNoiseGranularity;
    private final int horizontalNoiseGranularity;
    private final int noiseSizeX;
    private final int noiseSizeY;
    private final int noiseSizeZ;
    protected final WorldgenRandom randomSeed;
    private final PerlinNoise minLimitPerlinNoise;
    private final PerlinNoise maxLimitPerlinNoise;
    private final PerlinNoise mainPerlinNoise;
    private final SurfaceNoise surfaceDepthNoise;
    private final PerlinNoise depthNoise;
    @Nullable
    private final SimplexNoise islandNoise;
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    private final long seed;
    protected final Supplier<NoiseGeneratorSettings> dimensionSettings;
    private final int worldHeight;

    public AtumChunkGenerator(BiomeSource biomeProvider, Supplier<NoiseGeneratorSettings> dimensionSettings, long seed) {
        this(biomeProvider, biomeProvider, seed, dimensionSettings); //Set random seed
    }

    private AtumChunkGenerator(BiomeSource biomeProvider1, BiomeSource biomeProvider2, long seed, Supplier<NoiseGeneratorSettings> dimensionSettings) {
        super(biomeProvider1, biomeProvider2, dimensionSettings.get().structureSettings(), seed);
        this.seed = seed;
        NoiseGeneratorSettings dimensionsettings = dimensionSettings.get();
        this.dimensionSettings = dimensionSettings;
        NoiseSettings noisesettings = dimensionsettings.noiseSettings();
        this.worldHeight = noisesettings.height();
        this.verticalNoiseGranularity = noisesettings.noiseSizeVertical() * 4;
        this.horizontalNoiseGranularity = noisesettings.noiseSizeHorizontal() * 4;
        this.defaultBlock = dimensionsettings.getDefaultBlock();
        this.defaultFluid = dimensionsettings.getDefaultFluid();
        this.noiseSizeX = 16 / this.horizontalNoiseGranularity;
        this.noiseSizeY = noisesettings.height() / this.verticalNoiseGranularity;
        this.noiseSizeZ = 16 / this.horizontalNoiseGranularity;
        this.randomSeed = new WorldgenRandom(seed);
        this.minLimitPerlinNoise = new PerlinNoise(this.randomSeed, IntStream.rangeClosed(-15, 0));
        this.maxLimitPerlinNoise = new PerlinNoise(this.randomSeed, IntStream.rangeClosed(-15, 0));
        this.mainPerlinNoise = new PerlinNoise(this.randomSeed, IntStream.rangeClosed(-7, 0));
        this.surfaceDepthNoise = (noisesettings.useSimplexSurfaceNoise() ? new PerlinSimplexNoise(this.randomSeed, IntStream.rangeClosed(-3, 0)) : new PerlinNoise(this.randomSeed, IntStream.rangeClosed(-3, 0)));
        this.randomSeed.consumeCount(2620);
        this.depthNoise = new PerlinNoise(this.randomSeed, IntStream.rangeClosed(-15, 0));
        if (noisesettings.islandNoiseOverride()) {
            WorldgenRandom sharedseedrandom = new WorldgenRandom(seed);
            sharedseedrandom.consumeCount(17292);
            this.islandNoise = new SimplexNoise(sharedseedrandom);
        } else {
            this.islandNoise = null;
        }
    }

    @Override
    @Nonnull
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public ChunkGenerator withSeed(long seed) {
        return new AtumChunkGenerator(this.biomeSource.withSeed(seed), this.dimensionSettings, this.seed);
    }

    public boolean stable(long p_236088_1_, ResourceKey<NoiseGeneratorSettings> dimensionSettings) {
        return this.seed == p_236088_1_ && this.dimensionSettings.get().stable(dimensionSettings);
    }

    private double sampleAndClampNoise(int p_222552_1_, int p_222552_2_, int p_222552_3_, double p_222552_4_, double p_222552_6_, double p_222552_8_, double p_222552_10_) {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        boolean flag = true;
        double d3 = 1.0D;

        for (int i = 0; i < 16; ++i) {
            double d4 = PerlinNoise.wrap((double) p_222552_1_ * p_222552_4_ * d3);
            double d5 = PerlinNoise.wrap((double) p_222552_2_ * p_222552_6_ * d3);
            double d6 = PerlinNoise.wrap((double) p_222552_3_ * p_222552_4_ * d3);
            double d7 = p_222552_6_ * d3;
            ImprovedNoise improvednoisegenerator = this.minLimitPerlinNoise.getOctaveNoise(i);
            if (improvednoisegenerator != null) {
                d0 += improvednoisegenerator.noise(d4, d5, d6, d7, (double) p_222552_2_ * d7) / d3;
            }

            ImprovedNoise improvednoisegenerator1 = this.maxLimitPerlinNoise.getOctaveNoise(i);
            if (improvednoisegenerator1 != null) {
                d1 += improvednoisegenerator1.noise(d4, d5, d6, d7, (double) p_222552_2_ * d7) / d3;
            }

            if (i < 8) {
                ImprovedNoise improvednoisegenerator2 = this.mainPerlinNoise.getOctaveNoise(i);
                if (improvednoisegenerator2 != null) {
                    d2 += improvednoisegenerator2.noise(PerlinNoise.wrap((double) p_222552_1_ * p_222552_8_ * d3), PerlinNoise.wrap((double) p_222552_2_ * p_222552_10_ * d3), PerlinNoise.wrap((double) p_222552_3_ * p_222552_8_ * d3), p_222552_10_ * d3, (double) p_222552_2_ * p_222552_10_ * d3) / d3;
                }
            }

            d3 /= 2.0D;
        }

        return Mth.clampedLerp(d0 / 512.0D, d1 / 512.0D, (d2 / 10.0D + 1.0D) / 2.0D);
    }

    private double[] makeAndFillNoiseColumn(int noiseX, int noiseZ) {
        double[] noiseColumn = new double[this.noiseSizeY + 1];
        this.fillNoiseColumn(noiseColumn, noiseX, noiseZ);
        return noiseColumn;
    }

    private void fillNoiseColumn(double[] noiseColumn, int noiseX, int noiseZ) {
        NoiseSettings noisesettings = this.dimensionSettings.get().noiseSettings();
        double d0;
        double d1;
        if (this.islandNoise != null) {
            d0 = (TheEndBiomeSource.getHeightValue(this.islandNoise, noiseX, noiseZ) - 8.0F);
            if (d0 > 0.0D) {
                d1 = 0.25D;
            } else {
                d1 = 1.0D;
            }
        } else {
            float f = 0.0F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            int j = DimensionHelper.GROUND_LEVEL;
            float f3 = this.biomeSource.getNoiseBiome(noiseX, j, noiseZ).getDepth();

            for (int k = -2; k <= 2; ++k) {
                for (int l = -2; l <= 2; ++l) {
                    Biome biome = this.biomeSource.getNoiseBiome(noiseX + k, j, noiseZ + l);
                    float f4 = biome.getDepth();
                    float f5 = biome.getScale();
                    float f6;
                    float f7;
                    if (noisesettings.isAmplified() && f4 > 0.0F) {
                        f6 = 1.0F + f4 * 2.0F;
                        f7 = 1.0F + f5 * 4.0F;
                    } else {
                        f6 = f4;
                        f7 = f5;
                    }

                    float f8 = f4 > f3 ? 0.5F : 1.0F;
                    float f9 = f8 * BIOME_WEIGHTS[k + 2 + (l + 2) * 5] / (f6 + 2.0F);
                    f += f7 * f9;
                    f1 += f6 * f9;
                    f2 += f9;
                }
            }

            float f10 = f1 / f2;
            float f11 = f / f2;
            double d16 = (f10 * 0.5F - 0.125F);
            double d18 = (f11 * 0.9F + 0.1F);
            d0 = d16 * 0.265625D;
            d1 = 96.0D / d18;
        }

        double d12 = 684.412D * noisesettings.noiseSamplingSettings().xzScale();
        double d13 = 684.412D * noisesettings.noiseSamplingSettings().yScale();
        double d14 = d12 / noisesettings.noiseSamplingSettings().xzFactor();
        double d15 = d13 / noisesettings.noiseSamplingSettings().yFactor();
        double d17 = noisesettings.topSlideSettings().target();
        double d19 = noisesettings.topSlideSettings().size();
        double d20 = noisesettings.topSlideSettings().offset();
        double d21 = noisesettings.bottomSlideSettings().target();
        double d2 = noisesettings.bottomSlideSettings().size();
        double d3 = noisesettings.bottomSlideSettings().offset();
        double d4 = noisesettings.randomDensityOffset() ? this.getRandomDensity(noiseX, noiseZ) : 0.0D;
        double d5 = noisesettings.densityFactor();
        double d6 = noisesettings.densityOffset();

        for (int i1 = 0; i1 <= this.noiseSizeY; ++i1) {
            double d7 = this.sampleAndClampNoise(noiseX, i1, noiseZ, d12, d13, d14, d15);
            double d8 = 1.0D - (double) i1 * 2.0D / (double) this.noiseSizeY + d4;
            double d9 = d8 * d5 + d6;
            double d10 = (d9 + d0) * d1;
            if (d10 > 0.0D) {
                d7 = d7 + d10 * 4.0D;
            } else {
                d7 = d7 + d10;
            }

            if (d19 > 0.0D) {
                double d11 = ((double) (this.noiseSizeY - i1) - d20) / d19;
                d7 = Mth.clampedLerp(d17, d7, d11);
            }

            if (d2 > 0.0D) {
                double d22 = ((double) i1 - d3) / d2;
                d7 = Mth.clampedLerp(d21, d7, d22);
            }
            noiseColumn[i1] = d7;
        }
    }

    private double getRandomDensity(int p_236095_1_, int p_236095_2_) {
        double d0 = this.depthNoise.getValue((p_236095_1_ * 200), 10.0D, (p_236095_2_ * 200), 1.0D, 0.0D, true);
        double d1;
        if (d0 < 0.0D) {
            d1 = -d0 * 0.3D;
        } else {
            d1 = d0;
        }

        double d2 = d1 * 24.575625D - 2.0D;
        return d2 < 0.0D ? d2 * 0.009486607142857142D : Math.min(d2, 1.0D) * 0.006640625D;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmapType) {
        return this.iterateNoiseColumn(x, z, null, heightmapType.isOpaque());
    }

    @Override
    @Nonnull
    public BlockGetter getBaseColumn(int p_230348_1_, int p_230348_2_) {
        BlockState[] states = new BlockState[this.noiseSizeY * this.verticalNoiseGranularity];
        this.iterateNoiseColumn(p_230348_1_, p_230348_2_, states, null);
        return new NoiseColumn(states);
    }

    private int iterateNoiseColumn(int noiseX, int noiseZ, @Nullable BlockState[] states, @Nullable Predicate<BlockState> predicate) {
        int i = Math.floorDiv(noiseX, this.horizontalNoiseGranularity);
        int j = Math.floorDiv(noiseZ, this.horizontalNoiseGranularity);
        int k = Math.floorMod(noiseX, this.horizontalNoiseGranularity);
        int l = Math.floorMod(noiseZ, this.horizontalNoiseGranularity);
        double d0 = (double) k / (double) this.horizontalNoiseGranularity;
        double d1 = (double) l / (double) this.horizontalNoiseGranularity;
        double[][] adouble = new double[][]{this.makeAndFillNoiseColumn(i, j), this.makeAndFillNoiseColumn(i, j + 1), this.makeAndFillNoiseColumn(i + 1, j), this.makeAndFillNoiseColumn(i + 1, j + 1)};

        for (int i1 = this.noiseSizeY - 1; i1 >= 0; --i1) {
            double d2 = adouble[0][i1];
            double d3 = adouble[1][i1];
            double d4 = adouble[2][i1];
            double d5 = adouble[3][i1];
            double d6 = adouble[0][i1 + 1];
            double d7 = adouble[1][i1 + 1];
            double d8 = adouble[2][i1 + 1];
            double d9 = adouble[3][i1 + 1];

            for (int j1 = this.verticalNoiseGranularity - 1; j1 >= 0; --j1) {
                double d10 = (double) j1 / (double) this.verticalNoiseGranularity;
                double d11 = Mth.lerp3(d10, d0, d1, d2, d6, d4, d8, d3, d7, d5, d9);
                int k1 = i1 * this.verticalNoiseGranularity + j1;
                BlockState state = this.getDefaultBlockAndFluid(d11, k1);
                if (states != null) {
                    states[k1] = state;
                }

                if (predicate != null && predicate.test(state)) {
                    return k1 + 1;
                }
            }
        }
        return 0;
    }

    protected BlockState getDefaultBlockAndFluid(double horizontalNoise, int verticalNoise) {
        BlockState state;
        if (horizontalNoise > 0.0D) {
            state = this.defaultBlock;
        } else if (verticalNoise < this.getSeaLevel()) {
            state = this.defaultFluid;
        } else {
            state = AIR;
        }
        return state;
    }

    @Override
    public void buildSurfaceAndBedrock(@Nonnull WorldGenRegion genRegion, ChunkAccess chunk) {
        ChunkPos chunkPos = chunk.getPos();
        int x = chunkPos.x;
        int j = chunkPos.z;
        WorldgenRandom seedRandom = new WorldgenRandom();
        seedRandom.setBaseChunkSeed(x, j);
        ChunkPos chunkStartPos = chunk.getPos();
        int xStart = chunkStartPos.getMinBlockX();
        int zStart = chunkStartPos.getMinBlockZ();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int i1 = 0; i1 < 16; ++i1) {
            for (int j1 = 0; j1 < 16; ++j1) {
                int k1 = xStart + i1;
                int l1 = zStart + j1;
                int i2 = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, i1, j1) + 1;
                double d1 = this.surfaceDepthNoise.getSurfaceNoiseValue((double) k1 * 0.0625D, (double) l1 * 0.0625D, 0.0625D, (double) i1 * 0.0625D) * 15.0D;
                BlockPos biomePos = mutablePos.set(xStart + i1, i2, zStart + j1);
                genRegion.getBiome(biomePos).buildSurfaceAt(seedRandom, chunk, k1, l1, i2, d1, this.defaultBlock, this.defaultFluid, this.getSeaLevel(), genRegion.getSeed());
            }
        }
        this.makeBedrock(chunk, seedRandom);
    }

    private void makeBedrock(ChunkAccess chunk, Random rand) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        int x = chunk.getPos().getMinBlockX();
        int j = chunk.getPos().getMinBlockZ();
        NoiseGeneratorSettings dimensionsettings = this.dimensionSettings.get();
        int k = dimensionsettings.getBedrockFloorPosition();
        int l = this.worldHeight - 1 - dimensionsettings.getBedrockRoofPosition();
        int i1 = 5;
        boolean flag = l + 4 >= 0 && l < this.worldHeight;
        boolean flag1 = k + 4 >= 0 && k < this.worldHeight;
        if (flag || flag1) {
            for (BlockPos blockpos : BlockPos.betweenClosed(x, 0, j, x + 15, 0, j + 15)) {
                if (flag) {
                    for (int j1 = 0; j1 < 5; ++j1) {
                        if (j1 <= rand.nextInt(5)) {
                            chunk.setBlockState(blockpos$mutable.set(blockpos.getX(), l - j1, blockpos.getZ()), Blocks.BEDROCK.defaultBlockState(), false);
                        }
                    }
                }

                if (flag1) {
                    for (int k1 = 4; k1 >= 0; --k1) {
                        if (k1 <= rand.nextInt(5)) {
                            chunk.setBlockState(blockpos$mutable.set(blockpos.getX(), k + k1, blockpos.getZ()), Blocks.BEDROCK.defaultBlockState(), false);
                        }
                    }
                }
            }

        }
    }

    @Override
    public void fillFromNoise(@Nonnull LevelAccessor world, @Nonnull StructureFeatureManager manager, ChunkAccess chunk) {
        ObjectList<StructurePiece> objectlist = new ObjectArrayList<>(10);
        ObjectList<JigsawJunction> objectlist1 = new ObjectArrayList<>(32);
        ChunkPos chunkpos = chunk.getPos();
        int x = chunkpos.x;
        int j = chunkpos.z;
        int k = x << 4;
        int l = j << 4;

        for (StructureFeature<?> structure : StructureFeature.NOISE_AFFECTING_FEATURES) {
            manager.startsForFeature(SectionPos.of(chunkpos, 0), structure).forEach((p_236089_5_) -> {
                for (StructurePiece structurepiece1 : p_236089_5_.getPieces()) {
                    if (structurepiece1.isCloseToChunk(chunkpos, 12)) {
                        if (structurepiece1 instanceof PoolElementStructurePiece) {
                            PoolElementStructurePiece abstractvillagepiece = (PoolElementStructurePiece) structurepiece1;
                            StructureTemplatePool.Projection jigsawpattern$placementbehaviour = abstractvillagepiece.getElement().getProjection();
                            if (jigsawpattern$placementbehaviour == StructureTemplatePool.Projection.RIGID) {
                                objectlist.add(abstractvillagepiece);
                            }

                            for (JigsawJunction jigsawjunction1 : abstractvillagepiece.getJunctions()) {
                                int l5 = jigsawjunction1.getSourceX();
                                int i6 = jigsawjunction1.getSourceZ();
                                if (l5 > k - 12 && i6 > l - 12 && l5 < k + 15 + 12 && i6 < l + 15 + 12) {
                                    objectlist1.add(jigsawjunction1);
                                }
                            }
                        } else {
                            objectlist.add(structurepiece1);
                        }
                    }
                }

            });
        }

        double[][][] adouble = new double[2][this.noiseSizeZ + 1][this.noiseSizeY + 1];

        for (int i5 = 0; i5 < this.noiseSizeZ + 1; ++i5) {
            adouble[0][i5] = new double[this.noiseSizeY + 1];
            this.fillNoiseColumn(adouble[0][i5], x * this.noiseSizeX, j * this.noiseSizeZ + i5);
            adouble[1][i5] = new double[this.noiseSizeY + 1];
        }

        ProtoChunk chunkprimer = (ProtoChunk) chunk;
        Heightmap heightmap = chunkprimer.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap heightmap1 = chunkprimer.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        ObjectListIterator<StructurePiece> objectlistiterator = objectlist.iterator();
        ObjectListIterator<JigsawJunction> objectlistiterator1 = objectlist1.iterator();

        for (int i1 = 0; i1 < this.noiseSizeX; ++i1) {
            for (int j1 = 0; j1 < this.noiseSizeZ + 1; ++j1) {
                this.fillNoiseColumn(adouble[1][j1], x * this.noiseSizeX + i1 + 1, j * this.noiseSizeZ + j1);
            }

            for (int j5 = 0; j5 < this.noiseSizeZ; ++j5) {
                LevelChunkSection chunksection = chunkprimer.getOrCreateSection(15);
                chunksection.acquire();

                for (int k1 = this.noiseSizeY - 1; k1 >= 0; --k1) {
                    double d0 = adouble[0][j5][k1];
                    double d1 = adouble[0][j5 + 1][k1];
                    double d2 = adouble[1][j5][k1];
                    double d3 = adouble[1][j5 + 1][k1];
                    double d4 = adouble[0][j5][k1 + 1];
                    double d5 = adouble[0][j5 + 1][k1 + 1];
                    double d6 = adouble[1][j5][k1 + 1];
                    double d7 = adouble[1][j5 + 1][k1 + 1];

                    for (int l1 = this.verticalNoiseGranularity - 1; l1 >= 0; --l1) {
                        int i2 = k1 * this.verticalNoiseGranularity + l1;
                        int j2 = i2 & 15;
                        int k2 = i2 >> 4;
                        if (chunksection.bottomBlockY() >> 4 != k2) {
                            chunksection.release();
                            chunksection = chunkprimer.getOrCreateSection(k2);
                            chunksection.acquire();
                        }

                        double d8 = (double) l1 / (double) this.verticalNoiseGranularity;
                        double d9 = Mth.lerp(d8, d0, d4);
                        double d10 = Mth.lerp(d8, d2, d6);
                        double d11 = Mth.lerp(d8, d1, d5);
                        double d12 = Mth.lerp(d8, d3, d7);

                        for (int l2 = 0; l2 < this.horizontalNoiseGranularity; ++l2) {
                            int i3 = k + i1 * this.horizontalNoiseGranularity + l2;
                            int j3 = i3 & 15;
                            double d13 = (double) l2 / (double) this.horizontalNoiseGranularity;
                            double d14 = Mth.lerp(d13, d9, d10);
                            double d15 = Mth.lerp(d13, d11, d12);

                            for (int k3 = 0; k3 < this.horizontalNoiseGranularity; ++k3) {
                                int l3 = l + j5 * this.horizontalNoiseGranularity + k3;
                                int i4 = l3 & 15;
                                double d16 = (double) k3 / (double) this.horizontalNoiseGranularity;
                                double d17 = Mth.lerp(d16, d14, d15);
                                double d18 = Mth.clamp(d17 / 200.0D, -1.0D, 1.0D);

                                int j4;
                                int k4;
                                int l4;
                                for (d18 = d18 / 2.0D - d18 * d18 * d18 / 24.0D; objectlistiterator.hasNext(); d18 += getContribution(j4, k4, l4) * 0.8D) {
                                    StructurePiece structurepiece = objectlistiterator.next();
                                    BoundingBox mutableboundingbox = structurepiece.getBoundingBox();
                                    j4 = Math.max(0, Math.max(mutableboundingbox.x0 - i3, i3 - mutableboundingbox.x1));
                                    k4 = i2 - (mutableboundingbox.y0 + (structurepiece instanceof PoolElementStructurePiece ? ((PoolElementStructurePiece) structurepiece).getGroundLevelDelta() : 0));
                                    l4 = Math.max(0, Math.max(mutableboundingbox.z0 - l3, l3 - mutableboundingbox.z1));
                                }

                                objectlistiterator.back(objectlist.size());

                                while (objectlistiterator1.hasNext()) {
                                    JigsawJunction jigsawjunction = objectlistiterator1.next();
                                    int k5 = i3 - jigsawjunction.getSourceX();
                                    j4 = i2 - jigsawjunction.getSourceGroundY();
                                    k4 = l3 - jigsawjunction.getSourceZ();
                                    d18 += getContribution(k5, j4, k4) * 0.4D;
                                }

                                objectlistiterator1.back(objectlist1.size());
                                BlockState blockstate = this.getDefaultBlockAndFluid(d18, i2);
                                if (blockstate != AIR) {
                                    mutablePos.set(i3, i2, l3);
                                    if (blockstate.getLightValue(chunkprimer, mutablePos) != 0) {
                                        chunkprimer.addLight(mutablePos);
                                    }

                                    chunksection.setBlockState(j3, j2, i4, blockstate, false);
                                    heightmap.update(j3, i2, i4, blockstate);
                                    heightmap1.update(j3, i2, i4, blockstate);
                                }
                            }
                        }
                    }
                }
                chunksection.release();
            }
            double[][] adouble1 = adouble[0];
            adouble[0] = adouble[1];
            adouble[1] = adouble1;
        }
    }

    private static double getContribution(int p_222556_0_, int p_222556_1_, int p_222556_2_) {
        int i = p_222556_0_ + 12;
        int j = p_222556_1_ + 12;
        int k = p_222556_2_ + 12;
        if (i >= 0 && i < 24) {
            if (j >= 0 && j < 24) {
                return k >= 0 && k < 24 ? (double) BEARD_KERNEL[k * 24 * 24 + i * 24 + j] : 0.0D;
            } else {
                return 0.0D;
            }
        } else {
            return 0.0D;
        }
    }

    private static double computeContribution(int p_222554_0_, int p_222554_1_, int p_222554_2_) {
        double d0 = (p_222554_0_ * p_222554_0_ + p_222554_2_ * p_222554_2_);
        double d1 = (double) p_222554_1_ + 0.5D;
        double d2 = d1 * d1;
        double d3 = Math.pow(Math.E, -(d2 / 16.0D + d0 / 16.0D));
        double d4 = -d1 * Mth.fastInvSqrt(d2 / 2.0D + d0 / 2.0D) / 2.0D;
        return d4 * d3;
    }

    @Override
    public int getGenDepth() {
        return this.worldHeight;
    }

    @Override
    public int getSeaLevel() {
        return this.dimensionSettings.get().seaLevel();
    }

    @Override
    @Nonnull
    public List<MobSpawnSettings.SpawnerData> getMobsAt(@Nonnull Biome biome, @Nonnull StructureFeatureManager manager, @Nonnull MobCategory entityClassification, @Nonnull BlockPos pos) {
        List<MobSpawnSettings.SpawnerData> spawns = StructureSpawnManager.getStructureSpawns(manager, entityClassification, pos);
        if (spawns != null) return spawns;
        return super.getMobsAt(biome, manager, entityClassification, pos);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion genRegion) {
        //if (!this.dimensionSettings.get().disableMobGeneration()) { //Method protected. Boolean only used to disable mob spawning, which we never need to do.
        int x = genRegion.getCenterX();
        int z = genRegion.getCenterZ();
        Biome biome = genRegion.getBiome((new ChunkPos(x, z)).getWorldPosition());
        WorldgenRandom randomSeed = new WorldgenRandom();
        randomSeed.setDecorationSeed(genRegion.getSeed(), x << 4, z << 4);
        NaturalSpawner.spawnMobsForChunkGeneration(genRegion, biome, x, z, randomSeed);
        //}
    }
}