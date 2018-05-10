package com.teammetallurgy.atum.entity;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityUndeadBase extends EntityMob {

    public EntityUndeadBase(World world) {
        super(world);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityBanditBase.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityStone.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityAnimalBase.class, true));
    }

    @Override
    public boolean isPotionApplicable(@Nonnull PotionEffect potionEffect) {
        return potionEffect.getPotion() != MobEffects.POISON && super.isPotionApplicable(potionEffect);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.world.isDaytime() && this.world.canSeeSky(this.getPosition()) && !this.world.isRemote && !this.isImmuneToFire && this.shouldBurnInDay()) {
            this.setFire(8);
        }
    }

    @Override
    public void onEntityUpdate() {
        if (this.fire > 0) {
            if (!this.isImmuneToFire) {
                if (this.fire % 20 == 0) {
                    this.attackEntityFrom(DamageSource.ON_FIRE, getBurnDamage());
                }
                --this.fire;
            }
        }
        super.onEntityUpdate();
    }

    protected float getBurnDamage() {
        return 1.0F;
    }

    protected boolean shouldBurnInDay() {
        return true;
    }

    @Override
    @Nonnull
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(this.getEntityBoundingBox().minY);
        if (i <= 62) {
            return false;
        } else {
            return super.getCanSpawnHere();
        }
    }
}