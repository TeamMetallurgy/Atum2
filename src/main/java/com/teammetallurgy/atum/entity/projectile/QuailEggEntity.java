package com.teammetallurgy.atum.entity.projectile;

import com.teammetallurgy.atum.entity.animal.QuailEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.network.NetworkHooks;
import net.neoforged.neoforge.network.PlayMessages;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class QuailEggEntity extends ThrowableItemProjectile {

    public QuailEggEntity(PlayMessages.SpawnEntity spawnPacket, Level level) {
        this(AtumEntities.QUAIL_EGG.get(), level);
    }

    public QuailEggEntity(EntityType<? extends QuailEggEntity> entityType, Level level) {
        super(entityType, level);
    }

    public QuailEggEntity(Level level, LivingEntity thrower) {
        super(AtumEntities.QUAIL_EGG.get(), thrower, level);
    }

    public QuailEggEntity(Level level, double x, double y, double z) {
        super(AtumEntities.QUAIL_EGG.get(), x, y, z, level);
    }

    @Override
    @Nonnull
    protected Item getDefaultItem() {
        return AtumItems.QUAIL_EGG.get();
    }

    @Override
    @Nonnull
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            if (this.random.nextInt(8) == 0) {
                int i = 1;
                if (this.random.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    QuailEntity quail = AtumEntities.QUAIL.get().create(this.level);
                    if (quail != null) {
                        quail.setAge(-24000);
                        quail.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                        this.level.addFreshEntity(quail);
                    }
                }
            }
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }
}