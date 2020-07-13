package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BurningTrapTileEntity extends TrapTileEntity {

    public BurningTrapTileEntity() {
        super(AtumTileEntities.BURNING_TRAP);
    }

    @Override
    protected void triggerTrap(World world, Direction facing, LivingEntity livingBase) {
        double x = (double) this.pos.getX() + 0.5D;
        double y = (double) this.pos.getY() + world.rand.nextDouble() * 6.0D / 16.0D;
        double z = (double) this.pos.getZ() + 0.5D;
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;

        if (!world.isRemote) {
            livingBase.setFire(8);
        }

        if (world.rand.nextDouble() < 0.2D) {
            world.playSound((double) this.pos.getX() + 0.5D, this.pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
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