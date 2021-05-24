package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.*;

import javax.annotation.Nonnull;
import java.util.Random;

public class DirtyBoneFossilsFeature extends Feature<NoFeatureConfig> {
    private static final int FOSSIL_AMOUNT = 16;

    public DirtyBoneFossilsFeature(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(@Nonnull ISeedReader seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        Rotation[] rotations = Rotation.values();
        Rotation rotation = rotations[rand.nextInt(rotations.length)];
        int size = rand.nextInt(FOSSIL_AMOUNT);
        TemplateManager manager = seedReader.getWorld().getServer().getTemplateManager();
        Template template = manager.getTemplateDefaulted(new ResourceLocation(Atum.MOD_ID, "fossils/fossil_" + size));
        ChunkPos chunkPos = new ChunkPos(pos);
        MutableBoundingBox box = new MutableBoundingBox(chunkPos.getXStart(), 0, chunkPos.getZStart(), chunkPos.getXEnd(), 256, chunkPos.getZEnd());
        PlacementSettings settings = (new PlacementSettings()).setRotation(rotation).setBoundingBox(box).setRandom(rand).addProcessor(BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK);
        BlockPos transformedPos = template.transformedSize(rotation);
        int x = rand.nextInt(16 - transformedPos.getX());
        int z = rand.nextInt(16 - transformedPos.getZ());
        int height = 256;

        for (int x1 = 0; x1 < transformedPos.getX(); ++x1) {
            for (int z2 = 0; z2 < transformedPos.getZ(); ++z2) {
                height = Math.min(height, seedReader.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + x1 + x, pos.getZ() + z2 + z));
            }
        }

        int y = Math.max(height - 15 - rand.nextInt(10), 10);
        BlockPos genPos = template.getZeroPositionWithTransform(pos.add(x, y, z), Mirror.NONE, rotation);
        IntegrityProcessor integrity = new IntegrityProcessor(0.8F);
        settings.clearProcessors().addProcessor(integrity);
        template.func_237146_a_(seedReader, genPos, genPos, settings, rand, 4);
        return true;
    }
}