package com.teammetallurgy.atum.entity.ai.goal;

import com.teammetallurgy.atum.entity.animal.CamelEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class CamelCaravanGoal extends Goal {
    private CamelEntity camel;
    private double speedModifier;
    private int distCheckCounter;

    public CamelCaravanGoal(CamelEntity camel, double speedModifier) {
        this.camel = camel;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.camel.isTamed() && !this.canLeadCaravan(this.camel) && !this.camel.inCaravan()) {
            List<Entity> list = this.camel.level.getEntities(this.camel, this.camel.getBoundingBox().inflate(9.0D, 4.0D, 9.0D));
            CamelEntity camel = null;
            double distance = Double.MAX_VALUE;

            for (Entity entity : list) {
                if (entity instanceof CamelEntity caravanCamel) {
                    if (caravanCamel.isTamed() && caravanCamel.inCaravan() && !caravanCamel.hasCaravanTrail()) {
                        double distanceSq = this.camel.distanceToSqr(caravanCamel);
                        if (distanceSq <= distance) {
                            distance = distanceSq;
                            camel = caravanCamel;
                        }
                    }
                }
            }
            if (camel == null) {
                for (Entity entity : list) {
                    if (entity instanceof CamelEntity caravanLeader) {
                        if (caravanLeader.isTamed() && this.canLeadCaravan(caravanLeader) && !caravanLeader.hasCaravanTrail()) {
                            double distanceSq = this.camel.distanceToSqr(caravanLeader);
                            if (distanceSq <= distance) {
                                distance = distanceSq;
                                camel = caravanLeader;
                            }
                        }
                    }
                }
            }

            if (camel == null) {
                return false;
            } else if (distance < 4.0D) {
                return false;
            } else if (!this.canLeadCaravan(camel) && !this.firstCanBeCaravanLeader(camel, 1)) {
                return false;
            } else {
                this.camel.joinCaravan(camel);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.camel.inCaravan() && this.camel.getCaravanHead() != null && this.camel.getCaravanHead().isAlive() && this.firstCanBeCaravanLeader(this.camel, 0)) {
            double distanceSq = this.camel.distanceToSqr(this.camel.getCaravanHead());

            if (distanceSq > 676.0D) {
                if (this.speedModifier <= 3.0D) {
                    this.speedModifier *= 1.2D;
                    this.distCheckCounter = 40;
                    return true;
                }
                if (this.distCheckCounter == 0) {
                    return false;
                }
            }
            if (this.distCheckCounter > 0) {
                --this.distCheckCounter;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void stop() {
        this.camel.leaveCaravan();
        this.speedModifier = 2.1D;
    }

    @Override
    public void tick() {
        if (this.camel.inCaravan()) {
            CamelEntity caravanLeader = this.camel.getCaravanHead();
            double distance = this.camel.distanceTo(Objects.requireNonNull(caravanLeader));
            Vec3 vec3d = (new Vec3(caravanLeader.getX() - this.camel.getX(), caravanLeader.getY() - this.camel.getY(), caravanLeader.getZ() - this.camel.getZ())).normalize().scale(Math.max(distance - 2.0D, 0.0D));
            this.camel.getNavigation().moveTo(this.camel.getX() + vec3d.x, this.camel.getY() + vec3d.y, this.camel.getZ() + vec3d.z, this.speedModifier);
        }
    }

    private boolean firstCanBeCaravanLeader(CamelEntity camel, int amount) {
        if (amount > 8) {
            return false;
        } else if (camel.inCaravan()) {
            if (camel.getCaravanHead() != null && this.canLeadCaravan(camel.getCaravanHead())) {
                return true;
            } else {
                CamelEntity caravanLeader = camel.getCaravanHead();
                ++amount;
                return this.firstCanBeCaravanLeader(caravanLeader, amount);
            }
        } else {
            return false;
        }
    }

    private boolean canLeadCaravan(CamelEntity camel) {
        return camel.isLeashed() || camel.isSaddled() && camel.isVehicle() && (!camel.hasColor() || camel.getColor() == this.camel.getColor());
    }
}