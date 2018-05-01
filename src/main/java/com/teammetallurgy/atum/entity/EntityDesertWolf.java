package com.teammetallurgy.atum.entity;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityDesertWolf extends EntityWolf { //TODO

    public EntityDesertWolf(World world) {
        super(world);
        this.setSize(0.6F, 0.8F);
        this.setAngry(true);
        this.experienceValue = 6;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, false));
    }

    @Override
    protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR);

        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.40000001192092896D);

        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
        if (this.isTamed()) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        }

        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    @Override
    public void setAttackTarget(EntityLivingBase entityLivingBase) {
        super.setAttackTarget(entityLivingBase);

        if (entityLivingBase instanceof EntityPlayer) {
            this.setAngry(true);
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (pos.getY() <= 62) {
            return false;
        } else {
            return this.world.getBlockState(pos.down()) == AtumBlocks.SAND.getDefaultState() && this.world.getLight(pos) > 8 && this.getBlockPathWeight(pos) >= 0.0F && this.world.canBlockSeeSky(pos) &&
                    this.world.checkNoEntityCollision(this.getEntityBoundingBox()) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
        }
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        if (rand.nextInt(4) == 0) {
            int amount = rand.nextInt(2) + 1;
            this.dropItem(AtumItems.WOLF_PELT, amount);
        }
    }

    @Override
    public EntityDesertWolf createChild(EntityAgeable ageable) {
        EntityDesertWolf entityDesertWolf = new EntityDesertWolf(this.world);
        UUID uuid = this.getOwnerId();

        if (uuid != null) {
            entityDesertWolf.setOwnerId(uuid);
            entityDesertWolf.setTamed(true);
        }

        return entityDesertWolf;
    }

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(otherAnimal instanceof EntityDesertWolf)) {
            return false;
        } else {
            EntityDesertWolf entityDesertWolf = (EntityDesertWolf) otherAnimal;
            return entityDesertWolf.isTamed() && (!entityDesertWolf.isSitting() && (this.isInLove() && entityDesertWolf.isInLove()));
        }
    }

    @Override
    public boolean shouldAttackEntity(EntityLivingBase mob, EntityLivingBase player) {
        if (!(mob instanceof EntityCreeper) && !(mob instanceof EntityGhast)) {
            if (mob instanceof EntityDesertWolf) {
                EntityDesertWolf entityDesertWolf = (EntityDesertWolf) mob;

                if (entityDesertWolf.isTamed() && entityDesertWolf.getOwner() == player) {
                    return false;
                }
            }
            return (!(mob instanceof EntityPlayer) || !(player instanceof EntityPlayer) || ((EntityPlayer) player).canAttackPlayer((EntityPlayer) mob)) && (!(mob instanceof EntityHorse) || !((EntityHorse) mob).isTame());
        } else {
            return false;
        }
    }
}