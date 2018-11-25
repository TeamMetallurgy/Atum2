package com.teammetallurgy.atum.entity;

import com.teammetallurgy.atum.blocks.BlockHeartOfRa;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityHeartOfRa extends Entity {
    public int innerRotation;

    public EntityHeartOfRa(World world) {
        super(world);
        this.preventEntitySpawning = true;
        this.setSize(2.0F, 2.0F);
        this.innerRotation = this.rand.nextInt(500000);
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
    }

    @Override
    public void onUpdate() {
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
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else {
            if (!this.isDead && !this.world.isRemote) {
                this.setDead();
                if (!this.world.isRemote) {
                    if (!source.isExplosion()) {
                        this.world.createExplosion(null, this.posX, this.posY, this.posZ, 3.0F, true);
                        this.dropItem(Item.getItemFromBlock(AtumBlocks.HEART_OF_RA), 1);
                    }
                }
            }
            return true;
        }
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
    }
}