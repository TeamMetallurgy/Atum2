package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBlock;
import com.teammetallurgy.atum.blocks.wood.DeadwoodLogBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class ScarabEntity extends MonsterEntity {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(ScarabEntity.class, DataSerializers.VARINT);

    public ScarabEntity(EntityType<? extends ScarabEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new AIHideInBlock(this));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setCallsForHelp());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
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
    public ILivingEntityData onInitialSpawn(@Nonnull IWorld world, @Nonnull DifficultyInstance difficulty, @Nonnull SpawnReason spawnReason, @Nullable ILivingEntityData livingdata, @Nullable CompoundNBT nbt) {
        livingdata = super.onInitialSpawn(world, difficulty, spawnReason, livingdata, nbt);
        if (rand.nextDouble() <= 0.002D) {
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
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.ENTITY_ENDERMITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMITE_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        this.playSound(SoundEvents.ENTITY_ENDERMITE_STEP, 0.15F, 1.0F);
    }

    @Override
    protected int getExperiencePoints(@Nonnull PlayerEntity player) {
        return this.getVariant() == 1 ? 30 : 3;
    }

    @Override
    @Nonnull
    protected ResourceLocation getLootTable() {
        return this.getVariant() == 1 ? AtumLootTables.SCARAB_GOLDEN : AtumLootTables.SCARAB;
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
        Block block = this.world.getBlockState(pos.down()).getBlock();
        return block == AtumBlocks.LIMESTONE || block == AtumBlocks.DEADWOOD_LOG ? 10.0F : super.getBlockPathWeight(pos);
    }

    @Override
    public float getEyeHeight(@Nonnull Pose pose) {
        return 0.1F;
    }

    @Override
    public double getYOffset() {
        return 0.1D;
    }

    public static boolean canSpawn(EntityType<ScarabEntity> scarab, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (canMonsterSpawn(scarab, world, spawnReason, pos, random)) {
            PlayerEntity player = world.getClosestPlayer((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0D, true);
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
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    static class AIHideInBlock extends RandomWalkingGoal {
        private Direction facing;
        private boolean doMerge;

        AIHideInBlock(ScarabEntity scarab) {
            super(scarab, 1.0D, 10);
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            if (this.creature.getAttackTarget() != null) {
                return false;
            } else if (!this.creature.getNavigator().noPath()) {
                return false;
            } else {
                Random random = this.creature.getRNG();

                if (ForgeEventFactory.getMobGriefingEvent(this.creature.world, this.creature) && random.nextInt(10) == 0) {
                    this.facing = Direction.random(random);
                    BlockPos pos = (new BlockPos(this.creature.getPosX(), this.creature.getPosY() + 0.5D, this.creature.getPosZ())).offset(this.facing);
                    BlockState state = this.creature.world.getBlockState(pos);

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
                World world = this.creature.world;
                BlockPos pos = (new BlockPos(this.creature.getPosX(), this.creature.getPosY() + 0.5D, this.creature.getPosZ())).offset(this.facing);
                BlockState state = world.getBlockState(pos);

                if (state.getBlock() == AtumBlocks.LIMESTONE) {
                    world.setBlockState(pos, AtumBlocks.LIMESTONE.getDefaultState().with(LimestoneBlock.HAS_SCARAB, true), 3);
                    this.creature.spawnExplosionParticle();
                    this.creature.remove();
                }
                if (state.getBlock() == AtumBlocks.DEADWOOD_LOG) {
                    world.setBlockState(pos, AtumBlocks.DEADWOOD_LOG.getDefaultState().with(DeadwoodLogBlock.HAS_SCARAB, true), 3);
                    this.creature.spawnExplosionParticle();
                    this.creature.remove();
                }
            }
        }
    }
}