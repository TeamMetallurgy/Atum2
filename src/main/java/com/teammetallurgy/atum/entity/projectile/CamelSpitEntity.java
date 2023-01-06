package com.teammetallurgy.atum.entity.projectile;

import com.teammetallurgy.atum.entity.animal.CamelEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nonnull;

public class CamelSpitEntity extends LlamaSpit {

    public CamelSpitEntity(PlayMessages.SpawnEntity spawnPacket, Level world) {
        this(AtumEntities.CAMEL_SPIT.get(), world);
    }

    public CamelSpitEntity(EntityType<? extends CamelSpitEntity> entityType, Level world) {
        super(entityType, world);
    }

    public CamelSpitEntity(Level world, CamelEntity camel) {
        this(AtumEntities.CAMEL_SPIT.get(), world);
        super.setOwner(camel);
        this.setPos(camel.getX() - (double) (camel.getBbWidth() + 1.0F) * 0.5D * (double) Mth.sin(camel.yBodyRot * ((float) Math.PI / 180F)), camel.getY() + (double) camel.getEyeHeight() - (double) 0.1F, camel.getZ() + (double) (camel.getBbWidth() + 1.0F) * 0.5D * (double) Mth.cos(camel.yBodyRot * ((float) Math.PI / 180F)));
    }

    @OnlyIn(Dist.CLIENT)
    public CamelSpitEntity(Level world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this(AtumEntities.CAMEL_SPIT.get(), world);
        this.setPos(x, y, z);

        for (int i = 0; i < 7; ++i) {
            double d0 = 0.4D + 0.1D * (double) i;
            world.addParticle(ParticleTypes.SPIT, x, y, z, xSpeed * d0, ySpeed, zSpeed * d0);
        }
        this.setDeltaMovement(xSpeed, ySpeed, zSpeed);
    }

    @Override
    @Nonnull
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 motion = this.getDeltaMovement();
        HitResult raytrace = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (raytrace != null && raytrace.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytrace)) {
            this.onHit(raytrace);
        }

        double d0 = this.getX() + motion.x;
        double d1 = this.getY() + motion.y;
        double d2 = this.getZ() + motion.z;
        this.updateRotation();
        if (this.level.getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.discard();
        } else if (this.isInWaterOrBubble()) {
            this.discard();
        } else {
            this.setDeltaMovement(motion.scale(0.99F));
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.06F, 0.0D));
            }

            this.setPos(d0, d1, d2);
        }
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult rayTraceResult) {
        super.onHitEntity(rayTraceResult);
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            rayTraceResult.getEntity().hurt(DamageSource.indirectMobAttack(this, (LivingEntity) entity).setProjectile(), 1.0F);
        }
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult rayTraceResult) {
        super.onHitBlock(rayTraceResult);
        if (!this.level.isClientSide) {
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData() {
    }
}