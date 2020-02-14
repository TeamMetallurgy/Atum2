package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class TarTrapTileEntity extends TrapTileEntity {

    public TarTrapTileEntity() {
        super(AtumBlocks.AtumTileEntities.TAR_TRAP);
    }

    @Override
    protected void triggerTrap(World world, Direction facing, LivingEntity entity) {
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + world.rand.nextDouble() * 12.0D / 16.0D;
        double z = (double) pos.getZ() + 0.5D;
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;

        if (!entity.isPotionActive(Effects.SLOWNESS)) {
            if (!world.isRemote) {
                entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 120, 3, false, false));
            }
        }

        if (world.getGameTime() % 8L == 0L) {
            entity.playSound(SoundEvents.BLOCK_LAVA_POP, 1.0F, 1.0F);
            switch (facing) {
                case DOWN:
                    world.addParticle(AtumParticles.TAR, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                    break;
                case UP:
                    world.addParticle(AtumParticles.TAR, x - randomPos, y + 0.65D, z, 0.0D, 0.0D, 0.0D);
                    break;
                case WEST:
                    world.addParticle(AtumParticles.TAR, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                    break;
                case EAST:
                    world.addParticle(AtumParticles.TAR, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                    break;
                case NORTH:
                    world.addParticle(AtumParticles.TAR, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    world.addParticle(AtumParticles.TAR, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                    break;
            }
        }
    }
}