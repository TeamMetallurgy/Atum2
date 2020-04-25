package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Function;

public class StartStructureFeature extends Feature<NoFeatureConfig> {

    public StartStructureFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull IWorld world, @Nonnull ChunkGenerator<? extends GenerationSettings> generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            TemplateManager manager = serverWorld.getSaveHandler().getStructureTemplateManager();
            Template template = manager.getTemplate(new ResourceLocation(AtumConfig.ATUM_START.atumStartStructure.get()));

            if (template != null) {
                Rotation[] rotations = Rotation.values();
                Rotation rotation = rotations[rand.nextInt(rotations.length)];
                PlacementSettings settings = (new PlacementSettings()).setRotation(rotation).setRandom(rand);
                BlockPos rotatedPos = template.transformedSize(rotation);
                int x = rand.nextInt(rotatedPos.getX()) + template.getSize().getX();
                int z = rand.nextInt(rotatedPos.getZ()) + template.getSize().getZ();
                BlockPos posOffset = pos.add(x, 0, z);

                while (posOffset.getY() > 1 && world.isAirBlock(posOffset.down())) {
                    posOffset = posOffset.down();
                }
                while (!world.isAirBlock(posOffset.up()) && (world.getBlockState(posOffset.down()).getBlock() != AtumBlocks.SAND || world.getBlockState(posOffset.down()).getBlock() != AtumBlocks.SAND_LAYERED) || posOffset.getY() < 60) {
                    posOffset = posOffset.up();
                }
                template.addBlocksToWorld(world, posOffset, settings, 20);
                return true;
            } else {
                Atum.LOG.error(AtumConfig.ATUM_START.atumStartStructure.get() + " is not a valid structure");
                return false;
            }
        }
        return false;
    }
}