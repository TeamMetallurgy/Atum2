package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TarTrapTileEntity extends TrapTileEntity {

    public TarTrapTileEntity() {
        super(AtumTileEntities.TAR_TRAP);
    }

    @Override
    protected void triggerTrap(World world, Direction facing, LivingEntity entity) {
        double x = (double) this.pos.getX() + 0.5D;
        double y = (double) this.pos.getY() + world.rand.nextDouble() * 12.0D / 16.0D;
        double z = (double) this.pos.getZ() + 0.5D;
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;

        if (!entity.isPotionActive(Effects.SLOWNESS)) {
            if (!world.isRemote) {
                entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 120, 3, false, false));
            }
        }

        if (world.getGameTime() % 8L == 0L) {
            entity.playSound(SoundEvents.BLOCK_LAVA_POP, 1.0F, 1.0F);
            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                switch (facing) {
                    case DOWN:
                        serverWorld.spawnParticle(AtumParticles.TAR, x - randomPos, y - 0.2D, z, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                    case UP:
                        serverWorld.spawnParticle(AtumParticles.TAR, x - randomPos, y + 0.65D, z, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                    case WEST:
                        serverWorld.spawnParticle(AtumParticles.TAR, x - 0.52D, y, z + randomPos, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                    case EAST:
                        serverWorld.spawnParticle(AtumParticles.TAR, x + 0.52D, y, z + randomPos, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                    case NORTH:
                        serverWorld.spawnParticle(AtumParticles.TAR, x + randomPos, y, z - 0.52D, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                    case SOUTH:
                        serverWorld.spawnParticle(AtumParticles.TAR, x + randomPos, y, z + 0.52D, 2, 0.01D, 0.0D, 0.01D, 0.01D);
                        break;
                }
            }
        }
    }
}