package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;

public class TileEntityPoisonTrap extends TileEntityTrap {

    @Override
    protected void fire(EntityLivingBase livingBase) {
        if (!livingBase.isPotionActive(MobEffects.POISON)) {
            livingBase.addPotionEffect(new PotionEffect(MobEffects.POISON, 80, 3, false, false));
            world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
    }

    @Override
    protected void spawnParticles(EnumFacing facing, EntityLivingBase livingBase) {
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + world.rand.nextDouble() * 0.7D;
        double z = (double) pos.getZ() + 0.5D;

        switch (facing) {
            case DOWN:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, livingBase, x - randomPos, (double) pos.getY() - 0.1D, z - 0.2 + (world.rand.nextDouble() * 0.4D), 0.0D, 0.0D, 0.0D);
                break;
            case UP:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, livingBase, x - randomPos, (double) pos.getY() + 1.1D, z - 0.2 + (world.rand.nextDouble() * 0.4D), 0.0D, 0.0D, 0.0D);
                break;
            case WEST:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, livingBase, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case EAST:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, livingBase, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case NORTH:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, livingBase, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                break;
            case SOUTH:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, livingBase, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                break;
        }
    }
}