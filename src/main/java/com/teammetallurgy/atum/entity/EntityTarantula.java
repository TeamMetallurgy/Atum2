package com.teammetallurgy.atum.entity;

import com.teammetallurgy.atum.entity.undead.EntityUndeadBase;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityTarantula extends EntityMob implements IUnderground {
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntityTarantula.class, DataSerializers.BYTE);

    public EntityTarantula(World world) {
        super(world);
        this.setSize(0.85F, 0.55F);
        this.experienceValue = 5;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new AITarantulaAttack(this));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityUndeadBase.class, true));
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        return pos.getY() >= 40 && pos.getY() <= 62 && super.getCanSpawnHere() && !this.world.canBlockSeeSky(pos.down());
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    public double getMountedYOffset() {
        return (double) (this.height * 0.5F);
    }

    @Override
    @Nonnull
    protected PathNavigate createNavigator(@Nonnull World world) {
        return new PathNavigateClimber(this, world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, (byte) 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (super.attackEntityAsMob(entity)) {
            if (entity instanceof EntityLivingBase) {
                int i = 0;
                if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                    i = 5;
                } else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                    i = 8;
                }
                if (i > 0) {
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, i * 20, 0));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return AtumLootTables.TARANTULA;
    }

    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    @Override
    public void setInWeb() {
    }

    @Override
    @Nonnull
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean isPotionApplicable(@Nonnull PotionEffect effect) {
        return effect.getPotion() != MobEffects.POISON && super.isPotionApplicable(effect);
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
    public float getEyeHeight() {
        return 0.35F;
    }

    static class AITarantulaAttack extends EntityAIAttackMelee {
        AITarantulaAttack(EntityTarantula tarantula) {
            super(tarantula, 1.0D, true);
        }

        @Override
        protected double getAttackReachSqr(EntityLivingBase attackTarget) {
            return (double) (4.0F + attackTarget.width);
        }
    }
}