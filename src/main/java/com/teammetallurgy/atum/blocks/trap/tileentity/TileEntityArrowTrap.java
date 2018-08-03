package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class TileEntityArrowTrap extends TileEntityTrap implements ITickable {
    private int timer = 80;

    @Override
    public void update() {
        /*int range = 12;
        Vec3d vec3d1 = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytrace = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
        if (!this.isDisabled && raytrace != null && raytrace.typeOfHit.equals(RayTraceResult.Type.ENTITY) && raytrace.entityHit instanceof EntityPlayer && !((EntityPlayer) raytrace.entityHit).capabilities.isCreativeMode) {
            if (timer > 0) timer--;
            if (timer == 0) {
                EnumFacing facing = world.getBlockState(pos).getValue(BlockTrap.FACING);
                List<EntityLivingBase> players = world.getEntitiesWithinAABB(EntityLivingBase.class, getFacingBoxWithRange(facing, range));
                for (EntityLivingBase p : players) {
                    if (p != null) {
                        timer = 80;
                        this.fire(facing);
                    }
                }
            }
        }*/
    }

    private void fire(EnumFacing facing) {
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + world.rand.nextDouble() * 12.0D / 16.0D;
        double z = (double) pos.getZ() + 0.5D;
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;

        world.playSound(x, (double) pos.getY(), z, SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 1.0F, 1.2F, false);

        switch (facing) {
            case DOWN:
                fireArrow(world, facing, x - randomPos, y - 0.5D, z);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                break;
            case UP:
                fireArrow(world, facing, x - randomPos, y + 1.0D, z);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
                break;
            case WEST:
                fireArrow(world, facing, x - 0.52D, y, z + randomPos);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case EAST:
                fireArrow(world, facing, x + 0.52D, y, z + randomPos);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case NORTH:
                fireArrow(world, facing, x + randomPos, y, z - 0.52D);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                break;
            case SOUTH:
                fireArrow(world, facing, x + randomPos, y, z + 0.52D);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                break;
        }
    }

    private void fireArrow(World world, EnumFacing facing, double x, double y, double z) {
        if (!world.isRemote) {
            EntityArrow arrow = new EntityTippedArrow(world, x, y, z);
            arrow.shoot((double) facing.getFrontOffsetX(), (double) ((float) facing.getFrontOffsetY() + 0.1F), (double) facing.getFrontOffsetZ(), 1.1F, 6.0F);
            world.spawnEntity(arrow);
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