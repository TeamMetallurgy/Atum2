package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import java.util.List;

public class TileEntityBurningTrap extends TileEntityTrap implements ITickable {
    private boolean isDisabled = false;

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
            IBlockState state = world.getBlockState(pos);
            EnumFacing facing = state.getValue(BlockTrap.FACING);
            xMin += facing.getFrontOffsetX() * range;
            xMax += facing.getFrontOffsetX() * range;
            yMin += facing.getFrontOffsetY() * range;
            yMax += facing.getFrontOffsetY() * range;
            zMin += facing.getFrontOffsetZ() * range;
            zMax += facing.getFrontOffsetZ() * range;
            AxisAlignedBB bb = new AxisAlignedBB((double) xMin, (double) yMin, (double) zMin, (double) xMax, (double) yMax, (double) zMax);
            List<EntityMob> mobs = super.world.getEntitiesWithinAABB(EntityMob.class, bb);
            if (player != null && bb.contains(new Vec3d(player.posX, player.posY + 0.5D, player.posZ))) {
                player.setFire(8);
                this.spawnFlames(facing);
            }

            for (EntityMob mob : mobs) {
                if (mob != null) {
                    mob.setFire(8);
                }
            }
        }
    }

    private void spawnFlames(EnumFacing facing) {
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + world.rand.nextDouble() * 6.0D / 16.0D;
        double d2 = (double) pos.getZ() + 0.5D;
        double d4 = world.rand.nextDouble() * 0.6D - 0.3D;

        if (world.rand.nextDouble() < 0.1D) {
            world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
        switch (facing) {
            case WEST:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                break;
            case EAST:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                break;
            case NORTH:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                break;
            case SOUTH:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
        }
    }

    public void setDisabled() {
        this.isDisabled = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.isDisabled = compound.getBoolean("Disabled");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("Disabled", this.isDisabled);
        return compound;
    }
}