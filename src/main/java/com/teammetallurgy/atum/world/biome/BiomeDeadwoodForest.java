package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nonnull;
import java.util.Random;

public class BiomeDeadwoodForest extends AtumBiome {
    private WorldGenerator anputsFingersGen = new WorldGenBush(AtumBlocks.ANPUTS_FINGERS);

    public BiomeDeadwoodForest(AtumBiomeProperties properties) {
        super(properties);

        this.deadwoodRarity = 1.0D;
        this.decorator.grassPerChunk = 1;

        this.addDefaultSpawns();
    }

    @Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        super.decorate(world, random, pos);

        if (TerrainGen.decorate(world, random, new ChunkPos(pos), DecorateBiomeEvent.Decorate.EventType.SHROOM)) {
            for (int amount = 0; amount < 14; ++amount) {
                if (random.nextInt(10) == 0) {
                    int x = random.nextInt(16) + 8;
                    int z = random.nextInt(16) + 8;
                    BlockPos genPos = world.getHeight(pos.add(x, 0, z));
                    this.anputsFingersGen.generate(world, random, genPos);
                }
            }
        }
    }
}