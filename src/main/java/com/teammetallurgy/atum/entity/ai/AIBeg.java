package com.teammetallurgy.atum.entity.ai;

import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class AIBeg extends EntityAIBase {
    private final EntityDesertWolf desertWolf;
    private PlayerEntity player;
    private final World world;
    private final float minPlayerDistance;
    private int timeoutCounter;

    public AIBeg(EntityDesertWolf desertWolf, float minDistance) {
        this.desertWolf = desertWolf;
        this.world = desertWolf.world;
        this.minPlayerDistance = minDistance;
        this.setMutexBits(2);
    }

    @Override
    public boolean shouldExecute() {
        this.player = this.world.getClosestPlayerToEntity(this.desertWolf, (double) this.minPlayerDistance);
        return this.player != null && this.hasTemptationItemInHand(this.player);
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!this.player.isEntityAlive()) {
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
    public void updateTask() {
        this.desertWolf.getLookHelper().setLookPosition(this.player.posX, this.player.posY + (double) this.player.getEyeHeight(), this.player.posZ, 10.0F, (float) this.desertWolf.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }


    private boolean hasTemptationItemInHand(PlayerEntity player) {
        for (Hand enumhand : Hand.values()) {
            ItemStack heldStack = player.getHeldItem(enumhand);

            if (this.desertWolf.isTamed() && (heldStack.getItem() == Items.BONE || heldStack.getItem() == AtumItems.DUSTY_BONE)) {
                return true;
            }
            if (this.desertWolf.isBreedingItem(heldStack)) {
                return true;
            }
        }
        return false;
    }
}