package com.teammetallurgy.atum.entity.undead;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.UUID;

public class MummyEntity extends UndeadBaseEntity {
    private static final AttributeModifier SPEED_BOOST_BURNING = new AttributeModifier(UUID.fromString("2dc2358a-63df-435d-a602-2ff3d6bca8d1"), "Burning speed boost", 0.1D, AttributeModifier.Operation.ADDITION);

    public MummyEntity(EntityType<? extends UndeadBaseEntity> entityType, World world) {
        super(entityType, world);
        this.experienceValue = 8;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
    }

    public static AttributeModifierMap.MutableAttribute getAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 22.0F).createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 30.0D).createMutableAttribute(Attributes.ARMOR, 2.0F);
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public void livingTick() {
        super.livingTick();

        ModifiableAttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (this.isBurning() && !attribute.hasModifier(SPEED_BOOST_BURNING)) {
            attribute.applyNonPersistentModifier(SPEED_BOOST_BURNING);
        } else {
            attribute.removeModifier(SPEED_BOOST_BURNING);
        }
    }

    @Override
    protected float getBurnDamage() {
        return 2.0F;
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (source.isFireDamage()) {
            amount += 1;
        }
        if (this.isBurning()) {
            amount = (int) (amount * 1.5);
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entity) {
        boolean attackEntity = super.attackEntityAsMob(entity);

        if (attackEntity) {
            if (this.isBurning() && this.rand.nextFloat() < (float) this.world.getDifficulty().getId() * 0.4F) {
                entity.setFire(2 * this.world.getDifficulty().getId());
            }
            if (entity instanceof LivingEntity) {
                LivingEntity base = (LivingEntity) entity;
                base.addPotionEffect(new EffectInstance(Effects.WITHER, 80, 1));
            }
        }
        return attackEntity;
    }
}