package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import com.teammetallurgy.atum.items.RelicItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AtumVillagerGoalPackages { //Added EntityType parameter to all methods. Noted when additional changes have been made. Removed raid tasks

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> core(AtumVillagerProfession profession, float f) { //AtumPoiCompetitorScan, AtumYieldJobSite changed, AtumAssignProfessionTask, AtumChangeJobTask changed
        return ImmutableList.of(Pair.of(0, new Swim(0.8F)), Pair.of(0, InteractWithDoor.create()), Pair.of(0, new LookAtTargetSink(45, 90)), Pair.of(0, new VillagerPanicTrigger()), Pair.of(0, WakeUp.create()), Pair.of(0, ReactToBell.create()), Pair.of(0, SetRaidStatus.create()), Pair.of(0, ValidateNearbyPoi.create(profession.heldJobSite(), MemoryModuleType.JOB_SITE)), Pair.of(0, ValidateNearbyPoi.create(profession.acquirableJobSite(), MemoryModuleType.POTENTIAL_JOB_SITE)), Pair.of(1, new MoveToTargetSink()), Pair.of(2, PoiCompetitorScan.create()), Pair.of(3, new LookAndFollowTradingPlayerSink(f)), Pair.of(3, new CuratorStartAdmiringItemTask<>()), Pair.of(3, new CuratorAdmireItemTask<>(80)), Pair.of(5, GoToWantedItem.create(f, profession == AtumVillagerProfession.CURATOR.get(), profession == AtumVillagerProfession.CURATOR.get() ? 9 : 4)), Pair.of(5, GoToWantedItem.create(f, false, 4)), Pair.of(6, AcquirePoi.create(profession.acquirableJobSite(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())), Pair.of(7, new GoToPotentialJobSite(f)), Pair.of(8, AtumYieldJobSite.create(f)), Pair.of(10, AcquirePoi.create((p_217499_) -> p_217499_.is(PoiTypes.HOME), MemoryModuleType.HOME, false, Optional.of((byte)14))), Pair.of(10, AcquirePoi.create((p_217497_) -> p_217497_.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT, true, Optional.of((byte)14))), Pair.of(10, AtumAssignProfessionFromJobSite.create()), Pair.of(10, AtumResetProfession.create()));
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> work(EntityType<? extends AtumVillagerEntity> entityType, AtumVillagerProfession profession, float p_220639_1_) { //AtumFarmTask changed. GiveHeroGiftsTask removed
        WorkAtPoi spawngolemtask;
        if (profession == AtumVillagerProfession.FARMER.get()) {
            spawngolemtask = new AtumFarmerWorkTask();
        } else {
            spawngolemtask = new WorkAtPoi();
        }

        return ImmutableList.of(lookAtPlayerOrVillager(entityType), Pair.of(5, new RunOne<>(ImmutableList.of(Pair.of(spawngolemtask, 7), Pair.of(StrollAroundPoi.create(MemoryModuleType.JOB_SITE, 0.4F, 4), 2), Pair.of(StrollToPoi.create(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), 5), Pair.of(StrollToPoiList.create(MemoryModuleType.SECONDARY_JOB_SITE, p_220639_1_, 1, 6, MemoryModuleType.JOB_SITE), 5), Pair.of(new AtumFarmTask(), profession == AtumVillagerProfession.FARMER.get() ? 2 : 5), Pair.of(new UseBonemeal(), profession == AtumVillagerProfession.FARMER.get() ? 4 : 7)))), Pair.of(10, new ShowTradesToPlayer(400, 1600)), Pair.of(10, SetLookAndInteract.create(EntityType.PLAYER, 4)), Pair.of(2, SetWalkTargetFromBlockMemory.create(MemoryModuleType.JOB_SITE, p_220639_1_, 9, 100, 1200)), Pair.of(99, UpdateActivityFromSchedule.create()));
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> play(EntityType<? extends AtumVillagerEntity> entityType, float walkingSpeed) {
        return ImmutableList.of(Pair.of(0, new MoveToTargetSink(80, 120)), lookAtMany(entityType), Pair.of(5, PlayTagWithOtherKids.create()), Pair.of(5, new RunOne<>(ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_ABSENT), ImmutableList.of(Pair.of(InteractWith.of(entityType, 8, MemoryModuleType.INTERACTION_TARGET, walkingSpeed, 2), 2), Pair.of(InteractWith.of(AtumEntities.SERVAL.get(), 8, MemoryModuleType.INTERACTION_TARGET, walkingSpeed, 2), 1), Pair.of(VillageBoundRandomStroll.create(walkingSpeed), 1), Pair.of(SetWalkTargetFromLookTarget.create(walkingSpeed, 2), 1), Pair.of(new JumpOnBed(walkingSpeed), 2), Pair.of(new DoNothing(20, 40), 2)))), Pair.of(99, UpdateActivityFromSchedule.create()));
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> rest(EntityType<? extends AtumVillagerEntity> entityType, float walkingSpeed) {
        return ImmutableList.of(Pair.of(2, SetWalkTargetFromBlockMemory.create(MemoryModuleType.HOME, walkingSpeed, 1, 150, 1200)), Pair.of(3, ValidateNearbyPoi.create(poiTypeHolder -> poiTypeHolder.is(PoiTypes.HOME), MemoryModuleType.HOME)), Pair.of(3, new SleepInBed()), Pair.of(5, new RunOne<>(ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT), ImmutableList.of(Pair.of(SetClosestHomeAsWalkTarget.create(walkingSpeed), 1), Pair.of(InsideBrownianWalk.create(walkingSpeed), 4), Pair.of(GoToClosestVillage.create(walkingSpeed, 4), 2), Pair.of(new DoNothing(20, 40), 2)))), lookAtPlayerOrVillager(entityType), Pair.of(99, UpdateActivityFromSchedule.create()));
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> meet(EntityType<? extends AtumVillagerEntity> entityType, float p_220637_1_) { //AtumShareItemsTask changed
        return ImmutableList.of(Pair.of(2, new RunOne<>(ImmutableList.of(Pair.of(StrollAroundPoi.create(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2), Pair.of(SocializeAtBell.create(), 2)))), Pair.of(10, new ShowTradesToPlayer(400, 1600)), Pair.of(10, SetLookAndInteract.create(EntityType.PLAYER, 4)), Pair.of(2, SetWalkTargetFromBlockMemory.create(MemoryModuleType.MEETING_POINT, p_220637_1_, 6, 100, 200)), Pair.of(3, new GiveGiftToHero(100)), Pair.of(3, ValidateNearbyPoi.create(poiTypeHolder -> poiTypeHolder.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT)), Pair.of(3, new GateBehavior<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, ImmutableList.of(Pair.of(new AtumShareItemsTask(), 1)))), lookAtMany(entityType), Pair.of(99, UpdateActivityFromSchedule.create()));
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> idle(EntityType<? extends AtumVillagerEntity> entityType, float f) { //Changed to custom Breed Task, AtumShareItemsTask
        return ImmutableList.of(Pair.of(2, new RunOne<>(ImmutableList.of(Pair.of(InteractWith.of(entityType, 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 2), Pair.of(InteractWith.of(entityType == AtumEntities.VILLAGER_MALE.get() ? AtumEntities.VILLAGER_FEMALE.get() : AtumEntities.VILLAGER_MALE.get(), 8, AgeableMob::canBreed, AgeableMob::canBreed, MemoryModuleType.BREED_TARGET, f, 2), 1), Pair.of(InteractWith.of(AtumEntities.SERVAL.get(), 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 1), Pair.of(VillageBoundRandomStroll.create(f), 1), Pair.of(SetWalkTargetFromLookTarget.create(f, 2), 1), Pair.of(new JumpOnBed(f), 1), Pair.of(new DoNothing(30, 60), 1)))), Pair.of(3, new GiveGiftToHero(100)), Pair.of(3, SetLookAndInteract.create(EntityType.PLAYER, 4)), Pair.of(3, new ShowTradesToPlayer(400, 1600)), Pair.of(3, new GateBehavior<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, ImmutableList.of(Pair.of(new AtumShareItemsTask(), 1)))), Pair.of(3, new GateBehavior<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.BREED_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, ImmutableList.of(Pair.of(new CreateBabyVillagerWithGenderTask(), 1)))), lookAtMany(entityType), Pair.of(99, UpdateActivityFromSchedule.create()));
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> panic(EntityType<? extends AtumVillagerEntity> entityType, float f1) {
        float f = f1 * 1.5F;
        return ImmutableList.of(Pair.of(0, VillagerCalmDown.create()), Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_HOSTILE, f, 6, false)), Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.HURT_BY_ENTITY, f, 6, false)), Pair.of(3, VillageBoundRandomStroll.create(f, 2, 2)), lookAtPlayerOrVillager(entityType));
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> hide(EntityType<? extends AtumVillagerEntity> entityType, float f) {
        return ImmutableList.of(Pair.of(0, SetHiddenState.create(15, 3)), Pair.of(1, LocateHidingPlace.create(32, f * 1.25F, 2)), lookAtPlayerOrVillager(entityType));
    }

    private static Pair<Integer, BehaviorControl<LivingEntity>> lookAtMany(EntityType<? extends AtumVillagerEntity> entityType) {
        return Pair.of(5, new RunOne<>(ImmutableList.of(Pair.of(SetEntityLookTarget.create(AtumEntities.SERVAL.get(), 8.0F), 8), Pair.of(SetEntityLookTarget.create(entityType, 8.0F), 2), Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 2), Pair.of(SetEntityLookTarget.create(MobCategory.CREATURE, 8.0F), 1), Pair.of(SetEntityLookTarget.create(MobCategory.WATER_CREATURE, 8.0F), 1), Pair.of(SetEntityLookTarget.create(MobCategory.WATER_AMBIENT, 8.0F), 1), Pair.of(SetEntityLookTarget.create(MobCategory.MONSTER, 8.0F), 1), Pair.of(new DoNothing(30, 60), 2))));
    }

    private static Pair<Integer, BehaviorControl<LivingEntity>> lookAtPlayerOrVillager(EntityType<? extends AtumVillagerEntity> entityType) {
        return Pair.of(5, new RunOne<>(ImmutableList.of(Pair.of(SetEntityLookTarget.create(entityType, 8.0F), 2), Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 2), Pair.of(new DoNothing(30, 60), 8))));
    }

    //Curator
    protected static void trade(Villager entity, boolean shouldTrade) {
        ItemStack heldStack = entity.getItemInHand(InteractionHand.OFF_HAND);
        entity.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        if (!entity.isBaby()) {
            boolean isCurrency = heldStack.is(AtumAPI.Tags.RELIC_NON_DIRTY);
            if (shouldTrade && isCurrency) {
                spawnItem(entity, getCuratorTrades(heldStack));
            } else if (!isCurrency) {
                boolean flag1 = !entity.equipItemIfPossible(heldStack).equals(ItemStack.EMPTY);
                if (!flag1) {
                    addItemToInventory(entity, heldStack);
                }
            }
        }
    }

    private static void spawnItem(Villager entity, List<ItemStack> stacks) {
        Optional<Player> optional = entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        if (optional.isPresent()) {
            spawnItemNearPlayer(entity, optional.get(), stacks);
        } else {
            spawnItemOnGround(entity, stacks);
        }
    }

    private static List<ItemStack> getCuratorTrades(ItemStack relicStack) {
        List<ItemStack> trades = new ArrayList<>();
        if (relicStack.getItem() instanceof RelicItem) {
            RelicItem.Type type = RelicItem.getType(relicStack.getItem());
            RelicItem.Quality quality = RelicItem.getQuality(relicStack.getItem());
            trades.add(new ItemStack(AtumItems.GOLD_COIN.get(), getCoinAmount(type, quality)));
        }
        return trades;
    }

    private static int getCoinAmount(RelicItem.Type type, RelicItem.Quality quality) {
        double modifier = getCoinModifier(type);
        int amount = 0;

        if (quality == RelicItem.Quality.SILVER) {
            amount += modifier;
        } else if (quality == RelicItem.Quality.GOLD) {
            amount += modifier * 1.5D;
        } else if (quality == RelicItem.Quality.SAPPHIRE) {
            amount += modifier * 2.0D;
        } else if (quality == RelicItem.Quality.RUBY) {
            amount += modifier * 2.5D;
        } else if (quality == RelicItem.Quality.EMERALD) {
            amount += modifier * 3.5D;
        } else if (quality == RelicItem.Quality.DIAMOND) {
            amount += modifier * 5.0D;
        }
        return amount;
    }

    private static double getCoinModifier(RelicItem.Type type) {
        double modifier = 1.0D;
        if (type == RelicItem.Type.NECKLACE) {
            modifier = 1.5D;
        } else if (type == RelicItem.Type.BROOCH) {
            modifier = 2.0D;
        } else if (type == RelicItem.Type.BRACELET) {
            modifier = 2.5D;
        } else if (type == RelicItem.Type.IDOL) {
            modifier = 3.5D;
        }
        return modifier;
    }

    private static void spawnItemNearPlayer(Villager entity, Player player, List<ItemStack> stacks) {
        spawnItemNearEntity(entity, stacks, player.position());
    }

    private static void spawnItemNearEntity(Villager entity, List<ItemStack> stacks, Vec3 vec3d) {
        if (!stacks.isEmpty()) {
            entity.swing(InteractionHand.OFF_HAND);
            for (ItemStack stack : stacks) {
                BehaviorUtils.throwItem(entity, stack, vec3d.add(0.0D, 1.0D, 0.0D));
            }
        }
    }

    private static void spawnItemOnGround(Villager entity, List<ItemStack> stacks) {
        spawnItemNearEntity(entity, stacks, getLandPos(entity));
    }

    private static Vec3 getLandPos(Villager entity) {
        Vec3 vector3d = LandRandomPos.getPos(entity, 4, 2);
        return vector3d == null ? entity.position() : vector3d;
    }

    private static void addItemToInventory(Villager entity, @Nonnull ItemStack stack) {
        ItemStack itemstack = entity.getInventory().addItem(stack);
        spawnItemOnGround(entity, Collections.singletonList(itemstack));
    }

    public static boolean canPickup(Villager entity) {
        return entity.getOffhandItem().isEmpty() || !entity.getOffhandItem().is(AtumAPI.Tags.RELIC_NON_DIRTY);
    }

    public static void putInHand(Villager entity, ItemEntity itemEntity) {
        if (entity instanceof AtumVillagerEntity) {
            entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            entity.getNavigation().stop();
            entity.take(itemEntity, 1);
            ItemStack stack = setEntityItemStack(itemEntity);

            if (stack.is(AtumAPI.Tags.RELIC_NON_DIRTY)) {
                entity.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
                dropOffhand((AtumVillagerEntity) entity, stack);
                entity.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 120L);
            }
        }
    }

    private static ItemStack setEntityItemStack(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        ItemStack itemstack1 = itemstack.split(1);
        if (itemstack.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(itemstack);
        }
        return itemstack1;
    }

    private static void dropOffhand(AtumVillagerEntity entity, @Nonnull ItemStack stack) {
        if (!entity.getOffhandItem().isEmpty()) {
            entity.spawnAtLocation(entity.getItemInHand(InteractionHand.OFF_HAND));
        }
        entity.setOffHand(stack);
    }

    public static boolean canCuratorPickup(AtumVillagerEntity entity, ItemStack stack) {
        if (entity.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED) && entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            return false;
        } else {
            if (stack.is(AtumAPI.Tags.RELIC_NON_DIRTY)) {
                return canPickup(entity);
            }
        }
        return false;
    }
}