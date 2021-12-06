package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PoisonTrapTileEntity extends TrapTileEntity {

    public PoisonTrapTileEntity() {
        super(AtumTileEntities.POISON_TRAP);
    }

    @Override
    protected void triggerTrap(Level world, Direction facing, LivingEntity livingBase) {
        double randomPos = world.random.nextDouble() * 0.6D - 0.3D;
        double x = (double) worldPosition.getX() + 0.5D;
        double y = (double) worldPosition.getY() + world.random.nextDouble() * 0.7D;
        double z = (double) worldPosition.getZ() + 0.5D;

        if (!livingBase.hasEffect(MobEffects.POISON)) {
            if (!world.isClientSide) {
                livingBase.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 3, false, false));
            }
            world.playLocalSound((double) this.worldPosition.getX() + 0.5D, this.worldPosition.getY(), (double) this.worldPosition.getZ() + 0.5D, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        if (world instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel) world;
            switch (facing) {
                case DOWN:
                    serverWorld.sendParticles(AtumParticles.GAS, x - randomPos, (double) this.worldPosition.getY() - 0.1D, z - 0.2 + (world.random.nextDouble() * 0.4D), 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
                case UP:
                    serverWorld.sendParticles(AtumParticles.GAS, x - randomPos, (double) this.worldPosition.getY() + 1.1D, z - 0.2 + (world.random.nextDouble() * 0.4D), 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
                case WEST:
                    serverWorld.sendParticles(AtumParticles.GAS, x - 0.52D, y, z + randomPos, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
                case EAST:
                    serverWorld.sendParticles(AtumParticles.GAS, x + 0.52D, y, z + randomPos, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
                case NORTH:
                    serverWorld.sendParticles(AtumParticles.GAS, x + randomPos, y, z - 0.52D, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
                case SOUTH:
                    serverWorld.sendParticles(AtumParticles.GAS, x + randomPos, y, z + 0.52D, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
            }
        }
    }
}