package com.teammetallurgy.atum.entity.arrow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

import javax.annotation.Nonnull;

public class CustomArrow extends EntityArrow implements IThrowableEntity {
    public CustomArrow(World world) {
        super(world);
    }

    public CustomArrow(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public CustomArrow(World world, EntityLivingBase shooter) {
        super(world, shooter);
    }


    @Override
    @Nonnull
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public Entity getThrower() {
        return shootingEntity;
    }

    @Override
    public void setThrower(Entity entity) {
        shootingEntity = entity;
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation("minecraft", "arrow");
    }
}