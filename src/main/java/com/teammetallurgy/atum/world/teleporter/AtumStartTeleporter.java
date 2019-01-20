package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.WorldProviderAtum;
import com.teammetallurgy.atum.world.gen.feature.WorldGenBonusCrate;
import com.teammetallurgy.atum.world.gen.feature.WorldGenStartStructure;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class AtumStartTeleporter implements ITeleporter {
    private static WorldSettings worldSettings;

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        BlockPos pos = new BlockPos(MathHelper.floor(entity.posX), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ));
        while (pos.getY() > 1 && world.isAirBlock(pos.down())) {
            pos = pos.down();
        }
        while (!world.isAirBlock(pos.up()) && (world.getBlockState(pos.down()).getBlock() != AtumBlocks.SAND || world.getBlockState(pos.down()).getBlock() != AtumBlocks.SAND_LAYERED)) {
            pos = pos.up();
        }
        entity.moveToBlockPosAndAngles(pos.up(), yaw, entity.rotationPitch);

        WorldProviderAtum atum = (WorldProviderAtum) world.provider;
        if (!atum.hasStartStructureSpawned) {
            this.onAtumJoining(world, pos);
            atum.hasStartStructureSpawned = true;
        }
    }

    private void onAtumJoining(World world, BlockPos pos) {
        if (world.provider.getDimension() == AtumConfig.DIMENSION_ID) {
            if (AtumConfig.START_IN_ATUM_PORTAL) {
                AtumTeleporter.createPortal(world, pos, null);
            }
            if (!AtumConfig.ATUM_START_STRUCTURE.isEmpty()) {
                new WorldGenStartStructure().generate(world, world.rand, pos.down());
            }
            if (worldSettings != null && worldSettings.isBonusChestEnabled()) {
                WorldGenBonusCrate bonusCrate = new WorldGenBonusCrate();
                for (int i = 0; i < 10; ++i) {
                    int j = world.rand.nextInt(6) - world.rand.nextInt(6);
                    int k = world.rand.nextInt(6) - world.rand.nextInt(6);
                    BlockPos posTop = world.getTopSolidOrLiquidBlock(pos.add(j, 0, k)).up();
                    if (bonusCrate.generate(world, world.rand, posTop)) {
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCreateSpawnPos(WorldEvent.CreateSpawnPosition event) {
        worldSettings = event.getSettings();
    }
}