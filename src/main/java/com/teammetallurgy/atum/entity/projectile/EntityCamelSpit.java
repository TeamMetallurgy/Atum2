package com.teammetallurgy.atum.entity.projectile;

import com.teammetallurgy.atum.entity.animal.EntityCamel;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EntityCamelSpit extends EntityLlamaSpit {
    private EntityCamel owner;
    private CompoundNBT ownerNbt;

    public EntityCamelSpit(World world) {
        super(world);
    }

    public EntityCamelSpit(World world, EntityCamel camel) {
        super(world);
        this.owner = camel;
        this.setPosition(camel.posX - (double) (camel.width + 1.0F) * 0.5D * (double) MathHelper.sin(camel.renderYawOffset * 0.017453292F), camel.posY + (double) camel.getEyeHeight() - 0.10000000149011612D, camel.posZ + (double) (camel.width + 1.0F) * 0.5D * (double) MathHelper.cos(camel.renderYawOffset * 0.017453292F));
        this.setSize(0.25F, 0.25F);
    }

    @OnlyIn(Dist.CLIENT)
    public EntityCamelSpit(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void onHit(RayTraceResult rayTrace) {
        if (rayTrace.entityHit != null && this.owner != null) {
            rayTrace.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.owner).setProjectile(), 1.0F);
        }

        if (!this.world.isRemote) {
            this.setDead();
        }
    }

    @Override
    public void tick() {
        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        }
        this.baseTick();

        if (this.ownerNbt != null) {
            this.restoreOwnerFromSave();
        }
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (raytraceresult != null) {
            vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
        }
        Entity entity = this.getHitEntity(vec3d, vec3d1);

        if (entity != null) {
            raytraceresult = new RayTraceResult(entity);
        }

        if (raytraceresult != null && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

        for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }
        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }
        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        if (!this.world.isMaterialInBB(this.getBoundingBox(), Material.AIR)) {
            this.setDead();
        } else if (this.isInWater()) {
            this.setDead();
        } else {
            this.motionX *= 0.9900000095367432D;
            this.motionY *= 0.9900000095367432D;
            this.motionZ *= 0.9900000095367432D;

            if (!this.hasNoGravity()) {
                this.motionY -= 0.05999999865889549D;
            }
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    @Nullable
    private Entity getHitEntity(Vec3d pos, Vec3d motionPos) {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
        double distance = 0.0D;

        for (Entity hitEntity : list) {
            if (hitEntity != this.owner) {
                AxisAlignedBB box = hitEntity.getBoundingBox().grow(0.30000001192092896D);
                RayTraceResult rayTrace = box.calculateIntercept(pos, motionPos);
                if (rayTrace != null) {
                    double squareDistanceTo = pos.squareDistanceTo(rayTrace.hitVec);
                    if (squareDistanceTo < distance || distance == 0.0D) {
                        entity = hitEntity;
                        distance = squareDistanceTo;
                    }
                }
            }
        }
        return entity;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        if (compound.hasKey("Owner", 10)) {
            this.ownerNbt = compound.getCompoundTag("Owner");
        }
    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
        if (this.owner != null) {
            CompoundNBT nbttagcompound = new CompoundNBT();
            UUID uuid = this.owner.getUniqueID();
            nbttagcompound.setUniqueId("OwnerUUID", uuid);
            compound.setTag("Owner", nbttagcompound);
        }
    }

    private void restoreOwnerFromSave() {
        if (this.ownerNbt != null && this.ownerNbt.hasUniqueId("OwnerUUID")) {
            UUID uuid = this.ownerNbt.getUniqueId("OwnerUUID");

            for (EntityCamel entitycamel : this.world.getEntitiesWithinAABB(EntityCamel.class, this.getBoundingBox().grow(15.0D))) {
                if (entitycamel.getUniqueID().equals(uuid)) {
                    this.owner = entitycamel;
                    break;
                }
            }
        }
        this.ownerNbt = null;
    }
}