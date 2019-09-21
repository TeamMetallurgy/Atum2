package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.entity.ai.AIBowAttack;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityNomad extends EntityBanditBase implements IRangedAttackMob {
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(EntityNomad.class, DataSerializers.BOOLEAN);
    private AIBowAttack aiArrowAttack = new AIBowAttack<>(this, 0.75D, 35, 12.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.0D, false) {
        @Override
        public void resetTask() {
            super.resetTask();
            EntityNomad.this.setSwingingArms(false);
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
            EntityNomad.this.setSwingingArms(true);
        }
    };

    public EntityNomad(World world) {
        super(world);
        this.experienceValue = 6;

        this.setCombatTask();
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(13.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0F);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SWINGING_ARMS, false);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(AtumItems.SHORT_BOW));
    }

    @Override
    public ILivingEntityData onInitialSpawn(DifficultyInstance difficulty, ILivingEntityData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.goalSelector.addGoal(4, this.aiArrowAttack);

        return livingdata;
    }

    private void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);

            if (this.getHeldItemMainhand().getItem() instanceof ItemBow) {
                this.goalSelector.addGoal(4, this.aiArrowAttack);
            } else {
                this.goalSelector.addGoal(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public void attackEntityWithRangedAttack(@Nonnull LivingEntity target, float distanceFactor) {
        ArrowEntity arrow = getArrow(distanceFactor);
        double x = target.posX - this.posX;
        double y = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - arrow.posY;
        double z = target.posZ - this.posZ;
        double height = (double) MathHelper.sqrt(x * x + z * z);
        arrow.shoot(x, y + height * 0.20000000298023224D, z, 1.6F, (float) (11 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(arrow);
    }

    private ArrowEntity getArrow(float distanceFactor) {
        EntityTippedArrow arrow = new EntityTippedArrow(this.world, this);
        arrow.setEnchantmentEffectsFromEntity(this, distanceFactor);
        return arrow;
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setCombatTask();
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slot, @Nonnull ItemStack stack) {
        super.setItemStackToSlot(slot, stack);

        if (!this.world.isRemote && slot == EquipmentSlotType.MAINHAND) {
            this.setCombatTask();
        }
    }

    @Override
    public float getEyeHeight() {
        return 1.74F;
    }

    @Override
    public double getYOffset() {
        return -0.35D;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return AtumLootTables.NOMAD;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isSwingingArms() {
        return this.dataManager.get(SWINGING_ARMS);
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        this.dataManager.set(SWINGING_ARMS, swingingArms);
    }
}
