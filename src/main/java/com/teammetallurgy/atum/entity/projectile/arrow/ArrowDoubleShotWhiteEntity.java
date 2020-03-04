package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ArrowDoubleShotWhiteEntity extends CustomArrow {

    public ArrowDoubleShotWhiteEntity(EntityType<? extends ArrowDoubleShotWhiteEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowDoubleShotWhiteEntity(World world, LivingEntity shooter) {
        super(world, shooter);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getIsCritical()) {
            for (int l = 0; l < 8; ++l) {
                world.addParticle(AtumParticles.NUIT_WHITE, this.getPosX() + (world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), this.getPosY() + world.rand.nextDouble() * (double) this.getHeight(), this.getPosZ() + (world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_double_white.png");
    }
}