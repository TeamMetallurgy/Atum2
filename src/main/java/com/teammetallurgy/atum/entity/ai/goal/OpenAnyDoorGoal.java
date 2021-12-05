package com.teammetallurgy.atum.entity.ai.goal;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.InteractDoorGoal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OpenAnyDoorGoal extends InteractDoorGoal {
    private final boolean closeDoor;
    private final boolean noCooldown;
    private int closeDoorTemporisation;

    public OpenAnyDoorGoal(MobEntity entity, boolean closeDoor, boolean noCooldown) {
        super(entity);
        this.closeDoor = closeDoor;
        this.noCooldown = noCooldown;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.collidedHorizontally) {
            return false;
        } else {
            PathNavigator pathNavigator = this.entity.getNavigator();
            Path path = pathNavigator.getPath();
            if (path != null && !path.isFinished()) {
                for (int i = 0; i < Math.min(path.getCurrentPathIndex() + 2, path.getCurrentPathLength()); ++i) {
                    PathPoint pathPoint = path.getPathPointFromIndex(i);
                    this.doorPosition = new BlockPos(pathPoint.x, pathPoint.y + 1, pathPoint.z);
                    if (!(this.entity.getDistanceSq(this.doorPosition.getX(), this.entity.getPosY(), this.doorPosition.getZ()) > 2.25D)) {
                        this.doorInteract = canOpen(this.entity.world, this.doorPosition);
                        if (this.doorInteract) {
                            return true;
                        }
                    }
                }

                this.doorPosition = this.entity.getPosition().up();
                this.doorInteract = canOpen(this.entity.world, this.doorPosition);
                return this.doorInteract;
            } else {
                return false;
            }
        }
    }

    private boolean canOpen(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof DoorBlock && state.getMaterial() != Material.IRON;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (this.noCooldown || this.closeDoorTemporisation > 0) && super.shouldContinueExecuting();
    }

    @Override
    public void startExecuting() {
        if (!this.noCooldown) {
            this.closeDoorTemporisation = 20;
        }
        this.toggleDoor(true);
    }

    @Override
    public void resetTask() {
        if (this.closeDoor) {
            this.toggleDoor(false);
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