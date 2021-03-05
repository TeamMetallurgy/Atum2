package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AtumFindJobTask extends Task<VillagerEntity> {
    private final float f;

    public AtumFindJobTask(float f) {
        super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.JOB_SITE, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.MOBS, MemoryModuleStatus.VALUE_PRESENT));
        this.f = f;
    }

    @Override
    protected boolean shouldExecute(@Nonnull ServerWorld world, VillagerEntity owner) {
        if (owner.isChild()) {
            return false;
        } else {
            return owner instanceof AtumVillagerEntity && ((AtumVillagerEntity) owner).getAtumVillagerData().getProfession() == AtumVillagerProfession.NONE.get();
        }
    }

    @Override
    protected void startExecuting(ServerWorld worldIn, VillagerEntity entity, long gameTimeIn) {
        BlockPos blockpos = entity.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().getPos();
        Optional<PointOfInterestType> optional = worldIn.getPointOfInterestManager().getType(blockpos);
        if (optional.isPresent()) {
            BrainUtil.getNearbyVillagers(entity, (p_234021_3_) -> {
                return this.func_234018_a_(optional.get(), p_234021_3_, blockpos);
            }).findFirst().ifPresent((p_234023_4_) -> {
                this.func_234022_a_(worldIn, entity, p_234023_4_, blockpos, p_234023_4_.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent());
            });
        }
    }

    private boolean func_234018_a_(PointOfInterestType poi, VillagerEntity entity, BlockPos pos) {
        boolean flag = entity.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
        if (flag || !(entity instanceof AtumVillagerEntity)) {
            return false;
        } else {
            Optional<GlobalPos> optional = entity.getBrain().getMemory(MemoryModuleType.JOB_SITE);
            AtumVillagerProfession villagerprofession = ((AtumVillagerEntity) entity).getAtumVillagerData().getProfession();
            if (((AtumVillagerEntity) entity).getAtumVillagerData().getProfession() != AtumVillagerProfession.NONE.get() && villagerprofession.getPointOfInterest().getPredicate().test(poi)) {
                return !optional.isPresent() ? this.func_234020_a_(entity, pos, poi) : optional.get().getPos().equals(pos);
            } else {
                return false;
            }
        }
    }

    private void func_234022_a_(ServerWorld serverWorld, VillagerEntity p_234022_2_, VillagerEntity p_234022_3_, BlockPos p_234022_4_, boolean p_234022_5_) {
        this.func_234019_a_(p_234022_2_);
        if (!p_234022_5_) {
            BrainUtil.setTargetPosition(p_234022_3_, p_234022_4_, this.f, 1);
            p_234022_3_.getBrain().setMemory(MemoryModuleType.POTENTIAL_JOB_SITE, GlobalPos.getPosition(serverWorld.getDimensionKey(), p_234022_4_));
            DebugPacketSender.func_218801_c(serverWorld, p_234022_4_);
        }

    }

    private boolean func_234020_a_(VillagerEntity p_234020_1_, BlockPos p_234020_2_, PointOfInterestType p_234020_3_) {
        Path path = p_234020_1_.getNavigator().getPathToPos(p_234020_2_, p_234020_3_.getValidRange());
        return path != null && path.reachesTarget();
    }

    private void func_234019_a_(VillagerEntity p_234019_1_) {
        p_234019_1_.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);
        p_234019_1_.getBrain().removeMemory(MemoryModuleType.LOOK_TARGET);
        p_234019_1_.getBrain().removeMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
    }
}