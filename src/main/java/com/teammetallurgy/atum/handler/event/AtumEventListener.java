package com.teammetallurgy.atum.handler.event;

import com.teammetallurgy.atum.entity.*;
import com.teammetallurgy.atum.handler.AtumConfig;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class AtumEventListener {

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.world;
        if (!world.isRemote && AtumConfig.ALLOW_CREATION && event.phase == TickEvent.Phase.END && player.ticksExisted % 40 == 0) {
            if (world.provider.getDimension() == 0 || world.provider.getDimension() == AtumConfig.DIMENSION_ID) {
                for (EntityItem entityItem : world.getEntitiesWithinAABB(EntityItem.class, player.getEntityBoundingBox().grow(3.0F, 1.0F, 3.0F))) {
                    IBlockState state = world.getBlockState(entityItem.getPosition());
                    if (entityItem.getItem().getItem() == AtumItems.SCARAB && (state.getBlock() == Blocks.WATER || state == AtumBlocks.PORTAL.getDefaultState())) {
                        if (AtumBlocks.PORTAL.trySpawnPortal(world, entityItem.getPosition())) {
                            entityItem.setDead();
                            return;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDirtPlacing(BlockEvent.PlaceEvent event) {
        Block block = event.getPlacedBlock().getBlock();
        if (event.getPlayer().world.provider.getDimension() == AtumConfig.DIMENSION_ID && (block instanceof BlockDirt || block instanceof BlockGrass || block instanceof BlockMycelium || (block instanceof BlockFarmland && block != AtumBlocks.FERTILE_SOIL_TILLED))) {
            event.getWorld().setBlockState(event.getPos(), AtumBlocks.SAND.getDefaultState());
        }
    }

    @SubscribeEvent
    public static void onFallDamage(LivingFallEvent event) {
        if (event.getEntity() instanceof EntityWraith || event.getEntity() instanceof EntityPharaoh) {
            event.setDistance(0.0F);
        }

    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getDamageType().equals("drown") && (event.getEntity() instanceof EntityPharaoh || event.getEntity() instanceof EntityWraith || event.getEntity() instanceof EntityMummy || event.getEntity() instanceof EntityForsaken || event.getEntity() instanceof EntityStoneguard)) {
            event.setCanceled(true);
        }
    }

    // TODO: check if needed
    /*
    @SubscribeEvent
    public static boolean onBonemeal(BonemealEvent event) {
        if (!event.getWorld().isRemote) {

            Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
            if (block instanceof BlockAtumSapling) {
                if (event.getWorld().rand.nextInt(7) == 0) {
                    ((BlockAtumSapling) AtumBlocks.SAPLING).generateTree(event.getWorld(), event.getPos(), event.getWorld().getBlockState(event.getPos()), new Random());
                }
                event.setResult(Event.Result.ALLOW);
            }
            return false;
        }
        return true;
    }
    */

    @SubscribeEvent
    public static void onFishEvent(EntityJoinWorldEvent event) {
        if (event.getEntity().world.provider.getDimension() == AtumConfig.DIMENSION_ID && event.getEntity() instanceof EntityFishHook) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static boolean onHoeEvent(UseHoeEvent event) {
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        if (block == AtumBlocks.FERTILE_SOIL) {
            byte block2 = 0;
            if (event.getCurrent().getItem() == AtumItems.GEBS_BLESSING) {
                block2 = 4;
            }

            event.getWorld().setBlockState(event.getPos(), AtumBlocks.FERTILE_SOIL_TILLED.getStateFromMeta(block2), 2);
            event.setResult(Event.Result.ALLOW);
            event.getWorld().playSound(event.getEntityPlayer(), event.getPos(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return true;
        } else if ((block == Blocks.DIRT || block == Blocks.GRASS) && event.getCurrent().getItem() == AtumItems.GEBS_BLESSING) {
            event.getWorld().setBlockState(event.getPos(), AtumBlocks.FERTILE_SOIL_TILLED.getStateFromMeta(12), 2);
            event.setResult(Event.Result.ALLOW);
            event.getWorld().playSound(event.getEntityPlayer(), event.getPos(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return true;
        }
        return false;
    }
}