package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityAssassin extends EntityBanditBase {
    private final DamageSource ASSASSINATED = new EntityDamageSource("assassinated", this);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntityAssassin.class, DataSerializers.BYTE);

    public EntityAssassin(World world) {
        super(world);
        this.experienceValue = 12;
        (new PathNavigateClimber(this, world)).setBreakDoors(true);
    }

    @Override
    protected boolean hasSkinVariants() {
        return false;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIOpenDoor(this, false));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.2D, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, (byte) 0);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0F);
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
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(AtumItems.POISON_DAGGER));
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (!super.attackEntityAsMob(entity)) {
            return false;
        } else {
            if (this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == AtumItems.POISON_DAGGER && entity instanceof LivingEntity) {
                entity.attackEntityFrom(ASSASSINATED, (float) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                (((LivingEntity) entity)).addPotionEffect(new EffectInstance(Effects.POISON, 100, 1));
            }
            return true;
        }
    }
}