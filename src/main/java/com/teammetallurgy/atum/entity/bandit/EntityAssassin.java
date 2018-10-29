package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityAssassin extends EntityBanditBase {
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntityAssassin.class, DataSerializers.BYTE);

    public EntityAssassin(World world) {
        super(world);
        this.experienceValue = 12;
        (new PathNavigateClimber(this, world)).setEnterDoors(true);
    }

    @Override
    protected boolean hasSkinVariants() {
        return false;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.2D, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, (byte) 0);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(55.0D);
    }

    @Override
    @Nonnull
    protected PathNavigate createNavigator(@Nonnull World world) {
        return new PathNavigateClimber(this, world);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }

    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    private boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING) & 1) != 0;
    }

    private void setBesideClimbableBlock(boolean isClimbing) {
        byte climbing = this.dataManager.get(CLIMBING);

        if (isClimbing) {
            climbing = (byte) (climbing | 1);
        } else {
            climbing = (byte) (climbing & -2);
        }
        this.dataManager.set(CLIMBING, climbing);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {
    }

    @Override
    protected ResourceLocation getLootTable() {
        return AtumLootTables.ASSASSIN;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.setEquipmentBasedOnDifficulty(difficulty);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.POISON_DAGGER));
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == AtumItems.POISON_DAGGER && entity instanceof EntityLivingBase) {
            (((EntityLivingBase) entity)).addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 1));
        }
        return super.attackEntityAsMob(entity);
    }
}
