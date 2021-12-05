package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.network.FMLPlayMessages;

import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;

public class ArrowDoubleEntity extends CustomArrow {

    public ArrowDoubleEntity(FMLPlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AtumEntities.DOUBLE_ARROW, world);
    }

    public ArrowDoubleEntity(EntityType<? extends ArrowDoubleEntity> entityType, Level world) {
        super(entityType, world);
        this.pickup = Pickup.DISALLOWED;
    }

    public ArrowDoubleEntity(Level world, LivingEntity shooter) {
        super(AtumEntities.DOUBLE_ARROW, world, shooter);
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isCritArrow()) {
            if (level instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) level;
                serverWorld.sendParticles(AtumParticles.ISIS, this.getX() + (level.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), this.getY() + level.random.nextDouble() * (double) this.getBbHeight(), this.getZ() + (level.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), 2, 0.0D, 0.0D, 0.0D, 0.01D);
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_double.png");
    }
}