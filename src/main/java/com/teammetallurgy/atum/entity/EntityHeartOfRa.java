package com.teammetallurgy.atum.entity;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityHeartOfRa extends Entity {
    private static final DataParameter<Boolean> SHOW_BOTTOM = EntityDataManager.createKey(EntityHeartOfRa.class, DataSerializers.BOOLEAN);
    public int innerRotation;

    public EntityHeartOfRa(World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(2.0F, 2.0F);
        this.innerRotation = this.rand.nextInt(100000);
    }

    public EntityHeartOfRa(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(SHOW_BOTTOM, Boolean.TRUE);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.innerRotation;
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        compound.setBoolean("ShowBottom", this.shouldShowBottom());
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        if (compound.hasKey("ShowBottom", 1)) {
            this.setShowBottom(compound.getBoolean("ShowBottom"));
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else {
            if (!this.isDead && !this.world.isRemote) {
                this.setDead();
                if (!this.world.isRemote) {
                    if (!source.isExplosion()) {
                        this.world.createExplosion(null, this.posX, this.posY, this.posZ, 3.0F, true);
                        this.dropItem(AtumItems.HEART_OF_RA, 1);
                    }
                }
            }
            return true;
        }
    }

    @Override
    @Nonnull
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(AtumItems.HEART_OF_RA);
    }

    public void setShowBottom(boolean showBottom) {
        this.getDataManager().set(SHOW_BOTTOM, showBottom);
    }

    public boolean shouldShowBottom() {
        return this.getDataManager().get(SHOW_BOTTOM);
    }
}