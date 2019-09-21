package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityBrigand extends EntityBanditBase {

    public EntityBrigand(World world) {
        super(world);
        this.experienceValue = 8;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new EntityAIAttackMelee(this, 1.0D, false));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(18.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0F);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(AtumItems.SCIMITAR_IRON));
        this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(AtumItems.BRIGAND_SHIELD));
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return AtumLootTables.BRIGAND;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (!super.attackEntityAsMob(entity)) {
            return false;
        } else {
            if (this.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == AtumItems.GREATSWORD_IRON) {
                float f = (float) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
                float i = 1.2F;

                if (entity instanceof LivingEntity) {
                    f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity) entity).getCreatureAttribute());
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
            return true;
        }
    }
}
