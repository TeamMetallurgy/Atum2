package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.entity.ai.goal.OpenAnyDoorGoal;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class AssassinEntity extends BanditBaseEntity {
    private final DamageSource ASSASSINATED = new EntityDamageSource("assassinated", this);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(AssassinEntity.class, DataSerializers.BYTE);

    public AssassinEntity(EntityType<? extends AssassinEntity> entityType, World world) {
        super(entityType, world);
        this.experienceValue = 12;
        (new ClimberPathNavigator(this, world)).setBreakDoors(true);
        this.setCanPatrol(false);
    }

    @Override
    protected boolean hasSkinVariants() {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new OpenAnyDoorGoal(this, false));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(CLIMBING, (byte) 0);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0F);
    }

    @Override
    @Nonnull
    protected PathNavigator createNavigator(@Nonnull World world) {
        return new ClimberPathNavigator(this, world);
    }

    @Override
    public void tick() {
        super.tick();

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
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(@Nonnull DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(AtumItems.POISON_DAGGER));
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entity) {
        if (!super.attackEntityAsMob(entity)) {
            return false;
        } else {
            if (this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == AtumItems.POISON_DAGGER && entity instanceof LivingEntity) {
                entity.attackEntityFrom(ASSASSINATED, (float) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
                (((LivingEntity) entity)).addPotionEffect(new EffectInstance(Effects.POISON, 100, 1));
            }
            return true;
        }
    }

    public static boolean canSpawn(EntityType<? extends BanditBaseEntity> banditBase, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return spawnReason == SpawnReason.EVENT ? world.canBlockSeeSky(pos) && world.getLightFor(LightType.BLOCK, pos) <= 8 && canMonsterSpawn(banditBase, world, spawnReason, pos, random) : BanditBaseEntity.canSpawn(banditBase, world, spawnReason, pos, random);
    }
}