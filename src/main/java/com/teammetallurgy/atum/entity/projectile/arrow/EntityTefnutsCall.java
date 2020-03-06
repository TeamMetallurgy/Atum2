package com.teammetallurgy.atum.entity.projectile.arrow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class EntityTefnutsCall extends CustomArrow {
    private ItemStack stack = ItemStack.EMPTY;

    public EntityTefnutsCall(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    public EntityTefnutsCall(World world, EntityLivingBase shooter) {
        super(world, shooter);
        this.isImmuneToFire = true;
    }

    public void setStack(@Nonnull ItemStack stack) {
        this.stack = stack;
    }

    @Override
    @Nonnull
    protected ItemStack getArrowStack() {
        return stack;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    protected void onHit(RayTraceResult raytraceResult) {
        Entity entity = raytraceResult.entityHit;
        if (raytraceResult != null && raytraceResult.entityHit instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) raytraceResult.entityHit;

            if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                raytraceResult = null;
            }
        }

        if (raytraceResult != null && entity != null && raytraceResult.typeOfHit == RayTraceResult.Type.ENTITY) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            int i = MathHelper.ceil((double) f * this.getDamage());
            if (this.getIsCritical()) {
                i += this.rand.nextInt(i / 2 + 2);
            }

            DamageSource damagesource;

            if (this.shootingEntity == null) {
                damagesource = DamageSource.causeArrowDamage(this, this);
            } else {
                damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
            }

            if (this.isBurning() && !(entity instanceof EntityEnderman)) {
                entity.setFire(5);
            }

            if (entity.attackEntityFrom(damagesource, (float) i)) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase) entity;

                    if (!this.world.isRemote) {
                        entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                    }

                    if (this.shootingEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, entitylivingbase);
                    }

                    this.arrowHit(entitylivingbase);

                    if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                    }
                }
                world.addWeatherEffect(new EntityLightningBolt(world, posX, posY, posZ, false));
            }
        }
        super.onHit(raytraceResult);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        NBTTagCompound stackTag = new NBTTagCompound();
        stack.writeToNBT(stackTag);
        compound.setTag("stack", stackTag);
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        NBTTagCompound stackTag = compound.getCompoundTag("stack");
        stack = new ItemStack(stackTag);
    }
}