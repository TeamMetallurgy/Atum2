package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.AtumConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenStartStructure extends WorldGenerator {

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
        Template template = manager.get(world.getInstanceServer(), new ResourceLocation(AtumConfig.ATUM_START.atumStartStructure.get()));

        if (template != null) {
            Random random = world.getChunk(pos).getRandomWithSeed(955718210L);
            Rotation[] rotations = Rotation.values();
            Rotation rotation = rotations[random.nextInt(rotations.length)];
            PlacementSettings settings = (new PlacementSettings()).setRotation(rotation).setRandom(random);
            BlockPos rotatedPos = template.transformedSize(rotation);
            int x = random.nextInt(rotatedPos.getX()) + template.getSize().getX();
            int z = random.nextInt(rotatedPos.getZ()) + template.getSize().getZ();
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
}