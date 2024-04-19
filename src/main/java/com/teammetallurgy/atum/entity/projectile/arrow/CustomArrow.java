package com.teammetallurgy.atum.entity.projectile.arrow;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public abstract class CustomArrow extends Arrow {

    public CustomArrow(EntityType<? extends CustomArrow> entityType, Level level) {
        super(entityType, level);
    }

    public CustomArrow(EntityType<? extends CustomArrow> entityType, Level level, double x, double y, double z) {
        this(entityType, level);
        this.setPos(x, y, z);
    }

    public CustomArrow(EntityType<? extends CustomArrow> entityType, Level level, LivingEntity shooter) {
        this(entityType, level, shooter.getX(), shooter.getEyeY() - (double) 0.1F, shooter.getZ());
        this.setOwner(shooter);
        if (shooter instanceof Player) {
            this.pickup = AbstractArrow.Pickup.ALLOWED;
        }
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