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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PoisonTrapTileEntity extends TrapTileEntity {

    public PoisonTrapTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.POISON_TRAP.get(), pos, state);
    }

    @Override
    protected void triggerTrap(Level level, Direction facing, LivingEntity livingBase) {
        double randomPos = level.random.nextDouble() * 0.6D - 0.3D;
        double x = (double) worldPosition.getX() + 0.5D;
        double y = (double) worldPosition.getY() + level.random.nextDouble() * 0.7D;
        double z = (double) worldPosition.getZ() + 0.5D;

        if (!livingBase.hasEffect(MobEffects.POISON)) {
            if (!level.isClientSide) {
                livingBase.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 3, false, false));
            }
            level.playLocalSound((double) this.worldPosition.getX() + 0.5D, this.worldPosition.getY(), (double) this.worldPosition.getZ() + 0.5D, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        if (level instanceof ServerLevel serverLevel) {
            switch (facing) {
                case DOWN -> serverLevel.sendParticles(AtumParticles.GAS, x - randomPos, (double) this.worldPosition.getY() - 0.1D, z - 0.2 + (serverLevel.random.nextDouble() * 0.4D), 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                case UP -> serverLevel.sendParticles(AtumParticles.GAS, x - randomPos, (double) this.worldPosition.getY() + 1.1D, z - 0.2 + (serverLevel.random.nextDouble() * 0.4D), 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                case WEST -> serverLevel.sendParticles(AtumParticles.GAS, x - 0.52D, y, z + randomPos, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                case EAST -> serverLevel.sendParticles(AtumParticles.GAS, x + 0.52D, y, z + randomPos, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                case NORTH -> serverLevel.sendParticles(AtumParticles.GAS, x + randomPos, y, z - 0.52D, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                case SOUTH -> serverLevel.sendParticles(AtumParticles.GAS, x + randomPos, y, z + 0.52D, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
            }
        }
    }
}