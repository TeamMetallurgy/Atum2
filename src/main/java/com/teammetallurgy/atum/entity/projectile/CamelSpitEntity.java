package com.teammetallurgy.atum.entity.projectile;

import com.teammetallurgy.atum.entity.animal.CamelEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class CamelSpitEntity extends LlamaSpitEntity {

    public CamelSpitEntity(FMLPlayMessages.SpawnEntity spawnPacket, World world) {
        this(AtumEntities.CAMEL_SPIT, world);
    }

    public CamelSpitEntity(EntityType<? extends CamelSpitEntity> entityType, World world) {
        super(entityType, world);
    }

    public CamelSpitEntity(World world, CamelEntity camel) {
        this(AtumEntities.CAMEL_SPIT, world);
        super.setShooter(camel);
        this.setPosition(camel.getPosX() - (double) (camel.getWidth() + 1.0F) * 0.5D * (double) MathHelper.sin(camel.renderYawOffset * ((float) Math.PI / 180F)), camel.getPosY() + (double) camel.getEyeHeight() - (double) 0.1F, camel.getPosZ() + (double) (camel.getWidth() + 1.0F) * 0.5D * (double) MathHelper.cos(camel.renderYawOffset * ((float) Math.PI / 180F)));
    }

    @OnlyIn(Dist.CLIENT)
    public CamelSpitEntity(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this(AtumEntities.CAMEL_SPIT, world);
        this.setPosition(x, y, z);

        for (int i = 0; i < 7; ++i) {
            double d0 = 0.4D + 0.1D * (double) i;
            world.addParticle(ParticleTypes.SPIT, x, y, z, xSpeed * d0, ySpeed, zSpeed * d0);
        }
        this.setMotion(xSpeed, ySpeed, zSpeed);
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        Vector3d motion = this.getMotion();
        RayTraceResult raytrace = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
        if (raytrace != null && raytrace.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytrace)) {
            this.onImpact(raytrace);
        }

        double d0 = this.getPosX() + motion.x;
        double d1 = this.getPosY() + motion.y;
        double d2 = this.getPosZ() + motion.z;
        this.func_234617_x_();
        if (this.world.func_234853_a_(this.getBoundingBox()).noneMatch(AbstractBlock.AbstractBlockState::isAir)) {
            this.remove();
        } else if (this.isInWaterOrBubbleColumn()) {
            this.remove();
        } else {
            this.setMotion(motion.scale(0.99F));
            if (!this.hasNoGravity()) {
                this.setMotion(this.getMotion().add(0.0D, -0.06F, 0.0D));
            }

            this.setPosition(d0, d1, d2);
        }
    }

    @Override
    protected void onEntityHit(@Nonnull EntityRayTraceResult rayTraceResult) {
        super.onEntityHit(rayTraceResult);
        Entity entity = this.func_234616_v_();
        if (entity instanceof LivingEntity) {
            rayTraceResult.getEntity().attackEntityFrom(DamageSource.causeIndirectDamage(this, (LivingEntity) entity).setProjectile(), 1.0F);
        }
    }

    @Override
    protected void func_230299_a_(@Nonnull BlockRayTraceResult rayTraceResult) {
        super.func_230299_a_(rayTraceResult);
        if (!this.world.isRemote) {
            this.remove();
        }

    }

    @Override
    protected void registerData() {
    }
}