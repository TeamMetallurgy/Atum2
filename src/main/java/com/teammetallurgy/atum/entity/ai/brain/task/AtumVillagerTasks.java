package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.RelicItem;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.village.PointOfInterestType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AtumVillagerTasks { //Added EntityType parameter to all methods. Noted when additional changes have been made. Removed raid tasks

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> core(EntityType<? extends AtumVillagerEntity> entityType, AtumVillagerProfession profession, float f) { //SwitchAtumVillagerJobTask, AtumFindJobTask changed, AtumAssignProfessionTask, AtumChangeJobTask changed
        return ImmutableList.of(Pair.of(0, new SwimTask(0.8F)), Pair.of(0, new InteractWithDoorTask()), Pair.of(0, new LookTask(45, 90)), Pair.of(0, new PanicTask()), Pair.of(0, new WakeUpTask()), Pair.of(0, new HideFromRaidOnBellRingTask()), Pair.of(0, new BeginRaidTask()), Pair.of(0, new ExpirePOITask(profession.getPointOfInterest(), MemoryModuleType.JOB_SITE)), Pair.of(0, new ExpirePOITask(profession.getPointOfInterest(), MemoryModuleType.POTENTIAL_JOB_SITE)), Pair.of(1, new WalkToTargetTask()), Pair.of(2, new SwitchAtumVillagerJobTask(profession)), Pair.of(3, new TradeTask(f)), Pair.of(3, new CuratorStartAdmiringItemTask<>()), Pair.of(3, new CuratorAdmireItemTask<>(80)), Pair.of(5, new PickupWantedItemTask<>(f, profession == AtumVillagerProfession.CURATOR.get(), profession == AtumVillagerProfession.CURATOR.get() ? 9 : 4)), Pair.of(6, new GatherPOITask(profession.getPointOfInterest(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())), Pair.of(7, new FindPotentialJobTask(f)), Pair.of(8, new AtumFindJobTask(f)), Pair.of(10, new GatherPOITask(PointOfInterestType.HOME, MemoryModuleType.HOME, false, Optional.of((byte) 14))), Pair.of(10, new GatherPOITask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT, true, Optional.of((byte) 14))), Pair.of(10, new AtumAssignProfessionTask()), Pair.of(10, new AtumChangeJobTask()));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> work(EntityType<? extends AtumVillagerEntity> entityType, AtumVillagerProfession profession, float p_220639_1_) { //AtumFarmTask changed. GiveHeroGiftsTask removed
        SpawnGolemTask spawngolemtask;
        if (profession == AtumVillagerProfession.FARMER.get()) {
            spawngolemtask = new AtumFarmerWorkTask();
        } else {
            spawngolemtask = new SpawnGolemTask();
        }

        return ImmutableList.of(lookAtPlayerOrVillager(entityType), Pair.of(5, new FirstShuffledTask<>(ImmutableList.of(Pair.of(spawngolemtask, 7), Pair.of(new WorkTask(MemoryModuleType.JOB_SITE, 0.4F, 4), 2), Pair.of(new WalkTowardsPosTask(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), 5), Pair.of(new WalkTowardsRandomSecondaryPosTask(MemoryModuleType.SECONDARY_JOB_SITE, p_220639_1_, 1, 6, MemoryModuleType.JOB_SITE), 5), Pair.of(new AtumFarmTask(), profession == AtumVillagerProfession.FARMER.get() ? 2 : 5), Pair.of(new BoneMealCropsTask(), profession == AtumVillagerProfession.FARMER.get() ? 4 : 7)))), Pair.of(10, new ShowWaresTask(400, 1600)), Pair.of(10, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)), Pair.of(2, new StayNearPointTask(MemoryModuleType.JOB_SITE, p_220639_1_, 9, 100, 1200)), Pair.of(99, new UpdateActivityTask()));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> play(EntityType<? extends AtumVillagerEntity> entityType, float walkingSpeed) {
        return ImmutableList.of(Pair.of(0, new WalkToTargetTask(80, 120)), lookAtMany(entityType), Pair.of(5, new WalkToVillagerBabiesTask()), Pair.of(5, new FirstShuffledTask<>(ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleStatus.VALUE_ABSENT), ImmutableList.of(Pair.of(InteractWithEntityTask.func_220445_a(entityType, 8, MemoryModuleType.INTERACTION_TARGET, walkingSpeed, 2), 2), Pair.of(InteractWithEntityTask.func_220445_a(AtumEntities.SERVAL, 8, MemoryModuleType.INTERACTION_TARGET, walkingSpeed, 2), 1), Pair.of(new FindWalkTargetTask(walkingSpeed), 1), Pair.of(new WalkTowardsLookTargetTask(walkingSpeed, 2), 1), Pair.of(new JumpOnBedTask(walkingSpeed), 2), Pair.of(new DummyTask(20, 40), 2)))), Pair.of(99, new UpdateActivityTask()));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> rest(EntityType<? extends AtumVillagerEntity> entityType, float walkingSpeed) {
        return ImmutableList.of(Pair.of(2, new StayNearPointTask(MemoryModuleType.HOME, walkingSpeed, 1, 150, 1200)), Pair.of(3, new ExpirePOITask(PointOfInterestType.HOME, MemoryModuleType.HOME)), Pair.of(3, new SleepAtHomeTask()), Pair.of(5, new FirstShuffledTask<>(ImmutableMap.of(MemoryModuleType.HOME, MemoryModuleStatus.VALUE_ABSENT), ImmutableList.of(Pair.of(new WalkToHouseTask(walkingSpeed), 1), Pair.of(new WalkRandomlyInsideTask(walkingSpeed), 4), Pair.of(new WalkToPOITask(walkingSpeed, 4), 2), Pair.of(new DummyTask(20, 40), 2)))), lookAtPlayerOrVillager(entityType), Pair.of(99, new UpdateActivityTask()));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> meet(EntityType<? extends AtumVillagerEntity> entityType, float p_220637_1_) { //AtumShareItemsTask changed
        return ImmutableList.of(Pair.of(2, new FirstShuffledTask<>(ImmutableList.of(Pair.of(new WorkTask(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2), Pair.of(new CongregateTask(), 2)))), Pair.of(10, new ShowWaresTask(400, 1600)), Pair.of(10, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)), Pair.of(2, new StayNearPointTask(MemoryModuleType.MEETING_POINT, p_220637_1_, 6, 100, 200)), Pair.of(3, new GiveHeroGiftsTask(100)), Pair.of(3, new ExpirePOITask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT)), Pair.of(3, new MultiTask<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), MultiTask.Ordering.ORDERED, MultiTask.RunType.RUN_ONE, ImmutableList.of(Pair.of(new AtumShareItemsTask(), 1)))), lookAtMany(entityType), Pair.of(99, new UpdateActivityTask()));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> idle(EntityType<? extends AtumVillagerEntity> entityType, float f) { //Changed to custom Breed Task, AtumShareItemsTask
        return ImmutableList.of(Pair.of(2, new FirstShuffledTask<>(ImmutableList.of(Pair.of(InteractWithEntityTask.func_220445_a(entityType, 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 2), Pair.of(new InteractWithEntityTask<>(entityType == AtumEntities.VILLAGER_MALE ? AtumEntities.VILLAGER_FEMALE : AtumEntities.VILLAGER_MALE, 8, AgeableEntity::canBreed, AgeableEntity::canBreed, MemoryModuleType.BREED_TARGET, f, 2), 1), Pair.of(InteractWithEntityTask.func_220445_a(AtumEntities.SERVAL, 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 1), Pair.of(new FindWalkTargetTask(f), 1), Pair.of(new WalkTowardsLookTargetTask(f, 2), 1), Pair.of(new JumpOnBedTask(f), 1), Pair.of(new DummyTask(30, 60), 1)))), Pair.of(3, new GiveHeroGiftsTask(100)), Pair.of(3, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)), Pair.of(3, new ShowWaresTask(400, 1600)), Pair.of(3, new MultiTask<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), MultiTask.Ordering.ORDERED, MultiTask.RunType.RUN_ONE, ImmutableList.of(Pair.of(new AtumShareItemsTask(), 1)))), Pair.of(3, new MultiTask<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.BREED_TARGET), MultiTask.Ordering.ORDERED, MultiTask.RunType.RUN_ONE, ImmutableList.of(Pair.of(new CreateBabyVillagerWithGenderTask(), 1)))), lookAtMany(entityType), Pair.of(99, new UpdateActivityTask()));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> panic(EntityType<? extends AtumVillagerEntity> entityType, float f1) {
        float f = f1 * 1.5F;
        return ImmutableList.of(Pair.of(0, new ClearHurtTask()), Pair.of(1, RunAwayTask.func_233965_b_(MemoryModuleType.NEAREST_HOSTILE, f, 6, false)), Pair.of(1, RunAwayTask.func_233965_b_(MemoryModuleType.HURT_BY_ENTITY, f, 6, false)), Pair.of(3, new FindWalkTargetTask(f, 2, 2)), lookAtPlayerOrVillager(entityType));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> hide(EntityType<? extends AtumVillagerEntity> entityType, float f) {
        return ImmutableList.of(Pair.of(0, new ExpireHidingTask(15, 3)), Pair.of(1, new FindHidingPlaceTask(32, f * 1.25F, 2)), lookAtPlayerOrVillager(entityType));
    }

    private static Pair<Integer, Task<LivingEntity>> lookAtMany(EntityType<? extends AtumVillagerEntity> entityType) {
        return Pair.of(5, new FirstShuffledTask<>(ImmutableList.of(Pair.of(new LookAtEntityTask(AtumEntities.SERVAL, 8.0F), 8), Pair.of(new LookAtEntityTask(entityType, 8.0F), 2), Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 2), Pair.of(new LookAtEntityTask(EntityClassification.CREATURE, 8.0F), 1), Pair.of(new LookAtEntityTask(EntityClassification.WATER_CREATURE, 8.0F), 1), Pair.of(new LookAtEntityTask(EntityClassification.WATER_AMBIENT, 8.0F), 1), Pair.of(new LookAtEntityTask(EntityClassification.MONSTER, 8.0F), 1), Pair.of(new DummyTask(30, 60), 2))));
    }

    private static Pair<Integer, Task<LivingEntity>> lookAtPlayerOrVillager(EntityType<? extends AtumVillagerEntity> entityType) {
        return Pair.of(5, new FirstShuffledTask<>(ImmutableList.of(Pair.of(new LookAtEntityTask(entityType, 8.0F), 2), Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 2), Pair.of(new DummyTask(30, 60), 8))));
    }

    //Curator
    protected static void trade(VillagerEntity entity, boolean shouldTrade) {
        ItemStack heldStack = entity.getHeldItem(Hand.OFF_HAND);
        entity.setHeldItem(Hand.OFF_HAND, ItemStack.EMPTY);
        if (!entity.isChild()) {
            boolean isCurrency = heldStack.getItem().isIn(AtumAPI.Tags.RELIC_NON_DIRTY);
            if (shouldTrade && isCurrency) {
                spawnItem(entity, getCuratorTrades(heldStack));
            } else if (!isCurrency) {
                boolean flag1 = entity.func_233665_g_(heldStack);
                if (!flag1) {
                    addItemToInventory(entity, heldStack);
                }
            }
        }
    }

    private static void spawnItem(VillagerEntity entity, List<ItemStack> stacks) {
        Optional<PlayerEntity> optional = entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
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
            trades.add(new ItemStack(AtumItems.GOLD_COIN, getCoinAmount(type, quality)));
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

    private static void spawnItemNearPlayer(VillagerEntity entity, PlayerEntity player, List<ItemStack> stacks) {
        spawnItemNearEntity(entity, stacks, player.getPositionVec());
    }

    private static void spawnItemNearEntity(VillagerEntity entity, List<ItemStack> stacks, Vector3d vec3d) {
        if (!stacks.isEmpty()) {
            entity.swingArm(Hand.OFF_HAND);
            for (ItemStack stack : stacks) {
                BrainUtil.spawnItemNearEntity(entity, stack, vec3d.add(0.0D, 1.0D, 0.0D));
            }
        }
    }

    private static void spawnItemOnGround(VillagerEntity entity, List<ItemStack> stacks) {
        spawnItemNearEntity(entity, stacks, getLandPos(entity));
    }

    private static Vector3d getLandPos(VillagerEntity entity) {
        Vector3d vector3d = RandomPositionGenerator.getLandPos(entity, 4, 2);
        return vector3d == null ? entity.getPositionVec() : vector3d;
    }

    private static void addItemToInventory(VillagerEntity entity, @Nonnull ItemStack stack) {
        ItemStack itemstack = entity.getVillagerInventory().addItem(stack);
        spawnItemOnGround(entity, Collections.singletonList(itemstack));
    }

    public static boolean canPickup(VillagerEntity entity) {
        return entity.getHeldItemOffhand().isEmpty() || !entity.getHeldItemOffhand().getItem().isIn(AtumAPI.Tags.RELIC_NON_DIRTY);
    }

    public static void putInHand(VillagerEntity entity, ItemEntity itemEntity) {
        if (entity instanceof AtumVillagerEntity) {
            entity.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);
            entity.getNavigator().clearPath();
            entity.onItemPickup(itemEntity, 1);
            ItemStack stack = setEntityItemStack(itemEntity);

            Item item = stack.getItem();
            if (item.isIn(AtumAPI.Tags.RELIC_NON_DIRTY)) {
                entity.getBrain().removeMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
                dropOffhand((AtumVillagerEntity) entity, stack);
                entity.getBrain().replaceMemory(MemoryModuleType.ADMIRING_ITEM, true, 120L);
            }
        }
    }

    private static ItemStack setEntityItemStack(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        ItemStack itemstack1 = itemstack.split(1);
        if (itemstack.isEmpty()) {
            itemEntity.remove();
        } else {
            itemEntity.setItem(itemstack);
        }
        return itemstack1;
    }

    private static void dropOffhand(AtumVillagerEntity entity, @Nonnull ItemStack stack) {
        if (!entity.getHeldItemOffhand().isEmpty()) {
            entity.entityDropItem(entity.getHeldItem(Hand.OFF_HAND));
        }
        entity.setOffHand(stack);
    }

    public static boolean canCuratorPickup(AtumVillagerEntity entity, ItemStack stack) {
        Item item = stack.getItem();
        if (entity.getBrain().hasMemory(MemoryModuleType.ADMIRING_DISABLED) && entity.getBrain().hasMemory(MemoryModuleType.ATTACK_TARGET)) {
            return false;
        } else {
            if (item.isIn(AtumAPI.Tags.RELIC_NON_DIRTY)) {
                return canPickup(entity);
            }
        }
        return false;
    }
}