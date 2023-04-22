package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class BrigandEntity extends BanditBaseEntity {

    public BrigandEntity(EntityType<? extends BrigandEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 8;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return getBaseAttributes().add(Attributes.MAX_HEALTH, 18.0D).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.ARMOR, 4.0F);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, @Nonnull DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(AtumItems.SCIMITAR_IRON.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(AtumItems.BRIGAND_SHIELD.get()));
    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity entity) {
        if (!super.doHurtTarget(entity)) {
            return false;
        } else {
            if (this.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == AtumItems.GREATSWORD_IRON.get()) {
                float attackDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                float knockback = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);

                if (entity instanceof LivingEntity) {
                    attackDamage += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entity).getMobType());
                    knockback += EnchantmentHelper.getKnockbackBonus(this);
                }

                int fireAspect = EnchantmentHelper.getFireAspect(this);
                if (fireAspect > 0) {
                    entity.setSecondsOnFire(fireAspect * 4);
                }

                boolean attackEntity = entity.hurt(DamageSource.mobAttack(this), attackDamage);
                if (attackEntity) {
                    if (knockback > 0.0F && entity instanceof LivingEntity) {
                        ((LivingEntity) entity).knockback(knockback * 0.5F, Mth.sin(this.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(this.getYRot() * ((float) Math.PI / 180F)));
                        entity.push(-Mth.sin(this.getYRot() * (float) Math.PI / 180.0F) * knockback * 0.5F, 0.1D, Mth.cos(this.getYRot() * (float) Math.PI / 180.0F) * knockback * 0.5F);
                        this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                    }

                    int j = EnchantmentHelper.getFireAspect(this);
                    if (j > 0) {
                        entity.setSecondsOnFire(j * 4);
                    }
                    this.doEnchantDamageEffects(this, entity);
                }
                return attackEntity;
            }
            return true;
        }
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 3;
    }
}