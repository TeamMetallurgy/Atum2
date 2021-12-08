package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.trap.TrapBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class ArrowTrapTileEntity extends TrapTileEntity {
    private int timer = 80;

    public ArrowTrapTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.ARROW_TRAP.get(), pos, state);
    }

    public static void arrowTrackServerTick(Level level, BlockPos pos, BlockState state, ArrowTrapTileEntity trap) {
        boolean isBurningCheck = trap.isBurning();
        boolean isBurning = false;
        boolean canDamageEntity = false;

        if (trap.timer > 0) trap.timer--;
        if (!trap.isDisabled && trap.isBurning()) {
            if (state.getBlock() instanceof TrapBlock) {
                Direction facing = state.getValue(TrapBlock.FACING);
                Class<? extends LivingEntity> entity;
                if (trap.isInsidePyramid) {
                    entity = Player.class;
                } else {
                    entity = LivingEntity.class;
                }
                AABB box = getFacingBoxWithRange(pos, facing, 13).deflate(1);
                List<? extends LivingEntity> entities = level.getEntitiesOfClass(entity, box);
                for (LivingEntity livingBase : entities) {
                    BlockHitResult findBlock = rayTraceMinMax(level, box, livingBase);
                    boolean cantSeeEntity = trap.getDistance(findBlock.getBlockPos()) < trap.getDistance(livingBase.blockPosition());
                    if (livingBase instanceof Player ? !((Player) livingBase).isCreative() && !cantSeeEntity : !cantSeeEntity) {
                        if (trap.canSee(facing, level, livingBase)) {
                            canDamageEntity = true;
                            if (trap.timer == 0) {
                                trap.timer = 80;
                                trap.triggerTrap(level, facing, livingBase);
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
        if (trap.isInsidePyramid) {
            trap.burnTime = 1;
        }

        if (trap.isBurning() && !trap.isDisabled && canDamageEntity && !trap.isInsidePyramid) {
            --trap.burnTime;
        }

        if (!level.isClientSide && !trap.isDisabled) {
            ItemStack fuel = trap.inventory.get(0);
            if (trap.isBurning() || !fuel.isEmpty()) {
                if (!trap.isBurning()) {
                    trap.burnTime = ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING) / 10;
                    trap.currentItemBurnTime = trap.burnTime;
                    if (trap.isBurning()) {
                        isBurning = true;
                        if (!fuel.isEmpty()) {
                            fuel.shrink(1);
                        }
                    }
                }
            }
            if (isBurningCheck != trap.isBurning()) {
                isBurning = true;
            }
        }
        if (isBurning) {
            trap.setChanged();
        }
    }

    public boolean canSee(Direction facing, Level level, LivingEntity living) {
        Vec3i dir = facing.getNormal();
        Vec3 posDir = new Vec3(this.worldPosition.getX() + dir.getX(), this.worldPosition.getY(), this.worldPosition.getZ() + dir.getZ());
        Vec3 livingPos = new Vec3(living.getX(), living.getY() + (double) living.getEyeHeight(), living.getZ());
        return level.clip(new ClipContext(posDir, livingPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, living)).getType() == HitResult.Type.MISS;
    }

    private static BlockHitResult rayTraceMinMax(Level level, AABB box, LivingEntity living) {
        final Vec3 min = new Vec3(box.minX, box.minY, box.minZ);
        final Vec3 max = new Vec3(box.maxX, box.maxY + 0.05D, box.maxZ);
        return level.clip(new ClipContext(max, min, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, living));
    }

    private double getDistance(BlockPos position) {
        double d0 = position.getX() - this.worldPosition.getX();
        double d1 = position.getY() - this.worldPosition.getY();
        double d2 = position.getZ() - this.worldPosition.getZ();
        return Mth.sqrt((float) (d0 * d0 + d1 * d1 + d2 * d2));
    }

    @Override
    protected void triggerTrap(Level level, Direction facing, LivingEntity livingBase) {
        double x = (double) this.worldPosition.getX() + 0.5D;
        double y = (double) this.worldPosition.getY() + level.random.nextDouble() * 12.0D / 16.0D;
        double z = (double) this.worldPosition.getZ() + 0.5D;
        double randomPos = level.random.nextDouble() * 0.6D - 0.3D;

        level.playLocalSound(x, worldPosition.getY(), z, SoundEvents.DISPENSER_LAUNCH, SoundSource.BLOCKS, 1.0F, 1.2F, false);

        switch (facing) {
            case DOWN -> {
                fireArrow(level, facing, x - randomPos, y - 0.5D, z);
                level.addParticle(ParticleTypes.SMOKE, x - randomPos, y - 0.2D, z, 0.0D, 0.0D, 0.0D);
            }
            case UP -> {
                fireArrow(level, facing, x - randomPos, y + 1.0D, z);
                level.addParticle(ParticleTypes.SMOKE, x - randomPos, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
            }
            case WEST -> {
                fireArrow(level, facing, x - 0.52D, y, z + randomPos);
                level.addParticle(ParticleTypes.SMOKE, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
            }
            case EAST -> {
                fireArrow(level, facing, x + 0.52D, y, z + randomPos);
                level.addParticle(ParticleTypes.SMOKE, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
            }
            case NORTH -> {
                fireArrow(level, facing, x + randomPos, y, z - 0.52D);
                level.addParticle(ParticleTypes.SMOKE, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
            }
            case SOUTH -> {
                fireArrow(level, facing, x + randomPos, y, z + 0.52D);
                level.addParticle(ParticleTypes.SMOKE, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private void fireArrow(Level level, Direction facing, double x, double y, double z) {
        if (!level.isClientSide) {
            Arrow arrow = new Arrow(level, x, y, z);
            arrow.shoot(facing.getStepX(), (float) facing.getStepY() + 0.1F, facing.getStepZ(), 1.1F, 6.0F);
            level.addFreshEntity(arrow);
        }
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load( tag);
        this.timer = tag.getInt("Timer");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Timer", this.timer);
    }
}