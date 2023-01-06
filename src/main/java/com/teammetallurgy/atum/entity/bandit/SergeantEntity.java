package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.init.AtumEffects;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class SergeantEntity extends BanditBaseEntity {

    public SergeantEntity(EntityType<? extends BanditBaseEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 16;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
    }

    @Override
    boolean hasSkinVariants() {
        return false;
    }

    @Override
    public boolean canBeLeader() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return getBaseAttributes().add(Attributes.MAX_HEALTH, 36.0D).add(Attributes.ATTACK_DAMAGE, 5.0D).add(Attributes.ARMOR, 8.0F);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, @Nonnull DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(AtumItems.SCIMITAR_IRON.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
    }

    @Override
    public void die(@Nonnull DamageSource cause) {
        super.die(cause);
        Entity killer = cause.getEntity();
        if (killer instanceof Player) {
            double chance = this.random.nextDouble();
            if (chance <= 0.1D) {
                ((Player) killer).addEffect(new MobEffectInstance(AtumEffects.MARKED_FOR_DEATH.get(), 1020, 0, false, false, true));
            }
        }
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }
}