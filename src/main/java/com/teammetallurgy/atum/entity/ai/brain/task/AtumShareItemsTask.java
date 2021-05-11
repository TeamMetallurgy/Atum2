package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.server.ServerWorld;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class AtumShareItemsTask extends Task<VillagerEntity> {
    private Set<Item> field_220588_a = ImmutableSet.of();

    public AtumShareItemsTask() {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldExecute(ServerWorld world, VillagerEntity owner) {
        return BrainUtil.isCorrectVisibleType(owner.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityType.VILLAGER);
    }

    @Override
    protected boolean shouldContinueExecuting(ServerWorld world, VillagerEntity entity, long gameTimeIn) {
        return this.shouldExecute(world, entity);
    }

    @Override
    protected void startExecuting(ServerWorld world, VillagerEntity entity, long gameTimeIn) {
        VillagerEntity villagerentity = (VillagerEntity) entity.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        BrainUtil.lookApproachEachOther(entity, villagerentity, 0.5F);
        this.field_220588_a = func_220585_a(entity, villagerentity);
    }

    @Override
    protected void updateTask(ServerWorld world, VillagerEntity owner, long gameTime) {
        VillagerEntity villagerentity = (VillagerEntity) owner.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if (owner instanceof AtumVillagerEntity && villagerentity instanceof AtumVillagerEntity) {
            if (!(owner.getDistanceSq(villagerentity) > 5.0D)) {
                BrainUtil.lookApproachEachOther(owner, villagerentity, 0.5F);
                owner.func_242368_a(world, villagerentity, gameTime);
                if (owner.canAbondonItems() && (((AtumVillagerEntity) owner).getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.FARMER.get() || villagerentity.wantsMoreFood())) {
                    func_220586_a(owner, VillagerEntity.FOOD_VALUES.keySet(), villagerentity);
                }

                if (((AtumVillagerEntity) villagerentity).getAtumVillagerData().getAtumProfession() == AtumVillagerProfession.FARMER.get() && owner.getVillagerInventory().count(Items.WHEAT) > Items.WHEAT.getMaxStackSize() / 2) {
                    func_220586_a(owner, ImmutableSet.of(Items.WHEAT), villagerentity);
                }

                if (!this.field_220588_a.isEmpty() && owner.getVillagerInventory().hasAny(this.field_220588_a)) {
                    func_220586_a(owner, this.field_220588_a, villagerentity);
                }
            }
        }
    }

    @Override
    protected void resetTask(ServerWorld world, VillagerEntity entity, long gameTimeIn) {
        entity.getBrain().removeMemory(MemoryModuleType.INTERACTION_TARGET);
    }

    private static Set<Item> func_220585_a(VillagerEntity from, VillagerEntity to) {
        if (from instanceof AtumVillagerEntity && to instanceof AtumVillagerEntity) {
            ImmutableSet<Item> immutableset = ((AtumVillagerEntity) to).getAtumVillagerData().getAtumProfession().getSpecificItems();
            ImmutableSet<Item> immutableset1 = ((AtumVillagerEntity) from).getAtumVillagerData().getAtumProfession().getSpecificItems();
            return immutableset.stream().filter((p_220587_1_) -> {
                return !immutableset1.contains(p_220587_1_);
            }).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private static void func_220586_a(VillagerEntity p_220586_0_, Set<Item> p_220586_1_, LivingEntity p_220586_2_) {
        Inventory inventory = p_220586_0_.getVillagerInventory();
        ItemStack itemstack = ItemStack.EMPTY;
        int i = 0;

        while (i < inventory.getSizeInventory()) {
            ItemStack itemstack1;
            Item item;
            int j;
            label28:
            {
                itemstack1 = inventory.getStackInSlot(i);
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
            BrainUtil.spawnItemNearEntity(p_220586_0_, itemstack, p_220586_2_.getPositionVec());
        }

    }
}