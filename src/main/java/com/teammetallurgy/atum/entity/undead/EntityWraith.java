package com.teammetallurgy.atum.entity.undead;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityWraith extends EntityUndeadBase {
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntityWraith.class, DataSerializers.BYTE);
    private int cycleHeight;
    private int cycleTime;

    public EntityWraith(World world) {
        super(world);
        this.experienceValue = 6;

        cycleTime = (int) ((Math.random() * 40) + 80);
        cycleHeight = (int) (Math.random() * cycleTime);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityWraith.AIWraithAttack(this));
    }

    protected void applyEntityAI() {
        super.applyEntityAI();
        this.targetTasks.addTask(1, new EntityWraith.AIWraithTarget<>(this, EntityPlayer.class));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, (byte) 0);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
    }

    @Override
    @Nonnull
    protected PathNavigate createNavigator(@Nonnull World world) {
        return new PathNavigateClimber(this, world);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }

    @Override
    public void onLivingUpdate() {
        cycleHeight = (cycleHeight + 1) % cycleTime;

        super.onLivingUpdate();
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        if (this.rand.nextInt(4) == 0) {
            int amount = MathHelper.getInt(rand, 1, 3) + looting;
            this.dropItem(AtumItems.ECTOPLASM, amount);
        }
    }

    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
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

    private static class AIWraithAttack extends EntityAIAttackMelee {
        AIWraithAttack(EntityWraith wraith) {
            super(wraith, 1.0D, true);
        }

        @Override
        public boolean shouldContinueExecuting() {
            float f = this.attacker.getBrightness();

            if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
                this.attacker.setAttackTarget(null);
                return false;
            } else {
                return super.shouldContinueExecuting();
            }
        }

        @Override
        protected double getAttackReachSqr(EntityLivingBase attackTarget) {
            return (double) (4.0F + attackTarget.width);
        }
    }

    private static class AIWraithTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
        AIWraithTarget(EntityWraith wraith, Class<T> classTarget) {
            super(wraith, classTarget, true);
        }

        @Override
        public boolean shouldExecute() {
            float f = this.taskOwner.getBrightness();
            return f < 0.5F && super.shouldExecute();
        }
    }
}
