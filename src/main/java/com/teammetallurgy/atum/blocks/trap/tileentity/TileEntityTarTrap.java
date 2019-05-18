package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;

public class TileEntityTarTrap extends TileEntityTrap {

    @Override
    protected void triggerTrap(EnumFacing facing, EntityLivingBase entity) {
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + world.rand.nextDouble() * 12.0D / 16.0D;
        double z = (double) pos.getZ() + 0.5D;
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;

        if (!entity.isPotionActive(MobEffects.SLOWNESS)) {
            entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 120, 3, false, false));
        }

        if (world.getTotalWorldTime() % 8L == 0L) {
            world.playSound(x, (double) pos.getY(), z, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            switch (facing) {
                case DOWN:
                    Atum.proxy.spawnParticle(AtumParticles.Types.TAR, entity, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                    break;
                case UP:
                    Atum.proxy.spawnParticle(AtumParticles.Types.TAR, entity, x - randomPos, y + 0.65D, z, 0.0D, 0.0D, 0.0D);
                    break;
                case WEST:
                    Atum.proxy.spawnParticle(AtumParticles.Types.TAR, entity, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                    break;
                case EAST:
                    Atum.proxy.spawnParticle(AtumParticles.Types.TAR, entity, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                    break;
                case NORTH:
                    Atum.proxy.spawnParticle(AtumParticles.Types.TAR, entity, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    Atum.proxy.spawnParticle(AtumParticles.Types.TAR, entity, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                    break;
            }
        }
    }
}