package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.trap.TrapBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.List;

public class ArrowTrapTileEntity extends TrapTileEntity {
    private int timer = 80;

    public ArrowTrapTileEntity() {
        super(AtumTileEntities.ARROW_TRAP);
    }

    @Override
    public void tick() {
        boolean isBurningCheck = this.isBurning();
        boolean isBurning = false;
        boolean canDamageEntity = false;
        World world = this.world;
        if (world == null) return;

        if (this.timer > 0) this.timer--;
        if (!this.isDisabled && this.isBurning()) {
            Direction facing = world.getBlockState(this.pos).get(TrapBlock.FACING);
            Class<? extends LivingEntity> entity;
            if (this.isInsidePyramid) {
                entity = PlayerEntity.class;
            } else {
                entity = LivingEntity.class;
            }
            AxisAlignedBB box = getFacingBoxWithRange(facing, 13).shrink(1);
            List<LivingEntity> entities = world.getEntitiesWithinAABB(entity, box);
            for (LivingEntity livingBase : entities) {
                BlockRayTraceResult findBlock = this.rayTraceMinMax(world, box, livingBase);
                boolean cantSeeEntity = this.getDistance(findBlock.getPos()) < this.getDistance(livingBase.getPosition());
                if (livingBase instanceof PlayerEntity ? !((PlayerEntity) livingBase).isCreative() && !cantSeeEntity : !cantSeeEntity) {
                    if (canSee(facing, world, livingBase)) {
                        canDamageEntity = true;
                        if (this.timer == 0) {
                            this.timer = 80;
                            this.triggerTrap(world, facing, livingBase);
                        }
                    } else {
                        canDamageEntity = false;
                    }
                } else {
                    canDamageEntity = false;
                }
            }
        }

        //Copied from TileEntityTrap
        if (this.isInsidePyramid) {
            this.burnTime = 1;
        }

        if (this.isBurning() && !this.isDisabled && canDamageEntity && !this.isInsidePyramid) {
            --this.burnTime;
        }

        if (!world.isRemote && !this.isDisabled) {
            ItemStack fuel = this.inventory.get(0);
            if (this.isBurning() || !fuel.isEmpty()) {
                if (!this.isBurning()) {
                    this.burnTime = ForgeHooks.getBurnTime(fuel) / 10;
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

    private boolean canSee(Direction facing, World world, LivingEntity living) {
        Vec3i dir = facing.getDirectionVec();
        Vec3d posDir = new Vec3d(this.pos.getX() + dir.getX(), this.pos.getY(), this.pos.getZ() + dir.getZ());
        Vec3d livingPos = new Vec3d(living.getPosX(), living.getPosY() + (double) living.getEyeHeight(), living.getPosZ());
        return world.rayTraceBlocks(new RayTraceContext(posDir, livingPos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, living)).getType() == RayTraceResult.Type.MISS;
    }

    private BlockRayTraceResult rayTraceMinMax(World world, AxisAlignedBB box, LivingEntity living) {
        final Vec3d min = new Vec3d(box.minX, box.minY, box.minZ);
        final Vec3d max = new Vec3d(box.maxX, box.maxY + 0.05D, box.maxZ);
        return world.rayTraceBlocks(new RayTraceContext(max, min, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, living));
    }

    private double getDistance(BlockPos position) {
        double d0 = position.getX() - this.pos.getX();
        double d1 = position.getY() - this.pos.getY();
        double d2 = position.getZ() - this.pos.getZ();
        return MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    @Override
    protected void triggerTrap(World world, Direction facing, LivingEntity livingBase) {
        double x = (double) this.pos.getX() + 0.5D;
        double y = (double) this.pos.getY() + world.rand.nextDouble() * 12.0D / 16.0D;
        double z = (double) this.pos.getZ() + 0.5D;
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;

        world.playSound(x, pos.getY(), z, SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 1.0F, 1.2F, false);

        switch (facing) {
            case DOWN:
                fireArrow(world, facing, x - randomPos, y - 0.5D, z);
                world.addParticle(ParticleTypes.SMOKE, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
                break;
            case UP:
                fireArrow(world, facing, x - randomPos, y + 1.0D, z);
                world.addParticle(ParticleTypes.SMOKE, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
                break;
            case WEST:
                fireArrow(world, facing, x - 0.52D, y, z + randomPos);
                world.addParticle(ParticleTypes.SMOKE, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case EAST:
                fireArrow(world, facing, x + 0.52D, y, z + randomPos);
                world.addParticle(ParticleTypes.SMOKE, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case NORTH:
                fireArrow(world, facing, x + randomPos, y, z - 0.52D);
                world.addParticle(ParticleTypes.SMOKE, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                break;
            case SOUTH:
                fireArrow(world, facing, x + randomPos, y, z + 0.52D);
                world.addParticle(ParticleTypes.SMOKE, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                break;
        }
    }

    private void fireArrow(World world, Direction facing, double x, double y, double z) {
        if (!world.isRemote) {
            ArrowEntity arrow = new ArrowEntity(world, x, y, z);
            arrow.shoot(facing.getXOffset(), (float) facing.getYOffset() + 0.1F, facing.getZOffset(), 1.1F, 6.0F);
            world.addEntity(arrow);
        }
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        this.timer = compound.getInt("Timer");
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putInt("Timer", this.timer);
        return compound;
    }
}