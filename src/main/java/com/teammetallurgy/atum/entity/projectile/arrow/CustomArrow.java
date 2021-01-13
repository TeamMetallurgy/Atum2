package com.teammetallurgy.atum.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public abstract class CustomArrow extends ArrowEntity {

    public CustomArrow(EntityType<? extends CustomArrow> entityType, World world) {
        super(entityType, world);
    }

    public CustomArrow(EntityType<? extends CustomArrow> entityType, World world, double x, double y, double z) {
        this(entityType, world);
        this.setPosition(x, y, z);
    }

    public CustomArrow(EntityType<? extends CustomArrow> entityType, World world, LivingEntity shooter) {
        this(entityType, world, shooter.getPosX(), shooter.getPosYEye() - (double) 0.1F, shooter.getPosZ());
        this.setShooter(shooter);
        if (shooter instanceof PlayerEntity) {
            this.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
        }
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
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