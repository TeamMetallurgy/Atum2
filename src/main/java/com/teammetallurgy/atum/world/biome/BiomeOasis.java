package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import com.teammetallurgy.atum.world.gen.feature.WorldGenPalm;
import com.teammetallurgy.atum.world.gen.feature.WorldGenPapyrus;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

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

        new WorldGenLakes(Blocks.WATER).generate(world, random, pos.add(x, random.nextInt(256), z));

        if (random.nextFloat() <= 0.70F) {
            new WorldGenPalm(true, random.nextInt(4) + 5).generate(world, random, height);
        }

        if (TerrainGen.decorate(world, random, new ChunkPos(pos), DecorateBiomeEvent.Decorate.EventType.REED)) {
            for (int papyri = 0; papyri < 8; ++papyri) {
                int papyrusHeight = height.getY() * 2;
                if (papyrusHeight > 0) {
                    int randomPapyrusHeight = random.nextInt(papyrusHeight);
                    new WorldGenPapyrus().generate(world, random, pos.add(x, randomPapyrusHeight, z));
                }
            }
        }
        super.decorate(world, random, pos);
    }

    @Override
    public int getModdedBiomeFoliageColor(int original) {
        return 11987573;
    }
}