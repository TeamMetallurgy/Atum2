package com.teammetallurgy.atum.entity.projectile;

import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class SmallBoneEntity extends AbstractFireballEntity {

    public SmallBoneEntity(EntityType<? extends SmallBoneEntity> entityType, World p_i50166_2_) {
        super(entityType, p_i50166_2_);
    }

    public SmallBoneEntity(World world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(AtumEntities.SMALL_BONE, shooter, accelX, accelY, accelZ, world);
    }

    public SmallBoneEntity(World world, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(AtumEntities.SMALL_BONE, x, y, z, accelX, accelY, accelZ, world);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public ItemStack getItem() {
        ItemStack stack = this.func_213896_l();
        return stack.isEmpty() ? new ItemStack(AtumItems.DUSTY_BONE) : stack;
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) result).getEntity();
                boolean attackEntity = entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), 2.0F);
                if (attackEntity) {
                    this.applyEnchantments(this.shootingEntity, entity);
                }
            }
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