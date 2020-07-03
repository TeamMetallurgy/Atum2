package com.teammetallurgy.atum.entity.ai.brain;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.entity.efreet.SunspeakerEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class SunspeakerTradeTask extends Task<SunspeakerEntity> {
    private final float speed;

    public SunspeakerTradeTask(float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED), Integer.MAX_VALUE);
        this.speed = speed;
    }

    @Override
    protected boolean shouldExecute(@Nonnull ServerWorld world, SunspeakerEntity owner) {
        PlayerEntity customer = owner.getCustomer();
        return owner.isAlive() && customer != null && !owner.isInWater() && !owner.velocityChanged && owner.getDistanceSq(customer) <= 16.0D && customer.openContainer != null;
    }

    @Override
    protected boolean shouldContinueExecuting(@Nonnull ServerWorld world, @Nonnull SunspeakerEntity sunspeaker, long gameTimeIn) {
        return this.shouldExecute(world, sunspeaker);
    }

    @Override
    protected void startExecuting(@Nonnull ServerWorld world, @Nonnull SunspeakerEntity sunspeaker, long gameTimeIn) {
        this.walkAndLookCustomer(sunspeaker);
    }

    @Override
    protected void resetTask(@Nonnull ServerWorld world, SunspeakerEntity sunspeaker, long gameTimeIn) {
        Brain<?> brain = sunspeaker.getBrain();
        brain.removeMemory(MemoryModuleType.WALK_TARGET);
        brain.removeMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    protected void updateTask(@Nonnull ServerWorld world, @Nonnull SunspeakerEntity owner, long gameTime) {
        this.walkAndLookCustomer(owner);
    }

    @Override
    protected boolean isTimedOut(long gameTime) {
        return false;
    }

    private void walkAndLookCustomer(SunspeakerEntity owner) {
        if (owner.getCustomer() != null) {
            EntityPosWrapper entityposwrapper = new EntityPosWrapper(owner.getCustomer());
            Brain<?> brain = owner.getBrain();
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(entityposwrapper, this.speed, 2));
            brain.setMemory(MemoryModuleType.LOOK_TARGET, entityposwrapper);
        }
    }
}