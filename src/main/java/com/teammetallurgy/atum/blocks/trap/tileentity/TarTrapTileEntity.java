package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TarTrapTileEntity extends TrapTileEntity {

    public TarTrapTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.TAR_TRAP.get(), pos, state);
    }

    @Override
    protected void triggerTrap(ServerLevel serverLevel, Direction facing, LivingEntity entity) {
        double x = (double) this.worldPosition.getX() + 0.5D;
        double y = (double) this.worldPosition.getY() + serverLevel.random.nextDouble() * 12.0D / 16.0D;
        double z = (double) this.worldPosition.getZ() + 0.5D;
        double randomPos = serverLevel.random.nextDouble() * 0.6D - 0.3D;

        if (!entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 3, false, false));
        }

        if (serverLevel.getGameTime() % 8L == 0L) {
            serverLevel.playSound(null, (double) this.worldPosition.getX() + 0.5D, this.worldPosition.getY(), (double) this.worldPosition.getZ() + 0.5D, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.8F, 0.8F);
            switch (facing) {
                case DOWN -> serverLevel.sendParticles(AtumParticles.TAR.get(), x - randomPos, y - 0.2D, z, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                case UP -> serverLevel.sendParticles(AtumParticles.TAR.get(), x - randomPos, y + 0.65D, z, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                case WEST -> serverLevel.sendParticles(AtumParticles.TAR.get(), x - 0.52D, y, z + randomPos, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                case EAST -> serverLevel.sendParticles(AtumParticles.TAR.get(), x + 0.52D, y, z + randomPos, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                case NORTH -> serverLevel.sendParticles(AtumParticles.TAR.get(), x + randomPos, y, z - 0.52D, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                case SOUTH -> serverLevel.sendParticles(AtumParticles.TAR.get(), x + randomPos, y, z + 0.52D, 2, 0.01D, 0.0D, 0.01D, 0.01D);
            }
        }
    }
}