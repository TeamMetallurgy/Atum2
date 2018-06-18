package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TileEntityBurningTrap extends TileEntityTrap implements ITickable {

    @Override
    public void update() {
        if (!this.isDisabled) {
            int x = getPos().getX();
            int y = getPos().getY();
            int z = getPos().getZ();

            EntityPlayer player = world.getClosestPlayer((double) x, (double) y, (double) z, 4.0D, false);
            byte range = 1;
            int xMin = x;
            int xMax = x + range;
            int yMin = y;
            int yMax = y + range;
            int zMin = z;
            int zMax = z + range;
            EnumFacing facing = world.getBlockState(pos).getValue(BlockTrap.FACING);
            xMin += facing.getFrontOffsetX() * range;
            xMax += facing.getFrontOffsetX() * range;
            yMin += facing.getFrontOffsetY() * range;
            yMax += facing.getFrontOffsetY() * range;
            zMin += facing.getFrontOffsetZ() * range;
            zMax += facing.getFrontOffsetZ() * range;
            AxisAlignedBB bb = new AxisAlignedBB((double) xMin, (double) yMin, (double) zMin, (double) xMax, (double) yMax, (double) zMax);
            List<EntityLivingBase> mobs = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
            if (player != null && bb.contains(new Vec3d(player.posX, player.posY + 0.5D, player.posZ))) {
                player.setFire(8);
                this.spawnFlames(facing);
            }

            for (EntityLivingBase mob : mobs) {
                if (mob != null) {
                    this.spawnFlames(facing);
                    mob.setFire(8);
                }
            }
        }
    }

    private void spawnFlames(EnumFacing facing) {
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + world.rand.nextDouble() * 6.0D / 16.0D;
        double z = (double) pos.getZ() + 0.5D;
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;

        if (world.rand.nextDouble() < 0.1D) {
            world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
        switch (facing) {
            case DOWN:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                break;
            case UP:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
                break;
            case WEST:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case EAST:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case NORTH:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                break;
            case SOUTH:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                break;
        }
    }
}