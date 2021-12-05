package com.teammetallurgy.atum.entity.villager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.entity.ITexture;
import com.teammetallurgy.atum.entity.ai.brain.sensor.AtumSensorTypes;
import com.teammetallurgy.atum.entity.ai.brain.task.AtumVillagerTasks;
import com.teammetallurgy.atum.init.AtumDataSerializer;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumPointsOfInterest;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.SimpleContainer;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class AtumVillagerEntity extends Villager implements ITexture {
    private static final EntityDataAccessor<AtumVillagerData> ATUM_VILLAGER_DATA = SynchedEntityData.defineId(AtumVillagerEntity.class, AtumDataSerializer.VILLAGER_DATA);
    public static final Map<Item, Integer> FOOD_VALUES = ImmutableMap.of(AtumItems.EMMER_BREAD, 4, Items.BREAD, 4, Items.POTATO, 1, Items.CARROT, 1, Items.BEETROOT, 1);
    private static final Set<Item> ALLOWED_INVENTORY_ITEMS = ImmutableSet.of(AtumItems.EMMER_BREAD, AtumItems.EMMER_EAR, AtumItems.EMMER_SEEDS, Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(AtumVillagerEntity.class, EntityDataSerializers.INT);
    private String texturePath;
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM); //No changes
    private static final ImmutableList<SensorType<? extends Sensor<? super Villager>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, AtumSensorTypes.SECONDARY_POIS.get(), SensorType.GOLEM_DETECTED); //Added Atum secondary pois
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<AtumVillagerEntity, PoiType>> JOB_SITE_PREDICATE_MAP = ImmutableMap.of(MemoryModuleType.HOME, (villager, poiType) -> {
        return poiType == PoiType.HOME;
    }, MemoryModuleType.JOB_SITE, (villager, poiType) -> {
        return villager.getAtumVillagerData().getAtumProfession().getPointOfInterest() == poiType;
    }, MemoryModuleType.POTENTIAL_JOB_SITE, (villager, poiType) -> {
        return AtumPointsOfInterest.ANY_VILLAGER_WORKSTATION.test(poiType);
    }, MemoryModuleType.MEETING_POINT, (villager, poiType) -> {
        return poiType == PoiType.MEETING;
    });

    public AtumVillagerEntity(EntityType<? extends AtumVillagerEntity> type, Level world) {
        super(type, world, VillagerType.DESERT); //Type not used, by Atum villagers
        this.setAtumVillagerData(this.getAtumVillagerData().withProfession(AtumVillagerProfession.NONE.get()));
    }

    public boolean isFemale() {
        return this.getType() == AtumEntities.VILLAGER_FEMALE;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATUM_VILLAGER_DATA, new AtumVillagerData(AtumVillagerProfession.NONE.get(), 1, Race.HUMAN));
        this.entityData.define(VARIANT, 0);
    }

    @Override
    @Nonnull
    protected Brain<?> makeBrain(@Nonnull Dynamic<?> dynamic) {
        Brain<Villager> brain = this.brainProvider().makeBrain(dynamic);
        this.initBrain(brain);
        return brain;
    }

    @Override
    public void refreshBrain(@Nonnull ServerLevel serverWorld) {
        Brain<Villager> brain = this.getBrain();
        brain.stopAll(serverWorld, this);
        this.brain = brain.copyWithoutBehaviors();
        this.initBrain(this.getBrain());
    }

    @Override
    @Nonnull
    protected Brain.Provider<Villager> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    private void initBrain(Brain<Villager> brain) {
        AtumVillagerProfession profession = this.getAtumVillagerData().getAtumProfession();
        EntityType<? extends AtumVillagerEntity> entityType = (EntityType<? extends AtumVillagerEntity>) this.getType();

        if (this.isBaby()) {
            brain.setSchedule(Schedule.VILLAGER_BABY);
            brain.addActivity(Activity.PLAY, AtumVillagerTasks.play(entityType, 0.5F));
        } else {
            brain.setSchedule(Schedule.VILLAGER_DEFAULT);
            brain.addActivityWithConditions(Activity.WORK, AtumVillagerTasks.work(entityType, profession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT)));
        }

        brain.addActivity(Activity.CORE, AtumVillagerTasks.core(entityType, profession, 0.5F));
        brain.addActivityWithConditions(Activity.MEET, AtumVillagerTasks.meet(entityType, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)));
        brain.addActivity(Activity.REST, AtumVillagerTasks.rest(entityType, 0.5F));
        brain.addActivity(Activity.IDLE, AtumVillagerTasks.idle(entityType, 0.5F));
        brain.addActivity(Activity.PANIC, AtumVillagerTasks.panic(entityType, 0.5F));
        brain.addActivity(Activity.HIDE, AtumVillagerTasks.hide(entityType, 0.5F));
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
        brain.updateActivityFromSchedule(this.level.getDayTime(), this.level.getGameTime());
    }

    @Override
    public void releasePoi(@Nonnull MemoryModuleType<GlobalPos> moduleType) {
        if (this.level instanceof ServerLevel) {
            MinecraftServer server = ((ServerLevel) this.level).getServer();
            this.brain.getMemory(moduleType).ifPresent((jobSitePos) -> {
                ServerLevel serverWorld = server.getLevel(jobSitePos.dimension());
                if (serverWorld != null) {
                    PoiManager posManger = serverWorld.getPoiManager();
                    Optional<PoiType> optional = posManger.getType(jobSitePos.pos());
                    BiPredicate<AtumVillagerEntity, PoiType> p = JOB_SITE_PREDICATE_MAP.get(moduleType);
                    if (optional.isPresent() && p.test(this, optional.get())) {
                        posManger.release(jobSitePos.pos());
                        DebugPackets.sendPoiTicketCountPacket(serverWorld, jobSitePos.pos());
                    }
                }
            });
        }
    }

    @Override
    protected float getStandingEyeHeight(@Nonnull Pose pose, @Nonnull EntityDimensions size) {
        return this.isBaby() ? 0.65F : 1.55F;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.NONE.get() && this.isTrading()) {
            this.resetAtumCustomer();
        }
    }

    @Override
    protected void stopTrading() { //Changed to custom one, to prevent issues
    }

    protected void resetAtumCustomer() {
        this.setTradingPlayer(null);
        this.resetAllSpecialPrices();
    }

    private void resetAllSpecialPrices() {
        for (MerchantOffer offer : this.getOffers()) {
            offer.resetSpecialPriceDiff();
        }
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        boolean shouldReset = this.getTradingPlayer() != null && player == null;
        super.setTradingPlayer(player);
        if (shouldReset) {
            this.resetAtumCustomer();
        }
    }

    @Override
    public void die(@Nonnull DamageSource cause) {
        super.die(cause);
        this.resetAtumCustomer();
    }

    @Nullable
    public Entity changeDimension(@Nonnull ServerLevel server, @Nonnull ITeleporter teleporter) {
        this.resetAtumCustomer();
        return super.changeDimension(server, teleporter);
    }

    @Override
    @Deprecated
    //Use getAtumVillagerData
    public VillagerData getVillagerData() {
        return getAtumVillagerData();
    }

    public AtumVillagerData getAtumVillagerData() {
        return this.entityData.get(ATUM_VILLAGER_DATA);
    }

    @Override
    public void setVillagerData(@Nonnull VillagerData data) {
        if (data instanceof AtumVillagerData) {
            this.setAtumVillagerData((AtumVillagerData) data);
        }
        //Ignore places where vanilla sets villager data
    }

    public void setAtumVillagerData(@Nonnull AtumVillagerData data) {
        this.entityData.set(ATUM_VILLAGER_DATA, data);
    }

    @Override
    public Villager getBreedOffspring(@Nonnull ServerLevel serverWorld, @Nonnull AgableMob partner) {
        AtumVillagerEntity atumVillagerEntity = new AtumVillagerEntity(AtumEntities.VILLAGER_MALE, serverWorld);
        if (serverWorld.random.nextDouble() >= 0.5D) {
            atumVillagerEntity = new AtumVillagerEntity(AtumEntities.VILLAGER_FEMALE, serverWorld);
        }
        if (partner instanceof AtumVillagerEntity) {
            Race childRace = serverWorld.random.nextDouble() <= 0.5D ? ((AtumVillagerEntity) partner).getAtumVillagerData().getRace() : this.getAtumVillagerData().getRace();
            atumVillagerEntity.setAtumVillagerData(atumVillagerEntity.getAtumVillagerData().withRace(childRace));
        }
        atumVillagerEntity.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(atumVillagerEntity.blockPosition()), MobSpawnType.BREEDING, null, null);
        return atumVillagerEntity;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        Race race = Race.getRandomRaceWeighted();
        this.setAtumVillagerData(this.getAtumVillagerData().withRace(race));
        this.setRandomVariant(race);

        if (reason == MobSpawnType.BREEDING) {
            this.setAtumVillagerData(this.getAtumVillagerData().withProfession(AtumVillagerProfession.NONE.get()));
        }

        return super.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getVariant() == -1) {
            Race race = this.getAtumVillagerData().getRace();
            this.setRandomVariant(race);
        }
    }

    public void setRandomVariant(Race race) {
        final int variant = Mth.nextInt(this.random, 1, race.getVariantAmount());
        this.setVariant(variant);
    }

    @Override
    @Nonnull
    public Component getName() {
        AtumVillagerData villagerData = this.getAtumVillagerData();
        ResourceLocation profName = villagerData.getAtumProfession().getRegistryName();
        return new TranslatableComponent(this.getType().getDescriptionId() + '.' + villagerData.getRace().getName() + "." + profName.getPath());
    }

    private void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.entityData.get(VARIANT);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            AtumVillagerData atumVillagerData = this.getAtumVillagerData();
            String gender = this.isFemale() ? "female" : "male";
            this.texturePath = new ResourceLocation(Atum.MOD_ID, "textures/entity/villager/" + atumVillagerData.getRace().getName() + "/" + gender + "_" + this.getVariant()) + ".png";
        }
        return this.texturePath;
    }

    @Override
    public void playWorkSound() {
        SoundEvent soundEvent = this.getAtumVillagerData().getAtumProfession().getSound();
        if (soundEvent != null) {
            this.playSound(soundEvent, this.getSoundVolume(), this.getVoicePitch());
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        AtumVillagerData.CODEC.encodeStart(NbtOps.INSTANCE, this.getAtumVillagerData()).resultOrPartial(LOGGER::error).ifPresent((data) -> {
            compound.put("AtumVillagerData", data);
        });
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("AtumVillagerData", 10)) {
            DataResult<AtumVillagerData> dataresult = AtumVillagerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, compound.get("AtumVillagerData")));
            dataresult.resultOrPartial(LOGGER::error).ifPresent(this::setAtumVillagerData);
        }
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    protected void increaseMerchantCareer() {
        this.setAtumVillagerData(this.getAtumVillagerData().setLevel(this.getAtumVillagerData().getLevel() + 1));
        this.updateTrades();
    }

    private boolean isHungry() {
        return this.foodLevel < 12;
    }

    @Override
    protected void eatUntilFull() {
        if (isHungry() && this.countFoodPointsInInventory() != 0) {
            for (int i = 0; i < this.getInventory().getContainerSize(); ++i) {
                ItemStack stack = this.getInventory().getItem(i);
                if (!stack.isEmpty()) {
                    Integer integer = FOOD_VALUES.get(stack.getItem());
                    if (integer != null) {
                        int count = stack.getCount();
                        for (int k = count; k > 0; --k) {
                            this.foodLevel = (byte) (this.foodLevel + integer);
                            this.getInventory().removeItem(i, 1);
                            if (!this.isHungry()) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected int countFoodPointsInInventory() {
        SimpleContainer inventory = this.getInventory();
        return FOOD_VALUES.entrySet().stream().mapToInt((foodValueEntry) -> inventory.countItem(foodValueEntry.getKey()) * foodValueEntry.getValue()).sum();
    }


    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        Item item = stack.getItem();
        AtumVillagerProfession profession = this.getAtumVillagerData().getAtumProfession();
        return (ALLOWED_INVENTORY_ITEMS.contains(item) || (profession.getSpecificItems().contains(item) || (profession == AtumVillagerProfession.CURATOR.get() && (AtumVillagerTasks.canCuratorPickup(this, stack) || ALLOWED_INVENTORY_ITEMS.contains(item)))) && this.getInventory().canAddItem(stack));
    }

    @Override
    public boolean hasFarmSeeds() {
        return super.hasFarmSeeds() || this.getInventory().hasAnyOf(ImmutableSet.of(AtumItems.EMMER_SEEDS, AtumItems.FLAX_SEEDS));
    }

    @Override
    protected void updateTrades() {
        AtumVillagerData data = this.getAtumVillagerData();
        Int2ObjectMap<VillagerTrades.ItemListing[]> map = AtumVillagerTrades.VILLAGER_DEFAULT_TRADES.get(data.getAtumProfession());
        if (map != null && !map.isEmpty()) {
            VillagerTrades.ItemListing[] trades = map.get(data.getLevel());
            if (trades != null) {
                MerchantOffers offers = this.getOffers();
                this.addOffersFromItemListings(offers, trades, 2);
            }
        }
    }

    @Override
    protected void pickUpItem(@Nonnull ItemEntity itemEntity) {
        if (this.getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.CURATOR.get()) {
            this.onItemPickup(itemEntity);
            AtumVillagerTasks.putInHand(this, itemEntity);
        } else {
            super.pickUpItem(itemEntity);
        }
    }

    public void setOffHand(@Nonnull ItemStack stack) {
        if (stack.getItem().is(AtumAPI.Tags.RELIC_NON_DIRTY)) {
            this.setItemSlot(EquipmentSlot.OFFHAND, stack);
            this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
        } else {
            this.setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, stack);
        }
    }
}