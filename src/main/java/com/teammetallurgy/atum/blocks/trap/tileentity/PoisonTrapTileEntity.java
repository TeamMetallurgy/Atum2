package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class PoisonTrapTileEntity extends TrapTileEntity {

    public PoisonTrapTileEntity() {
        super(AtumTileEntities.POISON_TRAP);
    }

    @Override
    protected void triggerTrap(World world, Direction facing, LivingEntity livingBase) {
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + world.rand.nextDouble() * 0.7D;
        double z = (double) pos.getZ() + 0.5D;

        if (!livingBase.isPotionActive(Effects.POISON)) {
            if (!world.isRemote) {
                livingBase.addPotionEffect(new EffectInstance(Effects.POISON, 80, 3, false, false));
            }
            world.playSound((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            switch (facing) {
                case DOWN:
                    serverWorld.spawnParticle(AtumParticles.GAS, x - randomPos, (double) pos.getY() - 0.1D, z - 0.2 + (world.rand.nextDouble() * 0.4D), 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
                case UP:
                    serverWorld.spawnParticle(AtumParticles.GAS, x - randomPos, (double) pos.getY() + 1.1D, z - 0.2 + (world.rand.nextDouble() * 0.4D), 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
                case WEST:
                    serverWorld.spawnParticle(AtumParticles.GAS, x - 0.52D, y, z + randomPos, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
                case EAST:
                    serverWorld.spawnParticle(AtumParticles.GAS, x + 0.52D, y, z + randomPos, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
                case NORTH:
                    serverWorld.spawnParticle(AtumParticles.GAS, x + randomPos, y, z - 0.52D, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
                case SOUTH:
                    serverWorld.spawnParticle(AtumParticles.GAS, x + randomPos, y, z + 0.52D, 10, 0.0D, 0.0025D, 0.0D, 0.005D);
                    break;
            }
        }
    }
}