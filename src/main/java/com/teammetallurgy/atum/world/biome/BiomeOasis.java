package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import com.teammetallurgy.atum.world.gen.feature.WorldGenOasisPond;
import com.teammetallurgy.atum.world.gen.feature.WorldGenPalm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BiomeOasis extends AtumBiome {

    public BiomeOasis(AtumBiomeProperties properties) {
        super(properties);

        this.topBlock = AtumBlocks.FERTILE_SOIL.getDefaultState();

        // no hostile spawns here

        this.decorator.deadBushPerChunk = 0;
        this.atumDecorator.shrubChance = 0;
        this.decorator.grassPerChunk = 3;

        this.deadwoodRarity = -1;
    }

    @Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        int x = random.nextInt(16) + 8;
        int z = random.nextInt(16) + 8;
        BlockPos height = world.getHeight(pos.add(x, 0, z));

        new WorldGenOasisPond().generate(world, random, pos.add(x, height.getY(), z));

        if (random.nextFloat() <= 0.98F) {
            new WorldGenPalm(true, random.nextInt(4) + 5).generate(world, random, height);
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