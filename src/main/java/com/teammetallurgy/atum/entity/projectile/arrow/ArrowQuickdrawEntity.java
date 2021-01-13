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

public class ArrowQuickdrawEntity extends CustomArrow {

    public ArrowQuickdrawEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(AtumEntities.QUICKDRAW_ARROW, world);
    }

    public ArrowQuickdrawEntity(EntityType<? extends ArrowQuickdrawEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowQuickdrawEntity(World world, LivingEntity shooter) {
        super(AtumEntities.QUICKDRAW_ARROW, world, shooter);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/arrow/arrow_quickdraw.png");
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getIsCritical()) {
            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                serverWorld.spawnParticle(AtumParticles.SHU, this.getPosX() + (world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), this.getPosY() + world.rand.nextDouble() * (double) this.getHeight(), this.getPosZ() + (world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), 1, 0.0D, 0.0D, 0.0D, 0.01D);
            }
        }
    }
}