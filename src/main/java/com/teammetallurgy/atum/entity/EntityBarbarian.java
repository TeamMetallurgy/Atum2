package com.teammetallurgy.atum.entity;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityBarbarian extends EntityBanditBase {

    public EntityBarbarian(World world) {
        super(world);
        this.experienceValue = 9;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.GREATSWORD));
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingData) {
        livingData = super.onInitialSpawn(difficulty, livingData);

        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);

        for (int i = 0; i < this.inventoryArmorDropChances.length; ++i) {
            this.inventoryArmorDropChances[i] = 0F;
        }
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        return livingData;
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        if (rand.nextInt(20) == 0) {
            ItemStack greatswordStack = new ItemStack(AtumItems.GREATSWORD);
            this.entityDropItem(new ItemStack(greatswordStack.getItem(), 1, MathHelper.getInt(rand, 20, greatswordStack.getMaxDamage())), 0.0F);
        }

        if (rand.nextInt(4) == 0) {
            int amount = MathHelper.getInt(rand, 1, 2) + looting;
            this.dropItem(AtumItems.GOLD_COIN, amount);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == AtumItems.GREATSWORD) {
            float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            float i = 1.2F;

            if (entity instanceof EntityLivingBase) {
                f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entity).getCreatureAttribute());
                i += EnchantmentHelper.getKnockbackModifier(this);
            }

            boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);
            if (flag) {
                if (i > 0) {
                    entity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * i * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * i * 0.5F));
                    this.motionX *= 0.6D;
                    this.motionZ *= 0.6D;
                }

                int j = EnchantmentHelper.getFireAspectModifier(this);
                if (j > 0) {
                    entity.setFire(j * 4);
                }

                this.applyEnchantments(this, entity);

            }
            return flag;
        }
        return super.attackEntityAsMob(entity);
    }
}