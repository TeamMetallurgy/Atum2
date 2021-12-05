package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AtumFindJobTask extends Behavior<Villager> {
    private final float f;

    public AtumFindJobTask(float f) {
        super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
        this.f = f;
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel world, Villager owner) {
        if (owner.isBaby()) {
            return false;
        } else {
            return owner instanceof AtumVillagerEntity && ((AtumVillagerEntity) owner).getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.NONE.get();
        }
    }

    @Override
    protected void start(ServerLevel worldIn, Villager entity, long gameTimeIn) {
        BlockPos blockpos = entity.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().pos();
        Optional<PoiType> optional = worldIn.getPoiManager().getType(blockpos);
        if (optional.isPresent()) {
            BehaviorUtils.getNearbyVillagersWithCondition(entity, (p_234021_3_) -> {
                return this.nearbyWantsJobsite(optional.get(), p_234021_3_, blockpos);
            }).findFirst().ifPresent((p_234023_4_) -> {
                this.yieldJobSite(worldIn, entity, p_234023_4_, blockpos, p_234023_4_.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent());
            });
        }
    }

    private boolean nearbyWantsJobsite(PoiType poi, Villager entity, BlockPos pos) {
        boolean flag = entity.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
        if (flag || !(entity instanceof AtumVillagerEntity)) {
            return false;
        } else {
            Optional<GlobalPos> optional = entity.getBrain().getMemory(MemoryModuleType.JOB_SITE);
            AtumVillagerProfession villagerprofession = ((AtumVillagerEntity) entity).getAtumVillagerData().getAtumProfession();
            if (((AtumVillagerEntity) entity).getAtumVillagerData().getAtumProfession() != AtumVillagerProfession.NONE.get() && villagerprofession.getPointOfInterest().getPredicate().test(poi)) {
                return !optional.isPresent() ? this.canReachPos(entity, pos, poi) : optional.get().pos().equals(pos);
            } else {
                return false;
            }
        }
    }

    private void yieldJobSite(ServerLevel serverWorld, Villager p_234022_2_, Villager p_234022_3_, BlockPos p_234022_4_, boolean p_234022_5_) {
        this.eraseMemories(p_234022_2_);
        if (!p_234022_5_) {
            BehaviorUtils.setWalkAndLookTargetMemories(p_234022_3_, p_234022_4_, this.f, 1);
            p_234022_3_.getBrain().setMemory(MemoryModuleType.POTENTIAL_JOB_SITE, GlobalPos.of(serverWorld.dimension(), p_234022_4_));
            DebugPackets.sendPoiTicketCountPacket(serverWorld, p_234022_4_);
        }

    }

    private boolean canReachPos(Villager p_234020_1_, BlockPos p_234020_2_, PoiType p_234020_3_) {
        Path path = p_234020_1_.getNavigation().createPath(p_234020_2_, p_234020_3_.getValidRange());
        return path != null && path.canReach();
    }

    private void eraseMemories(Villager p_234019_1_) {
        p_234019_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_234019_1_.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        p_234019_1_.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
    }
}