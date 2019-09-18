package com.teammetallurgy.atum.blocks.trap.tileentity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class TileEntitySmokeTrap extends TileEntityTrap {

    @Override
    protected void triggerTrap(EnumFacing facing, LivingEntity livingBase) {
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + world.rand.nextDouble() * 6.0D / 16.0D;
        double z = (double) pos.getZ() + 0.5D;
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;

        if (!livingBase.isPotionActive(Effects.BLINDNESS)) {
            if (!world.isRemote) {
                livingBase.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 120));
            }
            world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.ENTITY_CAT_HISS, SoundCategory.BLOCKS, 0.3F, 0.8F, false);
        }

        switch (facing) {
            case DOWN:
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                break;
            case UP:
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
                break;
            case WEST:
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case EAST:
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case NORTH:
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                break;
            case SOUTH:
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                break;
        }
    }
}