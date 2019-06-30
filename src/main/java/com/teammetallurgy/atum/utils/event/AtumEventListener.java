package com.teammetallurgy.atum.utils.event;

import com.teammetallurgy.atum.blocks.BlockPortal;
import com.teammetallurgy.atum.blocks.vegetation.BlockFertileSoil;
import com.teammetallurgy.atum.blocks.vegetation.BlockFertileSoilTilled;
import com.teammetallurgy.atum.entity.bandit.EntityBanditBase;
import com.teammetallurgy.atum.entity.stone.EntityStoneBase;
import com.teammetallurgy.atum.entity.undead.EntityPharaoh;
import com.teammetallurgy.atum.entity.undead.EntityUndeadBase;
import com.teammetallurgy.atum.entity.undead.EntityWraith;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.items.ItemTexturedArmor;
import com.teammetallurgy.atum.items.artifacts.atum.ItemAtumsBounty;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.teleporter.AtumStartTeleporter;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class AtumEventListener {

    private static final String TAG_ATUM_START = "atum_start";

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        NBTTagCompound tag = event.player.getEntityData();
        NBTTagCompound persistedTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        boolean shouldStartInAtum = AtumConfig.START_IN_ATUM && !persistedTag.getBoolean(TAG_ATUM_START);

        persistedTag.setBoolean(TAG_ATUM_START, true);
        tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistedTag);

        if (shouldStartInAtum && event.player instanceof EntityPlayerMP && event.player.world instanceof WorldServer) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            WorldServer world = (WorldServer) player.world;
            BlockPortal.changeDimension(world, player, AtumConfig.DIMENSION_ID, new AtumStartTeleporter());
        }
    }

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
    public static void onPlace(BlockEvent.PlaceEvent event) {
        Block block = event.getPlacedBlock().getBlock();
        if (event.getPlayer().world.provider.getDimension() == AtumConfig.DIMENSION_ID) {
            if ((block instanceof BlockDirt || block instanceof BlockGrass || block instanceof BlockMycelium || (block instanceof BlockFarmland && block != AtumBlocks.FERTILE_SOIL_TILLED))) {
                event.getWorld().setBlockState(event.getPos(), AtumBlocks.SAND.getDefaultState());
            }
        }
    }

    @SubscribeEvent
    public static void onArmorClean(PlayerInteractEvent.RightClickBlock event) {
        try {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            EntityPlayer player = event.getEntityPlayer();
            ItemStack stack = player.getHeldItemMainhand();
            IFluidHandler tank = FluidUtil.getFluidHandler(world, pos, event.getFace());

            if (stack.getItem() instanceof ItemTexturedArmor && ((ItemTexturedArmor) stack.getItem()).hasColor(stack)) {
                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() instanceof BlockCauldron) {
                    int level = state.getValue(BlockCauldron.LEVEL);
                    if (level > 0) {
                        if (!world.isRemote) {
                            ((ItemTexturedArmor) stack.getItem()).removeColor(stack);
                            player.addStat(StatList.ARMOR_CLEANED);
                            ((BlockCauldron) state.getBlock()).setWaterLevel(world, pos, state, level - 1);
                        }
                        player.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.16F, 0.66F);
                        event.setUseItem(Event.Result.DENY);
                    }
                } else if (tank instanceof FluidTank) {
                    FluidTank fluidTank = (FluidTank) tank;

                    if (fluidTank.getFluid() != null && fluidTank.getFluid().getFluid() == FluidRegistry.WATER && fluidTank.getFluidAmount() >= 250) {
                        if (!world.isRemote) {
                            ((ItemTexturedArmor) stack.getItem()).removeColor(stack);
                            player.addStat(StatList.ARMOR_CLEANED);
                            tank.drain(250, true);
                        }
                        player.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.16F, 0.66F);
                        event.setUseItem(Event.Result.ALLOW);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onSeedUse(PlayerInteractEvent.RightClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        World world = event.getWorld();
        if (player.world.provider.getDimension() == AtumConfig.DIMENSION_ID) {
            if (player.getHeldItem(event.getHand()).getItem() == Items.WHEAT_SEEDS && world.getBlockState(event.getPos()).getBlock() instanceof BlockFarmland) {
                event.setCanceled(true);
            }
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
        if (event.getSource().getDamageType().equals("drown") && (event.getEntity() instanceof EntityUndeadBase || event.getEntity() instanceof EntityStoneBase)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onFishLoot(ItemFishedEvent event) {
        World world = event.getEntityPlayer().world;
        EntityFishHook fishHook = event.getHookEntity();
        EntityPlayer angler = fishHook.getAngler();
        ItemStack heldStack = angler.getHeldItem(angler.getActiveHand());
        LootContext.Builder builder = new LootContext.Builder((WorldServer) world);
        builder.withLuck((float) EnchantmentHelper.getFishingLuckBonus(heldStack) + angler.getLuck()).withPlayer(angler).withLootedEntity(fishHook);
        if (world.provider.getDimension() == AtumConfig.DIMENSION_ID) {
            event.setCanceled(true); //We don't want vanillas loot table
            if (heldStack.getItem() instanceof ItemAtumsBounty) {
                catchFish(world, angler, fishHook, builder, AtumLootTables.ATUMS_BOUNTY);
                angler.world.spawnEntity(new EntityXPOrb(angler.world, angler.posX, angler.posY + 0.5D, angler.posZ + 0.5D, world.rand.nextInt(6) + 1));
            } else {
                catchFish(world, angler, fishHook, builder, AtumLootTables.FISHING);
            }
        }
    }

    private static void catchFish(World world, EntityPlayer angler, EntityFishHook fishHook, LootContext.Builder builder, ResourceLocation lootTable) {
        List<ItemStack> loots = world.getLootTableManager().getLootTableFromLocation(lootTable).generateLootForPools(world.rand, builder.build());
        for (ItemStack loot : loots) {
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

    @SubscribeEvent
    public static void onHoeEvent(UseHoeEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        boolean result = false;
        if (state.getBlock() instanceof BlockFertileSoil) {
            if (event.getCurrent().getItem() == AtumItems.TEFNUTS_BLESSING) {
                world.setBlockState(pos, AtumBlocks.FERTILE_SOIL_TILLED.getDefaultState().withProperty(BlockFertileSoilTilled.MOISTURE, 7).withProperty(BlockFertileSoilTilled.BLESSED, true));
            } else {
                world.setBlockState(pos, AtumBlocks.FERTILE_SOIL_TILLED.getDefaultState());
            }
            result = true;
        }

        if (result) {
            event.setResult(Event.Result.ALLOW);

            world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onSpawn(LivingSpawnEvent.CheckSpawn event) { //TODO Remove in 1.14. Not needed
        EntityLivingBase livingBase = event.getEntityLiving();
        if (event.isSpawner() && (livingBase instanceof EntityUndeadBase || livingBase instanceof EntityBanditBase)) {
            EntityMob mob = (EntityMob) livingBase;
            if (mob instanceof EntityUndeadBase) {
                EntityUndeadBase undeadBase = (EntityUndeadBase) mob;
                if (undeadBase.canSpawnNoHeightCheck(true)) {
                    event.setResult(Event.Result.ALLOW);
                }
            } else if (mob instanceof EntityBanditBase) {
                EntityBanditBase banditBase = (EntityBanditBase) mob;
                if (banditBase.canSpawnNoHeightCheck(true)) {
                    event.setResult(Event.Result.ALLOW);
                }
            }
        }
    }
}