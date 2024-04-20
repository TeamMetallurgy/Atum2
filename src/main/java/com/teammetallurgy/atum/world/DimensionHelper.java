package com.teammetallurgy.atum.world;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class DimensionHelper {

    public static SavedData.Factory<AtumDimensionData> factory() {
        return new SavedData.Factory<>(AtumDimensionData::new, AtumDimensionData::load);
    }

    public static AtumDimensionData getData(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(factory(), AtumDimensionData.ID);
    }

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        LevelAccessor level = event.getLevel();
        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.dimension() == Atum.ATUM) {
                if (level.getLevelData() instanceof DerivedLevelData) {
                    ((DerivedLevelData) level.getLevelData()).wrapped.setDayTime(event.getNewTime()); //Workaround for making sleeping work in Atum
                }
            }
        }
    }

    public static int getSkyColorWithTemperatureModifier(float temperature) {
        float f = temperature / 3.0F;
        f = Mth.clamp(f, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }

    public static boolean canPlaceSandLayer(WorldGenLevel genLevel, BlockPos pos) {
        BlockState state = genLevel.getBlockState(pos);
        BlockState stateDown = genLevel.getBlockState(pos.below());
        Optional<ResourceKey<Biome>> biomeKey = genLevel.registryAccess().registryOrThrow(Registries.BIOME).getResourceKey(genLevel.getBiome(pos).value());
        return (biomeKey.isPresent() && biomeKey.get() != AtumBiomes.OASIS)
                //&& !StructureHelper.doesChunkHaveStructure(level, pos, AtumStructures.GENERIC_VILLAGE) //TODO Re-add ones Atum villages are in
                && (genLevel.isEmptyBlock(pos.above()) || state.is(BlockTags.REPLACEABLE))
                && stateDown.getBlock() != AtumBlocks.LIMESTONE_CRACKED.get()
                && Block.canSupportRigidBlock(genLevel, pos.below())
                && !(stateDown.getBlock() instanceof SandLayersBlock)
                && !(state.getBlock() instanceof SandLayersBlock);
    }

    /**
     * Only use when level#getHeight is not working
     *
     * @param level the level
     * @param pos original pos
     * @return surface pos
     */
    public static BlockPos getSurfacePos(Level level, BlockPos pos) {
        while (pos.getY() > 1 && level.isEmptyBlock(pos.below())) {
            pos = pos.below();
        }
        while (!level.canSeeSky(pos)) {
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