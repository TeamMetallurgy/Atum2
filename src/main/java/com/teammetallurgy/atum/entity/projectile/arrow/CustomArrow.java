package com.teammetallurgy.atum.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class CustomArrow extends ArrowEntity {

    public CustomArrow(EntityType<? extends CustomArrow> entityType, World world) {
        super(entityType, world);
    }

    public CustomArrow(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public CustomArrow(World world, LivingEntity shooter) {
        super(world, shooter);
    }

    @Override
    @Nonnull
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation("arrow");
    }
}