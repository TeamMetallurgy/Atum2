package com.teammetallurgy.atum.world;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class DimensionHelper {
    public static final List<Block> SURFACE_BLOCKS = Lists.newArrayList(AtumBlocks.SAND.get(), AtumBlocks.FERTILE_SOIL.get(), AtumBlocks.LIMESTONE_GRAVEL.get());
    public static final int GROUND_LEVEL = 63;

    public static AtumDimensionData getData(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(AtumDimensionData::load, AtumDimensionData::new, AtumDimensionData.ID);
    }

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        LevelAccessor world = event.getWorld();
        if (world instanceof ServerLevel serverLevel) {
            if (serverLevel.dimension() == Atum.ATUM) {
                if (world.getLevelData() instanceof DerivedLevelData) {
                    ((DerivedLevelData) world.getLevelData()).wrapped.setDayTime(event.getNewTime()); //Workaround for making sleeping work in Atum
                }
            }
        }
    }

    public static int getSkyColorWithTemperatureModifier(float temperature) {
        float f = temperature / 3.0F;
        f = Mth.clamp(f, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }

    public static boolean canPlaceSandLayer(ServerLevel world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockState stateDown = world.getBlockState(pos.below());
        Optional<ResourceKey<Biome>> biomeKey = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getResourceKey(world.getBiome(pos).value());
        return (biomeKey.isPresent() && biomeKey.get() != AtumBiomes.OASIS)
                //&& !StructureHelper.doesChunkHaveStructure(world, pos, AtumStructures.GENERIC_VILLAGE) //TODO Re-add ones structures are in
                && world.isEmptyBlock(pos.above())
                && state.getMaterial().isReplaceable()
                && stateDown.getBlock() != AtumBlocks.LIMESTONE_CRACKED.get()
                && Block.canSupportRigidBlock(world, pos.below())
                && !(stateDown.getBlock() instanceof SandLayersBlock)
                && !(state.getBlock() instanceof SandLayersBlock);
    }

    /**
     * Only use when world#getHeight is not working
     *
     * @param world the world
     * @param pos original pos
     * @return surface pos
     */
    public static BlockPos getSurfacePos(Level world, BlockPos pos) {
        while (pos.getY() > 1 && world.isEmptyBlock(pos.below())) {
            pos = pos.below();
        }
        while (!world.isEmptyBlock(pos.above()) && (SURFACE_BLOCKS.contains(world.getBlockState(pos.below()).getBlock()) || world.getBlockState(pos.below()).getBlock() != AtumBlocks.SAND_LAYERED.get()) || pos.getY() < 60) {
            pos = pos.above();
        }
        return pos;
    }

    public static boolean isBeatenPyramid(ServerLevel serverLevel, BoundingBox box2) {
        boolean validBox = false;
        for (BoundingBox box1 : getData(serverLevel).getBeatenPyramids()) {
            if (box1.minX() == box2.minX() && box1.minY() == box2.minY() && box1.minZ() == box2.minZ() && box1.maxX() == box2.maxX() && box1.maxY() == box2.maxY() && box1.maxZ() == box2.maxZ()) {
                validBox = true;
                break;
            }
        }
        return validBox;
    }
}