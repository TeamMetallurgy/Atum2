/*
package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nonnull;
import java.util.Random;

public class StartStructureFeature extends Feature<NoneFeatureConfiguration> { //TODO

    public StartStructureFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull WorldGenLevel seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoneFeatureConfiguration config) {
        if (seedReader instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) seedReader;
            StructureManager manager = serverLevel.getStructureManager();
            StructureTemplate template = manager.get(new ResourceLocation(AtumConfig.ATUM_START.atumStartStructure.get()));

            if (template != null) {
                Rotation[] rotations = Rotation.values();
                Rotation rotation = rotations[rand.nextInt(rotations.length)];
                StructurePlaceSettings settings = (new StructurePlaceSettings()).setRotation(rotation).setRandom(rand);
                BlockPos rotatedPos = template.getSize(rotation);
                int x = rand.nextInt(rotatedPos.getX()) + template.getSize().getX();
                int z = rand.nextInt(rotatedPos.getZ()) + template.getSize().getZ();
                BlockPos posOffset = DimensionHelper.getSurfacePos(serverLevel, pos.offset(x, 0, z));

                template.placeInWorld(serverLevel, posOffset, posOffset.below(), settings, rand, 20);
                return true;
            } else {
                Atum.LOG.error(AtumConfig.ATUM_START.atumStartStructure.get() + " is not a valid structure");
                return false;
            }
        }
        return false;
    }
}*/
