package com.teammetallurgy.atum.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import javax.annotation.Nonnull;
import java.util.Optional;

public class StartStructureFeature extends Feature<NoneFeatureConfiguration> {

    public StartStructureFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> placeContext) {
        WorldGenLevel genLevel = placeContext.level();
        ServerLevel serverLevel = genLevel.getLevel();
        RandomSource random = placeContext.random();
        BlockPos pos = placeContext.origin();

        StructureTemplateManager manager = serverLevel.getStructureManager();
        Optional<StructureTemplate> optionalTemplate = manager.get(new ResourceLocation(AtumConfig.ATUM_START.atumStartStructure.get()));

        if (optionalTemplate.isPresent()) {
            StructureTemplate template = optionalTemplate.get();
            Rotation[] rotations = Rotation.values();
            Rotation rotation = rotations[random.nextInt(rotations.length)];
            StructurePlaceSettings settings = (new StructurePlaceSettings()).setRotation(rotation).setRandom(random);
            Vec3i rotatedPos = template.getSize(rotation);
            int x = random.nextInt(rotatedPos.getX()) + template.getSize().getX();
            int z = random.nextInt(rotatedPos.getZ()) + template.getSize().getZ();
            BlockPos posOffset = DimensionHelper.getSurfacePos(serverLevel, pos.offset(x, 0, z));

            template.placeInWorld(serverLevel, posOffset, posOffset.below(), settings, random, 20);
            return true;
        } else {
            Atum.LOG.error(AtumConfig.ATUM_START.atumStartStructure.get() + " is not a valid structure");
            return false;
        }
    }
}