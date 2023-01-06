package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nonnull;

public class ArrowExplosiveEntity extends CustomArrow {
    private float velocity;

    public ArrowExplosiveEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AtumEntities.EXPLOSIVE_ARROW.get(), world);
    }

    public ArrowExplosiveEntity(EntityType<? extends ArrowExplosiveEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ArrowExplosiveEntity(Level world, LivingEntity shooter, float velocity) {
        super(AtumEntities.EXPLOSIVE_ARROW.get(), world, shooter);
        this.velocity = velocity;
    }

    @Override
    public void tick() {
        if (this.inGroundTime == 20 && velocity == 1.0F && !this.inGround && level.getGameTime() % 2L == 0L) {
            level.playSound(null, blockPosition(), SoundEvents.TNT_PRIMED, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        super.tick();
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);

        if (velocity == 1.0F) {
            if (!level.isClientSide) {
                level.explode(this, getX(), getY(), getZ(), 2.0F, Level.ExplosionInteraction.TNT);
            }
            this.discard();
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_explosive.png");
    }
}