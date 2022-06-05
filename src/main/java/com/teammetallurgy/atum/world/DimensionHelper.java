package com.teammetallurgy.atum.world;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class DimensionHelper {
    public static final List<Block> SURFACE_BLOCKS = Lists.newArrayList(AtumBlocks.SAND, AtumBlocks.FERTILE_SOIL, AtumBlocks.LIMESTONE_GRAVEL);
    public static final int GROUND_LEVEL = 63;

    public static AtumDimensionData getData(ServerWorld serverWorld) {
        return serverWorld.getSavedData().getOrCreate(AtumDimensionData::new, AtumDimensionData.ID);
    }

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        IWorld world = event.getWorld();
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            if (serverWorld.getDimensionKey() == Atum.ATUM) {
                if (world.getWorldInfo() instanceof DerivedWorldInfo) {
                    ((DerivedWorldInfo) world.getWorldInfo()).delegate.setDayTime(event.getNewTime()); //Workaround for making sleeping work in Atum
                }
            }
        }
    }

    public static int getSkyColorWithTemperatureModifier(float temperature) {
        float f = temperature / 3.0F;
        f = MathHelper.clamp(f, -1.0F, 1.0F);
        return MathHelper.hsvToRGB(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }

    public static boolean canPlaceSandLayer(ISeedReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockState stateDown = world.getBlockState(pos.down());
        Optional<RegistryKey<Biome>> biomeKey = world.func_241828_r().getRegistry(Registry.BIOME_KEY).getOptionalKey(world.getBiome(pos));
        return (biomeKey.isPresent() && biomeKey.get() != AtumBiomes.OASIS)
                && !StructureHelper.doesChunkHaveStructure(world, pos, Structure.VILLAGE)
                && world.isAirBlock(pos.up())
                && state.getMaterial().isReplaceable()
                && stateDown.getBlock() != AtumBlocks.LIMESTONE_CRACKED
                && Block.hasSolidSideOnTop(world, pos.down())
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
    public static BlockPos getSurfacePos(World world, BlockPos pos) {
        while (pos.getY() > 1 && world.isAirBlock(pos)) {
            pos = pos.down();
        }
        while (!world.canBlockSeeSky(pos)) {
            pos = pos.up();
        }
        return pos;
    }



    public static boolean isBeatenPyramid(ServerWorld serverWorld, MutableBoundingBox box2) {
        boolean validBox = false;
        for (MutableBoundingBox box1 : getData(serverWorld).getBeatenPyramids()) {
            if (box1.minX == box2.minX && box1.minY == box2.minY && box1.minZ == box2.minZ && box1.maxX == box2.maxX && box1.maxY == box2.maxY && box1.maxZ == box2.maxZ) {
                validBox = true;
                break;
            }
        }
        return validBox;
    }
}