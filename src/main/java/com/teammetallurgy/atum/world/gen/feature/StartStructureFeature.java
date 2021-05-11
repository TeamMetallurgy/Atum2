package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;

public class StartStructureFeature extends Feature<NoFeatureConfig> {

    public StartStructureFeature(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(@Nonnull ISeedReader seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        if (seedReader instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) seedReader;
            TemplateManager manager = serverWorld.getStructureTemplateManager();
            Template template = manager.getTemplate(new ResourceLocation(AtumConfig.ATUM_START.atumStartStructure.get()));

            if (template != null) {
                Rotation[] rotations = Rotation.values();
                Rotation rotation = rotations[rand.nextInt(rotations.length)];
                PlacementSettings settings = (new PlacementSettings()).setRotation(rotation).setRandom(rand);
                BlockPos rotatedPos = template.transformedSize(rotation);
                int x = rand.nextInt(rotatedPos.getX()) + template.getSize().getX();
                int z = rand.nextInt(rotatedPos.getZ()) + template.getSize().getZ();
                BlockPos posOffset = DimensionHelper.getSurfacePos(serverWorld, pos.add(x, 0, z));

                template.func_237146_a_(serverWorld, posOffset, posOffset.down(), settings, rand, 20);
                return true;
            } else {
                Atum.LOG.error(AtumConfig.ATUM_START.atumStartStructure.get() + " is not a valid structure");
                return false;
            }
        }
        return false;
    }
}