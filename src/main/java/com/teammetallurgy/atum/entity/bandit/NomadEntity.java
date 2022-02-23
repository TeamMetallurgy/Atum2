package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.entity.ai.goal.CustomRangedBowAttackGoal;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NomadEntity extends BanditBaseEntity implements RangedAttackMob {
    private final CustomRangedBowAttackGoal<NomadEntity> aiArrowAttack = new CustomRangedBowAttackGoal<>(this, 0.75D, 35, 12.0F);
    private final MeleeAttackGoal aiAttackOnCollide = new MeleeAttackGoal(this, 1.0D, false) {
        @Override
        public void stop() {
            super.stop();
            NomadEntity.this.setAggressive(false);
        }

        @Override
        public void start() {
            super.start();
            NomadEntity.this.setAggressive(true);
        }
    };

    public NomadEntity(EntityType<? extends NomadEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 6;
        this.setCombatTask();
    }

    @Override
    protected int getVariantAmount() {
        return 6;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return getBaseAttributes().add(Attributes.MAX_HEALTH, 13.0D).add(Attributes.MOVEMENT_SPEED, 0.20D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.ARMOR, 2.0F);
    }

    @Override
    protected void populateDefaultEquipmentSlots(@Nonnull DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(AtumItems.SHORT_BOW));
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag nbt) {
        livingdata = super.finalizeSpawn(world, difficulty, spawnReason, livingdata, nbt);
        this.setCombatTask();

        return livingdata;
    }

    private void setCombatTask() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.aiAttackOnCollide);
            this.goalSelector.removeGoal(this.aiArrowAttack);
            ItemStack heldBow = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, AtumItems.SHORT_BOW));
            if (heldBow.getItem() instanceof BowItem) {
                int cooldown = 20;
                if (this.level.getDifficulty() != Difficulty.HARD) {
                    cooldown = 35;
                }
                this.aiArrowAttack.setAttackCooldown(cooldown);
                this.goalSelector.addGoal(4, this.aiArrowAttack);
            } else {
                this.goalSelector.addGoal(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public void performRangedAttack(@Nonnull LivingEntity target, float distanceFactor) {
        ItemStack ammo = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, AtumItems.SHORT_BOW)));
        AbstractArrow arrow = ProjectileUtil.getMobArrow(this, ammo, distanceFactor);
        if (this.getMainHandItem().getItem() instanceof BowItem) {
            arrow = ((BowItem) this.getMainHandItem().getItem()).customArrow(arrow);
        }
        double x = target.getX() - this.getX();
        double y = target.getY(0.3333333333333333D) - arrow.getY();
        double z = target.getZ() - this.getZ();
        double height = Mth.sqrt((float) (x * x + z * z));
        arrow.shoot(x, y + height * 0.2D, z, 1.6F, (float) (12 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setCombatTask();
    }

    @Override
    public void setItemSlot(@Nonnull EquipmentSlot slot, @Nonnull ItemStack stack) {
        super.setItemSlot(slot, stack);
        if (!this.level.isClientSide) {
            this.setCombatTask();
        }
    }

    @Override
    public float getEyeHeight(@Nonnull Pose pose) {
        return 1.74F;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.35D;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 3;
    }
}