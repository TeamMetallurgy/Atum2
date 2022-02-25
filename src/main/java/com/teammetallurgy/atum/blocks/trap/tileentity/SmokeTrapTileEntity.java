package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SmokeTrapTileEntity extends TrapTileEntity {

    public SmokeTrapTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.SMOKE_TRAP.get(), pos, state);
    }

    @Override
    protected void triggerTrap(ServerLevel serverLevel, Direction facing, LivingEntity livingBase) {
        double x = (double) this.worldPosition.getX() + 0.5D;
        double y = (double) this.worldPosition.getY() + serverLevel.random.nextDouble() * 6.0D / 16.0D;
        double z = (double) this.worldPosition.getZ() + 0.5D;
        double randomPos = serverLevel.random.nextDouble() * 0.6D - 0.3D;

        if (!livingBase.hasEffect(MobEffects.BLINDNESS)) {
            livingBase.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 120));
            serverLevel.playSound(null, (double) this.worldPosition.getX() + 0.5D, this.worldPosition.getY(), (double) this.worldPosition.getZ() + 0.5D, SoundEvents.CAT_HISS, SoundSource.BLOCKS, 0.3F, 0.8F);
        }

        switch (facing) {
            case DOWN -> serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, x - randomPos, y - 0.2D, z, 2, 0.0D, 0.0D, 0.0D, 0.0D);
            case UP -> serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, x - randomPos, y + 1.0D, z, 2, 0.0D, 0.0D, 0.0D, 0.0D);
            case WEST -> serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, x - 0.52D, y, z + randomPos, 2, 0.0D, 0.0D, 0.0D, 0.0D);
            case EAST -> serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, x + 0.52D, y, z + randomPos, 2, 0.0D, 0.0D, 0.0D, 0.0D);
            case NORTH -> serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, x + randomPos, y, z - 0.52D, 2, 0.0D, 0.0D, 0.0D, 0.0D);
            case SOUTH -> serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, x + randomPos, y, z + 0.52D, 2, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }
}