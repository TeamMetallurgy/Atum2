package com.teammetallurgy.atum.entity.efreet;

import com.teammetallurgy.atum.entity.undead.EntityPharaoh;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.items.tools.ItemScepter;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntitySunspeaker extends EntityEfreetBase {

    public EntitySunspeaker(World world) {
        super(world);
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
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0F);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemScepter.getScepter(EntityPharaoh.God.RA)));
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) { //Don't drop the scepter
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return AtumLootTables.SUNSPEAKER;
    }

    @Override
    @Nullable
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) {
        EntitySunspeaker sunspeaker = new EntitySunspeaker(this.world);
        sunspeaker.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(sunspeaker)), null);
        return sunspeaker;
    }
}