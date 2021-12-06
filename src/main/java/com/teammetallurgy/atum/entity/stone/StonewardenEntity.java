package com.teammetallurgy.atum.entity.stone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class StonewardenEntity extends StoneBaseEntity {
    private int attackTimer;

    public StonewardenEntity(EntityType<? extends StonewardenEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 16;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return getBaseAttributes().add(Attributes.MAX_HEALTH, 120.0D).add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.ARMOR, 15.0F);
    }

    @Override
    protected void setFriendlyAttributes() {
        super.setFriendlyAttributes();
        final AttributeModifier FRIENDLY_HEALTH = new AttributeModifier(UUID.fromString("9661113c-650b-4b56-acac-803683d68d92"), "Friendly Stonewarden health", 60.0D, AttributeModifier.Operation.ADDITION);
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(FRIENDLY_HEALTH);
        this.heal(60);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag nbt) {
        livingdata = super.finalizeSpawn(world, difficulty, spawnReason, livingdata, nbt);

        if (!this.isPlayerCreated()) {
            this.setVariant(0);
        } else {
            this.setVariant(1);
        }
        return livingdata;
    }

    @Override
    public boolean isPreventingPlayerRest(@Nonnull Player player) { //isPreventingPlayerRest
        return this.getVariant() != 1;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.attackTimer > 0) {
            --this.attackTimer;
        }

        if (getHorizontalDistanceSqr(this.getDeltaMovement()) > (double) 2.5000003E-7F && this.random.nextInt(5) == 0) {
            int x = Mth.floor(this.getX());
            int y = Mth.floor(this.getY() - 0.20000000298023224D);
            int z = Mth.floor(this.getZ());

            BlockState state = this.level.getBlockState(new BlockPos(x, y, z));
            if (state.getMaterial() != Material.AIR) {
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state).setPos(new BlockPos(x, y, z)), this.getX() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(), this.getBoundingBox().minY + 0.1D, this.getZ() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(), 4.0D * ((double) this.random.nextFloat() - 0.5D), 0.5D, ((double) this.random.nextFloat() - 0.5D) * 4.0D);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        this.attackTimer = 10;
        this.level.broadcastEntityEvent(this, (byte) 4);
        boolean attack = entity.hurt(DamageSource.mobAttack(this), (float) (7 + this.random.nextInt(15)));

        if (attack) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, 0.4F, 0.0D));
            this.doEnchantDamageEffects(this, entity);
        }

        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.4F);
        return attack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackTimer = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.4F);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getAttackTimer() {
        return this.attackTimer;
    }
}