package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class TileEntityArrowTrap extends TileEntityTrap implements ITickable {
    private int timer = 80;

    @Override
    public void update() {
        if (timer > 0) timer --;
        int range = 6;
        EntityPlayer player = world.getClosestPlayer((double) getPos().getX(), (double) getPos().getY(), (double) getPos().getZ(), range, false);
        if (!this.isDisabled && player != null && timer == 0 /*&& !player.capabilities.isCreativeMode*/) {
            EnumFacing facing = world.getBlockState(pos).getValue(BlockTrap.FACING);
            List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, getFacingBoxWithRange(facing, range));
            for (EntityPlayer p : players) {
                if (p != null) {
                    timer = 80;
                    this.fireArrow(facing); //TODO add 4 second timer
                }
            }
        }
    }

    private EntityArrow getArrow(World world, IPosition pos) {
        EntityTippedArrow arrow = new EntityTippedArrow(world, pos.getX(), pos.getY(), pos.getZ());
        arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        return arrow;
    }

    private IPosition getFirePosition(EnumFacing facing, BlockPos pos) {
        double x = pos.getX() + 0.7D * (double) facing.getFrontOffsetX();
        double y = pos.getY() + 0.7D * (double) facing.getFrontOffsetY();
        double z = pos.getZ() + 0.7D * (double) facing.getFrontOffsetZ();
        return new PositionImpl(x, y, z);
    }

    private void fireArrow(EnumFacing facing) {
        IPosition iPos = getFirePosition(facing, getPos());
        IProjectile arrow = getArrow(world, iPos);
        arrow.shoot((double) facing.getFrontOffsetX(), (double) ((float) facing.getFrontOffsetY() + 0.1F), (double) facing.getFrontOffsetZ(), 1.1F, 6.0F);
        world.spawnEntity((Entity) arrow);

        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + world.rand.nextDouble() * 12.0D / 16.0D;
        double z = (double) pos.getZ() + 0.5D;
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;

        world.playSound(x, (double) pos.getY(), z, SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 1.0F, 1.2F, false);

        switch (facing) {
            case DOWN:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                break;
            case UP:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
                break;
            case WEST:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case EAST:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case NORTH:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                break;
            case SOUTH:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                break;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.timer = compound.getInteger("Timer");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Timer", this.timer);
        return compound;
    }
}