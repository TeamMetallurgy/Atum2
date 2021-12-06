package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class ArrowStraightEntity extends CustomArrow {
    private float velocity;

    public ArrowStraightEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AtumEntities.STRAIGHT_ARROW, world);
    }

    public ArrowStraightEntity(EntityType<? extends CustomArrow> entityType, Level world) {
        super(entityType, world);
    }

    public ArrowStraightEntity(Level world, LivingEntity shooter, float velocity) {
        super(AtumEntities.STRAIGHT_ARROW, world, shooter);
        this.velocity = velocity;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.velocity == 1.0F) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
            if (!this.inGround && this.tickCount > 300) {
                this.remove();
            }

            if (this.getOwner() instanceof LivingEntity && !this.inGround && this.velocity == 1.0F && this.isAlive()) {
                if (this.level instanceof ServerLevel && this.tickCount > 2) {
                    ServerLevel serverWorld = (ServerLevel) this.level;
                    serverWorld.sendParticles(AtumParticles.HORUS, getX(), getY() - 0.05D, getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_straight.png");
    }
}