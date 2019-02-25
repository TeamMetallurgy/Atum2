package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockDeadwood;
import com.teammetallurgy.atum.blocks.wood.BlockLeave;
import com.teammetallurgy.atum.entity.animal.EntityCamel;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import com.teammetallurgy.atum.world.gen.feature.WorldGenPalm;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLakes;

import javax.annotation.Nonnull;
import java.util.Random;

public class BiomeDeadOasis extends AtumBiome {

    public BiomeDeadOasis(AtumBiomeProperties properties) {
        super(properties);

        this.topBlock = AtumBlocks.LIMESTONE_GRAVEL.getDefaultState();
        this.fillerBlock = AtumBlocks.LIMESTONE_CRACKED.getDefaultState();

        this.deadwoodRarity = 0.0D;
        this.decorator.grassPerChunk = 2;

        this.addDefaultSpawns();
    }

    @Override
    protected void addDefaultSpawns() {
        super.addDefaultSpawns();

        addSpawn(EntityCamel.class, 6, 2, 6, EnumCreatureType.CREATURE);
    }

    @Override
    public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
        int x = random.nextInt(4) + 4;
        int z = random.nextInt(4) + 4;

        int i1 = random.nextInt(16) + 8;
        int j1 = random.nextInt(256);
        int k1 = random.nextInt(16) + 8;
        (new WorldGenLakes(Blocks.AIR)).generate(world, random, pos.add(i1, j1, k1));

        if (random.nextFloat() <= 0.70F) {
            new WorldGenPalm(true, 5, AtumBlocks.DEADWOOD_LOG.getDefaultState().withProperty(BlockDeadwood.HAS_SCARAB, true), BlockLeave.getLeave(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState().withProperty(BlockLeave.CHECK_DECAY, false), false).generate(world, random, world.getHeight(pos.add(x, 0, z)));
        }

        super.decorate(world, random, pos);
    }

    @Override
    public int getModdedBiomeFoliageColor(int original) {
        return 10189386;
    }

    @Override
    public int getModdedBiomeGrassColor(int original) {
        return 10189386;
    }
}