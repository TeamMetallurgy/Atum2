package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.trap.TrapBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

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
        Level world = this.level;
        if (world == null) return;

        if (this.timer > 0) this.timer--;
        if (!this.isDisabled && this.isBurning()) {
            BlockState state = world.getBlockState(this.worldPosition);
            if (state.getBlock() instanceof TrapBlock) {
                Direction facing = state.getValue(TrapBlock.FACING);
                Class<? extends LivingEntity> entity;
                if (this.isInsidePyramid) {
                    entity = Player.class;
                } else {
                    entity = LivingEntity.class;
                }
                AABB box = getFacingBoxWithRange(facing, 13).deflate(1);
                List<LivingEntity> entities = world.getEntitiesOfClass(entity, box);
                for (LivingEntity livingBase : entities) {
                    BlockHitResult findBlock = this.rayTraceMinMax(world, box, livingBase);
                    boolean cantSeeEntity = this.getDistance(findBlock.getBlockPos()) < this.getDistance(livingBase.blockPosition());
                    if (livingBase instanceof Player ? !((Player) livingBase).isCreative() && !cantSeeEntity : !cantSeeEntity) {
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
        }

        //Copied from TileEntityTrap
        if (this.isInsidePyramid) {
            this.burnTime = 1;
        }

        if (this.isBurning() && !this.isDisabled && canDamageEntity && !this.isInsidePyramid) {
            --this.burnTime;
        }

        if (!world.isClientSide && !this.isDisabled) {
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
            this.setChanged();
        }
    }

    private boolean canSee(Direction facing, Level world, LivingEntity living) {
        Vec3i dir = facing.getNormal();
        Vec3 posDir = new Vec3(this.worldPosition.getX() + dir.getX(), this.worldPosition.getY(), this.worldPosition.getZ() + dir.getZ());
        Vec3 livingPos = new Vec3(living.getX(), living.getY() + (double) living.getEyeHeight(), living.getZ());
        return world.clip(new ClipContext(posDir, livingPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, living)).getType() == HitResult.Type.MISS;
    }

    private BlockHitResult rayTraceMinMax(Level world, AABB box, LivingEntity living) {
        final Vec3 min = new Vec3(box.minX, box.minY, box.minZ);
        final Vec3 max = new Vec3(box.maxX, box.maxY + 0.05D, box.maxZ);
        return world.clip(new ClipContext(max, min, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, living));
    }

    private double getDistance(BlockPos position) {
        double d0 = position.getX() - this.worldPosition.getX();
        double d1 = position.getY() - this.worldPosition.getY();
        double d2 = position.getZ() - this.worldPosition.getZ();
        return Mth.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    @Override
    protected void triggerTrap(Level world, Direction facing, LivingEntity livingBase) {
        double x = (double) this.worldPosition.getX() + 0.5D;
        double y = (double) this.worldPosition.getY() + world.random.nextDouble() * 12.0D / 16.0D;
        double z = (double) this.worldPosition.getZ() + 0.5D;
        double randomPos = world.random.nextDouble() * 0.6D - 0.3D;

        world.playLocalSound(x, worldPosition.getY(), z, SoundEvents.DISPENSER_LAUNCH, SoundSource.BLOCKS, 1.0F, 1.2F, false);

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

    private void fireArrow(Level world, Direction facing, double x, double y, double z) {
        if (!world.isClientSide) {
            Arrow arrow = new Arrow(world, x, y, z);
            arrow.shoot(facing.getStepX(), (float) facing.getStepY() + 0.1F, facing.getStepZ(), 1.1F, 6.0F);
            world.addFreshEntity(arrow);
        }
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundTag compound) {
        super.load(state, compound);
        this.timer = compound.getInt("Timer");
    }

    @Override
    @Nonnull
    public CompoundTag save(@Nonnull CompoundTag compound) {
        super.save(compound);
        compound.putInt("Timer", this.timer);
        return compound;
    }
}