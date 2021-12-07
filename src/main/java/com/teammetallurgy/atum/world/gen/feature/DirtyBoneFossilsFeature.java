/*
package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import javax.annotation.Nonnull;
import java.util.Random;

public class DirtyBoneFossilsFeature extends Feature<NoneFeatureConfiguration> { //TODO
    private static final int FOSSIL_AMOUNT = 15;

    public DirtyBoneFossilsFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull WorldGenLevel seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoneFeatureConfiguration config) {
        Rotation[] rotations = Rotation.values();
        Rotation rotation = rotations[rand.nextInt(rotations.length)];
        int size = rand.nextInt(FOSSIL_AMOUNT) + 1;
        StructureManager manager = seedReader.getLevel().getServer().getStructureManager();
        StructureTemplate template = manager.getOrCreate(new ResourceLocation(Atum.MOD_ID, "fossils/fossil_" + size));
        ChunkPos chunkPos = new ChunkPos(pos);
        BoundingBox box = new BoundingBox(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ(), chunkPos.getMaxBlockX(), 256, chunkPos.getMaxBlockZ());
        StructurePlaceSettings settings = (new StructurePlaceSettings()).setRotation(rotation).setBoundingBox(box).setRandom(rand).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        BlockPos transformedPos = template.getSize(rotation);
        int x = rand.nextInt(16 - transformedPos.getX());
        int z = rand.nextInt(16 - transformedPos.getZ());
        int height = 256;

        for (int x1 = 0; x1 < transformedPos.getX(); ++x1) {
            for (int z2 = 0; z2 < transformedPos.getZ(); ++z2) {
                height = Math.min(height, seedReader.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX() + x1 + x, pos.getZ() + z2 + z));
            }
        }

        int y = Math.max(height - 15 - rand.nextInt(10), 10);
        BlockPos genPos = template.getZeroPositionWithTransform(pos.offset(x, y, z), Mirror.NONE, rotation);
        BlockRotProcessor integrity = new BlockRotProcessor(0.8F);
        settings.clearProcessors().addProcessor(integrity);
        template.placeInWorld(seedReader, genPos, genPos, settings, rand, 4);
        return true;
    }
}*/
