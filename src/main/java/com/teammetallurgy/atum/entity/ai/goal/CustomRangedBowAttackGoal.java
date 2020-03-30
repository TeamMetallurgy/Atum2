package com.teammetallurgy.atum.entity.ai.goal;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class CustomRangedBowAttackGoal<T extends MonsterEntity & IRangedAttackMob> extends Goal { //Copy of RangedBowAttackGoal. Changed bow item only.
    private final T entity;
    private final double moveSpeedAmp;
    private int attackCooldown;
    private final float maxAttackDistance;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public CustomRangedBowAttackGoal(T mob, double moveSpeedAmp, int attackCooldown, float maxAttackDistance) {
        this.entity = mob;
        this.moveSpeedAmp = moveSpeedAmp;
        this.attackCooldown = attackCooldown;
        this.maxAttackDistance = maxAttackDistance * maxAttackDistance;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public void setAttackCooldown(int attackCooldownIn) {
        this.attackCooldown = attackCooldownIn;
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.getAttackTarget() != null && this.isBowInMainhand();
    }

    protected boolean isBowInMainhand() {
        return this.entity.getHeldItemMainhand().getItem() instanceof BowItem || this.entity.getHeldItemOffhand().getItem() instanceof BowItem;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.isBowInMainhand();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.entity.setAggroed(true);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.entity.setAggroed(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.entity.resetActiveHand();
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.entity.getAttackTarget();
        if (livingentity != null) {
            double d0 = this.entity.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
            boolean flag = this.entity.getEntitySenses().canSee(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double) this.maxAttackDistance) && this.seeTime >= 20) {
                this.entity.getNavigator().clearPath();
                ++this.strafingTime;
            } else {
                this.entity.getNavigator().tryMoveToEntityLiving(livingentity, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entity.faceEntity(livingentity, 30.0F, 30.0F);
            } else {
                this.entity.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
            }

            if (this.entity.isHandActive()) {
                if (!flag && this.seeTime < -60) {
                    this.entity.resetActiveHand();
                } else if (flag) {
                    int i = this.entity.getItemInUseMaxCount();
                    if (i >= 20) {
                        this.entity.resetActiveHand();
                        this.entity.attackEntityWithRangedAttack(livingentity, BowItem.getArrowVelocity(i));
                        this.attackTime = this.attackCooldown;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.entity.setActiveHand(getHandWith(this.entity, BowItem.class));
            }
        }
    }

    public static Hand getHandWith(LivingEntity living, Class<? extends Item> itemClass) {
        return itemClass.isAssignableFrom(living.getHeldItemMainhand().getItem().getClass()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }
}