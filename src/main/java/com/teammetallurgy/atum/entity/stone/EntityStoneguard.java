package com.teammetallurgy.atum.entity.stone;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityStoneguard extends EntityStoneBase {

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
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_SWORD));
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);

        for (int i = 0; i < this.inventoryArmorDropChances.length; ++i) {
            this.inventoryArmorDropChances[i] = 0F;
        }

        return livingdata;
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
    protected void dropFewItems(boolean recentlyHit, int looting) {
        if (this.rand.nextInt(4) == 0) {
            this.dropItem(AtumItems.STONE_CHUNK, MathHelper.getInt(rand, 1, 2) + looting);
        }
    }
}
