package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ArrowExplosiveEntity extends CustomArrow {
    private float velocity;

    public ArrowExplosiveEntity(EntityType<? extends ArrowExplosiveEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowExplosiveEntity(World world, LivingEntity shooter, float velocity) {
        super(world, shooter);
        this.velocity = velocity;
    }

    @Override
    public void tick() {
        if (this.timeInGround == 20 && velocity == 1.0F && !this.inGround && world.getGameTime() % 2L == 0L) {
            world.playSound(null, getPosition(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
        super.tick();
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        super.onImpact(result);

        if (velocity == 1.0F) {
            if (!world.isRemote) {
                world.createExplosion(this, getPosX(), getPosY(), getPosZ(), 2.0F, Explosion.Mode.BREAK);
            }
            this.remove();
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_explosive.png");
    }
}