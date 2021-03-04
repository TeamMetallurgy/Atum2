package com.teammetallurgy.atum.entity.villager;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ITexture;
import com.teammetallurgy.atum.entity.ai.brain.task.AtumVillagerTasks;
import com.teammetallurgy.atum.init.AtumDataSerializer;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AtumVillagerEntity extends VillagerEntity implements ITexture {
    private static final DataParameter<AtumVillagerData> ATUM_VILLAGER_DATA = EntityDataManager.createKey(AtumVillagerEntity.class, AtumDataSerializer.VILLAGER_DATA);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(AtumVillagerEntity.class, DataSerializers.VARINT);
    private String texturePath;

    public AtumVillagerEntity(EntityType<? extends AtumVillagerEntity> type, World world) {
        super(type, world, VillagerType.DESERT); //Type not used, by Atum villagers
        this.setVillagerData(((AtumVillagerData) this.getVillagerData()).withGender(type == AtumEntities.VILLAGER_FEMALE).withProfession(VillagerProfession.NONE));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ATUM_VILLAGER_DATA, new AtumVillagerData(VillagerProfession.NONE, 1, Race.HUMAN, false));
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

    private void initBrain(Brain<VillagerEntity> brain) {
        VillagerProfession profession = this.getVillagerData().getProfession();
        EntityType<? extends AtumVillagerEntity> entityType = (EntityType<? extends AtumVillagerEntity>) this.getType();
        if (this.isChild()) {
            brain.setSchedule(Schedule.VILLAGER_BABY);
            brain.registerActivity(Activity.PLAY, AtumVillagerTasks.play(entityType, 0.5F));
        } else {
            brain.setSchedule(Schedule.VILLAGER_DEFAULT);
            brain.registerActivity(Activity.WORK, AtumVillagerTasks.work(entityType, profession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryModuleStatus.VALUE_PRESENT)));
        }

        brain.registerActivity(Activity.CORE, AtumVillagerTasks.core(entityType, profession, 0.5F));
        brain.registerActivity(Activity.MEET, AtumVillagerTasks.meet(entityType, profession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryModuleStatus.VALUE_PRESENT)));
        brain.registerActivity(Activity.REST, AtumVillagerTasks.rest(entityType, profession, 0.5F));
        brain.registerActivity(Activity.IDLE, AtumVillagerTasks.idle(entityType, profession, 0.5F));
        brain.registerActivity(Activity.PANIC, AtumVillagerTasks.panic(entityType, profession, 0.5F));
        brain.registerActivity(Activity.HIDE, AtumVillagerTasks.hide(entityType, profession, 0.5F));
        brain.setDefaultActivities(ImmutableSet.of(Activity.CORE));
        brain.setFallbackActivity(Activity.IDLE);
        brain.switchTo(Activity.IDLE);
        brain.updateActivity(this.world.getDayTime(), this.world.getGameTime());
    }

    @Override
    @Nonnull
    public VillagerData getVillagerData() {
        return this.dataManager.get(ATUM_VILLAGER_DATA);
    }

    @Override
    public void setVillagerData(@Nonnull VillagerData data) {
        if (data instanceof AtumVillagerData) {
            AtumVillagerData atumVillagerData = (AtumVillagerData) this.getVillagerData();
            if (atumVillagerData.getProfession() != data.getProfession()) {
                this.offers = null;
            }
            this.dataManager.set(ATUM_VILLAGER_DATA, (AtumVillagerData) data);
        }
    }

    @Override
    public VillagerEntity func_241840_a(@Nonnull ServerWorld serverWorld, @Nonnull AgeableEntity partner) {
        AtumVillagerEntity atumVillagerEntity = new AtumVillagerEntity(AtumEntities.VILLAGER_MALE, serverWorld);
        if (serverWorld.rand.nextDouble() >= 0.5D) {
            atumVillagerEntity = new AtumVillagerEntity(AtumEntities.VILLAGER_FEMALE, serverWorld);
        }
        if (partner instanceof AtumVillagerEntity) {
            Race childRace = serverWorld.rand.nextDouble() <= 0.5D ? ((AtumVillagerData) ((AtumVillagerEntity) partner).getVillagerData()).getRace() : ((AtumVillagerData) this.getVillagerData()).getRace();
            atumVillagerEntity.setVillagerData(((AtumVillagerData) atumVillagerEntity.getVillagerData()).withRace(childRace));
        }
        atumVillagerEntity.onInitialSpawn(serverWorld, serverWorld.getDifficultyForLocation(atumVillagerEntity.getPosition()), SpawnReason.BREEDING, null, null);
        return atumVillagerEntity;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(@Nonnull IServerWorld world, @Nonnull DifficultyInstance difficulty, @Nonnull SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
        final int variant = MathHelper.nextInt(this.rand, 1, getVariantAmount());
        this.setVariant(variant);
        this.setVillagerData(((AtumVillagerData) this.getVillagerData()).withRace(Race.getRandomRaceWeighted()));
        return super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
    }

    @Override
    @Nonnull
    public ITextComponent getName() {
        AtumVillagerData villagerData = (AtumVillagerData) this.getVillagerData();
        ResourceLocation profName = villagerData.getProfession().getRegistryName();
        return new TranslationTextComponent(this.getType().getTranslationKey() + '.' + villagerData.getRace().getName() + "." + (!"minecraft".equals(profName.getNamespace()) ? profName.getNamespace() + '.' : "") + profName.getPath());
    }

    protected int getVariantAmount() {
        return 6;
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
            AtumVillagerData atumVillagerData = (AtumVillagerData) this.getVillagerData();
            String gender = atumVillagerData.isFemale() ? "female" : "male";
            this.texturePath = new ResourceLocation(Atum.MOD_ID, "textures/entity/villager/" + atumVillagerData.getRace().getName() + "/" + gender + "_" + this.getVariant()) + ".png";
        }
        return this.texturePath;
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        AtumVillagerData.CODEC.encodeStart(NBTDynamicOps.INSTANCE, (AtumVillagerData) this.getVillagerData()).resultOrPartial(LOGGER::error).ifPresent((data) -> {
            compound.put("AtumVillagerData", data);
        });
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("AtumVillagerData", 10)) {
            DataResult<AtumVillagerData> dataresult = AtumVillagerData.CODEC.parse(new Dynamic<>(NBTDynamicOps.INSTANCE, compound.get("AtumVillagerData")));
            dataresult.resultOrPartial(LOGGER::error).ifPresent(this::setVillagerData);
        }
        this.setVariant(compound.getInt("Variant"));
    }
}