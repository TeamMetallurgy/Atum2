package com.teammetallurgy.atum.entity.ai.pathfinding;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class ClimberGroundPathNavigator extends GroundPathNavigation {
    private BlockPos targetPosition;

    public ClimberGroundPathNavigator(Mob mob, Level world) {
        super(mob, world);
    }

    @Override
    public Path createPath(@Nonnull BlockPos pos, int i) {
        this.targetPosition = pos;
        return super.createPath(pos, i);
    }

    @Override
    public Path createPath(@Nonnull Entity entity, int i) {
        this.targetPosition = entity.blockPosition();
        return super.createPath(entity, i);
    }

    @Override
    public boolean moveTo(@Nonnull Entity entity, double speed) {
        Path path = this.createPath(entity, 0);
        if (path != null) {
            return this.moveTo(path, speed);
        } else {
            this.targetPosition = entity.blockPosition();
            this.speedModifier = speed;
            return true;
        }
    }

    @Override
    public void tick() {
        if (!this.isDone()) {
            super.tick();
        } else {
            if (this.targetPosition != null) {
                float width = this.mob.getBbWidth() + 0.5F; //Added additional width, as the logic relies on the spider being more than one block wide
                if (!this.targetPosition.closerThan(this.mob.position(), width) && (!(this.mob.getY() > (double) this.targetPosition.getY()) || !(new BlockPos(this.targetPosition.getX(), this.mob.getY(), this.targetPosition.getZ())).closerThan(this.mob.position(), width))) {
                    this.mob.getMoveControl().setWantedPosition(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speedModifier);
                } else {
                    this.targetPosition = null;
                }
            }
        }
    }
}