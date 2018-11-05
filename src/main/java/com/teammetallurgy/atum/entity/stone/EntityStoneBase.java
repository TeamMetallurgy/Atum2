package com.teammetallurgy.atum.entity.stone;

import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestone;
import com.teammetallurgy.atum.entity.bandit.EntityBanditBase;
import com.teammetallurgy.atum.entity.undead.EntityUndeadBase;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityStoneBase extends EntityMob {

    EntityStoneBase(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityUndeadBase.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityBanditBase.class, true));
    }

    @Override
    public boolean isPotionApplicable(@Nonnull PotionEffect potionEffect) {
        return potionEffect.getPotion() != MobEffects.POISON && super.isPotionApplicable(potionEffect);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLOCK_STONE_STEP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BLOCK_STONE_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_STONE_BREAK;
    }

    @Override
    @Nonnull
    protected SoundEvent getFallSound(int height) {
        return height > 4 ? SoundEvents.ENTITY_HOSTILE_BIG_FALL : SoundEvents.BLOCK_STONE_FALL;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {
        this.playSound(SoundEvents.BLOCK_STONE_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && this.world.getBlockState((new BlockPos(this)).down()) instanceof BlockLimestone;
    }

    @Override
    protected boolean isValidLightLevel() {
        return world.getLightFor(EnumSkyBlock.BLOCK, this.getPosition()) == 0;
    }
}