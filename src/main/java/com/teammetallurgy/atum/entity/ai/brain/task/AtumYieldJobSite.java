package com.teammetallurgy.atum.entity.ai.brain.task;

import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.pathfinder.Path;

import java.util.List;
import java.util.Optional;

public class AtumYieldJobSite {
    public static BehaviorControl<Villager> create(float p_259768_) {
        return BehaviorBuilder.create((p_258916_) -> {
            return p_258916_.group(p_258916_.present(MemoryModuleType.POTENTIAL_JOB_SITE), p_258916_.absent(MemoryModuleType.JOB_SITE), p_258916_.present(MemoryModuleType.NEAREST_LIVING_ENTITIES), p_258916_.registered(MemoryModuleType.WALK_TARGET), p_258916_.registered(MemoryModuleType.LOOK_TARGET)).apply(p_258916_, (p_258901_, p_258902_, p_258903_, p_258904_, p_258905_) -> {
                return (p_258912_, villager, p_258914_) -> {
                    if (villager.isBaby()) {
                        return false;
                    } else if (villager instanceof AtumVillagerEntity && ((AtumVillagerEntity) villager).getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.NONE.get()) {
                        return false;
                    } else {
                        BlockPos blockpos = p_258916_.<GlobalPos>get(p_258901_).pos();
                        Optional<Holder<PoiType>> optional = p_258912_.getPoiManager().getType(blockpos);
                        if (optional.isEmpty()) {
                            return true;
                        } else {
                            p_258916_.<List<LivingEntity>>get(p_258903_).stream().filter((p_258898_) -> {
                                return p_258898_ instanceof AtumVillagerEntity && p_258898_ != villager;
                            }).map((p_258896_) -> {
                                return (AtumVillagerEntity) p_258896_;
                            }).filter(LivingEntity::isAlive).filter((p_258919_) -> {
                                return nearbyWantsJobsite(optional.get(), p_258919_, blockpos);
                            }).findFirst().ifPresent((p_258895_) -> {
                                p_258904_.erase();
                                p_258905_.erase();
                                p_258901_.erase();
                                if (p_258895_.getBrain().getMemory(MemoryModuleType.JOB_SITE).isEmpty()) {
                                    BehaviorUtils.setWalkAndLookTargetMemories(p_258895_, blockpos, p_259768_, 1);
                                    p_258895_.getBrain().setMemory(MemoryModuleType.POTENTIAL_JOB_SITE, GlobalPos.of(p_258912_.dimension(), blockpos));
                                    DebugPackets.sendPoiTicketCountPacket(p_258912_, blockpos);
                                }

                            });
                            return true;
                        }
                    }
                };
            });
        });
    }

    private static boolean nearbyWantsJobsite(Holder<PoiType> p_217511_, AtumVillagerEntity p_217512_, BlockPos p_217513_) {
        boolean flag = p_217512_.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
        if (flag) {
            return false;
        } else {
            Optional<GlobalPos> optional = p_217512_.getBrain().getMemory(MemoryModuleType.JOB_SITE);
            AtumVillagerProfession villagerprofession = p_217512_.getAtumVillagerData().getAtumProfession();
            if (villagerprofession.heldJobSite().test(p_217511_)) {
                return optional.isEmpty() ? canReachPos(p_217512_, p_217513_, p_217511_.value()) : optional.get().pos().equals(p_217513_);
            } else {
                return false;
            }
        }
    }

    private static boolean canReachPos(PathfinderMob p_260080_, BlockPos p_259875_, PoiType p_259606_) {
        Path path = p_260080_.getNavigation().createPath(p_259875_, p_259606_.validRange());
        return path != null && path.canReach();
    }
}