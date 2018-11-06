package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0F);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.SCIMITAR));
        this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(AtumItems.BRIGAND_SHIELD));
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return AtumLootTables.BRIGAND;
    }
}