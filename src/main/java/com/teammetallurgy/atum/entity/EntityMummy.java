package com.teammetallurgy.atum.entity;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.UUID;

public class EntityMummy extends EntityUndeadBase {
    private static final AttributeModifier SPEED_BOOST_BURNING = new AttributeModifier(UUID.fromString("2dc2358a-63df-435d-a602-2ff3d6bca8d1"), "Burning speed boost", 0.1D, 0);

    public EntityMummy(World world) {
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
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (this.isBurning() && !attribute.hasModifier(SPEED_BOOST_BURNING)) {
            attribute.applyModifier(SPEED_BOOST_BURNING);
        } else {
            attribute.removeModifier(SPEED_BOOST_BURNING);
        }
    }

    @Override
    protected float getBurnDamage() {
        return 2.0F;
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (source.isFireDamage()) {
            amount += 1;
        }
        if (this.isBurning()) {
            amount = (int) (amount * 1.5);
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        boolean flag = super.attackEntityAsMob(entity);

        if (flag) {
            if (this.isBurning() && this.rand.nextFloat() < (float) this.world.getDifficulty().getDifficultyId() * 0.4F) {
                entity.setFire(2 * this.world.getDifficulty().getDifficultyId());
            }
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase base = (EntityLivingBase) entity;
                base.addPotionEffect(new PotionEffect(MobEffects.WITHER, 80, 1));
            }
        }

        return flag;
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        if (rand.nextInt(4) == 0) {
            this.dropItem(Items.ROTTEN_FLESH, 1 + looting);
        }
        if (rand.nextInt(4) == 0) {
            int amount = MathHelper.getInt(rand, 1, 2) + looting;
            this.dropItem(AtumItems.SCRAP, amount);
        }
    }
}
