package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.entity.ai.goal.OpenAnyDoorGoal;
import com.teammetallurgy.atum.entity.ai.pathfinding.ClimberGroundPathNavigator;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class AssassinEntity extends BanditBaseEntity {
    private final DamageSource ASSASSINATED = new EntityDamageSource("assassinated", this);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(AssassinEntity.class, DataSerializers.BYTE);
    private LivingEntity markedTarget;

    public AssassinEntity(EntityType<? extends AssassinEntity> entityType, World world) {
        super(entityType, world);
        this.experienceValue = 12;
        (new ClimberGroundPathNavigator(this, world)).setBreakDoors(true);
        this.setCanPatrol(false);
    }

    @Override
    protected boolean hasSkinVariants() {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MarkedForDeathGoal(this, this.markedTarget));
        this.goalSelector.addGoal(3, new OpenAnyDoorGoal(this, false, true));
        this.goalSelector.addGoal(4, new AssassinMeleeAttackGoal(this, 1.2D, true));
    }

    @Override
    protected void applyEntityAI() {
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, BanditBaseEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(CLIMBING, (byte) 0);
    }

    public static AttributeModifierMap.MutableAttribute getAttributes() {
        return getBaseAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 40.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D).createMutableAttribute(Attributes.ARMOR, 4.0F);
    }

    @Override
    @Nonnull
    protected PathNavigator createNavigator(@Nonnull World world) {
        return new ClimberGroundPathNavigator(this, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }

    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    private boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING) & 1) != 0;
    }

    private void setBesideClimbableBlock(boolean isClimbing) {
        byte climbing = this.dataManager.get(CLIMBING);

        if (isClimbing) {
            climbing = (byte) (climbing | 1);
        } else {
            climbing = (byte) (climbing & -2);
        }
        this.dataManager.set(CLIMBING, climbing);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(@Nonnull DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(AtumItems.POISON_DAGGER));
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entity) {
        if (!super.attackEntityAsMob(entity)) {
            return false;
        } else {
            if (this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == AtumItems.POISON_DAGGER && entity instanceof LivingEntity) {
                entity.attackEntityFrom(ASSASSINATED, (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                (((LivingEntity) entity)).addPotionEffect(new EffectInstance(Effects.POISON, 100, 1));
            }
            return true;
        }
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.markedTarget instanceof PlayerEntity) {
            compound.putUniqueId("MarkedForDeathTarget", this.markedTarget.getUniqueID());
        }
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        PlayerEntity playerEntity = this.world.getPlayerByUuid(compound.getUniqueId("MarkedForDeathTarget"));
        if (playerEntity != null) {
            this.markedTarget = playerEntity;
        }
    }

    public static boolean canSpawn(EntityType<? extends BanditBaseEntity> banditBase, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return spawnReason == SpawnReason.EVENT ? world.canBlockSeeSky(pos) && world.getLightFor(LightType.BLOCK, pos) <= 8 : BanditBaseEntity.canSpawn(banditBase, world, spawnReason, pos, random);
    }

    public void setMarkedTarget(LivingEntity livingEntity) {
        this.markedTarget = livingEntity;
        this.goalSelector.addGoal(1, new MarkedForDeathGoal(this, livingEntity)); //Set goal initially
    }

    public static class MarkedForDeathGoal extends TargetGoal {
        protected LivingEntity markedTarget;

        public MarkedForDeathGoal(MobEntity mob, @Nullable LivingEntity markedTarget) {
            super(mob, false);
            this.markedTarget = markedTarget;
        }

        @Override
        protected double getTargetDistance() {
            return 128.0D;
        }

        @Override
        public boolean shouldContinueExecuting() {
            LivingEntity target;
            LivingEntity revenge = this.goalOwner.getRevengeTarget();
            if (revenge != null) {
                target = revenge;
            } else {
                target = this.markedTarget;
                if (target == null) {
                    target = this.goalOwner.getAttackTarget();
                }
            }

            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                double distance = this.getTargetDistance();
                if (this.goalOwner.getDistanceSq(target) > distance * distance) {
                    return false;
                } else {
                    if (target instanceof PlayerEntity && ((PlayerEntity) target).abilities.disableDamage) {
                        return false;
                    } else {
                        this.goalOwner.setAttackTarget(target);
                        return true;
                    }
                }
            }
        }

        @Override
        public boolean shouldExecute() {
            return this.markedTarget != null;
        }

        @Override
        public void resetTask() {
            super.resetTask();
            this.markedTarget = null;
            ((AssassinEntity) this.goalOwner).markedTarget = null;
        }
    }

    public static class AssassinMeleeAttackGoal extends MeleeAttackGoal {

        public AssassinMeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
            super(creature, speedIn, useLongMemory);
        }

        @Override
        public void tick() {
            if (this.attacker != null && this.attacker.getAttackTarget() != null) {
                super.tick();
            }
        }
    }
}