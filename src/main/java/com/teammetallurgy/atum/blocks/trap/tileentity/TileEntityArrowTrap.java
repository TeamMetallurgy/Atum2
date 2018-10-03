package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import java.util.List;

public class TileEntityArrowTrap extends TileEntityTrap {
    private int timer = 80;

    @Override
    public void update() {
        boolean isBurningCheck = this.isBurning();
        boolean isBurning = false;
        boolean canDamageEntity = false;

        if (timer > 0) timer--;
        if (!this.isDisabled && this.isBurning()) {
            EnumFacing facing = world.getBlockState(pos).getValue(BlockTrap.FACING);
            Class<? extends EntityLivingBase> entity;
            if (!world.isRemote && BlockTrap.isInsidePyramid((WorldServer) world, pos)) {
                entity = EntityPlayer.class;
            } else {
                entity = EntityLivingBase.class;
            }
            AxisAlignedBB box = getFacingBoxWithRange(facing, 13).shrink(1);
            RayTraceResult findBlock = this.rayTraceMinMax(world, box);
            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(entity, box);
            for (EntityLivingBase livingBase : entities) {
                boolean cantSeeEntity = findBlock != null && this.getDistance(findBlock.getBlockPos()) < this.getDistance(livingBase.getPosition());
                System.out.println(cantSeeEntity);
                if (livingBase instanceof EntityPlayer ? !((EntityPlayer) livingBase).capabilities.isCreativeMode && !cantSeeEntity : livingBase != null && !cantSeeEntity) {
                    canDamageEntity = true;
                    if (timer == 0) {
                        timer = 80;
                        this.spawnParticles(facing, livingBase);
                        this.fire(livingBase);
                    }
                } else {
                    canDamageEntity = false;
                }
            }
        }

        //Copied from TileEntityTrap
        if (!world.isRemote && BlockTrap.isInsidePyramid((WorldServer) world, pos)) {
            this.burnTime = 1;
        }

        if (this.isBurning() && !this.isDisabled && canDamageEntity && (!world.isRemote && !BlockTrap.isInsidePyramid((WorldServer) world, pos))) {
            --this.burnTime;
        }

        if (!this.world.isRemote && !this.isDisabled) {
            ItemStack fuel = this.inventory.get(0);
            if (this.isBurning() || !fuel.isEmpty()) {
                if (!this.isBurning()) {
                    this.burnTime = TileEntityFurnace.getItemBurnTime(fuel) / 10;
                    this.currentItemBurnTime = this.burnTime;
                    if (this.isBurning()) {
                        isBurning = true;
                        if (!fuel.isEmpty()) {
                            fuel.shrink(1);
                        }
                    }
                }
            }
            if (isBurningCheck != this.isBurning()) {
                isBurning = true;
            }
        }
        if (isBurning) {
            this.markDirty();
        }
    }

    private RayTraceResult rayTraceMinMax(World world, AxisAlignedBB box) {
        final Vec3d min = new Vec3d(box.minX, box.minY, box.minZ);
        final Vec3d max = new Vec3d(box.maxX, box.maxY + 0.05D, box.maxZ);
        return world.rayTraceBlocks(max, min, true, true, false);
    }

    private double getDistance(BlockPos position) {
        double d0 = position.getX() - this.pos.getX();
        double d1 = position.getY() - this.pos.getY();
        double d2 = position.getZ() - this.pos.getZ();
        return (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }
    
    @Override
    protected void spawnParticles(EnumFacing facing, EntityLivingBase livingBase) {
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
            arrow.shoot((double) facing.getXOffset(), (double) ((float) facing.getYOffset() + 0.1F), (double) facing.getZOffset(), 1.1F, 6.0F);
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