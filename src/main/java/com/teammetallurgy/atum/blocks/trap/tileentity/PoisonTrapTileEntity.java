package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class PoisonTrapTileEntity extends TrapTileEntity {

    @Override
    protected void triggerTrap(Direction facing, LivingEntity livingBase) {
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + world.rand.nextDouble() * 0.7D;
        double z = (double) pos.getZ() + 0.5D;

        if (!livingBase.isPotionActive(Effects.POISON)) {
            if (!world.isRemote) {
                livingBase.addPotionEffect(new EffectInstance(Effects.POISON, 80, 3, false, false));
            }
            world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
        switch (facing) {
            case DOWN:
                world.addParticle(AtumParticles.GAS, x - randomPos, (double) pos.getY() - 0.1D, z - 0.2 + (world.rand.nextDouble() * 0.4D), 0.0D, 0.0D, 0.0D);
                break;
            case UP:
                world.addParticle(AtumParticles.GAS, x - randomPos, (double) pos.getY() + 1.1D, z - 0.2 + (world.rand.nextDouble() * 0.4D), 0.0D, 0.0D, 0.0D);
                break;
            case WEST:
                world.addParticle(AtumParticles.GAS, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case EAST:
                world.addParticle(AtumParticles.GAS, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case NORTH:
                world.addParticle(AtumParticles.GAS, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                break;
            case SOUTH:
                world.addParticle(AtumParticles.GAS, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                break;
        }
    }
}