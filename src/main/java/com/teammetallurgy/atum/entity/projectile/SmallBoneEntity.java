package com.teammetallurgy.atum.entity.projectile;

import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class SmallBoneEntity extends AbstractFireballEntity {

    public SmallBoneEntity(FMLPlayMessages.SpawnEntity spawnPacket, World world) {
        this(AtumEntities.SMALL_BONE, world);
    }

    public SmallBoneEntity(EntityType<? extends SmallBoneEntity> entityType, World world) {
        super(entityType, world);
    }

    public SmallBoneEntity(World world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(AtumEntities.SMALL_BONE, shooter, accelX, accelY, accelZ, world);
    }

    public SmallBoneEntity(World world, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(AtumEntities.SMALL_BONE, x, y, z, accelX, accelY, accelZ, world);
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    @Nonnull
    protected IParticleData getParticle() {
        return AtumParticles.EMPTY;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public ItemStack getItem() {
        ItemStack stack = this.getStack();
        return stack.isEmpty() ? new ItemStack(AtumItems.DUSTY_BONE) : stack;
    }

    @Override
    protected void onEntityHit(@Nonnull EntityRayTraceResult rayTraceResult) {
        super.onEntityHit(rayTraceResult);
        if (!this.world.isRemote) {
            Entity entity = rayTraceResult.getEntity();
            Entity shootingEntity = this.func_234616_v_();
            boolean flag = entity.attackEntityFrom(DamageSource.func_233547_a_(this, shootingEntity), 5.0F);
            if (shootingEntity instanceof LivingEntity && flag) {
                this.applyEnchantments((LivingEntity) shootingEntity, entity);
            }
        }
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            this.remove();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        return false;
    }
}