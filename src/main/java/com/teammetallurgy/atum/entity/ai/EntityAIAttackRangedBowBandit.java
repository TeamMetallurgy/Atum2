package com.teammetallurgy.atum.entity.ai;

import com.teammetallurgy.atum.entity.EntityBanditBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;

public class EntityAIAttackRangedBowBandit extends EntityAIBase {
    private final EntityBanditBase bandit;
    private final double moveSpeedAmp;
    private final int field_188501_c;
    private final float maxAttackDistance;
    private int field_188503_e = -1;
    private int field_188504_f;
    private boolean field_188505_g;
    private boolean field_188506_h;
    private int field_188507_i = -1;

    public EntityAIAttackRangedBowBandit(EntityBanditBase bandit, double moveSpeedAmp, int p_i46805_4_, float p_i46805_5_) {
        this.bandit = bandit;
        this.moveSpeedAmp = moveSpeedAmp;
        this.field_188501_c = p_i46805_4_;
        this.maxAttackDistance = p_i46805_5_ * p_i46805_5_;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return this.bandit.getAttackTarget() != null && this.getHeldBow();
    }

    private boolean getHeldBow() {
        return this.bandit.getHeldItemMainhand().getItem() instanceof ItemBow;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.bandit.getNavigator().noPath()) && this.getHeldBow();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.bandit.startShooting(true);
    }

    @Override
    public void resetTask() {
        super.startExecuting();
        this.bandit.startShooting(false);
        this.field_188504_f = 0;
        this.field_188503_e = -1;
        this.bandit.resetActiveHand();
    }

    @Override
    public void updateTask() {
        EntityLivingBase entitylivingbase = this.bandit.getAttackTarget();

        if (entitylivingbase != null) {
            double d0 = this.bandit.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            boolean flag = this.bandit.getEntitySenses().canSee(entitylivingbase);
            boolean flag1 = this.field_188504_f > 0;

            if (flag != flag1) {
                this.field_188504_f = 0;
            }

            if (flag) {
                ++this.field_188504_f;
            } else {
                --this.field_188504_f;
            }

            if (d0 <= (double) this.maxAttackDistance && this.field_188504_f >= 20) {
                this.bandit.getNavigator().clearPath();
                ++this.field_188507_i;
            } else {
                this.bandit.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
                this.field_188507_i = -1;
            }

            if (this.field_188507_i >= 20) {
                if ((double) this.bandit.getRNG().nextFloat() < 0.3D) {
                    this.field_188505_g = !this.field_188505_g;
                }

                if ((double) this.bandit.getRNG().nextFloat() < 0.3D) {
                    this.field_188506_h = !this.field_188506_h;
                }

                this.field_188507_i = 0;
            }

            if (this.field_188507_i > -1) {
                if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
                    this.field_188506_h = false;
                } else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
                    this.field_188506_h = true;
                }

                this.bandit.getMoveHelper().strafe(this.field_188506_h ? -0.5F : 0.5F, this.field_188505_g ? 0.5F : -0.5F);
                this.bandit.faceEntity(entitylivingbase, 30.0F, 30.0F);
            } else {
                this.bandit.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
            }

            if (this.bandit.isHandActive()) {
                if (!flag && this.field_188504_f < -60) {
                    this.bandit.resetActiveHand();
                } else if (flag) {
                    int i = this.bandit.getItemInUseMaxCount();

                    if (i >= 20) {
                        this.bandit.resetActiveHand();
                        this.bandit.attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(i));
                        this.field_188503_e = this.field_188501_c;
                    }
                }
            } else if (--this.field_188503_e <= 0 && this.field_188504_f >= -60) {
                this.bandit.setActiveHand(EnumHand.MAIN_HAND);
            }
        }
    }
}