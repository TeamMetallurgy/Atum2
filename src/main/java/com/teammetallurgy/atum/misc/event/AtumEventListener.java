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
import com.teammetallurgy.atum.misc.SpawnHelper;
import com.teammetallurgy.atum.world.teleporter.TeleporterAtumStart;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumEventListener {
    private static final String TAG_ATUM_START = "atum_start";

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        CompoundTag tag = player.getPersistentData();
        CompoundTag persistedTag = tag.getCompound(Player.PERSISTED_NBT_TAG);
        boolean shouldStartInAtum = AtumConfig.ATUM_START.startInAtum.get() && !persistedTag.getBoolean(TAG_ATUM_START);

        persistedTag.putBoolean(TAG_ATUM_START, true);
        tag.put(Player.PERSISTED_NBT_TAG, persistedTag);

        if (shouldStartInAtum && player instanceof ServerPlayer serverPlayer && player.level instanceof ServerLevel level) {
            PortalBlock.changeDimension(level, serverPlayer, new TeleporterAtumStart());
            SpawnHelper.validateAndGetSpawnPoint(level, serverPlayer, 0);
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (AtumConfig.ATUM_START.startInAtum.get()) {
            LivingEntity livingEntity = event.getEntity();
            if (livingEntity instanceof ServerPlayer serverPlayer) {
                ServerLevel serverLevel = serverPlayer.getLevel();
                SpawnHelper.validateAndGetSpawnPoint(serverLevel, serverPlayer, 1);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            CompoundTag tag = serverPlayer.getPersistentData();
            CompoundTag persistedTag = tag.getCompound(Player.PERSISTED_NBT_TAG);
            if (persistedTag.getBoolean(SpawnHelper.TAG_ATUM_RESPAWN)) {
                SpawnHelper.sendBedMissingMsg(serverPlayer, 2);
                persistedTag.remove(SpawnHelper.TAG_ATUM_RESPAWN);
                tag.put(Player.PERSISTED_NBT_TAG, persistedTag);
            }
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level;
        if (!level.isClientSide && AtumConfig.GENERAL.allowCreation.get() && event.phase == TickEvent.Phase.END && player.tickCount % 20 == 0) {
            if (level.dimension() == Level.OVERWORLD || level.dimension() == Atum.ATUM) {
                for (ItemEntity entityItem : level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(10.0F, 1.0F, 10.0F))) {
                    BlockState state = level.getBlockState(entityItem.blockPosition());
                    if (entityItem.getItem().getItem() == AtumItems.SCARAB.get() && (state.getBlock() == Blocks.WATER || state == AtumBlocks.PORTAL.get().defaultBlockState())) {
                        if (((PortalBlock) AtumBlocks.PORTAL.get()).trySpawnPortal(level, entityItem.blockPosition())) {
                            entityItem.discard();
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
            if (((state.getMaterial() == Material.DIRT || state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.MYCELIUM) && state.getBlock() != AtumBlocks.FERTILE_SOIL_TILLED.get())) {
                event.getLevel().setBlock(event.getPos(), AtumBlocks.STRANGE_SAND.get().defaultBlockState(), 3);
            }
        }
    }

    @SubscribeEvent
    public static void onArmorClean(PlayerInteractEvent.RightClickBlock event) {
        try {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            Player player = event.getEntity();
            ItemStack stack = player.getMainHandItem();
            LazyOptional<IFluidHandler> lazyTank = FluidUtil.getFluidHandler(level, pos, event.getFace());

            if (stack.getItem() instanceof WandererDyeableArmor && ((WandererDyeableArmor) stack.getItem()).hasCustomColor(stack)) {
                BlockState state = level.getBlockState(pos);
                if (state.getBlock() instanceof LayeredCauldronBlock) {
                    int cauldronLevel = state.getValue(LayeredCauldronBlock.LEVEL);
                    if (cauldronLevel > 0) {
                        if (!level.isClientSide) {
                            ((WandererDyeableArmor) stack.getItem()).clearColor(stack);
                            player.awardStat(Stats.CLEAN_ARMOR);
                            LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                        }
                        player.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.16F, 0.66F);
                        event.setUseItem(Event.Result.DENY);
                    }
                } else if (lazyTank.map(f -> f instanceof FluidTank).orElse(false)) {
                    lazyTank.ifPresent(tank -> {
                                FluidTank fluidTank = (FluidTank) tank;

                                if (fluidTank.getFluid().getFluid().is(FluidTags.WATER) && fluidTank.getFluidAmount() >= 250) {
                                    if (!level.isClientSide) {
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
        Player player = event.getEntity();
        Level level = event.getLevel();
        if (player.level.dimension() == Atum.ATUM) {
            if (player.getItemInHand(event.getHand()).getItem() == Items.WHEAT_SEEDS && level.getBlockState(event.getPos()).getBlock() instanceof FarmBlock) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_DROWNING) && (event.getEntity() instanceof UndeadBaseEntity || event.getEntity() instanceof StoneBaseEntity)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onFishLoot(ItemFishedEvent event) {
        Level level = event.getEntity().level;
        FishingHook bobber = event.getHookEntity();
        Player angler = bobber.getPlayerOwner();
        if (angler != null) {
            ItemStack heldStack = angler.getItemInHand(angler.getUsedItemHand());
            LootContext.Builder builder = new LootContext.Builder((ServerLevel) level);
            builder.withLuck((float) EnchantmentHelper.getFishingLuckBonus(heldStack) + angler.getLuck()).withParameter(LootContextParams.KILLER_ENTITY, angler).withParameter(LootContextParams.THIS_ENTITY, bobber);
            if (level.dimension() == Atum.ATUM) {
                event.setCanceled(true); //We don't want vanillas loot table
                if (heldStack.getItem() instanceof AtemsBountyItem) {
                    catchFish((ServerLevel) level, angler, heldStack, bobber, builder, AtumLootTables.ATEMS_BOUNTY);
                    angler.level.addFreshEntity(new ExperienceOrb(angler.level, angler.getX(), angler.getY() + 0.5D, angler.getZ() + 0.5D, level.random.nextInt(6) + 1));
                } else {
                    catchFish((ServerLevel) level, angler, heldStack, bobber, builder, AtumLootTables.FISHING);
                }
            }
        }
    }

    private static void catchFish(ServerLevel level, Player angler, ItemStack heldStack, FishingHook bobber, LootContext.Builder builder, ResourceLocation lootTable) {
        List<ItemStack> loots = level.getServer().getLootTables().get(lootTable).getRandomItems(builder.withParameter(LootContextParams.ORIGIN, bobber.position()).withParameter(LootContextParams.TOOL, heldStack).withParameter(LootContextParams.THIS_ENTITY, bobber).withRandom(level.random).create(LootContextParamSets.FISHING));
        for (ItemStack loot : loots) {
            ItemEntity fish = new ItemEntity(bobber.level, bobber.getX(), bobber.getY(), bobber.getZ(), loot);
            double x = angler.getX() - bobber.getX();
            double y = angler.getY() - bobber.getY();
            double z = angler.getZ() - bobber.getZ();
            double swush = Mth.sqrt((float) (x * x + y * y + z * z));
            fish.setDeltaMovement(x * 0.1D, y * 0.1D + swush * 0.08D, z * 0.1D);
            level.addFreshEntity(fish);
        }
    }

    @SubscribeEvent
    public static void checkSpawn(MobSpawnEvent.FinalizeSpawn event) { //Prevent Phantom spawning in Atum
        LevelAccessor level = event.getLevel();
        if ((event.getEntity() instanceof Phantom || event.getEntity().getType() == EntityType.CAT) && (level instanceof ServerLevel && ((ServerLevel) level).dimension() == Atum.ATUM)) {
            event.setSpawnCancelled(true);
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
}