package com.teammetallurgy.atum.world;

import com.teammetallurgy.atum.blocks.BlockSandLayers;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import com.teammetallurgy.atum.world.gen.feature.WorldGenAtumDungeons;
import com.teammetallurgy.atum.world.gen.feature.WorldGenLava;
import com.teammetallurgy.atum.world.gen.structure.girafitomb.GirafiTombPieces;
import com.teammetallurgy.atum.world.gen.structure.girafitomb.MapGenGirafiTomb;
import com.teammetallurgy.atum.world.gen.structure.lighthouse.LighthousePieces;
import com.teammetallurgy.atum.world.gen.structure.lighthouse.MapGenLighthouse;
import com.teammetallurgy.atum.world.gen.structure.mineshaft.MapGenAtumMineshaft;
import com.teammetallurgy.atum.world.gen.structure.pyramid.MapGenPyramid;
import com.teammetallurgy.atum.world.gen.structure.pyramid.PyramidPieces;
import com.teammetallurgy.atum.world.gen.structure.ruins.MapGenRuin;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinPieces;
import com.teammetallurgy.atum.world.gen.structure.tomb.MapGenTomb;
import com.teammetallurgy.atum.world.gen.structure.tomb.TombPieces;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ChunkGeneratorAtum implements IChunkGenerator {
    private static final IBlockState LIMESTONE = AtumBlocks.LIMESTONE.getDefaultState();
    private final Random rand;
    private NoiseGeneratorOctaves minLimitPerlinNoise;
    private NoiseGeneratorOctaves maxLimitPerlinNoise;
    private NoiseGeneratorOctaves mainPerlinNoise;
    private NoiseGeneratorPerlin surfaceNoise;
    private NoiseGeneratorOctaves scaleNoise;
    private NoiseGeneratorOctaves depthNoise;
    private NoiseGeneratorOctaves forestNoise;
    private final World world;
    private final boolean mapFeaturesEnabled;
    private final double[] heightMap;
    private final float[] biomeWeights;
    private ChunkGeneratorSettings settings;
    private double[] depthBuffer = new double[256];
    private MapGenBase caveGenerator = new MapGenCaves();
    private MapGenAtumMineshaft mineshaftGenerator = new MapGenAtumMineshaft();
    private MapGenBase ravineGenerator = new MapGenRavine();
    public MapGenPyramid pyramidGenerator = new MapGenPyramid(this);
    private MapGenRuin ruinGenerator = new MapGenRuin(this);
    private MapGenTomb tombGenerator = new MapGenTomb(this);
    private MapGenGirafiTomb girafiTomb = new MapGenGirafiTomb(this);
    private MapGenLighthouse lighthouse = new MapGenLighthouse(this);
    private Biome[] biomesForGeneration;
    private double[] mainNoiseRegion;
    private double[] minLimitRegion;
    private double[] maxLimitRegion;
    private double[] depthRegion;

    public ChunkGeneratorAtum(World world, long seed, boolean mapFeaturesEnabled, String generatorOptions) {
        this.world = world;
        this.mapFeaturesEnabled = mapFeaturesEnabled;
        this.rand = new Random(seed);
        this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
        this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.heightMap = new double[825];
        this.biomeWeights = new float[25];

        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                float f = 10.0F / MathHelper.sqrt((float) (i * i + j * j) + 0.2F);
                this.biomeWeights[i + 2 + (j + 2) * 5] = f;
            }
        }

        if (generatorOptions != null) {
            this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(generatorOptions).build();
        }
        world.setSeaLevel(63);

        InitNoiseGensEvent.ContextOverworld ctx = new InitNoiseGensEvent.ContextOverworld(minLimitPerlinNoise, maxLimitPerlinNoise, mainPerlinNoise, surfaceNoise, scaleNoise, depthNoise, forestNoise);
        ctx = TerrainGen.getModdedNoiseGenerators(world, this.rand, ctx);
        this.minLimitPerlinNoise = ctx.getLPerlin1();
        this.maxLimitPerlinNoise = ctx.getLPerlin2();
        this.mainPerlinNoise = ctx.getPerlin();
        this.surfaceNoise = ctx.getHeight();
        this.scaleNoise = ctx.getScale();
        this.depthNoise = ctx.getDepth();
        this.forestNoise = ctx.getForest();
    }

    public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);
        this.generateHeightmap(x * 4, 0, z * 4);

        for (int i = 0; i < 4; ++i) {
            int j = i * 5;
            int k = (i + 1) * 5;

            for (int l = 0; l < 4; ++l) {
                int i1 = (j + l) * 33;
                int j1 = (j + l + 1) * 33;
                int k1 = (k + l) * 33;
                int l1 = (k + l + 1) * 33;

                for (int i2 = 0; i2 < 32; ++i2) {
                    double d0 = 0.125D;
                    double d1 = this.heightMap[i1 + i2];
                    double d2 = this.heightMap[j1 + i2];
                    double d3 = this.heightMap[k1 + i2];
                    double d4 = this.heightMap[l1 + i2];
                    double d5 = (this.heightMap[i1 + i2 + 1] - d1) * d0;
                    double d6 = (this.heightMap[j1 + i2 + 1] - d2) * d0;
                    double d7 = (this.heightMap[k1 + i2 + 1] - d3) * d0;
                    double d8 = (this.heightMap[l1 + i2 + 1] - d4) * d0;

                    for (int j2 = 0; j2 < 8; ++j2) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int k2 = 0; k2 < 4; ++k2) {
                            double d16 = (d11 - d10) * d9;
                            double lvt_45_1_ = d10 - d16;

                            for (int l2 = 0; l2 < 4; ++l2) {
                                if ((lvt_45_1_ += d16) > 0.0D) {
                                    primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, LIMESTONE);
                                }
                            }
                            d10 += d12;
                            d11 += d13;
                        }
                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    private void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, Biome[] biomes) {
        this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, (double) (x * 16), (double) (z * 16), 16, 16, 0.0625D, 0.0625D, 1.0D);

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                Biome biome = biomes[j + i * 16];
                biome.genTerrainBlocks(this.world, this.rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);
            }
        }
    }

    @Override
    @Nonnull
    public Chunk generateChunk(int x, int z) {
        this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.setBlocksInChunk(x, z, chunkprimer);
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        this.replaceBiomeBlocks(x, z, chunkprimer, this.biomesForGeneration);

        if (this.settings.useCaves) {
            this.caveGenerator.generate(this.world, x, z, chunkprimer);
        }

        if (this.settings.useRavines) {
            this.ravineGenerator.generate(this.world, x, z, chunkprimer);
        }

        if (this.mapFeaturesEnabled) {
            if (this.settings.useMineShafts) {
                this.mineshaftGenerator.generate(this.world, x, z, chunkprimer);
            }
            if (AtumConfig.PYRAMID_ENABLED) {
                this.pyramidGenerator.generate(this.world, x, z, chunkprimer);
            }
            this.ruinGenerator.generate(this.world, x, z, chunkprimer);
            this.tombGenerator.generate(this.world, x, z, chunkprimer);
            this.girafiTomb.generate(this.world, x, z, chunkprimer);
            this.lighthouse.generate(this.world, x, z, chunkprimer);
        }

        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte) AtumBiome.getIdForBiome(this.biomesForGeneration[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private void generateHeightmap(int x, int y, int z) {
        this.depthRegion = this.depthNoise.generateNoiseOctaves(this.depthRegion, x, z, 5, 5, (double) this.settings.depthNoiseScaleX, (double) this.settings.depthNoiseScaleZ, (double) this.settings.depthNoiseScaleExponent);
        float f = this.settings.coordinateScale;
        float f1 = this.settings.heightScale;
        this.mainNoiseRegion = this.mainPerlinNoise.generateNoiseOctaves(this.mainNoiseRegion, x, y, z, 5, 33, 5, (double) (f / this.settings.mainNoiseScaleX), (double) (f1 / this.settings.mainNoiseScaleY), (double) (f / this.settings.mainNoiseScaleZ));
        this.minLimitRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minLimitRegion, x, y, z, 5, 33, 5, (double) f, (double) f1, (double) f);
        this.maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxLimitRegion, x, y, z, 5, 33, 5, (double) f, (double) f1, (double) f);
        int i = 0;
        int j = 0;

        for (int k = 0; k < 5; ++k) {
            for (int l = 0; l < 5; ++l) {
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                int i1 = 2;
                Biome biome = this.biomesForGeneration[k + 2 + (l + 2) * 10];

                for (int j1 = -2; j1 <= 2; ++j1) {
                    for (int k1 = -2; k1 <= 2; ++k1) {
                        Biome biome1 = this.biomesForGeneration[k + j1 + 2 + (l + k1 + 2) * 10];
                        float f5 = this.settings.biomeDepthOffSet + biome1.getBaseHeight() * this.settings.biomeDepthWeight;
                        float f6 = this.settings.biomeScaleOffset + biome1.getHeightVariation() * this.settings.biomeScaleWeight;

                        float f7 = this.biomeWeights[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);

                        if (biome1.getBaseHeight() > biome.getBaseHeight()) {
                            f7 /= 2.0F;
                        }

                        f2 += f6 * f7;
                        f3 += f5 * f7;
                        f4 += f7;
                    }
                }

                f2 = f2 / f4;
                f3 = f3 / f4;
                f2 = f2 * 0.9F + 0.1F;
                f3 = (f3 * 4.0F - 1.0F) / 8.0F;
                double d7 = this.depthRegion[j] / 8000.0D;

                if (d7 < 0.0D) {
                    d7 = -d7 * 0.3D;
                }

                d7 = d7 * 3.0D - 2.0D;

                if (d7 < 0.0D) {
                    d7 = d7 / 2.0D;

                    if (d7 < -1.0D) {
                        d7 = -1.0D;
                    }

                    d7 = d7 / 1.4D;
                    d7 = d7 / 2.0D;
                } else {
                    if (d7 > 1.0D) {
                        d7 = 1.0D;
                    }

                    d7 = d7 / 8.0D;
                }

                ++j;
                double d8 = (double) f3;
                double d9 = (double) f2;
                d8 = d8 + d7 * 0.2D;
                d8 = d8 * (double) this.settings.baseSize / 8.0D;
                double d0 = (double) this.settings.baseSize + d8 * 4.0D;

                for (int l1 = 0; l1 < 33; ++l1) {
                    double d1 = ((double) l1 - d0) * (double) this.settings.stretchY * 128.0D / 256.0D / d9;

                    if (d1 < 0.0D) {
                        d1 *= 4.0D;
                    }

                    double d2 = this.minLimitRegion[i] / (double) this.settings.lowerLimitScale;
                    double d3 = this.maxLimitRegion[i] / (double) this.settings.upperLimitScale;
                    double d4 = (this.mainNoiseRegion[i] / 10.0D + 1.0D) / 2.0D;
                    double d5 = MathHelper.clampedLerp(d2, d3, d4) - d1;

                    if (l1 > 29) {
                        double d6 = (double) ((float) (l1 - 29) / 3.0F);
                        d5 = d5 * (1.0D - d6) + -10.0D * d6;
                    }
                    this.heightMap[i] = d5;
                    ++i;
                }
            }
        }
    }

    @Override
    public void populate(int x, int z) {
        BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.world.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) x * k + (long) z * l ^ this.world.getSeed());
        ChunkPos chunkpos = new ChunkPos(x, z);

        if (this.mapFeaturesEnabled) {
            if (this.settings.useMineShafts) {
                this.mineshaftGenerator.generateStructure(this.world, this.rand, chunkpos);
            }
            if (AtumConfig.PYRAMID_ENABLED) {
                this.pyramidGenerator.generateStructure(this.world, this.rand, chunkpos);
            }
            this.ruinGenerator.generateStructure(this.world, this.rand, chunkpos);
            this.tombGenerator.generateStructure(this.world, this.rand, chunkpos);
            this.girafiTomb.generateStructure(this.world, this.rand, chunkpos);
            this.lighthouse.generateStructure(this.world, this.rand, chunkpos);
        }

        if (this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes) {
            int i2 = this.rand.nextInt(16) + 8;
            int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
            int k3 = this.rand.nextInt(16) + 8;

            if (l2 < this.world.getSeaLevel() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0) {
                (new WorldGenLava(Blocks.LAVA)).generate(this.world, this.rand, blockpos.add(i2, l2, k3));
            }
        }
        if (this.settings.useDungeons) {
            for (int j2 = 0; j2 < this.settings.dungeonChance; ++j2) {
                int i3 = this.rand.nextInt(16) + 8;
                int l3 = this.rand.nextInt(256);
                int l1 = this.rand.nextInt(16) + 8;
                (new WorldGenAtumDungeons()).generate(this.world, this.rand, blockpos.add(i3, l3, l1));
            }
        }

        biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
        blockpos = blockpos.add(8, 0, 8);

        if (TerrainGen.populate(this, this.world, this.rand, x, z, false, PopulateChunkEvent.Populate.EventType.CUSTOM)) {
            for (int k2 = 0; k2 < 16; ++k2) {
                for (int j3 = 0; j3 < 16; ++j3) {
                    BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));

                    if (canPlaceSandLayer(world, blockpos1, biome) && world.isAirBlock(blockpos1)) {
                        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                            BlockPos posOffset = blockpos1.offset(facing);
                            if (world.getBlockState(posOffset).isSideSolid(world, posOffset, EnumFacing.UP)) {
                                int layers = MathHelper.getInt(rand, 1, 3);
                                this.world.setBlockState(blockpos1, AtumBlocks.SAND_LAYERED.getDefaultState().withProperty(BlockSandLayers.LAYERS, layers), 2);
                            }
                        }
                    }
                }
            }
        }
        BlockFalling.fallInstantly = false;
    }

    static boolean canPlaceSandLayer(World world, BlockPos pos, Biome biome) {
        IBlockState stateDown = world.getBlockState(pos.down());
        return biome != AtumBiomes.OASIS
                && stateDown.getBlock() != AtumBlocks.LIMESTONE_CRACKED
                && stateDown.isSideSolid(world, pos, EnumFacing.UP)
                && !(stateDown.getBlock() instanceof BlockSandLayers)
                && !(world.getBlockState(pos).getBlock() instanceof BlockSandLayers);
    }

    @Override
    public boolean generateStructures(@Nonnull Chunk chunk, int x, int z) {
        return false;
    }

    @Override
    @Nonnull
    public List<Biome.SpawnListEntry> getPossibleCreatures(@Nonnull EnumCreatureType creatureType, @Nonnull BlockPos pos) {
        if (this.lighthouse.isPositionInStructure(this.world, pos)) {
            if (creatureType == EnumCreatureType.AMBIENT) {
                return this.lighthouse.getNaturalSpawns();
            }
        }
        return this.world.getBiome(pos).getSpawnableList(creatureType);
    }

    @Override
    public boolean isInsideStructure(@Nonnull World world, @Nonnull String structureName, @Nonnull BlockPos pos) {
        if (!this.mapFeaturesEnabled) {
            return false;
        } else if (String.valueOf(MapGenAtumMineshaft.MINESHAFT).equals(structureName) && this.mineshaftGenerator != null) {
            return this.mineshaftGenerator.isInsideStructure(pos);
        } else if (String.valueOf(PyramidPieces.PYRAMID).equals(structureName) && this.pyramidGenerator != null) {
            return this.pyramidGenerator.isInsideStructure(pos);
        } else if (String.valueOf(RuinPieces.RUIN).equals(structureName) && this.ruinGenerator != null) {
            return this.ruinGenerator.isInsideStructure(pos);
        } else if (String.valueOf(TombPieces.TOMB).equals(structureName) && this.tombGenerator != null) {
            return this.tombGenerator.isInsideStructure(pos);
        } else if (String.valueOf(GirafiTombPieces.GIRAFI_TOMB).equals(structureName) && this.girafiTomb != null) {
            return this.girafiTomb.isInsideStructure(pos);
        } else if (String.valueOf(LighthousePieces.LIGHTHOUSE).equals(structureName) && this.lighthouse != null) {
            return this.lighthouse.isInsideStructure(pos);
        }
        return false;
    }

    @Override
    @Nullable
    public BlockPos getNearestStructurePos(@Nonnull World world, @Nonnull String structureName, @Nonnull BlockPos pos, boolean findUnexplored) {
        if (String.valueOf(MapGenAtumMineshaft.MINESHAFT).equals(structureName) && this.mineshaftGenerator != null) {
            return this.mineshaftGenerator.getNearestStructurePos(world, pos, findUnexplored);
        } else if (String.valueOf(PyramidPieces.PYRAMID).equals(structureName) && this.pyramidGenerator != null) {
            return this.pyramidGenerator.getNearestStructurePos(world, pos, findUnexplored);
        } else if (String.valueOf(RuinPieces.RUIN).equals(structureName) && this.ruinGenerator != null) {
            return this.ruinGenerator.getNearestStructurePos(world, pos, findUnexplored);
        } else if (String.valueOf(TombPieces.TOMB).equals(structureName) && this.tombGenerator != null) {
            return this.tombGenerator.getNearestStructurePos(world, pos, findUnexplored);
        } else if (String.valueOf(LighthousePieces.LIGHTHOUSE).equals(structureName) && this.lighthouse != null) {
            return this.lighthouse.getNearestStructurePos(world, pos, findUnexplored);
        }
        return null;
    }

    @Override
    public void recreateStructures(@Nonnull Chunk chunk, int x, int z) {
        if (this.mapFeaturesEnabled) {
            if (this.settings.useMineShafts) {
                this.mineshaftGenerator.generate(this.world, x, z, null);
            }
            if (AtumConfig.PYRAMID_ENABLED) {
                this.pyramidGenerator.generate(this.world, x, z, null);
            }
            this.ruinGenerator.generate(this.world, x, z, null);
            this.tombGenerator.generate(this.world, x, z, null);
            this.girafiTomb.generate(this.world, x, z, null);
            this.lighthouse.generate(this.world, x, z, null);
        }
    }
}