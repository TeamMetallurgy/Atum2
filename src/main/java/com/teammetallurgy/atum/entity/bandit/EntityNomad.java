package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.entity.ai.AtumEntityAIAttackRangedBow;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityNomad extends EntityBanditBase implements IRangedAttackMob {
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(EntityNomad.class, DataSerializers.BOOLEAN);
    private AtumEntityAIAttackRangedBow aiArrowAttack = new AtumEntityAIAttackRangedBow<>(this, 0.8D, 30, 15.0F);
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
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SWINGING_ARMS, false);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.SHORT_BOW));
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.tasks.addTask(4, this.aiArrowAttack);

        return livingdata;
    }

    private void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);

            if (this.getHeldItemMainhand().getItem() instanceof ItemBow) {
                this.tasks.addTask(4, this.aiArrowAttack);
            } else {
                this.tasks.addTask(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public void attackEntityWithRangedAttack(@Nonnull EntityLivingBase target, float distanceFactor) {
        EntityArrow arrow = getArrow(distanceFactor);
        double x = target.posX - this.posX;
        double y = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - arrow.posY;
        double z = target.posZ - this.posZ;
        double height = (double) MathHelper.sqrt(x * x + z * z);
        arrow.shoot(x, y + height * 0.20000000298023224D, z, 1.6F, (float) (14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(arrow);
    }

    private EntityArrow getArrow(float distanceFactor) {
        EntityTippedArrow arrow = new EntityTippedArrow(this.world, this);
        arrow.setEnchantmentEffectsFromEntity(this, distanceFactor);
        return arrow;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setCombatTask();
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slot, @Nonnull ItemStack stack) {
        super.setItemStackToSlot(slot, stack);

        if (!this.world.isRemote && slot == EntityEquipmentSlot.MAINHAND) {
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

    @SideOnly(Side.CLIENT)
    public boolean isSwingingArms() {
        return this.dataManager.get(SWINGING_ARMS);
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        this.dataManager.set(SWINGING_ARMS, swingingArms);
    }
}