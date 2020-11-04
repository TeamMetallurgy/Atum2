package com.teammetallurgy.atum.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.init.AtumBiomes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.jigsaw.JigsawJunction;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.world.StructureSpawnManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class AtumChunkGenerator extends ChunkGenerator { //Copied from NoiseChunkGenerator, to make noise seed random.
    public static final Codec<AtumChunkGenerator> CODEC = RecordCodecBuilder.create((c) -> {
        return c.group(BiomeProvider.CODEC.fieldOf("biome_source").forGetter((chunkGenerator) -> {
            return chunkGenerator.biomeProvider;
        }), DimensionSettings.field_236098_b_.fieldOf("settings").forGetter((chunkGenerator) -> {
            return chunkGenerator.dimensionSettings;
        })).apply(c, c.stable(AtumChunkGenerator::new));
    });
    private static final float[] field_222561_h = Util.make(new float[13824], (p_236094_0_) -> {
        for (int i = 0; i < 24; ++i) {
            for (int j = 0; j < 24; ++j) {
                for (int k = 0; k < 24; ++k) {
                    p_236094_0_[i * 24 * 24 + j * 24 + k] = (float) func_222554_b(j - 12, k - 12, i - 12);
                }
            }
        }

    });
    private static final float[] field_236081_j_ = Util.make(new float[25], (p_236092_0_) -> {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                float f = 10.0F / MathHelper.sqrt((float) (i * i + j * j) + 0.2F);
                p_236092_0_[i + 2 + (j + 2) * 5] = f;
            }
        }

    });
    private static final BlockState AIR = Blocks.AIR.getDefaultState();
    private final int verticalNoiseGranularity;
    private final int horizontalNoiseGranularity;
    private final int noiseSizeX;
    private final int noiseSizeY;
    private final int noiseSizeZ;
    protected final SharedSeedRandom randomSeed;
    private final OctavesNoiseGenerator field_222568_o;
    private final OctavesNoiseGenerator field_222569_p;
    private final OctavesNoiseGenerator field_222570_q;
    private final INoiseGenerator surfaceDepthNoise;
    private final OctavesNoiseGenerator field_236082_u_;
    @Nullable
    private final SimplexNoiseGenerator field_236083_v_;
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    private final long seed;
    protected final Supplier<DimensionSettings> dimensionSettings;
    private final int worldHeight;

    public AtumChunkGenerator(BiomeProvider biomeProvider, Supplier<DimensionSettings> dimensionSettings) {
        this(biomeProvider, biomeProvider, (new Random()).nextLong(), dimensionSettings); //Set random seed
    }

    private AtumChunkGenerator(BiomeProvider biomeProvider1, BiomeProvider biomeProvider2, long seed, Supplier<DimensionSettings> dimensionSettings) {
        super(biomeProvider1, biomeProvider2, dimensionSettings.get().getStructures(), seed);
        this.seed = seed;
        DimensionSettings dimensionsettings = dimensionSettings.get();
        this.dimensionSettings = dimensionSettings;
        NoiseSettings noisesettings = dimensionsettings.getNoise();
        this.worldHeight = noisesettings.func_236169_a_();
        this.verticalNoiseGranularity = noisesettings.func_236175_f_() * 4;
        this.horizontalNoiseGranularity = noisesettings.func_236174_e_() * 4;
        this.defaultBlock = dimensionsettings.getDefaultBlock();
        this.defaultFluid = dimensionsettings.getDefaultFluid();
        this.noiseSizeX = 16 / this.horizontalNoiseGranularity;
        this.noiseSizeY = noisesettings.func_236169_a_() / this.verticalNoiseGranularity;
        this.noiseSizeZ = 16 / this.horizontalNoiseGranularity;
        this.randomSeed = new SharedSeedRandom(seed);
        this.field_222568_o = new OctavesNoiseGenerator(this.randomSeed, IntStream.rangeClosed(-15, 0));
        this.field_222569_p = new OctavesNoiseGenerator(this.randomSeed, IntStream.rangeClosed(-15, 0));
        this.field_222570_q = new OctavesNoiseGenerator(this.randomSeed, IntStream.rangeClosed(-7, 0));
        this.surfaceDepthNoise = (noisesettings.func_236178_i_() ? new PerlinNoiseGenerator(this.randomSeed, IntStream.rangeClosed(-3, 0)) : new OctavesNoiseGenerator(this.randomSeed, IntStream.rangeClosed(-3, 0)));
        this.randomSeed.skip(2620);
        this.field_236082_u_ = new OctavesNoiseGenerator(this.randomSeed, IntStream.rangeClosed(-15, 0));
        if (noisesettings.func_236180_k_()) {
            SharedSeedRandom sharedseedrandom = new SharedSeedRandom(seed);
            sharedseedrandom.skip(17292);
            this.field_236083_v_ = new SimplexNoiseGenerator(sharedseedrandom);
        } else {
            this.field_236083_v_ = null;
        }
    }

    @Override
    @Nonnull
    protected Codec<? extends ChunkGenerator> func_230347_a_() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public ChunkGenerator func_230349_a_(long seed) {
        return new AtumChunkGenerator(this.biomeProvider.getBiomeProvider(seed), this.dimensionSettings);
    }

    public boolean func_236088_a_(long p_236088_1_, RegistryKey<DimensionSettings> dimensionSettings) {
        return this.seed == p_236088_1_ && this.dimensionSettings.get().func_242744_a(dimensionSettings);
    }

    private double func_222552_a(int p_222552_1_, int p_222552_2_, int p_222552_3_, double p_222552_4_, double p_222552_6_, double p_222552_8_, double p_222552_10_) {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        boolean flag = true;
        double d3 = 1.0D;

        for (int i = 0; i < 16; ++i) {
            double d4 = OctavesNoiseGenerator.maintainPrecision((double) p_222552_1_ * p_222552_4_ * d3);
            double d5 = OctavesNoiseGenerator.maintainPrecision((double) p_222552_2_ * p_222552_6_ * d3);
            double d6 = OctavesNoiseGenerator.maintainPrecision((double) p_222552_3_ * p_222552_4_ * d3);
            double d7 = p_222552_6_ * d3;
            ImprovedNoiseGenerator improvednoisegenerator = this.field_222568_o.getOctave(i);
            if (improvednoisegenerator != null) {
                d0 += improvednoisegenerator.func_215456_a(d4, d5, d6, d7, (double) p_222552_2_ * d7) / d3;
            }

            ImprovedNoiseGenerator improvednoisegenerator1 = this.field_222569_p.getOctave(i);
            if (improvednoisegenerator1 != null) {
                d1 += improvednoisegenerator1.func_215456_a(d4, d5, d6, d7, (double) p_222552_2_ * d7) / d3;
            }

            if (i < 8) {
                ImprovedNoiseGenerator improvednoisegenerator2 = this.field_222570_q.getOctave(i);
                if (improvednoisegenerator2 != null) {
                    d2 += improvednoisegenerator2.func_215456_a(OctavesNoiseGenerator.maintainPrecision((double) p_222552_1_ * p_222552_8_ * d3), OctavesNoiseGenerator.maintainPrecision((double) p_222552_2_ * p_222552_10_ * d3), OctavesNoiseGenerator.maintainPrecision((double) p_222552_3_ * p_222552_8_ * d3), p_222552_10_ * d3, (double) p_222552_2_ * p_222552_10_ * d3) / d3;
                }
            }

            d3 /= 2.0D;
        }

        return MathHelper.clampedLerp(d0 / 512.0D, d1 / 512.0D, (d2 / 10.0D + 1.0D) / 2.0D);
    }

    private double[] func_222547_b(int noiseX, int noiseZ) {
        double[] noiseColumn = new double[this.noiseSizeY + 1];
        this.fillNoiseColumn(noiseColumn, noiseX, noiseZ);
        return noiseColumn;
    }

    private void fillNoiseColumn(double[] noiseColumn, int noiseX, int noiseZ) {
        NoiseSettings noisesettings = this.dimensionSettings.get().getNoise();
        double d0;
        double d1;
        if (this.field_236083_v_ != null) {
            d0 = (EndBiomeProvider.getRandomNoise(this.field_236083_v_, noiseX, noiseZ) - 8.0F);
            if (d0 > 0.0D) {
                d1 = 0.25D;
            } else {
                d1 = 1.0D;
            }
        } else {
            float f = 0.0F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            int j = this.getSeaLevel();
            float f3 = this.biomeProvider.getNoiseBiome(noiseX, j, noiseZ).getDepth();

            for (int k = -2; k <= 2; ++k) {
                for (int l = -2; l <= 2; ++l) {
                    Biome biome = this.biomeProvider.getNoiseBiome(noiseX + k, j, noiseZ + l);
                    float f4 = biome.getDepth();
                    float f5 = biome.getScale();
                    float f6;
                    float f7;
                    if (noisesettings.func_236181_l_() && f4 > 0.0F) {
                        f6 = 1.0F + f4 * 2.0F;
                        f7 = 1.0F + f5 * 4.0F;
                    } else {
                        f6 = f4;
                        f7 = f5;
                    }

                    float f8 = f4 > f3 ? 0.5F : 1.0F;
                    float f9 = f8 * field_236081_j_[k + 2 + (l + 2) * 5] / (f6 + 2.0F);
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

        double d12 = 684.412D * noisesettings.func_236171_b_().func_236151_a_();
        double d13 = 684.412D * noisesettings.func_236171_b_().func_236153_b_();
        double d14 = d12 / noisesettings.func_236171_b_().func_236154_c_();
        double d15 = d13 / noisesettings.func_236171_b_().func_236155_d_();
        double d17 = noisesettings.func_236172_c_().func_236186_a_();
        double d19 = noisesettings.func_236172_c_().func_236188_b_();
        double d20 = noisesettings.func_236172_c_().func_236189_c_();
        double d21 = noisesettings.func_236173_d_().func_236186_a_();
        double d2 = noisesettings.func_236173_d_().func_236188_b_();
        double d3 = noisesettings.func_236173_d_().func_236189_c_();
        double d4 = noisesettings.func_236179_j_() ? this.func_236095_c_(noiseX, noiseZ) : 0.0D;
        double d5 = noisesettings.func_236176_g_();
        double d6 = noisesettings.func_236177_h_();

        for (int i1 = 0; i1 <= this.noiseSizeY; ++i1) {
            double d7 = this.func_222552_a(noiseX, i1, noiseZ, d12, d13, d14, d15);
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
                d7 = MathHelper.clampedLerp(d17, d7, d11);
            }

            if (d2 > 0.0D) {
                double d22 = ((double) i1 - d3) / d2;
                d7 = MathHelper.clampedLerp(d21, d7, d22);
            }
            noiseColumn[i1] = d7;
        }
    }

    private double func_236095_c_(int p_236095_1_, int p_236095_2_) {
        double d0 = this.field_236082_u_.getValue((p_236095_1_ * 200), 10.0D, (p_236095_2_ * 200), 1.0D, 0.0D, true);
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
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return this.func_236087_a_(x, z, null, heightmapType.getHeightLimitPredicate());
    }

    @Override
    @Nonnull
    public IBlockReader func_230348_a_(int p_230348_1_, int p_230348_2_) {
        BlockState[] states = new BlockState[this.noiseSizeY * this.verticalNoiseGranularity];
        this.func_236087_a_(p_230348_1_, p_230348_2_, states, null);
        return new Blockreader(states);
    }

    private int func_236087_a_(int noiseX, int noiseZ, @Nullable BlockState[] states, @Nullable Predicate<BlockState> predicate) {
        int i = Math.floorDiv(noiseX, this.horizontalNoiseGranularity);
        int j = Math.floorDiv(noiseZ, this.horizontalNoiseGranularity);
        int k = Math.floorMod(noiseX, this.horizontalNoiseGranularity);
        int l = Math.floorMod(noiseZ, this.horizontalNoiseGranularity);
        double d0 = (double) k / (double) this.horizontalNoiseGranularity;
        double d1 = (double) l / (double) this.horizontalNoiseGranularity;
        double[][] adouble = new double[][]{this.func_222547_b(i, j), this.func_222547_b(i, j + 1), this.func_222547_b(i + 1, j), this.func_222547_b(i + 1, j + 1)};

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
                double d11 = MathHelper.lerp3(d10, d0, d1, d2, d6, d4, d8, d3, d7, d5, d9);
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
    public void generateSurface(@Nonnull WorldGenRegion genRegion, IChunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        int x = chunkPos.x;
        int j = chunkPos.z;
        SharedSeedRandom seedRandom = new SharedSeedRandom();
        seedRandom.setBaseChunkSeed(x, j);
        ChunkPos chunkStartPos = chunk.getPos();
        int xStart = chunkStartPos.getXStart();
        int zStart = chunkStartPos.getZStart();
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for (int i1 = 0; i1 < 16; ++i1) {
            for (int j1 = 0; j1 < 16; ++j1) {
                int k1 = xStart + i1;
                int l1 = zStart + j1;
                int i2 = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE_WG, i1, j1) + 1;
                double d1 = this.surfaceDepthNoise.noiseAt((double) k1 * 0.0625D, (double) l1 * 0.0625D, 0.0625D, (double) i1 * 0.0625D) * 15.0D;
                BlockPos biomePos = mutablePos.setPos(xStart + i1, i2, zStart + j1);
                genRegion.getBiome(biomePos).buildSurface(seedRandom, chunk, k1, l1, i2, d1, this.defaultBlock, this.defaultFluid, this.getSeaLevel(), genRegion.getSeed());
            }
        }
        this.makeBedrock(chunk, seedRandom);
    }

    public static boolean canBiomeHaveWater (RegistryKey<Biome> biome) {
        return biome == AtumBiomes.OASIS || biome == AtumBiomes.DRIED_RIVER; //TODO Change river to new river biome
    }

    private void makeBedrock(IChunk chunk, Random rand) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        int x = chunk.getPos().getXStart();
        int j = chunk.getPos().getZStart();
        DimensionSettings dimensionsettings = this.dimensionSettings.get();
        int k = dimensionsettings.func_236118_f_();
        int l = this.worldHeight - 1 - dimensionsettings.func_236117_e_();
        int i1 = 5;
        boolean flag = l + 4 >= 0 && l < this.worldHeight;
        boolean flag1 = k + 4 >= 0 && k < this.worldHeight;
        if (flag || flag1) {
            for (BlockPos blockpos : BlockPos.getAllInBoxMutable(x, 0, j, x + 15, 0, j + 15)) {
                if (flag) {
                    for (int j1 = 0; j1 < 5; ++j1) {
                        if (j1 <= rand.nextInt(5)) {
                            chunk.setBlockState(blockpos$mutable.setPos(blockpos.getX(), l - j1, blockpos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
                        }
                    }
                }

                if (flag1) {
                    for (int k1 = 4; k1 >= 0; --k1) {
                        if (k1 <= rand.nextInt(5)) {
                            chunk.setBlockState(blockpos$mutable.setPos(blockpos.getX(), k + k1, blockpos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
                        }
                    }
                }
            }

        }
    }

    @Override
    public void func_230352_b_(@Nonnull IWorld world, @Nonnull StructureManager manager, IChunk chunk) {
        ObjectList<StructurePiece> objectlist = new ObjectArrayList<>(10);
        ObjectList<JigsawJunction> objectlist1 = new ObjectArrayList<>(32);
        ChunkPos chunkpos = chunk.getPos();
        int x = chunkpos.x;
        int j = chunkpos.z;
        int k = x << 4;
        int l = j << 4;

        for (Structure<?> structure : Structure.field_236384_t_) {
            manager.func_235011_a_(SectionPos.from(chunkpos, 0), structure).forEach((p_236089_5_) -> {
                for (StructurePiece structurepiece1 : p_236089_5_.getComponents()) {
                    if (structurepiece1.func_214810_a(chunkpos, 12)) {
                        if (structurepiece1 instanceof AbstractVillagePiece) {
                            AbstractVillagePiece abstractvillagepiece = (AbstractVillagePiece) structurepiece1;
                            JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour = abstractvillagepiece.getJigsawPiece().getPlacementBehaviour();
                            if (jigsawpattern$placementbehaviour == JigsawPattern.PlacementBehaviour.RIGID) {
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

        ChunkPrimer chunkprimer = (ChunkPrimer) chunk;
        Heightmap heightmap = chunkprimer.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmap1 = chunkprimer.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        ObjectListIterator<StructurePiece> objectlistiterator = objectlist.iterator();
        ObjectListIterator<JigsawJunction> objectlistiterator1 = objectlist1.iterator();

        for (int i1 = 0; i1 < this.noiseSizeX; ++i1) {
            for (int j1 = 0; j1 < this.noiseSizeZ + 1; ++j1) {
                this.fillNoiseColumn(adouble[1][j1], x * this.noiseSizeX + i1 + 1, j * this.noiseSizeZ + j1);
            }

            for (int j5 = 0; j5 < this.noiseSizeZ; ++j5) {
                ChunkSection chunksection = chunkprimer.getSection(15);
                chunksection.lock();

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
                        if (chunksection.getYLocation() >> 4 != k2) {
                            chunksection.unlock();
                            chunksection = chunkprimer.getSection(k2);
                            chunksection.lock();
                        }

                        double d8 = (double) l1 / (double) this.verticalNoiseGranularity;
                        double d9 = MathHelper.lerp(d8, d0, d4);
                        double d10 = MathHelper.lerp(d8, d2, d6);
                        double d11 = MathHelper.lerp(d8, d1, d5);
                        double d12 = MathHelper.lerp(d8, d3, d7);

                        for (int l2 = 0; l2 < this.horizontalNoiseGranularity; ++l2) {
                            int i3 = k + i1 * this.horizontalNoiseGranularity + l2;
                            int j3 = i3 & 15;
                            double d13 = (double) l2 / (double) this.horizontalNoiseGranularity;
                            double d14 = MathHelper.lerp(d13, d9, d10);
                            double d15 = MathHelper.lerp(d13, d11, d12);

                            for (int k3 = 0; k3 < this.horizontalNoiseGranularity; ++k3) {
                                int l3 = l + j5 * this.horizontalNoiseGranularity + k3;
                                int i4 = l3 & 15;
                                double d16 = (double) k3 / (double) this.horizontalNoiseGranularity;
                                double d17 = MathHelper.lerp(d16, d14, d15);
                                double d18 = MathHelper.clamp(d17 / 200.0D, -1.0D, 1.0D);

                                int j4;
                                int k4;
                                int l4;
                                for (d18 = d18 / 2.0D - d18 * d18 * d18 / 24.0D; objectlistiterator.hasNext(); d18 += func_222556_a(j4, k4, l4) * 0.8D) {
                                    StructurePiece structurepiece = objectlistiterator.next();
                                    MutableBoundingBox mutableboundingbox = structurepiece.getBoundingBox();
                                    j4 = Math.max(0, Math.max(mutableboundingbox.minX - i3, i3 - mutableboundingbox.maxX));
                                    k4 = i2 - (mutableboundingbox.minY + (structurepiece instanceof AbstractVillagePiece ? ((AbstractVillagePiece) structurepiece).getGroundLevelDelta() : 0));
                                    l4 = Math.max(0, Math.max(mutableboundingbox.minZ - l3, l3 - mutableboundingbox.maxZ));
                                }

                                objectlistiterator.back(objectlist.size());

                                while (objectlistiterator1.hasNext()) {
                                    JigsawJunction jigsawjunction = objectlistiterator1.next();
                                    int k5 = i3 - jigsawjunction.getSourceX();
                                    j4 = i2 - jigsawjunction.getSourceGroundY();
                                    k4 = l3 - jigsawjunction.getSourceZ();
                                    d18 += func_222556_a(k5, j4, k4) * 0.4D;
                                }

                                objectlistiterator1.back(objectlist1.size());
                                BlockState blockstate = this.getDefaultBlockAndFluid(d18, i2);
                                if (blockstate != AIR) {
                                    mutablePos.setPos(i3, i2, l3);
                                    if (blockstate.getLightValue(chunkprimer, mutablePos) != 0) {
                                        chunkprimer.addLightPosition(mutablePos);
                                    }

                                    chunksection.setBlockState(j3, j2, i4, blockstate, false);
                                    heightmap.update(j3, i2, i4, blockstate);
                                    heightmap1.update(j3, i2, i4, blockstate);
                                }
                            }
                        }
                    }
                }
                chunksection.unlock();
            }
            double[][] adouble1 = adouble[0];
            adouble[0] = adouble[1];
            adouble[1] = adouble1;
        }
    }

    private static double func_222556_a(int p_222556_0_, int p_222556_1_, int p_222556_2_) {
        int i = p_222556_0_ + 12;
        int j = p_222556_1_ + 12;
        int k = p_222556_2_ + 12;
        if (i >= 0 && i < 24) {
            if (j >= 0 && j < 24) {
                return k >= 0 && k < 24 ? (double) field_222561_h[k * 24 * 24 + i * 24 + j] : 0.0D;
            } else {
                return 0.0D;
            }
        } else {
            return 0.0D;
        }
    }

    private static double func_222554_b(int p_222554_0_, int p_222554_1_, int p_222554_2_) {
        double d0 = (p_222554_0_ * p_222554_0_ + p_222554_2_ * p_222554_2_);
        double d1 = (double) p_222554_1_ + 0.5D;
        double d2 = d1 * d1;
        double d3 = Math.pow(Math.E, -(d2 / 16.0D + d0 / 16.0D));
        double d4 = -d1 * MathHelper.fastInvSqrt(d2 / 2.0D + d0 / 2.0D) / 2.0D;
        return d4 * d3;
    }

    @Override
    public int getMaxBuildHeight() {
        return this.worldHeight;
    }

    @Override
    public int getSeaLevel() {
        return this.dimensionSettings.get().func_236119_g_();
    }

    @Override
    @Nonnull
    public List<MobSpawnInfo.Spawners> func_230353_a_(@Nonnull Biome biome, @Nonnull StructureManager manager, @Nonnull EntityClassification entityClassification, @Nonnull BlockPos pos) {
        List<MobSpawnInfo.Spawners> spawns = StructureSpawnManager.getStructureSpawns(manager, entityClassification, pos);
        if (spawns != null) return spawns;
        return super.func_230353_a_(biome, manager, entityClassification, pos);
    }

    @Override
    public void func_230354_a_(WorldGenRegion genRegion) {
        //if (!this.dimensionSettings.get().func_236120_h_()) { //Method protected. Boolean only used to disable mob spawning, which we never need to do.
        int x = genRegion.getMainChunkX();
        int z = genRegion.getMainChunkZ();
        Biome biome = genRegion.getBiome((new ChunkPos(x, z)).asBlockPos());
        SharedSeedRandom randomSeed = new SharedSeedRandom();
        randomSeed.setDecorationSeed(genRegion.getSeed(), x << 4, z << 4);
        WorldEntitySpawner.performWorldGenSpawning(genRegion, biome, x, z, randomSeed);
        //}
    }
}