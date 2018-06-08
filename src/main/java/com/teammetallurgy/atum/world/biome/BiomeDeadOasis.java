package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import com.teammetallurgy.atum.world.gen.feature.WorldGenDeadwood;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLakes;

import javax.annotation.Nonnull;
import java.util.Random;

public class BiomeDeadOasis extends AtumBiome { //TODO Dried up pools

    public BiomeDeadOasis(AtumBiomeProperties properties) {
        super(properties);

        this.fillerBlock = AtumBlocks.LIMESTONE_CRACKED.getDefaultState();

        //no hostile spawns here

        this.deadwoodRarity = 1;
        this.pyramidRarity = -1;
    }

    @Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        int x = random.nextInt(4) + 4;
        int z = random.nextInt(4) + 4;

        if (random.nextFloat() <= 0.90F) {
            new WorldGenDeadwood(true, random.nextInt(1) + 6).generate(world, random, world.getHeight(pos.add(x, 0, z)));
        }

        new WorldGenLakes(Blocks.AIR).generate(world, random, pos.add(random.nextInt(16) + 8, random.nextInt(256), random.nextInt(16) + 8));
        super.decorate(world, random, pos);
    }
}