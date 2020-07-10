package com.teammetallurgy.atum.entity.ai.goal;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.OpenDoorGoal;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OpenAnyDoorGoal extends OpenDoorGoal {

    public OpenAnyDoorGoal(MobEntity entity, boolean closeDoor) {
        super(entity, closeDoor);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.collidedHorizontally) {
            return false;
        } else {
            GroundPathNavigator navigator = (GroundPathNavigator) this.entity.getNavigator();
            Path path = navigator.getPath();
            if (path != null && !path.isFinished() && navigator.getEnterDoors()) {
                for (int i = 0; i < Math.min(path.getCurrentPathIndex() + 2, path.getCurrentPathLength()); ++i) {
                    PathPoint pathPoint = path.getPathPointFromIndex(i);
                    this.doorPosition = new BlockPos(pathPoint.x, pathPoint.y + 1, pathPoint.z);
                    if (this.entity.getDistanceSq(this.doorPosition.getX(), this.entity.getPosY(), this.doorPosition.getZ()) <= 2.25D) {
                        this.doorInteract = canOpen(this.entity.world, this.doorPosition);
                        if (this.doorInteract) {
                            return true;
                        }
                    }
                }
                this.doorPosition = (new BlockPos(this.entity)).up();
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
}