package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class ArrowStraightEntity extends CustomArrow {
    private float velocity;

    public ArrowStraightEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(AtumEntities.STRAIGHT_ARROW, world);
    }

    public ArrowStraightEntity(EntityType<? extends CustomArrow> entityType, World world) {
        super(entityType, world);
    }

    public ArrowStraightEntity(World world, LivingEntity shooter, float velocity) {
        super(AtumEntities.STRAIGHT_ARROW, world, shooter);
        this.velocity = velocity;
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            super.tick();
        }

        if (this.velocity == 1.0F) {
            this.setMotion(this.getMotion().add(0.0D, 0.05D, 0.0D));
            if (!this.inGround && ticksExisted > 300) {
                this.remove();
            }

            if (this.func_234616_v_() instanceof LivingEntity && !inGround && velocity == 1.0F && this.isAlive()) {
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) world;
                    serverWorld.spawnParticle(AtumParticles.HORUS, getPosX(), getPosY() - 0.05D, getPosZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_straight.png");
    }
}