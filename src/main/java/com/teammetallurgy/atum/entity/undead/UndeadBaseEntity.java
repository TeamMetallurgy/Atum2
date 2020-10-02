package com.teammetallurgy.atum.entity.undead;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ITexture;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import com.teammetallurgy.atum.entity.animal.TarantulaEntity;
import com.teammetallurgy.atum.entity.bandit.BanditBaseEntity;
import com.teammetallurgy.atum.entity.efreet.EfreetBaseEntity;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class UndeadBaseEntity extends MonsterEntity implements ITexture {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(UndeadBaseEntity.class, DataSerializers.VARINT);
    private String texturePath;

    public UndeadBaseEntity(EntityType<? extends UndeadBaseEntity> entityType, World world) {
        super(entityType, world);
        new GroundPathNavigator(this, world).getNodeProcessor().setCanEnterDoors(true);
    }

    boolean hasSkinVariants() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(6, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.applyEntityAI();
    }

    void applyEntityAI() {
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, UndeadBaseEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, BanditBaseEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, StoneBaseEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, EfreetBaseEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, DesertWolfEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, DesertWolfEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, WolfEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ZombifiedPiglinEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, TarantulaEntity.class, true));
    }

    @Override
    public boolean canAttack(@Nonnull EntityType<?> type) {
        return type != this.getType() && super.canAttack(type);
    }

    @Override
    protected void registerData() {
        super.registerData();
        if (this.hasSkinVariants()) {
            this.dataManager.register(VARIANT, 0);
        }
    }

    @Override
    public ILivingEntityData onInitialSpawn(@Nonnull IServerWorld world, @Nonnull DifficultyInstance difficulty, @Nonnull SpawnReason spawnReason, @Nullable ILivingEntityData livingData, @Nullable CompoundNBT nbt) {
        livingData = super.onInitialSpawn(world, difficulty, spawnReason, livingData, nbt);

        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        if (this.hasSkinVariants()) {
            final int variant = MathHelper.nextInt(world.getRandom(), 0, this.getVariantAmount());
            this.setVariant(variant);
            this.setVariantAbilities(difficulty, variant);
        }
        return livingData;
    }

    int getVariantAmount() {
        return 6;
    }

    void setVariantAbilities(DifficultyInstance difficulty, int variant) {
    }

    @Override
    public boolean isPotionApplicable(@Nonnull EffectInstance potionEffect) {
        return potionEffect.getPotion() != Effects.POISON && super.isPotionApplicable(potionEffect);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.texturePath = null;
        }
    }

    @Override
    public void livingTick() {
        if (this.isAlive() && this.shouldBurnInDay() && this.isInDaylight()) {
            this.setFire(8);
        }
        super.livingTick();
    }

    @Override
    public void baseTick() {
        if (this.getFireTimer() > 0) {
            int fire = this.getFireTimer();
            if (!this.isImmuneToFire()) {
                if (this.getFireTimer() % 20 == 0) {
                    this.attackEntityFrom(DamageSource.ON_FIRE, getBurnDamage());
                }
                --fire;
                this.forceFireTicks(fire);
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
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public boolean canSpawn(@Nonnull IWorld world, @Nonnull SpawnReason spawnReason) {
        return spawnReason == SpawnReason.SPAWNER || super.canSpawn(world, spawnReason);
    }

    public static boolean canSpawn(EntityType<? extends UndeadBaseEntity> undeadBase, IServerWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return (spawnReason == SpawnReason.SPAWNER || pos.getY() > 62) && canMonsterSpawnInLight(undeadBase, world, spawnReason, pos, random);
    }

    void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            String entityName = Objects.requireNonNull(this.getType().getRegistryName()).getPath();

            if (this.hasSkinVariants()) {
                this.texturePath = new ResourceLocation(Atum.MOD_ID, "textures/entity/" + entityName + "_" + this.getVariant()) + ".png";
            } else {
                this.texturePath = new ResourceLocation(Atum.MOD_ID, "textures/entity/" + entityName) + ".png";
            }
        }
        return this.texturePath;
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.hasSkinVariants()) {
            compound.putInt("Variant", this.getVariant());
        }
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasSkinVariants()) {
            this.setVariant(compound.getInt("Variant"));
        }
    }
}