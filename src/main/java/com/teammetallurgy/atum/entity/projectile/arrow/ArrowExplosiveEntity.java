package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nonnull;

public class ArrowExplosiveEntity extends CustomArrow {
    private float velocity;

    public ArrowExplosiveEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(AtumEntities.EXPLOSIVE_ARROW, world);
    }

    public ArrowExplosiveEntity(EntityType<? extends ArrowExplosiveEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowExplosiveEntity(World world, LivingEntity shooter, float velocity) {
        super(AtumEntities.EXPLOSIVE_ARROW, world, shooter);
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