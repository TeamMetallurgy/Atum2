package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class AtumShareItemsTask extends Behavior<Villager> {
    private Set<Item> trades = ImmutableSet.of();

    public AtumShareItemsTask() {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Villager owner) {
        return BehaviorUtils.targetIsValid(owner.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityType.VILLAGER);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Villager entity, long gameTimeIn) {
        return this.checkExtraStartConditions(level, entity);
    }

    @Override
    protected void start(ServerLevel level, Villager entity, long gameTimeIn) {
        Villager villagerentity = (Villager) entity.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        BehaviorUtils.lockGazeAndWalkToEachOther(entity, villagerentity, 0.5F);
        this.trades = figureOutWhatIAmWillingToTrade(entity, villagerentity);
    }

    @Override
    protected void tick(ServerLevel level, Villager owner, long gameTime) {
        Villager villagerentity = (Villager) owner.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if (owner instanceof AtumVillagerEntity && villagerentity instanceof AtumVillagerEntity) {
            if (!(owner.distanceToSqr(villagerentity) > 5.0D)) {
                BehaviorUtils.lockGazeAndWalkToEachOther(owner, villagerentity, 0.5F);
                owner.gossip(level, villagerentity, gameTime);
                if (owner.hasExcessFood() && (((AtumVillagerEntity) owner).getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.FARMER.get() || villagerentity.wantsMoreFood())) {
                    throwHalfStack(owner, Villager.FOOD_POINTS.keySet(), villagerentity);
                }

                if (((AtumVillagerEntity) villagerentity).getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.FARMER.get() && owner.getInventory().countItem(Items.WHEAT) > Items.WHEAT.getMaxStackSize() / 2) {
                    throwHalfStack(owner, ImmutableSet.of(Items.WHEAT), villagerentity);
                }

                if (!this.trades.isEmpty() && owner.getInventory().hasAnyOf(this.trades)) {
                    throwHalfStack(owner, this.trades, villagerentity);
                }
            }
        }
    }

    @Override
    protected void stop(ServerLevel level, Villager entity, long gameTimeIn) {
        entity.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
    }

    private static Set<Item> figureOutWhatIAmWillingToTrade(Villager from, Villager to) {
        if (from instanceof AtumVillagerEntity && to instanceof AtumVillagerEntity) {
            ImmutableSet<Item> immutableset = ((AtumVillagerEntity) to).getAtumVillagerData().getAtumProfession().getSpecificItems();
            ImmutableSet<Item> immutableset1 = ((AtumVillagerEntity) from).getAtumVillagerData().getAtumProfession().getSpecificItems();
            return immutableset.stream().filter((p_220587_1_) -> {
                return !immutableset1.contains(p_220587_1_);
            }).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private static void throwHalfStack(Villager p_220586_0_, Set<Item> p_220586_1_, LivingEntity p_220586_2_) {
        SimpleContainer inventory = p_220586_0_.getInventory();
        ItemStack itemstack = ItemStack.EMPTY;
        int i = 0;

        while (i < inventory.getContainerSize()) {
            ItemStack itemstack1;
            Item item;
            int j;
            label28:
            {
                itemstack1 = inventory.getItem(i);
                if (!itemstack1.isEmpty()) {
                    item = itemstack1.getItem();
                    if (p_220586_1_.contains(item)) {
                        if (itemstack1.getCount() > itemstack1.getMaxStackSize() / 2) {
                            j = itemstack1.getCount() / 2;
                            break label28;
                        }

                        if (itemstack1.getCount() > 24) {
                            j = itemstack1.getCount() - 24;
                            break label28;
                        }
                    }
                }

                ++i;
                continue;
            }

            itemstack1.shrink(j);
            itemstack = new ItemStack(item, j);
            break;
        }

        if (!itemstack.isEmpty()) {
            BehaviorUtils.throwItem(p_220586_0_, itemstack, p_220586_2_.position());
        }

    }
}