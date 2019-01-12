package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.world.gen.feature.WorldGenBonusCrate;
import com.teammetallurgy.atum.world.gen.feature.WorldGenStartStructure;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class AtumStartTeleporter implements ITeleporter {
    private static boolean hasStartStructureSpawned;
    private static WorldSettings worldSettings;

    public AtumStartTeleporter(WorldServer world) {
        System.out.println(world.getWorldInfo().getDimensionData(world.provider.getDimension()));
        NBTTagCompound tagCompound = world.getWorldInfo().getDimensionData(world.provider.getDimension()).getCompoundTag("StartStructure");
        System.out.println("Tag: " + tagCompound);
        if (tagCompound.hasKey("HasStartStructureSpawned")) {
            hasStartStructureSpawned = tagCompound.getBoolean("HasStartStructureSpawned");
            System.out.println("StartStructureSpawned hasKey in constructor" + " Value: " + hasStartStructureSpawned);

        }
    }

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

        System.out.println("Place Entity");
        if (!hasStartStructureSpawned) {
            System.out.println("onAtumJoining called");
            this.onAtumJoining(world, pos);
            hasStartStructureSpawned = true;
        }
    }

    private void onAtumJoining(World world, BlockPos pos) {
        if (world.provider.getDimension() == AtumConfig.DIMENSION_ID) {
            if (AtumConfig.START_IN_ATUM_PORTAL) {
                System.out.println("Create start portal");
                AtumTeleporter.createPortal(world, pos, null);
            }
            if (!AtumConfig.ATUM_START_STRUCTURE.isEmpty()) {
                System.out.println("Start structure");
                new WorldGenStartStructure().generate(world, world.rand, pos.down());
            }
            if (worldSettings != null && worldSettings.isBonusChestEnabled()) {
                System.out.println("Bonus chest");
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

    public static NBTTagCompound writeToNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        System.out.println("Write to NBT");
        tagCompound.setBoolean("HasStartStructureSpawned", hasStartStructureSpawned);
        return tagCompound;
    }

    @SubscribeEvent
    public static void onCreateSpawnPos(WorldEvent.CreateSpawnPosition event) {
        worldSettings = event.getSettings();
    }
}