package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.entity.undead.UndeadBaseEntity;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;

public class TarantulaEntity extends MonsterEntity {
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(TarantulaEntity.class, DataSerializers.BYTE);

    public TarantulaEntity(EntityType<? extends MonsterEntity> entityType, World world) {
        super(entityType, world);
        this.experienceValue = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new AITarantulaAttack(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, TarantulaEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, UndeadBaseEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute getAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 15.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 36.0D);
    }

    @Override
    public boolean canSpawn(@Nonnull IWorld world, @Nonnull SpawnReason spawnReason) {
        return spawnReason == SpawnReason.SPAWNER || super.canSpawn(world, spawnReason);
    }

    public static boolean canSpawn(EntityType<? extends TarantulaEntity> tarantula, IServerWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return (spawnReason == SpawnReason.SPAWNER || pos.getY() >= 40 && pos.getY() <= 62 && !world.canBlockSeeSky(pos.down())) && canMonsterSpawnInLight(tarantula, world, spawnReason, pos, random) &&
                world instanceof ServerWorld && !StructureHelper.doesChunkHaveStructure((ServerWorld) world, pos, AtumStructures.PYRAMID_STRUCTURE);
    }

    @Override
    public double getMountedYOffset() {
        return this.getHeight() * 0.5F;
    }

    @Override
    @Nonnull
    protected PathNavigator createNavigator(@Nonnull World world) {
        return new ClimberPathNavigator(this, world);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(CLIMBING, (byte) 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    @Override
    @Nonnull
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entity) {
        if (super.attackEntityAsMob(entity)) {
            if (entity instanceof LivingEntity) {
                int i = 0;
                if (this.world.getDifficulty() == Difficulty.NORMAL) {
                    i = 5;
                } else if (this.world.getDifficulty() == Difficulty.HARD) {
                    i = 8;
                }
                if (i > 0) {
                    ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.WEAKNESS, i * 20, 0));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isPotionApplicable(@Nonnull EffectInstance effect) {
        return effect.getPotion() != Effects.POISON && super.isPotionApplicable(effect);
    }

    private boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING) & 1) != 0;
    }

    private void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.dataManager.get(CLIMBING);

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataManager.set(CLIMBING, b0);
    }

    @Override
    public float getEyeHeight(@Nonnull Pose pose) {
        return 0.35F;
    }

    static class AITarantulaAttack extends MeleeAttackGoal {
        AITarantulaAttack(TarantulaEntity tarantula) {
            super(tarantula, 1.0D, true);
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0F + attackTarget.getWidth();
        }
    }
}