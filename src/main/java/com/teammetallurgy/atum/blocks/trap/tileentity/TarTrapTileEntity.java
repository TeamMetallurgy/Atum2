package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class TarTrapTileEntity extends TrapTileEntity {

    public TarTrapTileEntity() {
        super(AtumTileEntities.TAR_TRAP);
    }

    @Override
    protected void triggerTrap(Level world, Direction facing, LivingEntity entity) {
        double x = (double) this.worldPosition.getX() + 0.5D;
        double y = (double) this.worldPosition.getY() + world.random.nextDouble() * 12.0D / 16.0D;
        double z = (double) this.worldPosition.getZ() + 0.5D;
        double randomPos = world.random.nextDouble() * 0.6D - 0.3D;

        if (!entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            if (!world.isClientSide) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 3, false, false));
            }
        }

        if (world.getGameTime() % 8L == 0L) {
            entity.playSound(SoundEvents.LAVA_POP, 1.0F, 1.0F);
            if (world instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) world;
                switch (facing) {
                    case DOWN:
                        serverWorld.sendParticles(AtumParticles.TAR, x - randomPos, y - 0.2D, z, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                    case UP:
                        serverWorld.sendParticles(AtumParticles.TAR, x - randomPos, y + 0.65D, z, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                    case WEST:
                        serverWorld.sendParticles(AtumParticles.TAR, x - 0.52D, y, z + randomPos, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                    case EAST:
                        serverWorld.sendParticles(AtumParticles.TAR, x + 0.52D, y, z + randomPos, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                    case NORTH:
                        serverWorld.sendParticles(AtumParticles.TAR, x + randomPos, y, z - 0.52D, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                    case SOUTH:
                        serverWorld.sendParticles(AtumParticles.TAR, x + randomPos, y, z + 0.52D, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                }
            }
        }
    }
}