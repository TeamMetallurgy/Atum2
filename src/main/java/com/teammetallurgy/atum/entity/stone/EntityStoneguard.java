package com.teammetallurgy.atum.entity.stone;

import com.teammetallurgy.atum.entity.bandit.EntityBanditBase;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityStoneguard extends EntityStoneBase {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityBanditBase.class, DataSerializers.VARINT);

    public EntityStoneguard(World world) {
        super(world);
        this.experienceValue = 8;

    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, 0);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_SWORD));
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);

        final int variant = MathHelper.getInt(world.rand, 0, 7);
        this.setVariant(variant);

        return livingdata;
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (super.attackEntityFrom(source, amount)) {
            if (source.getTrueSource() != null) {
                Entity entity = source.getTrueSource();
                int j = 0;
                if (entity instanceof EntityLiving) {
                    j += EnchantmentHelper.getKnockbackModifier(this);

                    if (j > 0) {
                        this.motionX /= 0.6D;
                        this.motionZ /= 0.6D;
                        this.addVelocity((double) (MathHelper.sin(entity.rotationYaw * (float) Math.PI / 180.0F) * (float) j * 0.5F), -0.1D, (double) (-MathHelper.cos(entity.rotationYaw * (float) Math.PI / 180.0F) * (float) j * 0.5F));
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void knockBack(@Nonnull Entity entity, float strength, double xRatio, double zRatio) {
        this.isAirBorne = true;
        float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
        float f1 = 0.2F;
        this.motionX /= 2.0D;
        this.motionY /= 2.0D;
        this.motionZ /= 2.0D;
        this.motionX -= xRatio / (double) f * (double) f1;
        this.motionZ -= zRatio / (double) f * (double) f1;

        if (this.motionY > 0.4000000059604645D) {
            this.motionY = 0.4000000059604645D;
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
    }
}