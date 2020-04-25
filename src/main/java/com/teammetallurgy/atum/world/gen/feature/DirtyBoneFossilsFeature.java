package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.teammetallurgy.atum.Atum;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.*;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Function;

public class DirtyBoneFossilsFeature extends Feature<NoFeatureConfig> {
    private static final int FOSSIL_AMOUNT = 16;

    public DirtyBoneFossilsFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull IWorld world, @Nonnull ChunkGenerator<? extends GenerationSettings> generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        Rotation[] rotations = Rotation.values();
        Rotation rotation = rotations[rand.nextInt(rotations.length)];
        int size = rand.nextInt(FOSSIL_AMOUNT);
        TemplateManager manager = ((ServerWorld) world.getWorld()).getSaveHandler().getStructureTemplateManager();
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
                height = Math.min(height, world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + x1 + x, pos.getZ() + z2 + z));
            }
        }

        int y = Math.max(height - 15 - rand.nextInt(10), 10);
        BlockPos genPos = template.getZeroPositionWithTransform(pos.add(x, y, z), Mirror.NONE, rotation);
        IntegrityProcessor integrity = new IntegrityProcessor(0.8F);
        settings.clearProcessors().addProcessor(integrity);
        template.addBlocksToWorld(world, genPos, settings, 4);
        return true;
    }
}