package com.teammetallurgy.atum.world.biome.base;

import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestone;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.world.gen.feature.WorldGenShrub;
import com.teammetallurgy.atum.world.gen.feature.WorldGenSpring;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.Post;
import net.minecraftforge.event.terraingen.OreGenEvent.Pre;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

public class BiomeDecoratorAtum extends BiomeDecorator {
    private WorldGenerator emeraldGen;
    private WorldGenerator boneGen;
    private WorldGenerator relicGen;
    private WorldGenerator alabasterGen;
    private WorldGenerator porphyryGen;
    private WorldGenerator khnumite;
    private WorldGenerator limestoneScarab;
    public float shrubChance;

    BiomeDecoratorAtum() {
        super();
        //Atum ores
        this.alabasterGen = generateMineable(AtumBlocks.ALABASTER.getDefaultState(), 33);
        this.porphyryGen = generateMineable(AtumBlocks.PORPHYRY.getDefaultState(), 33);
        this.khnumite = generateMineable(AtumBlocks.KHNUMITE_RAW.getDefaultState(), 5);
        this.limestoneScarab = generateMineable(AtumBlocks.LIMESTONE.getDefaultState().withProperty(BlockLimestone.HAS_SCARAB, true), 10);
        this.boneGen = generateMineable(AtumBlocks.BONE_ORE.getDefaultState(), 8);
        this.relicGen = generateMineable(AtumBlocks.RELIC_ORE.getDefaultState(), 4);

        //Vanilla based ores
        this.coalGen = generateMineable(AtumBlocks.COAL_ORE.getDefaultState(), 16);
        this.ironGen = generateMineable(AtumBlocks.IRON_ORE.getDefaultState(), 8);
        this.goldGen = generateMineable(AtumBlocks.GOLD_ORE.getDefaultState(), 8);
        this.redstoneGen = generateMineable(AtumBlocks.REDSTONE_ORE.getDefaultState(), 7);
        this.diamondGen = generateMineable(AtumBlocks.DIAMOND_ORE.getDefaultState(), 7);
        this.emeraldGen = generateMineable(AtumBlocks.EMERALD_ORE.getDefaultState(), 5);
        this.lapisGen = generateMineable(AtumBlocks.LAPIS_ORE.getDefaultState(), 6);

        //Noise
        this.dirtGen = generateMineable(AtumBlocks.SAND.getDefaultState(), 32);
        this.gravelGen = generateMineable(AtumBlocks.LIMESTONE_GRAVEL.getDefaultState(), 32);
        this.clayGen = generateMineable(AtumBlocks.MARL.getDefaultState(), 12);

        this.treesPerChunk = 0;
        this.shrubChance = 0.3F;
        this.generateFalls = false;
    }

    private WorldGenMinable generateMineable(BlockState state, int size) {
        String category = AtumConfig.OREGEN + Configuration.CATEGORY_SPLITTER + Objects.requireNonNull(state.getBlock().getRegistryName()).getPath();
        size = AtumConfig.config.get(category, "vein size", size).getInt();
        AtumConfig.config.save();
        if (size > 0) {
            return new WorldGenMinable(state, size, BlockMatcher.forBlock(AtumBlocks.LIMESTONE));
        } else {
            return null;
        }
    }

    @Override
    public void decorate(World world, @Nonnull Random random, @Nonnull Biome biomeGenBase, @Nonnull BlockPos pos) {
        if (this.decorating) {
            throw new RuntimeException("Already decorating!!");
        } else {
            this.chunkPos = pos;
            this.genDecorations(biomeGenBase, world, random);
            this.decorating = false;
        }
    }

    @Override
    protected void generateOres(@Nonnull World world, @Nonnull Random random) {
        MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(world, random, chunkPos));
        if (TerrainGen.generateOre(world, random, this.coalGen, chunkPos, OreGenEvent.GenerateMinable.EventType.COAL)) {
            this.genStandardOre1(world, random, 20, this.coalGen, 0, 128);
        }

        if (TerrainGen.generateOre(world, random, this.ironGen, chunkPos, OreGenEvent.GenerateMinable.EventType.IRON)) {
            this.genStandardOre1(world, random, 20, this.ironGen, 0, 62);
        }

        if (TerrainGen.generateOre(world, random, this.goldGen, chunkPos, OreGenEvent.GenerateMinable.EventType.GOLD)) {
            this.genStandardOre1(world, random, 2, this.goldGen, 0, 32);
        }

        if (TerrainGen.generateOre(world, random, this.redstoneGen, chunkPos, OreGenEvent.GenerateMinable.EventType.REDSTONE)) {
            this.genStandardOre1(world, random, 8, this.redstoneGen, 0, 16);
        }

        if (TerrainGen.generateOre(world, random, this.emeraldGen, chunkPos, OreGenEvent.GenerateMinable.EventType.EMERALD)) {
            this.genStandardOre1(world, random, 3, this.emeraldGen, 4, 32);
        }

        if (TerrainGen.generateOre(world, random, this.diamondGen, chunkPos, OreGenEvent.GenerateMinable.EventType.DIAMOND)) {
            this.genStandardOre1(world, random, 1, this.diamondGen, 0, 16);
        }

        if (TerrainGen.generateOre(world, random, this.lapisGen, chunkPos, OreGenEvent.GenerateMinable.EventType.LAPIS)) {
            this.genStandardOre2(world, random, 1, this.lapisGen, 16, 16);
        }

        if (TerrainGen.generateOre(world, random, this.boneGen, chunkPos, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            this.genStandardOre1(world, random, 12, this.boneGen, 0, 128);
        }

        if (TerrainGen.generateOre(world, random, this.relicGen, chunkPos, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            this.genStandardOre1(world, random, 4, this.relicGen, 0, 64);
        }

        if (TerrainGen.generateOre(world, random, this.dirtGen, chunkPos, OreGenEvent.GenerateMinable.EventType.DIRT)) {
            this.genStandardOre1(world, random, 32, this.dirtGen, 0, 256);
        }

        if (TerrainGen.generateOre(world, random, this.gravelGen, chunkPos, OreGenEvent.GenerateMinable.EventType.GRAVEL)) {
            this.genStandardOre1(world, random, 10, this.gravelGen, 0, 256);
        }

        if (TerrainGen.generateOre(world, random, this.clayGen, chunkPos, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            this.genStandardOre1(world, random, 8, this.clayGen, 0, 50);
        }

        if (TerrainGen.generateOre(world, random, this.alabasterGen, chunkPos, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            this.genStandardOre1(world, random, 10, this.alabasterGen, 0, 60);
        }

        if (TerrainGen.generateOre(world, random, this.porphyryGen, chunkPos, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            this.genStandardOre1(world, random, 10, this.porphyryGen, 0, 60);
        }

        if (TerrainGen.generateOre(world, random, this.khnumite, chunkPos, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            this.genStandardOre1(world, random, 4, this.khnumite, 0, 20);
        }

        if (TerrainGen.generateOre(world, random, this.limestoneScarab, chunkPos, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            this.genStandardOre1(world, random, 8, this.limestoneScarab, 11, 80);
        }
        MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(world, random, chunkPos));
    }

    @Override
    protected void genDecorations(@Nonnull Biome biomeGenBase, @Nonnull World world, Random random) {
        ChunkPos chunkPosition = new ChunkPos(chunkPos);
        MinecraftForge.EVENT_BUS.post(new Pre(world, random, this.chunkPos));
        this.generateOres(world, random);
        boolean doGen = TerrainGen.decorate(world, random, chunkPosition, EventType.SAND_PASS2);

        int i;
        int j;
        int k;
        for (i = 0; doGen && i < this.sandPatchesPerChunk; ++i) {
            j = random.nextInt(16) + 8;
            k = random.nextInt(16) + 8;
            this.sandGen.generate(world, random, world.getTopSolidOrLiquidBlock(this.chunkPos.add(j, 0, k)));
        }

        i = this.treesPerChunk;
        if (random.nextInt(10) == 0) {
            ++i;
        }

        for (j = 0; TerrainGen.decorate(world, random, chunkPosition, EventType.GRASS) && j < this.grassPerChunk; ++j) {
            int j7 = random.nextInt(16) + 8;
            int i11 = random.nextInt(16) + 8;
            int k14 = world.getHeight(this.chunkPos.add(j7, 0, i11)).getY() * 2;

            if (k14 > 0) {
                int l17 = random.nextInt(k14);
                biomeGenBase.getRandomWorldGenForGrass(random).generate(world, random, this.chunkPos.add(j7, l17, i11));
            }
        }

        if (random.nextFloat() < this.shrubChance) {
            int k7 = random.nextInt(16) + 8;
            int j11 = random.nextInt(16) + 8;
            int l14 = world.getHeight(this.chunkPos.add(k7, 0, j11)).getY() * 2;

            if (l14 > 0) {
                int i18 = random.nextInt(l14);
                (new WorldGenShrub(AtumBlocks.SHRUB, 8)).generate(world, random, this.chunkPos.add(k7, i18, j11));
            }
        }

        if (random.nextFloat() < this.shrubChance) {
            int k7 = random.nextInt(16) + 8;
            int j11 = random.nextInt(16) + 8;
            int l14 = world.getHeight(this.chunkPos.add(k7, 0, j11)).getY() * 2;

            if (l14 > 0) {
                int i18 = random.nextInt(l14);
                (new WorldGenShrub(AtumBlocks.WEED, 8)).generate(world, random, this.chunkPos.add(k7, i18, j11));
            }
        }

        if (TerrainGen.decorate(world, random, chunkPosition, DecorateBiomeEvent.Decorate.EventType.LAKE_WATER)) {
            for (int k5 = 0; k5 < 50; ++k5) {
                int x = random.nextInt(16) + 8;
                int z = random.nextInt(16) + 8;
                int y = random.nextInt(248) + 8;

                if (y > 0) {
                    int randomY = random.nextInt(y);
                    BlockPos pos = this.chunkPos.add(x, randomY, z);
                    (new WorldGenSpring(Blocks.FLOWING_WATER)).generate(world, random, pos);
                }
            }
        }
        MinecraftForge.EVENT_BUS.post(new Post(world, random, chunkPos));
    }
}
