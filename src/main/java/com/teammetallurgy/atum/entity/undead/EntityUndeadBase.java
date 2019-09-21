package com.teammetallurgy.atum.entity.undead;

import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import com.teammetallurgy.atum.entity.animal.EntityTarantula;
import com.teammetallurgy.atum.entity.bandit.EntityBanditBase;
import com.teammetallurgy.atum.entity.efreet.EntityEfreetBase;
import com.teammetallurgy.atum.entity.stone.EntityStoneBase;
import com.teammetallurgy.atum.integration.champion.ChampionsHelper;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class EntityUndeadBase extends MonsterEntity {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityUndeadBase.class, DataSerializers.VARINT);
    private String texturePath;

    public EntityUndeadBase(World world) {
        super(world);
        new PathNavigateGround(this, world).setEnterDoors(true);
    }

    boolean hasSkinVariants() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(6, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.addGoal(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.goalSelector.addGoal(8, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    void applyEntityAI() {
        this.targetSelector.addGoal(0, new EntityAIHurtByTarget(this, false, EntityUndeadBase.class));
        this.targetSelector.addGoal(1, new EntityAINearestAttackableTarget<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(1, new EntityAINearestAttackableTarget<>(this, EntityBanditBase.class, true));
        this.targetSelector.addGoal(1, new EntityAINearestAttackableTarget<>(this, EntityStoneBase.class, true));
        this.targetSelector.addGoal(1, new EntityAINearestAttackableTarget<>(this, EntityEfreetBase.class, true));
        this.targetSelector.addGoal(2, new EntityAINearestAttackableTarget<>(this, EntityDesertWolf.class, true));
        this.targetSelector.addGoal(2, new EntityAINearestAttackableTarget<>(this, EntityDesertWolf.class, true));
        this.targetSelector.addGoal(2, new EntityAINearestAttackableTarget<>(this, WolfEntity.class, true));
        this.targetSelector.addGoal(2, new EntityAINearestAttackableTarget<>(this, ZombiePigmanEntity.class, true));
        this.targetSelector.addGoal(2, new EntityAINearestAttackableTarget<>(this, EntityTarantula.class, true));
    }

    @Override
    public boolean canAttackClass(Class<? extends LivingEntity> cls) {
        return !EntityUndeadBase.class.isAssignableFrom(cls) && super.canAttackClass(cls);
    }

    @Override
    protected void registerData() {
        super.registerData();
        if (this.hasSkinVariants()) {
            this.dataManager.register(VARIANT, 0);
        }
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason spawnReason, @Nullable ILivingEntityData livingdata, @Nullable CompoundNBT nbt) {
        livingdata = super.onInitialSpawn(world, difficulty, spawnReason, livingdata, nbt);

        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        if (this.hasSkinVariants()) {
            final int variant = MathHelper.nextInt(world.getRandom(), 0, this.getVariantAmount());
            this.setVariant(variant);
            this.setVariantAbilities(difficulty, variant);
        }
        return livingdata;
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
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
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
        super.livingTick();
        if (this.world.isDaytime() && this.world.canBlockSeeSky(this.getPosition()) && !this.world.isRemote && !this.isImmuneToFire() && this.shouldBurnInDay()) {
            this.setFire(8);
        }
    }

    @Override
    public void baseTick() {
        if (this.func_223314_ad() > 0) {
            int fire = this.func_223314_ad();
            if (!this.isImmuneToFire()) {
                if (this.func_223314_ad() % 20 == 0) {
                    this.attackEntityFrom(DamageSource.ON_FIRE, getBurnDamage());
                }
                --fire;
                this.func_223308_g(fire);
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
    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(this.getBoundingBox().minY);
        if (i <= 62) {
            return false;
        } else {
            return canSpawnNoHeightCheck(false);
        }
    }

    public boolean canSpawnNoHeightCheck(boolean isFromSpawner) {
        return super.getCanSpawnHere() && this.world.checkNoEntityCollision(this.getBoundingBox()) && this.world.getCollisionBoxes(this, this.getBoundingBox()).isEmpty()
                && (isFromSpawner || !this.world.containsAnyLiquid(this.getBoundingBox()));
    }

    void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            String entityName = Objects.requireNonNull(this.getType().getRegistryName()).getPath();

            if (ChampionsHelper.isChampion(this)) {
                ResourceLocation texture = ChampionsHelper.getTexture(this, entityName);
                if (texture != null) {
                    this.texturePath = texture.toString();
                    return texturePath;
                }
            }

            if (this.hasSkinVariants()) {
                this.texturePath = new ResourceLocation(Constants.MOD_ID, "textures/entity/" + entityName + "_" + this.getVariant()) + ".png";
            } else {
                this.texturePath = new ResourceLocation(Constants.MOD_ID, "textures/entity/" + entityName) + ".png";
            }
        }
        return this.texturePath;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.hasSkinVariants()) {
            compound.putInt("Variant", this.getVariant());
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasSkinVariants()) {
            this.setVariant(compound.getInt("Variant"));
        }
    }
}