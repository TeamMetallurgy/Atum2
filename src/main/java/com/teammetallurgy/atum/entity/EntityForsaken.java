package com.teammetallurgy.atum.entity;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityForsaken extends EntityUndeadBase {
    boolean onFire = false;

    public EntityForsaken(World world) {
        super(world);
        this.isImmuneToFire = true;
        this.experienceValue = 6;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        double speed = 0.53000000417232513D;
        if (this.onFire) {
            speed = 0.9D;
        }
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(speed);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10.0D);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.isBurning()) {
            this.onFire = true;
            this.applyEntityAttributes();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        switch (this.rand.nextInt(4)) {
            case 0:
                int amount = rand.nextInt(2) + 1;
                this.dropItem(AtumItems.DUSTY_BONE, amount);
                break;
        }
    }
}