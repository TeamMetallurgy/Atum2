package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BurningTrapTileEntity extends TrapTileEntity {

    public BurningTrapTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.BURNING_TRAP.get(), pos, state);
    }

    @Override
    protected void triggerTrap(Level level, Direction facing, LivingEntity livingBase) {
        double x = (double) this.worldPosition.getX() + 0.5D;
        double y = (double) this.worldPosition.getY() + level.random.nextDouble() * 6.0D / 16.0D;
        double z = (double) this.worldPosition.getZ() + 0.5D;
        double randomPos = level.random.nextDouble() * 0.6D - 0.3D;

        if (!level.isClientSide) {
            livingBase.setSecondsOnFire(8);
        }

        if (level.random.nextDouble() < 0.2D) {
            level.playLocalSound((double) this.worldPosition.getX() + 0.5D, this.worldPosition.getY(), (double) worldPosition.getZ() + 0.5D, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        switch (facing) {
            case DOWN -> {
                level.addParticle(ParticleTypes.SMOKE, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                level.addParticle(ParticleTypes.FLAME, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
            }
            case UP -> {
                level.addParticle(ParticleTypes.SMOKE, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
                level.addParticle(ParticleTypes.FLAME, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
            }
            case WEST -> {
                level.addParticle(ParticleTypes.SMOKE, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                level.addParticle(ParticleTypes.FLAME, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
            }
            case EAST -> {
                level.addParticle(ParticleTypes.SMOKE, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                level.addParticle(ParticleTypes.FLAME, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
            }
            case NORTH -> {
                level.addParticle(ParticleTypes.SMOKE, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                level.addParticle(ParticleTypes.FLAME, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
            }
            case SOUTH -> {
                level.addParticle(ParticleTypes.SMOKE, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                level.addParticle(ParticleTypes.FLAME, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}