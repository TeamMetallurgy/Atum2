package com.teammetallurgy.atum.entity.ai;

import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import java.util.EnumSet;

public class GoalBeg extends Goal {
    private final DesertWolfEntity desertWolf;
    private PlayerEntity player;
    private final World world;
    private final float minPlayerDistance;
    private int timeoutCounter;
    private final EntityPredicate predicate;

    public GoalBeg(DesertWolfEntity desertWolf, float minDistance) {
        this.desertWolf = desertWolf;
        this.world = desertWolf.world;
        this.minPlayerDistance = minDistance;
        this.predicate = (new EntityPredicate()).setDistance(minDistance).allowInvulnerable().allowFriendlyFire().setSkipAttackChecks();
        this.setMutexFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        this.player = this.world.getClosestPlayer(predicate, this.desertWolf);
        return this.player != null && this.hasTemptationItemInHand(this.player);
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!this.player.isAlive()) {
            return false;
        } else if (this.desertWolf.getDistanceSq(this.player) > (double) (this.minPlayerDistance * this.minPlayerDistance)) {
            return false;
        } else {
            return this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
        }
    }

    @Override
    public void startExecuting() {
        this.desertWolf.setBegging(true);
        this.timeoutCounter = 40 + this.desertWolf.getRNG().nextInt(40);
    }

    @Override
    public void resetTask() {
        this.desertWolf.setBegging(false);
        this.player = null;
    }

    @Override
    public void tick() {
        this.desertWolf.getLookController().setLookPosition(this.player.posX, this.player.posY + (double) this.player.getEyeHeight(), this.player.posZ, 10.0F, (float) this.desertWolf.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }


    private boolean hasTemptationItemInHand(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack heldStack = player.getHeldItem(hand);

            if (this.desertWolf.isTamed() && heldStack.getItem().isIn(Tags.Items.BONES)) {
                return true;
            }
            if (this.desertWolf.isBreedingItem(heldStack)) {
                return true;
            }
        }
        return false;
    }
}