package com.teammetallurgy.atum.entity.projectile;

import com.teammetallurgy.atum.entity.animal.QuailEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class QuailEggEntity extends ProjectileItemEntity {

    public QuailEggEntity(FMLPlayMessages.SpawnEntity spawnPacket, World world) {
        this(AtumEntities.QUAIL_EGG, world);
    }

    public QuailEggEntity(EntityType<? extends QuailEggEntity> entityType, World world) {
        super(entityType, world);
    }

    public QuailEggEntity(World world, LivingEntity thrower) {
        super(AtumEntities.QUAIL_EGG, thrower, world);
    }

    public QuailEggEntity(World world, double x, double y, double z) {
        super(AtumEntities.QUAIL_EGG, x, y, z, world);
    }

    @Override
    @Nonnull
    protected Item getDefaultItem() {
        return AtumItems.QUAIL_EGG;
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, this.getItem()), this.getPosX(), this.getPosY(), this.getPosZ(), ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    protected void onEntityHit(@Nonnull EntityRayTraceResult result) {
        super.onEntityHit(result);
        result.getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), 0.0F);
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            if (this.rand.nextInt(8) == 0) {
                int i = 1;
                if (this.rand.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    QuailEntity quail = AtumEntities.QUAIL.create(this.world);
                    quail.setGrowingAge(-24000);
                    quail.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
                    this.world.addEntity(quail);
                }
            }
            this.world.setEntityState(this, (byte) 3);
            this.remove();
        }
    }
}