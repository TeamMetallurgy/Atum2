package com.teammetallurgy.atum.blocks.trap.tileentity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class BurningTrapTileEntity extends TrapTileEntity {

    @Override
    protected void triggerTrap(Direction facing, LivingEntity livingBase) {
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + world.rand.nextDouble() * 6.0D / 16.0D;
        double z = (double) pos.getZ() + 0.5D;
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;

        if (!world.isRemote) {
            livingBase.setFire(8);
        }

        if (world.rand.nextDouble() < 0.2D) {
            world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
        switch (facing) {
            case DOWN:
                world.addParticle(ParticleTypes.SMOKE_NORMAL, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                break;
            case UP:
                world.addParticle(ParticleTypes.SMOKE_NORMAL, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
                break;
            case WEST:
                world.addParticle(ParticleTypes.SMOKE_NORMAL, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case EAST:
                world.addParticle(ParticleTypes.SMOKE_NORMAL, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case NORTH:
                world.addParticle(ParticleTypes.SMOKE_NORMAL, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                break;
            case SOUTH:
                world.addParticle(ParticleTypes.SMOKE_NORMAL, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                break;
        }
    }
}