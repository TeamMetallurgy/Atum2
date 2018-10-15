package com.teammetallurgy.atum.world.gen.feature;

import com.teammetallurgy.atum.blocks.BlockStrangeSand;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenDeadwood extends WorldGenAbstractTree {
    private static final ResourceLocation TREES = new ResourceLocation(Constants.MOD_ID, "deadwoodtrees/deadwoodtree_");

    public WorldGenDeadwood(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        Random random = world.getChunk(pos).getRandomWithSeed(987234911L);
        MinecraftServer server = world.getMinecraftServer();
        Rotation[] rotations = Rotation.values();
        Rotation rotation = rotations[random.nextInt(rotations.length)];
        int amount = 17;

        TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
        Template template = manager.getTemplate(server, new ResourceLocation(TREES.toString() + String.valueOf(rand.nextInt(amount))));

        ChunkPos chunkPos = new ChunkPos(pos);
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(chunkPos.getXStart(), 0, chunkPos.getZStart(), chunkPos.getXEnd(), world.getHeight(), chunkPos.getZEnd());
        PlacementSettings settings = (new PlacementSettings()).setRotation(rotation).setBoundingBox(structureboundingbox).setRandom(random);
        BlockPos blockpos = template.transformedSize(rotation);
        int x = random.nextInt(16 - blockpos.getX());
        int z = random.nextInt(16 - blockpos.getZ());
        BlockPos placeCheck = pos.add(x, 0, z);

        while (placeCheck.getY() > 1 && world.isAirBlock(placeCheck.down())) {
            placeCheck = placeCheck.down();
        }

        while (!world.isAirBlock(placeCheck.up()) && (world.getBlockState(placeCheck).getBlock() != AtumBlocks.SAND)) {
            placeCheck = placeCheck.up();
        }


        if (world.canSeeSky(placeCheck) && (this.isReplaceable(world, placeCheck) || world.getBlockState(placeCheck.down()).getBlock() instanceof BlockStrangeSand)) {
            System.out.println("CanPlace");
            BlockPos zero = template.getZeroPositionWithTransform(placeCheck, Mirror.NONE, rotation);
            template.addBlocksToWorld(world, zero, settings, 20);
            return true;
        }
        return false;
    }
}