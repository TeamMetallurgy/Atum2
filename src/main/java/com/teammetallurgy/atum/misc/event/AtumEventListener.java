package com.teammetallurgy.atum.misc.event;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.PortalBlock;
import com.teammetallurgy.atum.blocks.base.IUnbreakable;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.entity.undead.UndeadBaseEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.items.WandererDyeableArmor;
import com.teammetallurgy.atum.items.artifacts.atem.AtemsBountyItem;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.misc.StackHelper;
import com.teammetallurgy.atum.world.teleporter.TeleporterAtumStart;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumEventListener {
    private static final String TAG_ATUM_START = "atum_start";

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();
        CompoundTag tag = player.getPersistentData();
        CompoundTag persistedTag = tag.getCompound(Player.PERSISTED_NBT_TAG);
        boolean shouldStartInAtum = AtumConfig.ATUM_START.startInAtum.get() && !persistedTag.getBoolean(TAG_ATUM_START);

        persistedTag.putBoolean(TAG_ATUM_START, true);
        tag.put(Player.PERSISTED_NBT_TAG, persistedTag);

        if (shouldStartInAtum && player instanceof ServerPlayer && player.level instanceof ServerLevel) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            ServerLevel world = (ServerLevel) player.level;
            PortalBlock.changeDimension(world, serverPlayer, new TeleporterAtumStart());
            serverPlayer.setRespawnPosition(Atum.ATUM, serverPlayer.blockPosition(), serverPlayer.getYHeadRot(), true, false); //Set players spawn point in Atum, when starting in Atum
            world.setDefaultSpawnPos(serverPlayer.blockPosition(), 16);
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (AtumConfig.ATUM_START.startInAtum.get()) {
            LivingEntity livingEntity = event.getEntityLiving();
            if (livingEntity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer) livingEntity;
                ServerLevel serverWorld = serverPlayer.getLevel();
                BlockPos respawnPos = serverPlayer.getRespawnPosition();
                if (respawnPos != null) {
                    Optional<Vec3> bedPos = Player.findRespawnPositionAndUseSpawnBlock(serverWorld, respawnPos, serverPlayer.getRespawnAngle(), serverPlayer.isRespawnForced(), false);
                    if (!bedPos.isPresent()) {
                        serverPlayer.setRespawnPosition(Atum.ATUM, serverWorld.getSharedSpawnPos(), serverPlayer.getYHeadRot(), true, false); //Ensure that the player respawns in Atum, when bed is broken
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level world = player.level;
        if (!world.isClientSide && AtumConfig.GENERAL.allowCreation.get() && event.phase == TickEvent.Phase.END && player.tickCount % 20 == 0) {
            if (world.dimension() == Level.OVERWORLD || world.dimension() == Atum.ATUM) {
                for (ItemEntity entityItem : world.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(10.0F, 1.0F, 10.0F))) {
                    BlockState state = world.getBlockState(entityItem.blockPosition());
                    if (entityItem.getItem().getItem() == AtumItems.SCARAB && (state.getBlock() == Blocks.WATER || state == AtumBlocks.PORTAL.defaultBlockState())) {
                        if (AtumBlocks.PORTAL.trySpawnPortal(world, entityItem.blockPosition())) {
                            entityItem.remove();
                            return;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        BlockState state = event.getPlacedBlock();
        if (event.getEntity() != null && event.getEntity().level.dimension() == Atum.ATUM) {
            if (((state.getMaterial() == Material.DIRT || state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.MYCELIUM) && state.getBlock() != AtumBlocks.FERTILE_SOIL_TILLED)) {
                event.getWorld().setBlock(event.getPos(), AtumBlocks.SAND.defaultBlockState(), 3);
            }
        }
    }

    @SubscribeEvent
    public static void onArmorClean(PlayerInteractEvent.RightClickBlock event) {
        try {
            Level world = event.getWorld();
            BlockPos pos = event.getPos();
            Player player = event.getPlayer();
            ItemStack stack = player.getMainHandItem();
            LazyOptional<IFluidHandler> lazyTank = FluidUtil.getFluidHandler(world, pos, event.getFace());

            if (stack.getItem() instanceof WandererDyeableArmor && ((WandererDyeableArmor) stack.getItem()).hasCustomColor(stack)) {
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() instanceof CauldronBlock) {
                    int level = state.getValue(CauldronBlock.LEVEL);
                    if (level > 0) {
                        if (!world.isClientSide) {
                            ((WandererDyeableArmor) stack.getItem()).clearColor(stack);
                            player.awardStat(Stats.CLEAN_ARMOR);
                            ((CauldronBlock) state.getBlock()).setWaterLevel(world, pos, state, level - 1);
                        }
                        player.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.16F, 0.66F);
                        event.setUseItem(Event.Result.DENY);
                    }
                } else if (lazyTank.map(f -> f instanceof FluidTank).orElse(false)) {
                    lazyTank.ifPresent(tank -> {
                                FluidTank fluidTank = (FluidTank) tank;

                                if (fluidTank.getFluid().getFluid().is(FluidTags.WATER) && fluidTank.getFluidAmount() >= 250) {
                                    if (!world.isClientSide) {
                                        ((WandererDyeableArmor) stack.getItem()).clearColor(stack);
                                        player.awardStat(Stats.CLEAN_ARMOR);
                                        tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
                                    }
                                    player.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.16F, 0.66F);
                                    event.setUseItem(Event.Result.ALLOW);
                                }
                            }
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onSeedUse(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        Level world = event.getWorld();
        if (player.level.dimension() == Atum.ATUM) {
            if (player.getItemInHand(event.getHand()).getItem() == Items.WHEAT_SEEDS && world.getBlockState(event.getPos()).getBlock() instanceof FarmBlock) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().equals(DamageSource.DROWN) && (event.getEntity() instanceof UndeadBaseEntity || event.getEntity() instanceof StoneBaseEntity)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onFishLoot(ItemFishedEvent event) {
        Level world = event.getPlayer().level;
        FishingHook bobber = event.getHookEntity();
        Player angler = bobber.getPlayerOwner();
        if (angler != null) {
            ItemStack heldStack = angler.getItemInHand(angler.getUsedItemHand());
            LootContext.Builder builder = new LootContext.Builder((ServerLevel) world);
            builder.withLuck((float) EnchantmentHelper.getFishingLuckBonus(heldStack) + angler.getLuck()).withParameter(LootContextParams.KILLER_ENTITY, angler).withParameter(LootContextParams.THIS_ENTITY, bobber);
            if (world.dimension() == Atum.ATUM) {
                event.setCanceled(true); //We don't want vanillas loot table
                if (heldStack.getItem() instanceof AtemsBountyItem) {
                    catchFish((ServerLevel) world, angler, heldStack, bobber, builder, AtumLootTables.ATEMS_BOUNTY);
                    angler.level.addFreshEntity(new ExperienceOrb(angler.level, angler.getX(), angler.getY() + 0.5D, angler.getZ() + 0.5D, world.random.nextInt(6) + 1));
                } else {
                    catchFish((ServerLevel) world, angler, heldStack, bobber, builder, AtumLootTables.FISHING);
                }
            }
        }
    }

    private static void catchFish(ServerLevel world, Player angler, ItemStack heldStack, FishingHook bobber, LootContext.Builder builder, ResourceLocation lootTable) {
        List<ItemStack> loots = world.getServer().getLootTables().get(lootTable).getRandomItems(builder.withParameter(LootContextParams.ORIGIN, bobber.position()).withParameter(LootContextParams.TOOL, heldStack).withParameter(LootContextParams.THIS_ENTITY, bobber).withRandom(world.random).create(LootContextParamSets.FISHING));
        for (ItemStack loot : loots) {
            ItemEntity fish = new ItemEntity(bobber.level, bobber.getX(), bobber.getY(), bobber.getZ(), loot);
            double x = angler.getX() - bobber.getX();
            double y = angler.getY() - bobber.getY();
            double z = angler.getZ() - bobber.getZ();
            double swush = Mth.sqrt(x * x + y * y + z * z);
            fish.setDeltaMovement(x * 0.1D, y * 0.1D + swush * 0.08D, z * 0.1D);
            world.addFreshEntity(fish);
        }
    }

    @SubscribeEvent
    public static void checkSpawn(LivingSpawnEvent.CheckSpawn event) { //Prevent Phantom spawning in Atum
        LevelAccessor world = event.getWorld();
        if ((event.getEntityLiving() instanceof Phantom || event.getEntityLiving().getType() == EntityType.CAT) && (world instanceof ServerLevel && ((ServerLevel) world).dimension() == Atum.ATUM)) {
            event.setResult(Event.Result.DENY);
        }
    }

    //IUnbreakable
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockState state = event.getState();
        if (state.getBlock() instanceof IUnbreakable && state.getValue(IUnbreakable.UNBREAKABLE) && !event.getPlayer().isCreative()) {
            event.setCanceled(true);
        }
    }

    //Ra Armor
    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        if (StackHelper.hasFullArmorSet(event.getEntityLiving(), AtumItems.HALO_OF_RA, AtumItems.BODY_OF_RA, AtumItems.LEGS_OF_RA, AtumItems.FEET_OF_RA) && event.getSource().isFire()) {
            event.setAmount(0.0F);
        }
    }
}