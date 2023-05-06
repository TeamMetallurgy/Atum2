package com.teammetallurgy.atum.entity.ai.goal;

import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

import java.util.EnumSet;

public class BegGoal extends Goal {
    private final DesertWolfEntity desertWolf;
    private Player player;
    private final Level level;
    private final float minPlayerDistance;
    private int timeoutCounter;
    private final TargetingConditions predicate;

    public BegGoal(DesertWolfEntity desertWolf, float minDistance) {
        this.desertWolf = desertWolf;
        this.level = desertWolf.level;
        this.minPlayerDistance = minDistance;
        this.predicate = TargetingConditions.forNonCombat().range(minDistance);
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.player = this.level.getNearestPlayer(predicate, this.desertWolf);
        return this.player != null && this.hasTemptationItemInHand(this.player);
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.player.isAlive()) {
            return false;
        } else if (this.desertWolf.distanceToSqr(this.player) > (double) (this.minPlayerDistance * this.minPlayerDistance)) {
            return false;
        } else {
            return this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
        }
    }

    @Override
    public void start() {
        this.desertWolf.setIsInterested(true);
        this.timeoutCounter = 40 + this.desertWolf.getRandom().nextInt(40);
    }

    @Override
    public void stop() {
        this.desertWolf.setIsInterested(false);
        this.player = null;
    }

    @Override
    public void tick() {
        this.desertWolf.getLookControl().setLookAt(this.player.getX(), this.player.getY() + (double) this.player.getEyeHeight(), this.player.getZ(), 10.0F, (float) this.desertWolf.getMaxHeadXRot());
        --this.timeoutCounter;
    }


    private boolean hasTemptationItemInHand(Player player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack heldStack = player.getItemInHand(hand);

            if (this.desertWolf.isTame() && heldStack.is(Tags.Items.BONES)) {
                return true;
            }
            if (this.desertWolf.isFood(heldStack)) {
                return true;
            }
        }
        return false;
    }
}