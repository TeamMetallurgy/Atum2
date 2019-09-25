package com.teammetallurgy.atum.entity;

import com.teammetallurgy.atum.blocks.beacon.BlockHeartOfRa;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class HeartOfRaEntity extends Entity {
    public int innerRotation;

    public HeartOfRaEntity(EntityType<? extends HeartOfRaEntity> entityType, World world) {
        super(entityType, world);
        this.preventEntitySpawning = true;
        this.innerRotation = this.rand.nextInt(500000);
    }

    public HeartOfRaEntity(World world, double x, double y, double z) {
        this(AtumEntities.HEART_OF_RA, world);
        this.setPosition(x, y, z);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void registerData() {
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.innerRotation;

        if (!(world.getBlockState(getPosition()).getBlock() instanceof BlockHeartOfRa)) {
            this.attackEntityFrom(DamageSource.GENERIC, 1);
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (this.isAlive() && !this.world.isRemote) {
                this.remove();
                if (!this.world.isRemote) {
                    if (!source.isExplosion()) {
                        this.world.createExplosion(null, this.posX, this.posY, this.posZ, 3.0F, Explosion.Mode.DESTROY);
                        this.entityDropItem(AtumBlocks.HEART_OF_RA.asItem(), 1);
                    }
                }
            }
            return true;
        }
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
    }

    @Override
    protected void readAdditional(@Nonnull CompoundNBT compound) {
    }
}