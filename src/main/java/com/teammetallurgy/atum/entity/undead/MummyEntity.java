package com.teammetallurgy.atum.entity.undead;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.UUID;

public class MummyEntity extends UndeadBaseEntity {
    private static final AttributeModifier SPEED_BOOST_BURNING = new AttributeModifier(UUID.fromString("2dc2358a-63df-435d-a602-2ff3d6bca8d1"), "Burning speed boost", 0.1D, AttributeModifier.Operation.ADDITION);

    public MummyEntity(EntityType<? extends UndeadBaseEntity> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(false);
        this.xpReward = 8;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 22.0F).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 30.0D).add(Attributes.ARMOR, 2.0F);
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (this.isOnFire() && !attribute.hasModifier(SPEED_BOOST_BURNING)) {
            attribute.addTransientModifier(SPEED_BOOST_BURNING);
        } else {
            attribute.removeModifier(SPEED_BOOST_BURNING);
        }
    }

    @Override
    protected float getBurnDamage() {
        return 2.0F;
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            amount += 1;
        }
        if (this.isOnFire()) {
            amount = (int) (amount * 1.5);
        }

        return super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity entity) {
        boolean attackEntity = super.doHurtTarget(entity);

        if (attackEntity) {
            if (this.isOnFire() && this.random.nextFloat() < (float) this.level.getDifficulty().getId() * 0.4F) {
                entity.setSecondsOnFire(2 * this.level.getDifficulty().getId());
            }
            if (entity instanceof LivingEntity) {
                LivingEntity base = (LivingEntity) entity;
                base.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1));
            }
        }
        return attackEntity;
    }
}