package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BurningTrapTileEntity extends TrapTileEntity {

    public BurningTrapTileEntity() {
        super(AtumTileEntities.BURNING_TRAP);
    }

    @Override
    protected void triggerTrap(Level world, Direction facing, LivingEntity livingBase) {
        double x = (double) this.worldPosition.getX() + 0.5D;
        double y = (double) this.worldPosition.getY() + world.random.nextDouble() * 6.0D / 16.0D;
        double z = (double) this.worldPosition.getZ() + 0.5D;
        double randomPos = world.random.nextDouble() * 0.6D - 0.3D;

        if (!world.isClientSide) {
            livingBase.setSecondsOnFire(8);
        }

        if (world.random.nextDouble() < 0.2D) {
            world.playLocalSound((double) this.worldPosition.getX() + 0.5D, this.worldPosition.getY(), (double) worldPosition.getZ() + 0.5D, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        switch (facing) {
            case DOWN:
                world.addParticle(ParticleTypes.SMOKE, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                break;
            case UP:
                world.addParticle(ParticleTypes.SMOKE, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
                break;
            case WEST:
                world.addParticle(ParticleTypes.SMOKE, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case EAST:
                world.addParticle(ParticleTypes.SMOKE, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case NORTH:
                world.addParticle(ParticleTypes.SMOKE, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                break;
            case SOUTH:
                world.addParticle(ParticleTypes.SMOKE, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                break;
        }
    }
}