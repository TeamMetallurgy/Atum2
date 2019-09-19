package com.teammetallurgy.atum.entity.stone;

import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityStonewarden extends EntityStoneBase {
    private int attackTimer;

    public EntityStonewarden(World world) {
        super(world);
        this.setSize(1.4F, 2.7F);
        this.experienceValue = 16;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    @Override
    protected void setFriendlyAttributes() {
        super.setFriendlyAttributes();
        final AttributeModifier FRIENDLY_HEALTH = new AttributeModifier(UUID.fromString("9661113c-650b-4b56-acac-803683d68d92"), "Friendly Stonewarden health", 60.0D, 0);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(FRIENDLY_HEALTH);
        this.heal(60);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (!this.isPlayerCreated()) {
            this.setVariant(0);
        } else {
            this.setVariant(1);
        }
        return livingdata;
    }

    @Override
    public boolean isPreventingPlayerRest(PlayerEntity player) {
        return this.getVariant() != 1;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (this.attackTimer > 0) {
            --this.attackTimer;
        }

        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0) {
            int x = MathHelper.floor(this.posX);
            int y = MathHelper.floor(this.posY - 0.20000000298023224D);
            int z = MathHelper.floor(this.posZ);
            BlockState state = this.world.getBlockState(new BlockPos(x, y, z));

            if (state.getMaterial() != Material.AIR) {
                this.world.addParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double) this.rand.nextFloat() - 0.5D) * (double) this.getWidth(), this.getBoundingBox().minY + 0.1D, this.posZ + ((double) this.rand.nextFloat() - 0.5D) * (double) this.getWidth(), 4.0D * ((double) this.rand.nextFloat() - 0.5D), 0.5D, ((double) this.rand.nextFloat() - 0.5D) * 4.0D, Block.getStateId(state));
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte) 4);
        boolean attack = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) (7 + this.rand.nextInt(15)));

        if (attack) {
            entity.motionY += 0.4000000059604645D;
            this.applyEnchantments(this, entity);
        }

        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.4F);
        return attack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 4) {
            this.attackTimer = 10;
            this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.4F);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getAttackTimer() {
        return this.attackTimer;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return AtumLootTables.STONEWARDEN;
    }
}