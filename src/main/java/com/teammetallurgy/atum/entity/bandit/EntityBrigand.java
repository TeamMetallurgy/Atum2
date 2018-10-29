package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityBrigand extends EntityBanditBase {

    public EntityBrigand(World world) {
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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.SCIMITAR));
        this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(AtumItems.BRIGAND_SHIELD));
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);

        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        for (int i = 0; i < this.inventoryArmorDropChances.length; ++i) {
            this.inventoryArmorDropChances[i] = 0F;
        }
        return livingdata;
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        if (rand.nextInt(20) == 0) {
            ItemStack scimitarStack = new ItemStack(AtumItems.SCIMITAR);
            this.entityDropItem(new ItemStack(scimitarStack.getItem(), 1, MathHelper.getInt(rand, 20, scimitarStack.getMaxDamage())), 0.0F);
        }
        if (rand.nextInt(25) == 0) {
            ItemStack scimitarStack = new ItemStack(AtumItems.BRIGAND_SHIELD);
            this.entityDropItem(new ItemStack(scimitarStack.getItem(), 1, MathHelper.getInt(rand, 20, scimitarStack.getMaxDamage())), 0.0F);
        }
        if (rand.nextInt(10) == 0) {
            int amount = MathHelper.getInt(rand, 1, 2) + looting;
            this.dropItem(AtumItems.GOLD_COIN, amount);
        }
    }
}
