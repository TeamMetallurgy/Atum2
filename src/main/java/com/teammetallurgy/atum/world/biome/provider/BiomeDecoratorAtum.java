/*
package com.teammetallurgy.atum.world.biome.base;

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
        this.limestoneScarab = generateMineable(AtumBlocks.LIMESTONE.getDefaultState().with(BlockLimestone.HAS_SCARAB, true), 10);
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

        this.shrubChance = 0.3F;
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
        for (i = 0; doGen && i < this.sandPatchesPerChunk; ++i) {
            j = random.nextInt(16) + 8;
            k = random.nextInt(16) + 8;
            this.sandGen.generate(world, random, world.getTopSolidOrLiquidBlock(this.chunkPos.add(j, 0, k)));
        }

        i = this.treesPerChunk;
        if (random.nextInt(10) == 0) {
            ++i;
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
    }
}*/