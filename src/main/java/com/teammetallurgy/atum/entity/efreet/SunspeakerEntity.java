package com.teammetallurgy.atum.entity.efreet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.teammetallurgy.atum.entity.ai.brain.SunspeakerShowWaresTask;
import com.teammetallurgy.atum.entity.ai.brain.SunspeakerTradeTask;
import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import com.teammetallurgy.atum.init.*;
import com.teammetallurgy.atum.items.LootItem;
import com.teammetallurgy.atum.items.tools.ScepterItem;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.IReputationTracking;
import net.minecraft.entity.merchant.IReputationType;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.GossipManager;
import net.minecraft.village.GossipType;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiPredicate;

public class SunspeakerEntity extends EfreetBaseEntity implements IReputationTracking, IMerchant { //TODO Change Relics trading/buying to same system as Piglins
    private static final DataParameter<SunspeakerData> SUNSPEAKER_DATA = EntityDataManager.createKey(SunspeakerEntity.class, AtumDataSerializer.SUNSPEAKER_DATA);
    @Nullable
    private PlayerEntity customer;
    @Nullable
    protected MerchantOffers offers;
    private int timeUntilReset;
    private boolean leveledUp;
    @Nullable
    private PlayerEntity previousCustomer;
    private final GossipManager gossip = new GossipManager();
    private long lastGossipDecay;
    private int xp;
    private long lastRestock;
    private int restocksToday;
    private long restockTime;
    private static final List<VillagerTrades.ITrade[]> TRADES = Arrays.asList(
    /*Tier 1*/ new VillagerTrades.ITrade[]{new ItemsForCoins(32, AtumBlocks.PALM_SAPLING.asItem(), 4, 5, 4, 3), new ItemsForCoins(16, Blocks.SUNFLOWER.asItem(), 1, 2, 7, 1), new ItemsForCoins(24, AtumItems.DATE, 14, 16, 3, 2), new ItemsForCoins(24, AtumItems.EMMER_BREAD, 3, 4, 6, 2)},
    /*Tier 2*/ new VillagerTrades.ITrade[]{new ItemsForCoins(36, AtumItems.LINEN_CLOTH, 5, 10, 5, 3), new ItemsForCoins(48, AtumItems.CAMEL_RAW, 13, 18, 10, 2), new ItemsForCoins(48, AtumItems.SCROLL, 9, 12, 4, 4), new ItemsForCoins(32, AtumItems.ANPUTS_FINGERS_SPORES, 8, 10, 2, 2)},
    /*Tier 3*/ new VillagerTrades.ITrade[]{new ItemsForCoins(48, Blocks.GLOWSTONE.asItem(), 3, 4, 10, 2), new ItemsForCoins(48, Items.NAME_TAG, 1, 2, 16, 1), new ItemsForCoins(64, Items.BREWING_STAND, 1, 1, 2, 9), new ItemsForCoins(36, Items.BLAZE_POWDER, 4, 5, 16, 4)},
    /*Tier 4*/ new VillagerTrades.ITrade[]{new ItemsForCoins(48, Items.SADDLE, 1, 1, 12, 4), new ItemsForCoins(64, AtumItems.ENCHANTED_GOLDEN_DATE, 1, 2, 4, 10), new ItemsForCoins(48, Items.ENDER_PEARL, 3, 4, 8, 15), new ItemsForCoins(64, AtumItems.DISENCHANTING_SCROLL, 1, 1, 16, 14)});
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.MEETING_POINT, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.INTERACTABLE_DOORS, MemoryModuleType.OPENED_DOORS, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN);
    private static final ImmutableList<SensorType<? extends Sensor<? super SunspeakerEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY);
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<SunspeakerEntity, PointOfInterestType>> HOME = ImmutableMap.of(MemoryModuleType.HOME, (sunspeaker, poi) -> poi == PointOfInterestType.HOME);

    public SunspeakerEntity(EntityType<? extends SunspeakerEntity> entityType, World world) {
        super(entityType, world);
        ((GroundPathNavigator) this.getNavigator()).setBreakDoors(true);
        this.getNavigator().setCanSwim(true);
        this.brain = this.createBrain(new Dynamic<>(NBTDynamicOps.INSTANCE, new CompoundNBT()));
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ScepterItem.getScepter(PharaohEntity.God.RA)));
        this.setSunspeakerData(this.getSunspeakerData().withLevel(0));
    }

    @Override
    protected void dropSpecialItems(@Nonnull DamageSource source, int looting, boolean recentlyHit) {
        //Don't drop the scepter
    }

    @Override
    @Nonnull
    public Brain<SunspeakerEntity> getBrain() {
        return (Brain<SunspeakerEntity>) super.getBrain();
    }

    @Override
    @Nonnull
    protected Brain.BrainCodec<SunspeakerEntity> getBrainCodec() {
        return Brain.createCodec(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    @Nonnull
    protected Brain<?> createBrain(@Nonnull Dynamic<?> dynamic) {
        Brain<SunspeakerEntity> brain = this.getBrainCodec().deserialize(dynamic);
        this.initBrain(brain);
        return brain;
    }

    public void resetBrain(ServerWorld serverWorld) {
        Brain<SunspeakerEntity> brain =  this.getBrain();
        brain.stopAllTasks(serverWorld, this);
        this.brain = brain.copy();
        this.initBrain(this.getBrain());
    }

    private void initBrain(Brain<SunspeakerEntity> brain) {
        float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) + 0.75F; //Add additional speed, due to weird issue

        brain.registerActivity(Activity.CORE, SunspeakerTasks.core(speed));
        brain.registerActivity(Activity.MEET, SunspeakerTasks.meet(speed), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryModuleStatus.VALUE_PRESENT)));
        brain.registerActivity(Activity.REST, SunspeakerTasks.rest(speed));
        brain.registerActivity(Activity.IDLE, SunspeakerTasks.idle(speed));
        brain.setDefaultActivities(ImmutableSet.of(Activity.CORE));
        brain.setFallbackActivity(Activity.IDLE);
        brain.switchTo(Activity.IDLE);
        brain.updateActivity(this.world.getDayTime(), this.world.getGameTime());
    }

    @Override
    protected void onGrowingAdult() {
        super.onGrowingAdult();
        if (this.world instanceof ServerWorld) {
            this.resetBrain((ServerWorld) this.world);
        }
    }

    @Override
    protected void registerGoals() {
        super.getSuperGoals();
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        super.applyEntityAI();
    }

    public static AttributeModifierMap.MutableAttribute getAttributes() {
        return getBaseAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 20.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D).createMutableAttribute(Attributes.ARMOR, 4.0F);
    }

    @Override
    protected void updateAITasks() {
        this.world.getProfiler().startSection("villagerBrain");
        this.getBrain().tick((ServerWorld) this.world, this);
        this.world.getProfiler().endSection();
        if (!this.hasCustomer() && this.timeUntilReset > 0) {
            --this.timeUntilReset;
            if (this.timeUntilReset <= 0) {
                if (this.leveledUp) {
                    this.levelUp();
                    this.leveledUp = false;
                }
                this.addPotionEffect(new EffectInstance(Effects.REGENERATION, 200, 0));
            }
        }

        if (this.previousCustomer != null && this.world instanceof ServerWorld) {
            ((ServerWorld) this.world).updateReputation(IReputationType.TRADE, this.previousCustomer, this);
            this.world.setEntityState(this, (byte) 14);
            this.previousCustomer = null;
        }
        super.updateAITasks();
    }

    @Override
    public void tick() {
        super.tick();
        this.tickGossip();
    }

    @Nullable
    @Override
    public Entity changeDimension(@Nonnull ServerWorld world, @Nonnull ITeleporter teleporter) {
        this.resetCustomer();
        return super.changeDimension(world, teleporter);
    }

    @Override
    @Nullable
    public AgeableEntity func_241840_a(@Nonnull ServerWorld world, @Nonnull AgeableEntity ageable) {
        SunspeakerEntity sunspeaker = new SunspeakerEntity(AtumEntities.SUNSPEAKER, world);
        sunspeaker.onInitialSpawn(world, this.world.getDifficultyForLocation(sunspeaker.getPosition()), SpawnReason.BREEDING, null, null);
        return sunspeaker;
    }

    @Override
    public void updateReputation(@Nonnull IReputationType type, @Nonnull Entity target) {
        if (type == IReputationType.TRADE) {
            this.gossip.add(target.getUniqueID(), GossipType.TRADING, 2);
        } else if (type == IReputationType.VILLAGER_HURT) {
            this.gossip.add(target.getUniqueID(), GossipType.MINOR_NEGATIVE, 25);
        } else if (type == IReputationType.VILLAGER_KILLED) {
            this.gossip.add(target.getUniqueID(), GossipType.MAJOR_NEGATIVE, 25);
        }
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity player) {
        this.customer = player;
    }

    @Nullable
    @Override
    public PlayerEntity getCustomer() {
        return this.customer;
    }

    public boolean hasCustomer() {
        return this.customer != null;
    }

    protected void resetCustomer() {
        this.setCustomer(null);
        this.resetAllSpecialPrices();
    }

    @Override
    @Nonnull
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.populateTradeData();
        }
        return this.offers;
    }

    @Override
    public void setClientSideOffers(@Nullable MerchantOffers offers) {
    }

    @Override
    public void onTrade(@Nonnull MerchantOffer offer) {
        offer.increaseUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.onSunSpeakerTrade(offer);
    }

    public void setSunspeakerData(SunspeakerData sunspeakerData) {
        this.dataManager.set(SUNSPEAKER_DATA, sunspeakerData);
    }

    public SunspeakerData getSunspeakerData() {
        return this.dataManager.get(SUNSPEAKER_DATA);
    }

    protected void onSunSpeakerTrade(MerchantOffer offer) {
        int xpAmount = 3 + this.rand.nextInt(4);
        this.xp += offer.getGivenExp();
        this.previousCustomer = this.getCustomer();
        if (this.canLevelUp()) {
            this.timeUntilReset = 40;
            this.leveledUp = true;
            xpAmount += 5;
        }

        if (offer.getDoesRewardExp()) {
            this.world.addEntity(new ExperienceOrbEntity(this.world, this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), xpAmount));
        }
    }

    @Override
    public void setRevengeTarget(@Nullable LivingEntity livingBase) {
        if (livingBase != null && this.world instanceof ServerWorld) {
            ((ServerWorld) this.world).updateReputation(IReputationType.VILLAGER_HURT, livingBase, this);
            if (this.isAlive() && livingBase instanceof PlayerEntity) {
                this.world.setEntityState(this, (byte) 13);
            }
        }
        super.setRevengeTarget(livingBase);
    }


    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        Entity entity = cause.getTrueSource();
        if (entity != null) {
            this.updateKilledReputation(entity);
        }

        this.runMemoryModule(MemoryModuleType.HOME);
        this.runMemoryModule(MemoryModuleType.MEETING_POINT);

        super.onDeath(cause);

        this.resetCustomer();
        Entity killer = cause.getTrueSource();
        if (killer instanceof PlayerEntity) {
            double chance = this.rand.nextDouble();
            if (chance <= 0.5D) {
                ((PlayerEntity) killer).addPotionEffect(new EffectInstance(AtumEffects.MARKED_FOR_DEATH, 1200, 1, false, false, true));
            }
        }
    }

    private void updateKilledReputation(Entity entity) {
        if (this.world instanceof ServerWorld) {
            Optional<List<LivingEntity>> optional = this.brain.getMemory(MemoryModuleType.VISIBLE_MOBS);
            if (optional.isPresent()) {
                ServerWorld serverWorld = (ServerWorld) this.world;
                optional.get().stream().filter((livingEntity) -> livingEntity instanceof IReputationTracking).forEach((livingEntity) -> {
                    serverWorld.updateReputation(IReputationType.VILLAGER_KILLED, entity, (IReputationTracking) livingEntity);
                });
            }
        }
    }

    public void runMemoryModule(MemoryModuleType<GlobalPos> globalPosMemory) {
        if (this.world instanceof ServerWorld) {
            MinecraftServer server = ((ServerWorld) this.world).getServer();
            this.brain.getMemory(globalPosMemory).ifPresent((globalPos) -> {
                ServerWorld serverWorld = server.getWorld(globalPos.getDimension());
                PointOfInterestManager poiManager = serverWorld.getPointOfInterestManager();
                Optional<PointOfInterestType> optional = poiManager.getType(globalPos.getPos());
                BiPredicate<SunspeakerEntity, PointOfInterestType> predicate = HOME.get(globalPosMemory);
                if (optional.isPresent() && predicate.test(this, optional.get())) {
                    poiManager.release(globalPos.getPos());
                }
            });
        }
    }

    public int getPlayerReputation(PlayerEntity player) {
        return this.gossip.getReputation(player.getUniqueID(), (gossipType) -> true);
    }

    private boolean canLevelUp() {
        int level = this.getSunspeakerData().getLevel();
        return level < 4 && VillagerData.canLevelUp(level) && this.xp >= VillagerData.getExperienceNext(level);
    }

    private void levelUp() {
        this.setSunspeakerData(this.getSunspeakerData().withLevel(this.getSunspeakerData().getLevel() + 1));
        this.populateTradeData();
    }

    @Override
    public void verifySellingItem(@Nonnull ItemStack stack) {
        if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
            this.livingSoundTime = -this.getTalkInterval();
            this.playSound(this.getYesNoSound(!stack.isEmpty()), this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Override
    @Nonnull
    public World getWorld() {
        return this.world;
    }

    @Override
    public int getXp() {
        return this.xp;
    }

    @Override
    public void setXP(int xp) {
        this.xp = xp;
    }

    @Override
    public boolean hasXPBar() { //No clue what this is
        return true;
    }

    @Override
    @Nonnull
    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_VILLAGER_YES;
    }

    protected SoundEvent getYesNoSound(boolean getYesSound) {
        return getYesSound ? SoundEvents.ENTITY_VILLAGER_YES : SoundEvents.ENTITY_VILLAGER_NO;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    protected void populateTradeData() {
        SunspeakerData sunspeakerData = this.getSunspeakerData();
        List<VillagerTrades.ITrade[]> trades = TRADES;
        if (trades != null && !trades.isEmpty()) {
            VillagerTrades.ITrade[] trade = trades.get(sunspeakerData.getLevel() - 1);
            if (trade != null) {
                MerchantOffers merchantoffers = this.getOffers();
                this.addTrades(merchantoffers, trade, 2);
            }
        }
    }

    private void tickGossip() {
        long gameTime = this.world.getGameTime();
        if (this.lastGossipDecay == 0L) {
            this.lastGossipDecay = gameTime;
        } else if (gameTime >= this.lastGossipDecay + 24000L) {
            this.gossip.tick();
            this.lastGossipDecay = gameTime;
        }
    }

    protected void addTrades(MerchantOffers givenMerchantOffers, VillagerTrades.ITrade[] newTrades, int maxNumbers) {
        Set<Integer> set = Sets.newHashSet();
        if (newTrades.length > maxNumbers) {
            while (set.size() < maxNumbers) {
                set.add(this.rand.nextInt(newTrades.length));
            }
        } else {
            for (int i = 0; i < newTrades.length; ++i) {
                set.add(i);
            }
        }
        for (Integer integer : set) {
            VillagerTrades.ITrade trade = newTrades[integer];
            MerchantOffer merchantOffer = trade.getOffer(this, this.rand);
            if (merchantOffer != null) {
                givenMerchantOffers.add(merchantOffer);
            }
        }
    }

    @Override
    @Nonnull
    public ActionResultType func_230254_b_(PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        boolean nameTag = heldStack.getItem() == Items.NAME_TAG;
        boolean isAgressiveTowards = player.getUniqueID() == this.angerTargetUUID;

        if (nameTag) {
            heldStack.interactWithEntity(player, this, hand);
            return ActionResultType.SUCCESS;
        } else if (!(heldStack.getItem() instanceof LootItem) && !(heldStack.getItem() instanceof SpawnEggItem) && this.isAlive() && !this.hasCustomer() && !this.isSleeping() && !player.isSecondaryUseActive()) {
            if (this.isChild()) {
                this.shakeHead();
                return ActionResultType.func_233537_a_(this.world.isRemote);
            } else {
                boolean noOffers = this.getOffers().isEmpty();
                if (hand == Hand.MAIN_HAND) {
                    if (noOffers && !this.world.isRemote) {
                        this.shakeHead();
                    }
                    player.addStat(Stats.TALKED_TO_VILLAGER);
                }
                if (noOffers || isAgressiveTowards) {
                    if (isAgressiveTowards && heldStack.getItem() == AtumItems.GOLD_COIN) {
                        this.angerTargetUUID = null;
                        this.setRevengeTarget(null);
                        this.setAttackTarget(null);
                        this.attackingPlayer = null;
                        this.recentlyHit = 0;
                        this.angerLevel = 0;
                        this.gossip.add(player.getUniqueID(), GossipType.MINOR_POSITIVE, 10);
                        return ActionResultType.SUCCESS;
                    } else {
                        return super.func_230254_b_(player, hand);
                    }
                } else {
                    if (!this.world.isRemote && !this.getOffers().isEmpty()) {
                        this.displayMerchantGui(player);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        } else if (heldStack.getItem() instanceof LootItem) {
            LootItem.Type type = LootItem.getType(heldStack.getItem());
            LootItem.Quality quality = LootItem.getQuality(heldStack.getItem());

            if (isAgressiveTowards) {
                this.angerTargetUUID = null;
                this.setRevengeTarget(null);
                this.setAttackTarget(null);
                this.attackingPlayer = null;
                this.recentlyHit = 0;
                this.angerLevel = 0;
                this.gossip.add(player.getUniqueID(), GossipType.MINOR_POSITIVE, 10);
                return ActionResultType.SUCCESS;
            } else {

                if (quality != LootItem.Quality.DIRTY) {
                    double modifier = 1.0D;
                    if (type == LootItem.Type.NECKLACE) {
                        modifier = 2.0D;
                    } else if (type == LootItem.Type.BROOCH) {
                        modifier = 2.5D;
                    } else if (type == LootItem.Type.SCEPTER) {
                        modifier = 3.0D;
                    } else if (type == LootItem.Type.IDOL) {
                        modifier = 5.0D;
                    }
                    if (!player.isCreative()) {
                        heldStack.shrink(1);
                    }
                    this.handleRelicTrade(player, hand, modifier, quality);
                    return ActionResultType.SUCCESS;

                } else {
                    return super.func_230254_b_(player, hand);
                }
            }
        } else {
            return super.func_230254_b_(player, hand);
        }
    }

    private void handleRelicTrade(PlayerEntity player, Hand hand, double modifier, LootItem.Quality quality) {
        int amount = 0;

        if (quality == LootItem.Quality.SILVER) {
            amount += modifier;
        } else if (quality == LootItem.Quality.GOLD) {
            amount += modifier * 2;
        } else if (quality == LootItem.Quality.SAPPHIRE) {
            amount += modifier * 3;
        } else if (quality == LootItem.Quality.RUBY) {
            amount += modifier * 4;
        } else if (quality == LootItem.Quality.EMERALD) {
            amount += modifier * 5;
        } else if (quality == LootItem.Quality.DIAMOND) {
            amount += modifier * 10;
        }

        if (amount > 0) {
            if (this.world.isRemote) {
                this.addParticles(ParticleTypes.HAPPY_VILLAGER);
                this.playSound(this.getYesSound(), this.getSoundVolume(), this.getSoundPitch());
            }

            if (!this.world.isRemote) {
                this.playSound(this.getYesSound(), this.getSoundVolume(), this.getSoundPitch());
                StackHelper.giveItem(player, hand, new ItemStack(AtumItems.GOLD_COIN, amount));
                this.updateReputation(IReputationType.TRADE, player);
            }
        }
    }

    private void shakeHead() {
        if (!this.world.isRemote()) {
            this.playSound(SoundEvents.ENTITY_VILLAGER_NO, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    private void displayMerchantGui(PlayerEntity player) {
        this.recalculateSpecialPricesFor(player);
        this.setCustomer(player);
        this.openMerchantContainer(player, this.getDisplayName(), this.getSunspeakerData().getLevel());
    }

    private void resetAllSpecialPrices() {
        for (MerchantOffer merchantOffer : this.getOffers()) {
            merchantOffer.resetSpecialPrice();
        }
    }

    @Override
    public boolean canRestockTrades() {
        return true;
    }

    public void restock() {
        this.calculateDemandOfOffers();

        for (MerchantOffer merchantoffer : this.getOffers()) {
            merchantoffer.resetUses();
        }

        this.lastRestock = this.world.getGameTime();
        ++this.restocksToday;
    }

    private boolean hasUsedOffer() {
        for (MerchantOffer merchantOffer : this.getOffers()) {
            if (merchantOffer.hasBeenUsed()) {
                return true;
            }
        }
        return false;
    }

    private boolean canRestock() {
        return this.restocksToday == 0 || this.restocksToday < 2 && this.world.getGameTime() > this.lastRestock + 2400L;
    }

    public boolean canResetStock() {
        long i = this.lastRestock + 12000L;
        long j = this.world.getGameTime();
        boolean flag = j > i;
        long k = this.world.getDayTime();
        if (this.restockTime > 0L) {
            long l = this.restockTime / 24000L;
            long i1 = k / 24000L;
            flag |= i1 > l;
        }

        this.restockTime = k;
        if (flag) {
            this.lastRestock = j;
            this.resetRestock();
        }
        return this.canRestock() && this.hasUsedOffer();
    }

    private void resetRestock() {
        this.resetOffersAndAdjustForDemand();
        this.restocksToday = 0;
    }

    private void resetOffersAndAdjustForDemand() {
        int i = 2 - this.restocksToday;
        if (i > 0) {
            for (MerchantOffer merchantOffer : this.getOffers()) {
                merchantOffer.resetUses();
            }
        }
        for (int j = 0; j < i; ++j) {
            this.calculateDemandOfOffers();
        }
    }

    private void calculateDemandOfOffers() {
        for (MerchantOffer merchantOffer : this.getOffers()) {
            merchantOffer.calculateDemand();
        }
    }

    private void recalculateSpecialPricesFor(PlayerEntity player) {
        int reputation = this.getPlayerReputation(player);
        if (reputation != 0) {
            for (MerchantOffer merchantOffer : this.getOffers()) {
                merchantOffer.increaseSpecialPrice(-MathHelper.floor((float) reputation * merchantOffer.getPriceMultiplier()));
            }
        }
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return null;
        } else {
            return this.hasCustomer() ? SoundEvents.ENTITY_VILLAGER_TRADE : super.getAmbientSound();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void addParticles(BasicParticleType particleType) {
        for (int amount = 0; amount < 5; ++amount) {
            double x = this.rand.nextGaussian() * 0.02D;
            double y = this.rand.nextGaussian() * 0.02D;
            double z = this.rand.nextGaussian() * 0.02D;
            this.world.addParticle(particleType, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + 1.0D + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), x, y, z);
        }
    }

    @Override
    public boolean canBeLeashedTo(@Nonnull PlayerEntity player) {
        return false;
    }

    @Override
    public void startSleeping(@Nonnull BlockPos pos) {
        super.startSleeping(pos);
        this.brain.setMemory(MemoryModuleType.LAST_SLEPT, this.world.getGameTime());
    }

    @Override
    public void wakeUp() {
        super.wakeUp();
        this.brain.setMemory(MemoryModuleType.LAST_WOKEN, this.world.getGameTime());
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SUNSPEAKER_DATA, new SunspeakerData(1));
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        MerchantOffers merchantOffers = this.getOffers();
        if (!merchantOffers.isEmpty()) {
            compound.put("Offers", merchantOffers.write());
        }
        SunspeakerData.CODEC.encodeStart(NBTDynamicOps.INSTANCE, this.getSunspeakerData()).resultOrPartial(LOGGER::error).ifPresent((data) -> {
            compound.put("SunspeakerData", data);
        });
        compound.put("Gossips", this.gossip.write(NBTDynamicOps.INSTANCE).getValue());
        compound.putInt("Xp", this.xp);
        compound.putLong("LastRestock", this.lastRestock);
        compound.putLong("LastGossipDecay", this.lastGossipDecay);
        compound.putInt("RestocksToday", this.restocksToday);
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("SunspeakerData", 10)) {
            DataResult<SunspeakerData> dataresult = SunspeakerData.CODEC.parse(new Dynamic<>(NBTDynamicOps.INSTANCE, compound.get("SunspeakerData")));
            dataresult.resultOrPartial(LOGGER::error).ifPresent(this::setSunspeakerData);
        }
        if (compound.contains("Offers", 10)) {
            this.offers = new MerchantOffers(compound.getCompound("Offers"));
        }
        ListNBT gossips = compound.getList("Gossips", 10);
        this.gossip.read(new Dynamic<>(NBTDynamicOps.INSTANCE, gossips));

        if (compound.contains("Xp", 3)) {
            this.xp = compound.getInt("Xp");
        }

        this.lastRestock = compound.getLong("LastRestock");
        this.lastGossipDecay = compound.getLong("LastGossipDecay");
        if (this.world instanceof ServerWorld) {
            this.resetBrain((ServerWorld) this.world);
        }
        this.restocksToday = compound.getInt("RestocksToday");
    }

    public static class SunspeakerTasks {
        public static ImmutableList<Pair<Integer, ? extends Task<? super SunspeakerEntity>>> core(float speed) {
            return ImmutableList.of(Pair.of(0, new SwimTask(0.8F)), Pair.of(0, new InteractWithDoorTask()), Pair.of(0, new LookTask(45, 90)), Pair.of(0, new WakeUpTask()), Pair.of(1, new WalkToTargetTask()), Pair.of(3, new SunspeakerTradeTask(speed)), Pair.of(5, new PickupWantedItemTask<>(speed, false, 4)), Pair.of(10, new GatherPOITask(PointOfInterestType.HOME, MemoryModuleType.HOME, false, Optional.of((byte)14))));
        }

        public static ImmutableList<Pair<Integer, ? extends Task<? super SunspeakerEntity>>> meet(float speed) {
            return ImmutableList.of(Pair.of(2, new FirstShuffledTask<>(ImmutableList.of(Pair.of(new WorkTask(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2), Pair.of(new CongregateTask(), 2)))), Pair.of(10, new SunspeakerShowWaresTask(400, 1600)), Pair.of(10, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)), Pair.of(3, new ExpirePOITask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT)), lookAtMany(), Pair.of(99, new UpdateActivityTask()));
        }

        public static ImmutableList<Pair<Integer, ? extends Task<? super SunspeakerEntity>>> rest(float speed) {
            return ImmutableList.of(/*Pair.of(2, new StayNearPointTask(MemoryModuleType.HOME, speed, 1, 150, 1200)),*/ Pair.of(3, new ExpirePOITask(PointOfInterestType.HOME, MemoryModuleType.HOME)), Pair.of(3, new SleepAtHomeTask()), Pair.of(5, new FirstShuffledTask<>(ImmutableMap.of(MemoryModuleType.HOME, MemoryModuleStatus.VALUE_ABSENT), ImmutableList.of(Pair.of(new WalkToHouseTask(speed), 1), Pair.of(new WalkRandomlyTask(speed), 4), Pair.of(new DummyTask(20, 40), 2)))), lookAtPlayerOrSunspeaker(), Pair.of(99, new UpdateActivityTask()));
        }

        public static ImmutableList<Pair<Integer, ? extends Task<? super SunspeakerEntity>>> idle(float p_220641_1_) {
            return ImmutableList.of(Pair.of(2, new FirstShuffledTask<>(ImmutableList.of(Pair.of(InteractWithEntityTask.func_220445_a(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, p_220641_1_, 2), 2), Pair.of(new FindWalkTargetTask(p_220641_1_), 1), Pair.of(new WalkTowardsLookTargetTask(p_220641_1_, 2), 1), Pair.of(new JumpOnBedTask(p_220641_1_), 1), Pair.of(new DummyTask(30, 60), 1)))), Pair.of(3, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)), Pair.of(3, new SunspeakerShowWaresTask(400, 1600)), lookAtMany(), Pair.of(99, new UpdateActivityTask()));
        }

        private static Pair<Integer, Task<LivingEntity>> lookAtMany() {
            return Pair.of(5, new FirstShuffledTask<>(ImmutableList.of(Pair.of(new LookAtEntityTask(AtumEntities.SUNSPEAKER, 8.0F), 2), Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 2), Pair.of(new LookAtEntityTask(EntityClassification.CREATURE, 8.0F), 1), Pair.of(new LookAtEntityTask(EntityClassification.WATER_CREATURE, 8.0F), 1), Pair.of(new LookAtEntityTask(EntityClassification.MONSTER, 8.0F), 1), Pair.of(new DummyTask(30, 60), 2))));
        }

        private static Pair<Integer, Task<LivingEntity>> lookAtPlayerOrSunspeaker() {
            return Pair.of(5, new FirstShuffledTask<>(ImmutableList.of(Pair.of(new LookAtEntityTask(AtumEntities.SUNSPEAKER, 8.0F), 2), Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 2), Pair.of(new DummyTask(30, 60), 8))));
        }
    }

    static class ItemsForCoins implements VillagerTrades.ITrade {
        private final int price;
        private final Item buyingItem;
        private final int maxUses;
        private final int givenExp;
        private final int minAmount;
        private final int maxAmount;

        ItemsForCoins(int price, Item item, int minAmount, int maxAmount, int maxUses, int givenExp) {
            this.buyingItem = item;
            this.price = price;
            this.maxUses = maxUses;
            this.givenExp = givenExp;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN, this.price), new ItemStack(this.buyingItem, MathHelper.nextInt(new Random(), this.minAmount, this.maxAmount)), this.maxUses, this.givenExp, 0.1F);
        }
    }
}