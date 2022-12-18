package com.teammetallurgy.atum.entity.undead;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ITexture;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import com.teammetallurgy.atum.entity.animal.TarantulaEntity;
import com.teammetallurgy.atum.entity.bandit.BanditBaseEntity;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class UndeadBaseEntity extends Monster implements ITexture {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(UndeadBaseEntity.class, EntityDataSerializers.INT);
    private String texturePath;

    public UndeadBaseEntity(EntityType<? extends UndeadBaseEntity> entityType, Level world) {
        super(entityType, world);
        new GroundPathNavigation(this, world).getNodeEvaluator().setCanPassDoors(true);
    }

    boolean hasSkinVariants() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.applyEntityAI();
    }

    void applyEntityAI() {
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, UndeadBaseEntity.class));
        boolean checkSight = this.canChangeDimensions();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, checkSight));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, BanditBaseEntity.class, checkSight));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, StoneBaseEntity.class, checkSight));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, DesertWolfEntity.class, checkSight));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Wolf.class, checkSight));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ZombifiedPiglin.class, checkSight));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, TarantulaEntity.class, checkSight));
    }

    @Override
    public boolean canAttackType(@Nonnull EntityType<?> type) {
        return type != this.getType() && super.canAttackType(type);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (this.hasSkinVariants()) {
            this.entityData.define(VARIANT, 0);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag nbt) {
        livingData = super.finalizeSpawn(world, difficulty, spawnReason, livingData, nbt);

        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * difficulty.getSpecialMultiplier());

        if (this.hasSkinVariants() && spawnReason != MobSpawnType.CONVERSION) {
            final int variant = Mth.nextInt(world.getRandom(), 0, this.getVariantAmount());
            this.setVariantWithAbilities(variant, this.random, difficulty);
        }
        return livingData;
    }

    int getVariantAmount() {
        return 6;
    }

    public void setVariantWithAbilities(int variant, RandomSource randomSource, DifficultyInstance difficulty) {
        this.setVariant(variant);
        this.setVariantAbilities(difficulty, randomSource, variant);
    }

    void setVariantAbilities(DifficultyInstance difficulty, RandomSource randomSource, int variant) {
    }

    @Override
    public boolean canBeAffected(@Nonnull MobEffectInstance potionEffect) {
        return potionEffect.getEffect() != MobEffects.POISON && super.canBeAffected(potionEffect);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.entityData.isDirty()) {
            //this.entityData.clearDirty(); //TODO?
            this.texturePath = null;
        }
    }

    @Override
    public void aiStep() {
        if (this.isAlive() && this.shouldBurnInDay() && this.isSunBurnTick()) {
            this.setSecondsOnFire(8);
        }
        super.aiStep();
    }

    @Override
    public void baseTick() {
        if (this.getRemainingFireTicks() > 0) {
            int fire = this.getRemainingFireTicks();
            if (!this.fireImmune()) {
                if (this.getRemainingFireTicks() % 20 == 0) {
                    this.hurt(DamageSource.ON_FIRE, getBurnDamage());
                }
                --fire;
                this.setRemainingFireTicks(fire);
            }
        }
        super.baseTick();
    }

    float getBurnDamage() {
        return 1.0F;
    }

    boolean shouldBurnInDay() {
        return true;
    }

    @Override
    @Nonnull
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public boolean checkSpawnRules(@Nonnull LevelAccessor world, @Nonnull MobSpawnType spawnReason) {
        return spawnReason == MobSpawnType.SPAWNER || super.checkSpawnRules(world, spawnReason);
    }

    public static boolean canSpawn(EntityType<? extends UndeadBaseEntity> undeadBase, ServerLevelAccessor levelAccessor, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        return (spawnReason == MobSpawnType.SPAWNER || pos.getY() > 62) && checkMonsterSpawnRules(undeadBase, levelAccessor, spawnReason, pos, random);
    }

    void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
        this.texturePath = null;
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            String entityName = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(this.getType())).getPath();

            if (this.hasSkinVariants()) {
                this.texturePath = new ResourceLocation(Atum.MOD_ID, "textures/entity/" + entityName + "_" + this.getVariant()) + ".png";
            } else {
                this.texturePath = new ResourceLocation(Atum.MOD_ID, "textures/entity/" + entityName) + ".png";
            }
        }
        return this.texturePath;
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.hasSkinVariants()) {
            compound.putInt("Variant", this.getVariant());
        }
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (this.hasSkinVariants()) {
            this.setVariant(compound.getInt("Variant"));
        }
    }
}