package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestone;
import com.teammetallurgy.atum.blocks.wood.BlockDeadwood;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class EntityScarab extends EntityMob {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityScarab.class, DataSerializers.VARINT);

    public EntityScarab(World world) {
        super(world);
        this.setSize(0.4F, 0.3F);
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player) {
        if (this.getVariant() == 1) {
            return 30;
        } else {
            return 3;
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.goalSelector.addGoal(3, new EntityAIWanderAvoidWater(this, 1.0D));
        this.goalSelector.addGoal(5, new AIHideInBlock(this));
        this.goalSelector.addGoal(7, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new EntityAILookIdle(this));
        this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this, true));
        this.targetSelector.addGoal(2, new EntityAINearestAttackableTarget<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);

        if (this.getVariant() == 1) {
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
            this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        } else {
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
            this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, 0);
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(DifficultyInstance difficulty, @Nullable ILivingEntityData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        if (world.rand.nextDouble() <= 0.002D) {
            this.setVariant(1);
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
            this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
            this.heal(16); //Make sure Golden scarab have have full health on initial spawn
        } else {
            this.setVariant(0);
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
            this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        }
        return livingdata;
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDERMITE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ENDERMITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMITE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_ENDERMITE_STEP, 0.15F, 1.0F);
    }

    @Override
    protected ResourceLocation getLootTable() {
        if (this.getVariant() == 1) {
            return AtumLootTables.SCARAB_GOLDEN;
        } else {
            return AtumLootTables.SCARAB;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
        }
    }

    @Override
    public float getBlockPathWeight(@Nonnull BlockPos pos) {
        return this.world.getBlockState(pos.down()).getBlock() == AtumBlocks.LIMESTONE ? 10.0F : super.getBlockPathWeight(pos);
    }

    @Override
    public float getEyeHeight() {
        return 0.1F;
    }

    @Override
    public double getYOffset() {
        return 0.1D;
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        if (super.getCanSpawnHere()) {
            PlayerEntity player = this.world.getClosestPlayerToEntity(this, 5.0D);
            return player == null;
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    static class AIHideInBlock extends EntityAIWander {
        private Direction facing;
        private boolean doMerge;

        AIHideInBlock(EntityScarab scarab) {
            super(scarab, 1.0D, 10);
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            if (this.entity.getAttackTarget() != null) {
                return false;
            } else if (!this.entity.getNavigator().noPath()) {
                return false;
            } else {
                Random random = this.entity.getRNG();

                if (ForgeEventFactory.getMobGriefingEvent(this.entity.world, this.entity) && random.nextInt(10) == 0) {
                    this.facing = Direction.random(random);
                    BlockPos pos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
                    BlockState state = this.entity.world.getBlockState(pos);

                    if (state.getBlock() == AtumBlocks.LIMESTONE || state.getBlock() == AtumBlocks.DEADWOOD_LOG) {
                        this.doMerge = true;
                        return true;
                    }
                }
                this.doMerge = false;
                return super.shouldExecute();
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return !this.doMerge && super.shouldContinueExecuting();
        }

        @Override
        public void startExecuting() {
            if (!this.doMerge) {
                super.startExecuting();
            } else {
                World world = this.entity.world;
                BlockPos pos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
                BlockState state = world.getBlockState(pos);

                if (state.getBlock() == AtumBlocks.LIMESTONE) {
                    world.setBlockState(pos, AtumBlocks.LIMESTONE.getDefaultState().withProperty(BlockLimestone.HAS_SCARAB, true), 3);
                    this.entity.spawnExplosionParticle();
                    this.entity.setDead();
                }
                if (state.getBlock() == AtumBlocks.DEADWOOD_LOG) {
                    world.setBlockState(pos, AtumBlocks.DEADWOOD_LOG.getDefaultState().withProperty(BlockDeadwood.HAS_SCARAB, true), 3);
                    this.entity.spawnExplosionParticle();
                    this.entity.setDead();
                }
            }
        }
    }
}