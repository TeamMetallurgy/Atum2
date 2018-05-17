package com.teammetallurgy.atum.handler.event;

import com.teammetallurgy.atum.entity.*;
import com.teammetallurgy.atum.handler.AtumConfig;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.items.artifacts.ItemAnuketsBounty;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

@Mod.EventBusSubscriber
public class AtumEventListener {

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.world;
        if (!world.isRemote && AtumConfig.ALLOW_CREATION && event.phase == TickEvent.Phase.END && player.ticksExisted % 20 == 0) {
            if (world.provider.getDimension() == 0 || world.provider.getDimension() == AtumConfig.DIMENSION_ID) {
                for (EntityItem entityItem : world.getEntitiesWithinAABB(EntityItem.class, player.getEntityBoundingBox().grow(10.0F, 1.0F, 10.0F))) {
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
    public static void onFishLoot(ItemFishedEvent event) {
        World world = event.getEntityPlayer().world;
        EntityFishHook fishHook = event.getHookEntity();
        EntityPlayer angler = fishHook.getAngler();
        ItemStack heldStack = angler.getHeldItem(angler.getActiveHand());
        LootContext.Builder builder = new LootContext.Builder((WorldServer) world);
        builder.withLuck((float) EnchantmentHelper.getFishingLuckBonus(heldStack) + angler.getLuck()).withPlayer(angler).withLootedEntity(fishHook);
        if (world.provider.getDimension() == AtumConfig.DIMENSION_ID) {
            event.setCanceled(true); //We don't want any vanilla loot
            if (heldStack.getItem() instanceof ItemAnuketsBounty) {
                List<ItemStack> lootTable = world.getLootTableManager().getLootTableFromLocation(AtumLootTables.FISH).generateLootForPools(world.rand, builder.build());
                for (ItemStack loot : lootTable) {
                    EntityItem fish = new EntityItem(fishHook.world, fishHook.posX, fishHook.posY, fishHook.posZ, loot);
                    double x = angler.posX - fishHook.posX;
                    double y = angler.posY - fishHook.posY;
                    double z = angler.posZ - fishHook.posZ;
                    double swush = (double) MathHelper.sqrt(x * x + y * y + z * z);
                    fish.motionX = x * 0.1D;
                    fish.motionY = y * 0.1D + (double) MathHelper.sqrt(swush) * 0.08D;
                    fish.motionZ = z * 0.1D;
                    world.spawnEntity(fish);
                }
            }
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