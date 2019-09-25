package com.teammetallurgy.atum.entity.stone;

import com.teammetallurgy.atum.entity.bandit.BanditBaseEntity;
import com.teammetallurgy.atum.entity.undead.UndeadBaseEntity;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class StoneBaseEntity extends MonsterEntity implements IUnderground {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(StoneBaseEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> PLAYER_CREATED = EntityDataManager.createKey(StoneBaseEntity.class, DataSerializers.BYTE);
    private int homeCheckTimer;

    StoneBaseEntity(EntityType<? extends StoneBaseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.applyEntityAI();
    }

    private void applyEntityAI() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, input -> !isPlayerCreated()));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, StoneBaseEntity.class, 10, true, false, input -> input instanceof StoneBaseEntity && (!((StoneBaseEntity) input).isPlayerCreated() && isPlayerCreated() || ((StoneBaseEntity) input).isPlayerCreated() && !isPlayerCreated())));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, UndeadBaseEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, BanditBaseEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, MonsterEntity.class, 10, true, false, input -> input != null && this.isPlayerCreated() && !(input instanceof StoneBaseEntity) && input.getCreatureAttribute() == CreatureAttribute.UNDEAD));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();

        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
    }

    void setFriendlyAttributes() {
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(PLAYER_CREATED, (byte) 0);
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason spawnReason, @Nullable ILivingEntityData livingdata, @Nullable CompoundNBT nbt) {
        if (this.isPlayerCreated()) {
            this.setFriendlyAttributes();
        }
        return super.onInitialSpawn(world, difficulty, spawnReason, livingdata, nbt);
    }

    void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    protected int getVariantAmount() {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
        }
    }

    @Override
    public void remove() {
        if (!world.isRemote && this.isPlayerCreated() && this.world.getDifficulty() == Difficulty.PEACEFUL) {
            //Don't set player created stone mobs as dead on peaceful
        } else {
            super.remove();
        }
    }

    @Override
    protected void updateAITasks() {
        if (--this.homeCheckTimer <= 0) {
            this.homeCheckTimer = 70 + this.rand.nextInt(50);

            if (!this.detachHome()) {
                this.setHomePosAndDistance(new BlockPos(this), 16);
            }
        }
        super.updateAITasks();
    }

    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);

        if (heldStack.getItem() == AtumItems.KHNUMITE) {
            if (!player.abilities.isCreativeMode) {
                heldStack.shrink(1);
            }

            if (!this.world.isRemote) {
                this.heal(5.0F);
            }
            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    @Override
    public boolean isPotionApplicable(@Nonnull EffectInstance potionEffect) {
        return potionEffect.getPotion() != Effects.POISON && super.isPotionApplicable(potionEffect);
    }

    @Override
    public void knockBack(@Nonnull Entity entity, float strength, double xRatio, double zRatio) {
        //Immune to knockback
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return !this.isPlayerCreated();
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
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
    public int getTalkInterval() {
        return 120;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.BLOCK_STONE_STEP, 0.15F, 1.0F);
    }

    public static boolean isValidLightLevel(IWorld world, BlockPos pos, Random random) {
        return world.getLightFor(LightType.SKY, pos) == 0 && world.getLight(pos) <= random.nextInt(10);
    }

    public static boolean canSpawn(EntityType<? extends StoneBaseEntity> stoneBase, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return isValidLightLevel(world, pos, random) && func_223324_d(stoneBase, world, spawnReason, pos, random);
    }

    boolean isPlayerCreated() {
        return (this.dataManager.get(PLAYER_CREATED) & 1) != 0;
    }

    public void setPlayerCreated(boolean playerCreated) {
        byte b = this.dataManager.get(PLAYER_CREATED);

        if (playerCreated) {
            this.dataManager.set(PLAYER_CREATED, (byte) (b | 1));
        } else {
            this.dataManager.set(PLAYER_CREATED, (byte) (b & -2));
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("PlayerCreated", this.isPlayerCreated());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setPlayerCreated(compound.getBoolean("PlayerCreated"));
    }
}