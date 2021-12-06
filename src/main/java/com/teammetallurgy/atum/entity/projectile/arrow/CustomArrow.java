package com.teammetallurgy.atum.entity.projectile.arrow;

import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;

public abstract class CustomArrow extends Arrow {

    public CustomArrow(EntityType<? extends CustomArrow> entityType, Level world) {
        super(entityType, world);
    }

    public CustomArrow(EntityType<? extends CustomArrow> entityType, Level world, double x, double y, double z) {
        this(entityType, world);
        this.setPos(x, y, z);
    }

    public CustomArrow(EntityType<? extends CustomArrow> entityType, Level world, LivingEntity shooter) {
        this(entityType, world, shooter.getX(), shooter.getEyeY() - (double) 0.1F, shooter.getZ());
        this.setOwner(shooter);
        if (shooter instanceof Player) {
            this.pickup = AbstractArrow.Pickup.ALLOWED;
        }
    }

    @Override
    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @Nonnull
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation("arrow");
    }
}