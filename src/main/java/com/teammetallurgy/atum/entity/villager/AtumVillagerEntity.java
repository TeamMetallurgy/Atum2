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
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

public class AtumVillagerEntity extends VillagerEntity implements ITexture {
    private static final DataParameter<AtumVillagerData> ATUM_VILLAGER_DATA = EntityDataManager.createKey(AtumVillagerEntity.class, AtumDataSerializer.VILLAGER_DATA);
    public static final Map<Item, Integer> FOOD_VALUES = ImmutableMap.of(AtumItems.EMMER_BREAD, 4, Items.BREAD, 4, Items.POTATO, 1, Items.CARROT, 1, Items.BEETROOT, 1);
    private static final Set<Item> ALLOWED_INVENTORY_ITEMS = ImmutableSet.of(AtumItems.EMMER_BREAD, AtumItems.EMMER, AtumItems.EMMER_SEEDS, Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(AtumVillagerEntity.class, DataSerializers.VARINT);
    private String texturePath;
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.OPENED_DOORS, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM); //No changes
    private static final ImmutableList<SensorType<? extends Sensor<? super VillagerEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, AtumSensorTypes.SECONDARY_POIS.get(), SensorType.GOLEM_DETECTED); //Added Atum secondary pois
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<AtumVillagerEntity, PointOfInterestType>> JOB_SITE_PREDICATE_MAP = ImmutableMap.of(MemoryModuleType.HOME, (villager, poiType) -> {
        return poiType == PointOfInterestType.HOME;
    }, MemoryModuleType.JOB_SITE, (villager, poiType) -> {
        return villager.getAtumVillagerData().getAtumProfession().getPointOfInterest() == poiType;
    }, MemoryModuleType.POTENTIAL_JOB_SITE, (villager, poiType) -> {
        return AtumPointsOfInterest.ANY_VILLAGER_WORKSTATION.test(poiType);
    }, MemoryModuleType.MEETING_POINT, (villager, poiType) -> {
        return poiType == PointOfInterestType.MEETING;
    });

    public AtumVillagerEntity(EntityType<? extends AtumVillagerEntity> type, World world) {
        super(type, world, VillagerType.DESERT); //Type not used, by Atum villagers
        this.setAtumVillagerData(this.getAtumVillagerData().withProfession(AtumVillagerProfession.NONE.get()));
    }

    public boolean isFemale() {
        return this.getType() == AtumEntities.VILLAGER_FEMALE;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ATUM_VILLAGER_DATA, new AtumVillagerData(AtumVillagerProfession.NONE.get(), 1, Race.HUMAN));
        this.dataManager.register(VARIANT, 0);
    }

    @Override
    @Nonnull
    protected Brain<?> createBrain(@Nonnull Dynamic<?> dynamic) {
        Brain<VillagerEntity> brain = this.getBrainCodec().deserialize(dynamic);
        this.initBrain(brain);
        return brain;
    }

    @Override
    public void resetBrain(@Nonnull ServerWorld serverWorld) {
        Brain<VillagerEntity> brain = this.getBrain();
        brain.stopAllTasks(serverWorld, this);
        this.brain = brain.copy();
        this.initBrain(this.getBrain());
    }

    @Override
    @Nonnull
    protected Brain.BrainCodec<VillagerEntity> getBrainCodec() {
        return Brain.createCodec(MEMORY_TYPES, SENSOR_TYPES);
    }

    private void initBrain(Brain<VillagerEntity> brain) {
        AtumVillagerProfession profession = this.getAtumVillagerData().getAtumProfession();
        EntityType<? extends AtumVillagerEntity> entityType = (EntityType<? extends AtumVillagerEntity>) this.getType();

        if (this.isChild()) {
            brain.setSchedule(Schedule.VILLAGER_BABY);
            brain.registerActivity(Activity.PLAY, AtumVillagerTasks.play(entityType, 0.5F));
        } else {
            brain.setSchedule(Schedule.VILLAGER_DEFAULT);
            brain.registerActivity(Activity.WORK, AtumVillagerTasks.work(entityType, profession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryModuleStatus.VALUE_PRESENT)));
        }

        brain.registerActivity(Activity.CORE, AtumVillagerTasks.core(entityType, profession, 0.5F));
        brain.registerActivity(Activity.MEET, AtumVillagerTasks.meet(entityType, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryModuleStatus.VALUE_PRESENT)));
        brain.registerActivity(Activity.REST, AtumVillagerTasks.rest(entityType, 0.5F));
        brain.registerActivity(Activity.IDLE, AtumVillagerTasks.idle(entityType, 0.5F));
        brain.registerActivity(Activity.PANIC, AtumVillagerTasks.panic(entityType, 0.5F));
        brain.registerActivity(Activity.HIDE, AtumVillagerTasks.hide(entityType, 0.5F));
        brain.setDefaultActivities(ImmutableSet.of(Activity.CORE));
        brain.setFallbackActivity(Activity.IDLE);
        brain.switchTo(Activity.IDLE);
        brain.updateActivity(this.world.getDayTime(), this.world.getGameTime());
    }

    @Override
    public void resetMemoryPoint(@Nonnull MemoryModuleType<GlobalPos> moduleType) {
        if (this.world instanceof ServerWorld) {
            MinecraftServer server = ((ServerWorld) this.world).getServer();
            this.brain.getMemory(moduleType).ifPresent((jobSitePos) -> {
                ServerWorld serverWorld = server.getWorld(jobSitePos.getDimension());
                if (serverWorld != null) {
                    PointOfInterestManager posManger = serverWorld.getPointOfInterestManager();
                    Optional<PointOfInterestType> optional = posManger.getType(jobSitePos.getPos());
                    BiPredicate<AtumVillagerEntity, PointOfInterestType> p = JOB_SITE_PREDICATE_MAP.get(moduleType);
                    if (optional.isPresent() && p.test(this, optional.get())) {
                        posManger.release(jobSitePos.getPos());
                        DebugPacketSender.func_218801_c(serverWorld, jobSitePos.getPos());
                    }
                }
            });
        }
    }

    @Override
    protected float getStandingEyeHeight(@Nonnull Pose pose, @Nonnull EntitySize size) {
        return this.isChild() ? 0.65F : 1.55F;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.NONE.get() && this.hasCustomer()) {
            this.resetAtumCustomer();
        }
    }

    @Override
    protected void resetCustomer() { //Changed to custom one, to prevent issues
    }

    protected void resetAtumCustomer() {
        this.setCustomer(null);
        this.resetAllSpecialPrices();
    }

    private void resetAllSpecialPrices() {
        for (MerchantOffer offer : this.getOffers()) {
            offer.resetSpecialPrice();
        }
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity player) {
        boolean shouldReset = this.getCustomer() != null && player == null;
        super.setCustomer(player);
        if (shouldReset) {
            this.resetAtumCustomer();
        }
    }

    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        super.onDeath(cause);
        this.resetAtumCustomer();
    }

    @Nullable
    public Entity changeDimension(@Nonnull ServerWorld server, @Nonnull ITeleporter teleporter) {
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
        return this.dataManager.get(ATUM_VILLAGER_DATA);
    }

    @Override
    public void setVillagerData(@Nonnull VillagerData data) {
        if (data instanceof AtumVillagerData) {
            this.setAtumVillagerData((AtumVillagerData) data);
        }
        //Ignore places where vanilla sets villager data
    }

    public void setAtumVillagerData(@Nonnull AtumVillagerData data) {
        this.dataManager.set(ATUM_VILLAGER_DATA, data);
    }

    @Override
    public VillagerEntity func_241840_a(@Nonnull ServerWorld serverWorld, @Nonnull AgeableEntity partner) {
        AtumVillagerEntity atumVillagerEntity = new AtumVillagerEntity(AtumEntities.VILLAGER_MALE, serverWorld);
        if (serverWorld.rand.nextDouble() >= 0.5D) {
            atumVillagerEntity = new AtumVillagerEntity(AtumEntities.VILLAGER_FEMALE, serverWorld);
        }
        if (partner instanceof AtumVillagerEntity) {
            Race childRace = serverWorld.rand.nextDouble() <= 0.5D ? ((AtumVillagerEntity) partner).getAtumVillagerData().getRace() : this.getAtumVillagerData().getRace();
            atumVillagerEntity.setAtumVillagerData(atumVillagerEntity.getAtumVillagerData().withRace(childRace));
        }
        atumVillagerEntity.onInitialSpawn(serverWorld, serverWorld.getDifficultyForLocation(atumVillagerEntity.getPosition()), SpawnReason.BREEDING, null, null);
        return atumVillagerEntity;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(@Nonnull IServerWorld world, @Nonnull DifficultyInstance difficulty, @Nonnull SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
        Race race = Race.getRandomRaceWeighted();
        this.setAtumVillagerData(this.getAtumVillagerData().withRace(race));
        this.setRandomVariant(race);

        if (reason == SpawnReason.BREEDING) {
            this.setAtumVillagerData(this.getAtumVillagerData().withProfession(AtumVillagerProfession.NONE.get()));
        }

        return super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
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
        final int variant = MathHelper.nextInt(this.rand, 1, race.getVariantAmount());
        this.setVariant(variant);
    }

    @Override
    @Nonnull
    public ITextComponent getName() {
        AtumVillagerData villagerData = this.getAtumVillagerData();
        ResourceLocation profName = villagerData.getAtumProfession().getRegistryName();
        return new TranslationTextComponent(this.getType().getTranslationKey() + '.' + villagerData.getRace().getName() + "." + profName.getPath());
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
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
    public void playWorkstationSound() {
        SoundEvent soundEvent = this.getAtumVillagerData().getAtumProfession().getSound();
        if (soundEvent != null) {
            this.playSound(soundEvent, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        AtumVillagerData.CODEC.encodeStart(NBTDynamicOps.INSTANCE, this.getAtumVillagerData()).resultOrPartial(LOGGER::error).ifPresent((data) -> {
            compound.put("AtumVillagerData", data);
        });
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("AtumVillagerData", 10)) {
            DataResult<AtumVillagerData> dataresult = AtumVillagerData.CODEC.parse(new Dynamic<>(NBTDynamicOps.INSTANCE, compound.get("AtumVillagerData")));
            dataresult.resultOrPartial(LOGGER::error).ifPresent(this::setAtumVillagerData);
        }
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    protected void levelUp() {
        this.setAtumVillagerData(this.getAtumVillagerData().withLevel(this.getAtumVillagerData().getLevel() + 1));
        this.populateTradeData();
    }

    private boolean isHungry() {
        return this.foodLevel < 12;
    }

    @Override
    protected void eat() {
        if (isHungry() && this.getFoodValueFromInventory() != 0) {
            for (int i = 0; i < this.getVillagerInventory().getSizeInventory(); ++i) {
                ItemStack stack = this.getVillagerInventory().getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Integer integer = FOOD_VALUES.get(stack.getItem());
                    if (integer != null) {
                        int count = stack.getCount();
                        for (int k = count; k > 0; --k) {
                            this.foodLevel = (byte) (this.foodLevel + integer);
                            this.getVillagerInventory().decrStackSize(i, 1);
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
    protected int getFoodValueFromInventory() {
        Inventory inventory = this.getVillagerInventory();
        return FOOD_VALUES.entrySet().stream().mapToInt((foodValueEntry) -> inventory.count(foodValueEntry.getKey()) * foodValueEntry.getValue()).sum();
    }


    @Override
    public boolean func_230293_i_(ItemStack stack) {
        Item item = stack.getItem();
        AtumVillagerProfession profession = this.getAtumVillagerData().getAtumProfession();
        return (ALLOWED_INVENTORY_ITEMS.contains(item) || (profession.getSpecificItems().contains(item) || (profession == AtumVillagerProfession.CURATOR.get() && AtumVillagerTasks.canCuratorPickup(this, stack))) && this.getVillagerInventory().func_233541_b_(stack));
    }

    @Override
    public boolean isFarmItemInInventory() {
        return super.isFarmItemInInventory() || this.getVillagerInventory().hasAny(ImmutableSet.of(AtumItems.EMMER_SEEDS, AtumItems.FLAX_SEEDS));
    }

    @Override
    protected void populateTradeData() {
        AtumVillagerData data = this.getAtumVillagerData();
        Int2ObjectMap<VillagerTrades.ITrade[]> map = AtumVillagerTrades.VILLAGER_DEFAULT_TRADES.get(data.getAtumProfession());
        if (map != null && !map.isEmpty()) {
            VillagerTrades.ITrade[] trades = map.get(data.getLevel());
            if (trades != null) {
                MerchantOffers offers = this.getOffers();
                this.addTrades(offers, trades, 2);
            }
        }
    }

    @Override
    protected void updateEquipmentIfNeeded(@Nonnull ItemEntity itemEntity) {
        if (this.getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.CURATOR.get()) {
            this.triggerItemPickupTrigger(itemEntity);
            AtumVillagerTasks.putInHand(this, itemEntity);
        } else {
            super.updateEquipmentIfNeeded(itemEntity);
        }
    }

    public void setOffHand(@Nonnull ItemStack stack) {
        if (stack.getItem().isIn(AtumAPI.Tags.RELIC_NON_DIRTY)) {
            this.setItemStackToSlot(EquipmentSlotType.OFFHAND, stack);
            this.func_233663_d_(EquipmentSlotType.OFFHAND);
        } else {
            this.func_233657_b_(EquipmentSlotType.OFFHAND, stack);
        }
    }
}