package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import com.teammetallurgy.atum.world.gen.feature.WorldGenOasisPond;
import com.teammetallurgy.atum.world.gen.feature.WorldGenPalm;
import com.teammetallurgy.atum.world.gen.feature.WorldGenPapyrus;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nonnull;
import java.util.Random;

public class BiomeOasis extends AtumBiome {

    public BiomeOasis(AtumBiomeProperties properties) {
        super(properties);
        this.topBlock = AtumBlocks.FERTILE_SOIL.getDefaultState();

        this.decorator.deadBushPerChunk = 0;
        this.atumDecorator.shrubChance = 0;
        this.decorator.grassPerChunk = 3;
        this.decorator.waterlilyPerChunk = 100;
        this.deadwoodRarity = 0.0D;
    }

    @Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        int x = random.nextInt(16) + 8;
        int z = random.nextInt(16) + 8;
        BlockPos height = world.getHeight(pos.add(x, 0, z));
        ChunkPos chunkPos = new ChunkPos(pos);

        new WorldGenOasisPond().generate(world, random, height);

        if (random.nextFloat() <= 0.98F) {
            new WorldGenPalm(true, random.nextInt(4) + 5).generate(world, random, height);
        }

        if (TerrainGen.decorate(world, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.REED)) {
            int reedsPerChunk = 50;
            for (int reeds = 0; reeds < reedsPerChunk; ++reeds) {
                int y = height.getY() * 2;

                if (y > 0) {
                    int randomY = random.nextInt(y);
                    new WorldGenPapyrus().generate(world, random, pos.add(x, randomY, z));
                }
            }
        }
        if (TerrainGen.decorate(world, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.LILYPAD)) {
            for (int amount = 0; amount < 2; ++amount) {
                int y = world.getHeight(pos.add(x, 0, z)).getY() * 2;
                if (y > 0) {
                    int randomY = random.nextInt(y);
                    BlockPos lilyPos;
                    BlockPos waterPos;
                    for (lilyPos = pos.add(x, randomY, z); lilyPos.getY() > 0; lilyPos = waterPos) {
                        waterPos = lilyPos.down();
                        if (!world.isAirBlock(waterPos)) {
                            break;
                        }
                    }
                    new WorldGenWaterlily().generate(world, random, lilyPos);
                }
            }
        }
        super.decorate(world, random, pos);
    }

    @Override
    public int getModdedBiomeFoliageColor(int original) {
        return 11987573;
    }

    @Override
    public int getModdedBiomeGrassColor(int original) {
        return 11987573;
    }
}