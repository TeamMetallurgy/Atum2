package com.teammetallurgy.atum.entity.ai.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ClimberGroundPathNavigator extends GroundPathNavigator {
    private BlockPos targetPosition;

    public ClimberGroundPathNavigator(MobEntity mob, World world) {
        super(mob, world);
    }

    @Override
    public Path getPathToPos(@Nonnull BlockPos pos, int i) {
        this.targetPosition = pos;
        return super.getPathToPos(pos, i);
    }

    @Override
    public Path getPathToEntity(@Nonnull Entity entity, int i) {
        this.targetPosition = entity.getPosition();
        return super.getPathToEntity(entity, i);
    }

    @Override
    public boolean tryMoveToEntityLiving(@Nonnull Entity entity, double speed) {
        Path path = this.getPathToEntity(entity, 0);
        if (path != null) {
            return this.setPath(path, speed);
        } else {
            this.targetPosition = entity.getPosition();
            this.speed = speed;
            return true;
        }
    }

    @Override
    public void tick() {
        if (!this.noPath()) {
            super.tick();
        } else {
            if (this.targetPosition != null) {
                float width = this.entity.getWidth() + 0.5F; //Added additional width, as the logic relies on the spider being more than one block wide
                if (!this.targetPosition.withinDistance(this.entity.getPositionVec(), width) && (!(this.entity.getPosY() > (double) this.targetPosition.getY()) || !(new BlockPos(this.targetPosition.getX(), this.entity.getPosY(), this.targetPosition.getZ())).withinDistance(this.entity.getPositionVec(), width))) {
                    this.entity.getMoveHelper().setMoveTo(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speed);
                } else {
                    this.targetPosition = null;
                }
            }
        }
    }
}