package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.teammetallurgy.atum.entity.efreet.SunspeakerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SunspeakerShowWaresTask extends Task<SunspeakerEntity> {
    @Nullable
    private ItemStack field_220559_a;
    private final List<ItemStack> field_220560_b = Lists.newArrayList();
    private int field_220561_c;
    private int field_220562_d;
    private int field_220563_e;

    public SunspeakerShowWaresTask(int durationMin, int durationMax) {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryModuleStatus.VALUE_PRESENT), durationMin, durationMax);
    }

    @Override
    public boolean shouldExecute(@Nonnull ServerWorld world, SunspeakerEntity owner) {
        Brain<?> brain = owner.getBrain();
        if (!brain.getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent()) {
            return false;
        } else {
            LivingEntity livingentity = brain.getMemory(MemoryModuleType.INTERACTION_TARGET).get();
            return livingentity.getType() == EntityType.PLAYER && owner.isAlive() && livingentity.isAlive() && !owner.isChild() && owner.getDistanceSq(livingentity) <= 17.0D;
        }
    }

    @Override
    public boolean shouldContinueExecuting(@Nonnull ServerWorld world, @Nonnull SunspeakerEntity sunspeaker, long gameTime) {
        return this.shouldExecute(world, sunspeaker) && this.field_220563_e > 0 && sunspeaker.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent();
    }

    @Override
    public void startExecuting(@Nonnull ServerWorld world, @Nonnull SunspeakerEntity sunspeaker, long gameTime) {
        super.startExecuting(world, sunspeaker, gameTime);
        this.func_220557_c(sunspeaker);
        this.field_220561_c = 0;
        this.field_220562_d = 0;
        this.field_220563_e = 40;
    }

    @Override
    public void updateTask(@Nonnull ServerWorld world, @Nonnull SunspeakerEntity owner, long gameTime) {
        LivingEntity livingentity = this.func_220557_c(owner);
        this.func_220556_a(livingentity, owner);
        if (!this.field_220560_b.isEmpty()) {
            this.func_220553_d(owner);
        } else {
            owner.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
            this.field_220563_e = Math.min(this.field_220563_e, 40);
        }

        --this.field_220563_e;
    }

    @Override
    public void resetTask(@Nonnull ServerWorld world, @Nonnull SunspeakerEntity sunspeaker, long gameTime) {
        super.resetTask(world, sunspeaker, gameTime);
        sunspeaker.getBrain().removeMemory(MemoryModuleType.INTERACTION_TARGET);
        sunspeaker.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
        this.field_220559_a = null;
    }

    private void func_220556_a(LivingEntity entity, SunspeakerEntity sunspeaker) {
        boolean flag = false;
        ItemStack heldStack = entity.getHeldItemMainhand();
        if (this.field_220559_a == null || !ItemStack.areItemsEqual(this.field_220559_a, heldStack)) {
            this.field_220559_a = heldStack;
            flag = true;
            this.field_220560_b.clear();
        }

        if (flag && !this.field_220559_a.isEmpty()) {
            this.func_220555_b(sunspeaker);
            if (!this.field_220560_b.isEmpty()) {
                this.field_220563_e = 900;
                this.func_220558_a(sunspeaker);
            }
        }

    }

    private void func_220558_a(SunspeakerEntity sunspeaker) {
        sunspeaker.setItemStackToSlot(EquipmentSlotType.MAINHAND, this.field_220560_b.get(0));
    }

    private void func_220555_b(SunspeakerEntity sunspeaker) {
        for (MerchantOffer merchantoffer : sunspeaker.getOffers()) {
            if (!merchantoffer.hasNoUsesLeft() && this.func_220554_a(merchantoffer)) {
                this.field_220560_b.add(merchantoffer.getSellingStack());
            }
        }

    }

    private boolean func_220554_a(MerchantOffer offer) {
        return ItemStack.areItemsEqual(this.field_220559_a, offer.getDiscountedBuyingStackFirst()) || ItemStack.areItemsEqual(this.field_220559_a, offer.getBuyingStackSecond());
    }

    private LivingEntity func_220557_c(SunspeakerEntity sunspeaker) {
        Brain<?> brain = sunspeaker.getBrain();
        LivingEntity livingentity = brain.getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(livingentity, true));
        return livingentity;
    }

    private void func_220553_d(SunspeakerEntity sunspeaker) {
        if (this.field_220560_b.size() >= 2 && ++this.field_220561_c >= 40) {
            ++this.field_220562_d;
            this.field_220561_c = 0;
            if (this.field_220562_d > this.field_220560_b.size() - 1) {
                this.field_220562_d = 0;
            }

            sunspeaker.setItemStackToSlot(EquipmentSlotType.MAINHAND, this.field_220560_b.get(this.field_220562_d));
        }
    }
}