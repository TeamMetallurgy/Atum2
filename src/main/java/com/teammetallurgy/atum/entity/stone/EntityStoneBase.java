package com.teammetallurgy.atum.entity.stone;

import com.teammetallurgy.atum.entity.IUnderground;
import com.teammetallurgy.atum.entity.bandit.EntityBanditBase;
import com.teammetallurgy.atum.entity.undead.EntityUndeadBase;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityStoneBase extends MonsterEntity implements IUnderground {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityStoneBase.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> PLAYER_CREATED = EntityDataManager.createKey(EntityStoneBase.class, DataSerializers.BYTE);
    private int homeCheckTimer;

    EntityStoneBase(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, PlayerEntity.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    private void applyEntityAI() {
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, PlayerEntity.class, 10, true, false, input -> !isPlayerCreated()));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityStoneBase.class, 10, true, false, input -> input != null && (!input.isPlayerCreated() && isPlayerCreated() || input.isPlayerCreated() && !isPlayerCreated())));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityUndeadBase.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityBanditBase.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityMob.class, 10, true, false, input -> input != null && this.isPlayerCreated() && !(input instanceof EntityStoneBase) && input.getCreatureAttribute() == CreatureAttribute.UNDEAD));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();

        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
    }

    void setFriendlyAttributes() {
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(PLAYER_CREATED, (byte) 0);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        if (this.isPlayerCreated()) {
            this.setFriendlyAttributes();
        }
        return super.onInitialSpawn(difficulty, livingdata);
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
    public void setDead() {
        if (!world.isRemote && this.isPlayerCreated() && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            //Don't set player created stone mobs as dead on peaceful
        } else {
            super.setDead();
        }
    }

    @Override
    protected void updateAITasks() {
        if (--this.homeCheckTimer <= 0) {
            this.homeCheckTimer = 70 + this.rand.nextInt(50);

            if (!this.hasHome()) {
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
    protected boolean canDespawn() {
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
    protected void playStepSound(BlockPos pos, Block block) {
        this.playSound(SoundEvents.BLOCK_STONE_STEP, 0.15F, 1.0F);
    }

    @Override
    protected boolean isValidLightLevel() {
        BlockPos pos = new BlockPos(this.posX, this.getBoundingBox().minY, this.posZ);
        if (world.getLightFor(EnumSkyBlock.SKY, pos) != 0) {
            return false;
        } else {
            return this.world.getLightFromNeighbors(pos) <= this.rand.nextInt(10);
        }
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
    public void writeEntityToNBT(CompoundNBT compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setBoolean("PlayerCreated", this.isPlayerCreated());
    }

    @Override
    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setPlayerCreated(compound.getBoolean("PlayerCreated"));
    }
}