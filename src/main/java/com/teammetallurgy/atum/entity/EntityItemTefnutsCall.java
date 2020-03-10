package com.teammetallurgy.atum.entity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityItemTefnutsCall extends ItemEntity {

    public EntityItemTefnutsCall(World world, double x, double y, double z, @Nonnull ItemStack stack) {
        super(world, x, y, z, stack);
        this.setPickupDelay(40);
    }

    @Override
    protected void dealFireDamage(int amount) {
        if (this.isInLava()) { //Only deal fire damage when in lava
            super.dealFireDamage(amount);
        }
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }
}