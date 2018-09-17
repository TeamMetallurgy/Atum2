package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenFossil extends WorldGenerator {
    private static final ResourceLocation FOSSIL = new ResourceLocation(Constants.MOD_ID, "fossils/fossil_");

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        Random random = world.getChunk(pos).getRandomWithSeed(987234911L);
        MinecraftServer server = world.getMinecraftServer();
        Rotation[] rotations = Rotation.values();
        Rotation rotation = rotations[random.nextInt(rotations.length)];
        int size = random.nextInt(16);
        TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
        Template template = manager.getTemplate(server, new ResourceLocation(FOSSIL.toString() + "1"));
        for (int i = 0; i <= size; ++i) {
            template = manager.getTemplate(server, new ResourceLocation(FOSSIL.toString() + String.valueOf(i)));
        }
        ChunkPos chunkPos = new ChunkPos(pos);
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(chunkPos.getXStart(), 0, chunkPos.getZStart(), chunkPos.getXEnd(), 256, chunkPos.getZEnd());
        PlacementSettings settings = (new PlacementSettings()).setRotation(rotation).setBoundingBox(structureboundingbox).setRandom(random);
        BlockPos blockpos = template.transformedSize(rotation);
        int j = random.nextInt(16 - blockpos.getX());
        int k = random.nextInt(16 - blockpos.getZ());
        int l = 256;

        for (int i1 = 0; i1 < blockpos.getX(); ++i1) {
            for (int j1 = 0; j1 < blockpos.getX(); ++j1) {
                l = Math.min(l, world.getHeight(pos.getX() + i1 + j, pos.getZ() + j1 + k));
            }
        }

        int k1 = Math.max(l - 15 - random.nextInt(10), 10);
        BlockPos zero = template.getZeroPositionWithTransform(pos.add(j, k1, k), Mirror.NONE, rotation);
        settings.setIntegrity(0.8F);
        template.addBlocksToWorld(world, zero, settings, 20);
        return true;
    }
}