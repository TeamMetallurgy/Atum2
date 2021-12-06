package com.teammetallurgy.atum.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

public class OpenAnyDoorGoal extends DoorInteractGoal {
    private final boolean closeDoor;
    private final boolean noCooldown;
    private int closeDoorTemporisation;

    public OpenAnyDoorGoal(Mob entity, boolean closeDoor, boolean noCooldown) {
        super(entity);
        this.closeDoor = closeDoor;
        this.noCooldown = noCooldown;
    }

    @Override
    public boolean canUse() {
        if (!this.mob.horizontalCollision) {
            return false;
        } else {
            PathNavigation pathNavigator = this.mob.getNavigation();
            Path path = pathNavigator.getPath();
            if (path != null && !path.isDone()) {
                for (int i = 0; i < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); ++i) {
                    Node pathPoint = path.getNode(i);
                    this.doorPos = new BlockPos(pathPoint.x, pathPoint.y + 1, pathPoint.z);
                    if (!(this.mob.distanceToSqr(this.doorPos.getX(), this.mob.getY(), this.doorPos.getZ()) > 2.25D)) {
                        this.hasDoor = canOpen(this.mob.level, this.doorPos);
                        if (this.hasDoor) {
                            return true;
                        }
                    }
                }

                this.doorPos = this.mob.blockPosition().above();
                this.hasDoor = canOpen(this.mob.level, this.doorPos);
                return this.hasDoor;
            } else {
                return false;
            }
        }
    }

    private boolean canOpen(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof DoorBlock && state.getMaterial() != Material.METAL;
    }

    @Override
    public boolean canContinueToUse() {
        return (this.noCooldown || this.closeDoorTemporisation > 0) && super.canContinueToUse();
    }

    @Override
    public void start() {
        if (!this.noCooldown) {
            this.closeDoorTemporisation = 20;
        }
        this.setOpen(true);
    }

    @Override
    public void stop() {
        if (this.closeDoor) {
            this.setOpen(false);
        }
    }

    @Override
    public void tick() {
        if (!this.noCooldown) {
            --this.closeDoorTemporisation;
        }
        super.tick();
    }
}